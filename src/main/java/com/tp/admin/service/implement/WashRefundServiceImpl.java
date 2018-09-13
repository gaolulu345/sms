package com.tp.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.OrderDao;
import com.tp.admin.dao.RefundDao;
import com.tp.admin.data.dto.OrderDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.entity.Order;
import com.tp.admin.data.entity.Refund;
import com.tp.admin.data.search.RefundSearch;
import com.tp.admin.enums.OrderTypeEnum;
import com.tp.admin.enums.RefundStatusEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.MiniOrderPayManagerI;
import com.tp.admin.manage.impl.MiniOrderPayManagerImpl;
import com.tp.admin.service.WashRefundServiceI;
import com.tp.admin.utils.ExcelUtil;
import com.tp.admin.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class WashRefundServiceImpl implements WashRefundServiceI {

    @Autowired
    RefundDao refundDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    MiniOrderPayManagerI miniOrderPayManager;

    private static final Logger logger = LoggerFactory.getLogger(MiniOrderPayManagerImpl.class);

    @Override
    public ApiResult list(HttpServletRequest request, RefundSearch refundSearch) {
        refundSearch.builData();
        List<Refund> list = refundDao.listBySearch(refundSearch);
        if (null != list && !list.isEmpty()) {
            for (Refund o : list){
                o.build();
            }
        }
        int cnt = refundDao.cntBySearch(refundSearch);
        refundSearch.setResult(list);
        refundSearch.setTotalCnt(cnt);
        return ApiResult.ok(refundSearch);
    }

    @Override
    public ResponseEntity<FileSystemResource> listExport(HttpServletRequest request, HttpServletResponse response, RefundSearch refundSearch) {
        List<Refund> list = refundDao.listBySearch(refundSearch);
        String fileName = ExcelUtil.createXlxs(Constant.WASH_REFUND, refundSearch.getStartTime(), refundSearch.getEndTime());
        String path = System.getProperty(Constant.TMP_DIR) + Constant._XLSX_DIR;
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        try {
            ExcelUtils.getInstance().exportObjects2Excel(list, Refund.class, true, "sheet0", true, path + fileName);
        } catch (Excel4JException | IOException e) {
            e.printStackTrace();
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        File file = new File(path + fileName);
        return ExcelUtil.fileExcel(request,fileName,file);
    }


    @Override
    public ApiResult approved(HttpServletRequest request, RefundSearch refundSearch) {
        int id = refundSearch.getId();
        int status = refundSearch.getStatus();
        Refund refund = refundDao.findById(id);
        if (null == refund) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        if (refund.getStatus() != RefundStatusEnum.REQUEST_REFUND.getValue()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        if (status != RefundStatusEnum.APPROVED.getValue() &&
            status != RefundStatusEnum.REQUEST_REJECTION.getValue()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        refund.setSysAdminNameCheck(adminAccount.getName());
        refund.setCheckTime(new Timestamp(System.currentTimeMillis()));
        refund.setStatus(status);
        int res = refundDao.update(refund);
        if (0 == res) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
}

    @Override
    public ApiResult payBack(HttpServletRequest request, RefundSearch refundSearch) {
        int id = refundSearch.getId();
        Refund refund = refundDao.findById(id);
        if (null == refund) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        if (refund.getStatus() != RefundStatusEnum.APPROVED.getValue()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        Order order = orderDao.findById(refund.getOrderId());
        if (null == order) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        int orderType = order.getType();
        // 如果是免费的，不退款。
        if (orderType == OrderTypeEnum.FREE.ordinal()) {
            // 免费的什么都不做.直接更新数据库。
        }else if (orderType == OrderTypeEnum.ALIPAY.ordinal()) {
            miniOrderPayManager.aliPayBack(order);
        }else if (orderType == OrderTypeEnum.WXPAY.ordinal() || orderType == OrderTypeEnum.TEST.ordinal()) {
            miniOrderPayManager.wxinPayBack(order);
        }else {
            // 如果订单状态不正确则拒绝退款。
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        refund.setSysAdminNamePay(adminAccount.getName());
        refund.setStatus(RefundStatusEnum.REFUNDED.getValue());
        refund.setRefundTime(new Timestamp(System.currentTimeMillis()));
        int sqlRes = refundDao.update(refund);
        if (sqlRes == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }


}
