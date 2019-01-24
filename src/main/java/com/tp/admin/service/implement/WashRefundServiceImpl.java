package com.tp.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.OrderDao;
import com.tp.admin.dao.PartnerUserWashCardDao;
import com.tp.admin.dao.RefundDao;
import com.tp.admin.data.dto.PartnerUserWashCardDTO;
import com.tp.admin.data.dto.PartnerUserWashCardDetailDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.entity.Order;
import com.tp.admin.data.entity.Refund;
import com.tp.admin.data.search.RefundSearch;
import com.tp.admin.enums.OrderTypeEnum;
import com.tp.admin.enums.RefundStatusEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.MiniOrderPayManagerI;
import com.tp.admin.manage.TransactionalServiceI;
import com.tp.admin.service.WashRefundServiceI;
import com.tp.admin.utils.ExcelUtil;
import com.tp.admin.utils.SessionUtils;
import com.tp.admin.utils.StringUtil;
import com.tp.admin.utils.TimeUtil;
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
import java.util.Date;
import java.util.List;

@Service
public class WashRefundServiceImpl implements WashRefundServiceI {

    private static final Logger logger = LoggerFactory.getLogger(WashRefundServiceImpl.class);

    @Autowired
    RefundDao refundDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    MiniOrderPayManagerI miniOrderPayManager;

    @Autowired
    TransactionalServiceI transactionalService;

    @Autowired
    PartnerUserWashCardDao partnerUserWashCardDao;

    @Override
    public ApiResult list(HttpServletRequest request, RefundSearch refundSearch) {
        refundSearch.builData();
        List<Refund> list = refundDao.listBySearch(refundSearch);
        if (null != list && !list.isEmpty()) {
            for (Refund o : list){
                o.build();
            }
            Integer cnt = refundDao.cntBySearch(refundSearch);
            refundSearch.setResult(list);
            refundSearch.setTotalCnt(cnt);
        }else{
            refundSearch.setTotalCnt(0);
        }
        return ApiResult.ok(refundSearch);
    }

    @Override
    public ResponseEntity<FileSystemResource> listExport(HttpServletRequest request, HttpServletResponse response, RefundSearch refundSearch) {
        int days = 0;
        try {
            String st = refundSearch.getStartTime();
            String et = refundSearch.getEndTime();
            Date startTime = StringUtil.toSearchDate(st);
            Date endTime = StringUtil.toSearchDate(et);
            endTime = StringUtil.addOneDay(endTime);
            days = TimeUtil.getDiffDays(startTime,endTime);
            refundSearch.setEndTime(StringUtil.getDateTime(endTime));
            logger.info("导出数据天数间隔 {}",days);
        }catch (Exception e){
            logger.error("筛选时间请求参数异常{}" , e.getMessage());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        if (days > 90) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"导出时间间隔天数不能大于9天");
        }
        List<Refund> list = refundDao.listBySearch(refundSearch);
        if (null != list && !list.isEmpty()) {
            for (Refund o : list){
                o.build();
            }
        }
        String fileName = ExcelUtil.createXlxs(Constant.WASH_REFUND, refundSearch.getStartTime(), StringUtil.downOneDay(refundSearch.getEndTime()));
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
        refund.build();
        long refundApplyTimeStamp =  refund.getCreateTimeMillis();
        if (refund.getStatus() != RefundStatusEnum.APPROVED.getValue()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        Order order = orderDao.findById(refund.getOrderId());
        if (null == order) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        int orderType = order.getType();
        partnerUserWashCardDao.updateCardInvalid(new Timestamp(System.currentTimeMillis()));
        partnerUserWashCardDao.updateCardInvalidByCnt();
        // 如果是免费的，不退款。
        if (orderType == OrderTypeEnum.FREE.ordinal()) {
            // 免费的什么都不做.直接更新数据库。
        }else if (orderType == OrderTypeEnum.ALIPAY.ordinal()) {
            if (order.getCardId() != 0){
                //使用了洗车卡，查找订单中的洗车卡
                PartnerUserWashCardDetailDTO partnerUserWashCardDetailDTO = partnerUserWashCardDao.findById(order.getCardId());
                if (null == partnerUserWashCardDetailDTO){
                    throw new BaseException(ExceptionCode.NO_THIS_CARD);
                }
                partnerUserWashCardDetailDTO.build();
                //判断是否是无次数限制使用的
                if (!partnerUserWashCardDetailDTO.getCntLess()){
                    if (partnerUserWashCardDetailDTO.getInvalid() != true){
                        //洗车卡未失效，更新洗车卡次数加1
                        partnerUserWashCardDao.addUpdateCnt(partnerUserWashCardDetailDTO.getId());
                        miniOrderPayManager.aliPayBack(order);
                    } else if (partnerUserWashCardDetailDTO.getInvalid() == true && partnerUserWashCardDetailDTO.getCnt() == 0 && refundApplyTimeStamp < partnerUserWashCardDetailDTO.getValidTimeMillis()){
                        //洗车卡失效，查看是否是因为这次洗车而失效的
                        transactionalService.recoveryWashCard(partnerUserWashCardDetailDTO);
                        miniOrderPayManager.aliPayBack(order);
                    }
                } else {
                    miniOrderPayManager.aliPayBack(order);
                }

            } else {
                miniOrderPayManager.aliPayBack(order);
            }

            //miniOrderPayManager.aliPayBackCredence(order);
        }else if (orderType == OrderTypeEnum.WXPAY.ordinal() || orderType == OrderTypeEnum.TEST.ordinal()) {
            if (order.getCardId() != 0){
                //使用了洗车卡，查找订单中的洗车卡
                PartnerUserWashCardDetailDTO partnerUserWashCardDetailDTO = partnerUserWashCardDao.findById(order.getCardId());
                if (null == partnerUserWashCardDetailDTO){
                    throw new BaseException(ExceptionCode.NO_THIS_CARD);
                }
                partnerUserWashCardDetailDTO.build();
                //判断是否是无次数限制使用的

                if (!partnerUserWashCardDetailDTO.getCntLess()){

                    if (partnerUserWashCardDetailDTO.getInvalid() != true){
                        //洗车卡未失效，更新洗车卡次数加1
                        partnerUserWashCardDao.addUpdateCnt(partnerUserWashCardDetailDTO.getId());
                        miniOrderPayManager.wxinPayBack(order);
                    } else if (partnerUserWashCardDetailDTO.getInvalid() == true && partnerUserWashCardDetailDTO.getCnt() == 0 &&  refundApplyTimeStamp < partnerUserWashCardDetailDTO.getValidTimeMillis()){
                        //洗车卡失效，查看是否是因为这次洗车而失效的
                        transactionalService.recoveryWashCard(partnerUserWashCardDetailDTO);
                        miniOrderPayManager.wxinPayBack(order);
                    }
                } else {
                    miniOrderPayManager.aliPayBack(order);
                }
            } else {
                miniOrderPayManager.wxinPayBack(order);
            }

            //miniOrderPayManager.wxinPayBackCredence(order);
        }else {
            // 如果订单状态不正确则拒绝退款。
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        refund.setSysAdminNamePay(adminAccount.getName());
        refund.setStatus(RefundStatusEnum.REFUNDED.getValue());
        refund.setRefundTime(new Timestamp(System.currentTimeMillis()));
        transactionalService.payBack(refund,order);
        return ApiResult.ok();
    }

}
