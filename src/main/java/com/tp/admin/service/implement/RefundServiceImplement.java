package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.RefundDao;
import com.tp.admin.data.dto.RefundDTO;
import com.tp.admin.data.search.RefundSearch;
import com.tp.admin.service.RefundServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RefundServiceImplement implements RefundServiceI {

    @Autowired
    RefundDao refundDao;

    @Override
    public ApiResult list(HttpServletRequest request, RefundSearch refundSearch) {
        refundSearch.build();
        List<RefundDTO> list = refundDao.listBySearch(refundSearch);
        if (null != list && !list.isEmpty()) {
            for (RefundDTO o : list){
                o.build();
            }
        }
        int cnt = refundDao.cntBySearch(refundSearch);
        refundSearch.setResult(list);
        refundSearch.setTotalCnt(cnt);
        return ApiResult.ok(refundSearch);
    }


//    public AjaxResult updateRefundState(HttpSession session, RefundSearch refundSearch) {
//        int refundId = refundSearch.getId();
//        int status = inrefund.getStatus();
//        Refund checkRefund = refundService.findByRefundID(refundID);
//        if (checkRefund == null || checkRefund.getRefundID() != refundID) {
//            errMsg = "non-existent matching refund for ID --> " + refundID;
//            throw new BaseException(baseErrCode.NoDBResultException, errMsg);
//        }
//        int ostatus = checkRefund.getStatus();
//        String adminName = (String) session.getAttribute(Constant.SESSIONUSERNAME);
//        if (ostatus != RefundState.Asked.ordinal()) {
//            errMsg = "refund has checked yet";
//            throw new BaseException(baseErrCode.InvalidAccessException, errMsg);
//        }
//        int res = refundService.updateStatus(ostatus, refundID, status, adminName);
//        if (res == 0) {
//            errMsg = "fail to update refund status by ID--> " + refundID;
//            throw new BaseException(baseErrCode.NoDBRowAffectedException, errMsg);
//        }
//        Refund refund = refundService.findByRefundID(refundID);
//        if (refund == null || refund.getRefundID() != refundID) {
//            errMsg = "non-existent matching refund after update for ID --> " + refundID;
//            throw new BaseException(baseErrCode.NoDBResultException, errMsg);
//        }
//        logService.create(new SysLog(SessionUtil.getAdminName(session), curAction.getValue(), checkRefund.toString(), refund.toString()));
//        return AjaxResult.getOK();
//    }

//    public AjaxResult export(HttpServletResponse response,
//                             @RequestParam(value = "orderID", required = false, defaultValue = "0") int orderID,
//                             @RequestParam(value = "status", required = false) Integer status,
//                             @RequestParam(value = "reason", required = false) Integer reason) {
//
//        RefundSearch search = new RefundSearch(orderID, 0, 0, status, reason);
//        AjaxResult result = ExcelUtil.export(response, search, refundService, "refund", "退款列表",
//                ExcelUtil.refundHeaderName(), false);
//        return result;
//    }

//    @ResponseBody
//    @PostMapping(value = "/payback")
//    public AjaxResult payBack(@RequestParam(value = "refundId") Integer refundId, HttpSession session) {
//        SysLogTypeEnum curAction = SysLogTypeEnum.REFUND_PAY;
//        if (!SessionUtil.checkPermit(session, curAction)) {
//            return AjaxResult.getError(baseErrCode.NO_PERMIT, null, null);
//        }
//        String logMsg = "";
//        String ipt = String.valueOf(refundId);
//        String adminName = (String) session.getAttribute(Constant.SESSIONUSERNAME);
//        if (refundId < 1) {
//            logMsg = "payback request with illegal refundId";
//            return AjaxResult.getError(ResultCode.ParamException, logMsg + ipt, null);
//        }
//        Refund dbRefund = refundService.findByRefundID(refundId);
//        if (dbRefund == null || dbRefund.getStatus() != RefundState.Checked.ordinal()) {
//            logMsg = "plz recheck refundId & status";
//            return AjaxResult.getError(ResultCode.ParamException, logMsg + ipt, null);
//        }
//        int orderId = dbRefund.getOrderID();
//        Order dbOrder = orderService.findById(orderId);
//        if (dbOrder == null) {
//            logMsg = "plz recheck refundId as unexist apporder";
//            return AjaxResult.getError(ResultCode.ParamException, logMsg + ipt, null);
//        }
//        int channel = dbOrder.getChannel();
//        int type = dbOrder.getType();
//        if (type == OrderType.Free.ordinal()) {
//            int sqlRes = refundService.updateStatePay(refundId, adminName);
//            if (sqlRes == 0) {
//                logMsg = "DB BUSY when update refund state to paying & refund was success!" + ipt;
//                LogUtils.warn(logMsg);
//                return AjaxResult.getError(baseErrCode.DBBusyException, logMsg, null);
//            }
//            return AjaxResult.getOK();
//        }
//        String result = "";
//        if (type == OrderType.AliPayOrder.ordinal()) {
//            result = payBackAliPayOrder(channel == AppOrderChannel.AliminiApp.ordinal(), dbOrder);
//        }
//        if (type == OrderType.WxPayOrder.ordinal() || type == OrderType.TestOr0PayOrder.ordinal()) {
//            result = payBackWxPayOrder(
//                    channel == AppOrderChannel.WXminiApp.ordinal() || type == OrderType.TestOr0PayOrder.ordinal(),
//                    dbOrder);
//        }
//        if (result.equals("success")) {
//            // 修改订单状态为已退款
//            int sqlRes = refundService.updateStatePay(refundId, adminName);
//            if (sqlRes == 0) {
//                logMsg = "DB BUSY when update refund state to paying & refund was success!" + ipt;
//                LogUtils.warn(logMsg);
//                return AjaxResult.getError(baseErrCode.DBBusyException, logMsg, null);
//            }
//            return AjaxResult.getOK();
//        }
//        return AjaxResult.getError(baseErrCode.RefundException, result, null);
//    }

//    private String payBackAliPayOrder(boolean miniApp, Order order) {
//        String result = "";
//        String aliURL = "https://openapi.alipay.com/gateway.do";
//        String appId = miniApp ? Constant.ALiMiniAppID : Constant.ALiPayID;
//        String privateKey = miniApp ? Constant.ALiMiniAppPrivateKey : Constant.ALiPayPrivateKey;
//        String praviteKey = miniApp ? Constant.ALiMiniAppPublicKey : Constant.ALiPayPublicKey;
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("out_trade_no", order.getAlipayStr());
//        jsonObject.put("refund_amount", String.valueOf(Float.valueOf(order.getAmount()) / 100));
//
//        AlipayClient alipayClient = new DefaultAlipayClient(aliURL, appId, privateKey, "json", "GBK", praviteKey,
//                "RSA2");
//        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
//        request.setBizContent(jsonObject.toJSONString());
//        String aliRes = "";
//        String logMsg = "";
//        try {
//            AlipayTradeRefundResponse response = alipayClient.execute(request);
//            aliRes = response.getCode();
//            LogUtils.warn(JSON.toJSONString(response));
//        } catch (Exception e) {
//            logMsg = "fail to get ali refund responce" + JSON.toJSONString(order);
//            LogUtils.warn(logMsg);
//            result = logMsg;
//        }
//        if (StringUtils.isEmpty(aliRes)) {
//            logMsg = "fail to get ali refund responce";
//            LogUtils.warn(logMsg + JSON.toJSONString(order));
//            result = logMsg;
//        }
//        if (aliRes.equals("10000")) {
//            result = "success";
//        }
//        if (aliRes.equals("20000")) {
//            result = "invalid service";
//        }
//        if (aliRes.equals("20001")) {
//            result = "no auth to payback";
//        }
//        if (aliRes.equals("40001")) {
//            result = "param miss";
//        }
//        if (aliRes.equals("40002")) {
//            result = "illegal param";
//        }
//        if (aliRes.equals("40004")) {
//            result = "service fails";
//        }
//        if (aliRes.equals("40006")) {
//            result = "another: no auth to payback";
//        }
//        return result;
//    }
//
//    private String payBackWxPayOrder(boolean miniApp, Order order) {
//        WxRefundOrder order2 = buildWxRefundOrder(order, miniApp);
//        if (order2 == null) {
//            return "fail to create refund order!" + JSON.toJSONString(order);
//        }
//        WxRefundResult result = getWxServerResult(order2);
//        if (result == null) {
//            return "wx refund result = null";
//        }
//        if (result.getReturn_code().equals("SUCCESS")) {
//            if (result.getResult_code().equals("SUCCESS")) {
//                return "success";
//            }
//            String errDesc = result.getResult_code() + result.getErr_code_des();
//            if (StringUtils.isEmpty(errDesc)) {
//                return "unkonw reason";
//            }
//            return errDesc;
//        }
//        String returnMsg = result.getReturn_msg();
//        if (StringUtils.isEmpty(returnMsg)) {
//            return "unknown reason fail";
//        }
//        return returnMsg;
//    }
//
//    private WxRefundOrder buildWxRefundOrder(Order order, boolean miniAppOrder) {
//        WxRefundOrder wxOrder = new WxRefundOrder();
//        wxOrder.setAppid(!miniAppOrder ? Constant.WxAppID : Constant.WXMiniAppID);
//        wxOrder.setMch_id(Constant.WxMchID);
//        wxOrder.setNonce_str("NEDEJC");
//        String outTradeNo = String.valueOf(order.getId());
//        if (Constant.DevEnvironment) {
//            outTradeNo = "test" + outTradeNo;
//        }
//        wxOrder.setOut_trade_no(outTradeNo);
//        wxOrder.setOut_refund_no(String.valueOf(order.getRefundId()));
//        wxOrder.setTotal_fee(String.valueOf(order.getAmount()));
//        wxOrder.setRefund_fee(wxOrder.getTotal_fee());
//        wxOrder.setSign(wxOrder.signForRefund(miniAppOrder));
//        return wxOrder;
//    }
//
//    private WxRefundResult getWxServerResult(WxRefundOrder order) {
//        HttpClient client = null;
//        String logMsg = "";
//        try {
//            KeyStore keyStore = KeyStore.getInstance("PKCS12");
//            FileInputStream instream = new FileInputStream(new File(Constant.WxApiCertLaotion));// 放退款证书的路径
//            try {
//                keyStore.load(instream, Constant.WxMchID.toCharArray());
//            } finally {
//                instream.close();
//            }
//
//            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, Constant.WxMchID.toCharArray())
//                    .build();
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
//                    null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//            // CloseableHttpClient httpclient =
//            // HttpClients.custom().setSSLSocketFactory(sslsf).build();
//            client = HCB.custom().setSSLSocketFactory(sslsf).build();
//        } catch (Exception e) {
//            logMsg = "fail to create ssl client";
//            LogUtils.warn(logMsg + JSON.toJSONString(order) + e.getMessage());
//        }
//        if (client == null) {
//            return null;
//        }
//
//        Header[] headers = HttpHeader.custom().other("Content-Type", "text/xml").build();
//        String url = Constant.WxRefundApi;
//        HttpConfig config = HttpConfig.custom().url(url).headers(headers).encoding("utf-8").json(order.xmlStr())
//                .client(client);
//
//        String result = new String();
//        try {
//            result = HttpClientUtil.post(config);
//        } catch (Exception e) {
//            logMsg = "fail to link to wx server";
//            LogUtils.warn(logMsg + JSON.toJSONString(order) + e.getMessage());
//        }
//        if (StringUtils.isEmpty(result)) {
//            logMsg = "wx server responce with null";
//            LogUtils.warn(logMsg + JSON.toJSONString(order));
//            return null;
//        }
//        LogUtils.info(JSON.toJSONString(result));
//        Class<?>[] classes = new Class[] { WxRefundResult.class };
//        XStream xStream = new XStream();
//        XStream.setupDefaultSecurity(xStream);
//        xStream.addPermission(NoTypePermission.NONE);
//        xStream.addPermission(AnyTypePermission.ANY);
//        // allow some basics
//        xStream.addPermission(NullPermission.NULL);
//        xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
//        xStream.allowTypes(classes);
//
//        xStream.processAnnotations(WxRefundResult.class);
//        xStream.alias("xml", WxRefundResult.class);
//
//        WxRefundResult wxResult = (WxRefundResult) xStream.fromXML(result);
//        return wxResult;
//    }
}
