package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
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
import com.tp.admin.service.WashRefundServiceI;
import com.tp.admin.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@Service
public class WashRefundServiceImpl implements WashRefundServiceI {

    @Autowired
    RefundDao refundDao;

    @Autowired
    OrderDao orderDao;

    @Override
    public ApiResult list(HttpServletRequest request, RefundSearch refundSearch) {
        refundSearch.build();
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
        if (status != RefundStatusEnum.APPROVED.getValue() ||
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
        OrderDTO orderDTO = orderDao.findOrderDTOById(refund.getOrderId());
        if (null == orderDTO) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        int orderType = orderDTO.getType();
        // 如果是免费的，不退款。
        if (orderType == OrderTypeEnum.FREE.ordinal()) {
            refund.setSysAdminNamePay(adminAccount.getName());
            refund.setStatus(RefundStatusEnum.REFUNDED.getValue());
            refund.setRefundTime(new Timestamp(System.currentTimeMillis()));
        }else if (orderType == OrderTypeEnum.ALIPAY.ordinal()) {
//            payBackAliPayOrder(channel == AppOrderChannel.AliminiApp.ordinal(), dbOrder);
        }else if (orderType == OrderTypeEnum.WXPAY.ordinal() || orderType == OrderTypeEnum.TEST.ordinal()) {
//            payBackWxPayOrder(
//                    channel == AppOrderChannel.WXminiApp.ordinal() || type == OrderType.TestOr0PayOrder.ordinal(),
//                    dbOrder);
        }else {
            // 如果订单状态不正确则拒绝退款。
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        int sqlRes = refundDao.update(refund);
        if (sqlRes == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

//    public ApiResult export(HttpServletResponse response,
//                             @RequestParam(value = "orderID", required = false, defaultValue = "0") int orderID,
//                             @RequestParam(value = "status", required = false) Integer status,
//                             @RequestParam(value = "reason", required = false) Integer reason) {
//
//        RefundSearch search = new RefundSearch(orderID, 0, 0, status, reason);
//        AjaxResult result = ExcelUtil.export(response, search, refundService, "refund", "退款列表",
//                ExcelUtil.refundHeaderName(), false);
//        return result;
//    }



}
