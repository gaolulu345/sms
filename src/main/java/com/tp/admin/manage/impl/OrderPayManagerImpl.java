package com.tp.admin.manage.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.tp.admin.common.TestConstant;
import com.tp.admin.config.AliPayProperties;
import com.tp.admin.config.WXinPayProperties;
import com.tp.admin.data.entity.Order;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.OrderPayManagerI;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OrderPayManagerImpl implements OrderPayManagerI {

    private static final Logger logger = LoggerFactory.getLogger(OrderPayManagerImpl.class);

    @Autowired
    AliPayProperties aliPayProperties;

    @Autowired
    WXinPayProperties wXinPayProperties;

    @Override
    public void aliPayBack(boolean miniApp, Order order) {
        logger.info("订单号：{}生成支付宝支付码",order.getAlipayStr());
        String aliURL = "https://openapi.alipay.com/gateway.do";
        String appId = miniApp ? TestConstant.ALiMiniAppID : TestConstant.ALiPayID;
        String privateKey = miniApp ? TestConstant.ALiMiniAppPrivateKey : TestConstant.ALiPayPrivateKey;
        String praviteKey = miniApp ? TestConstant.ALiMiniAppPublicKey : TestConstant.ALiPayPublicKey;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", order.getAlipayStr());
        jsonObject.put("refund_amount", String.valueOf(Float.valueOf(order.getAmount()) / 100));
        AlipayClient alipayClient = new DefaultAlipayClient(aliURL, appId, privateKey, "json", "GBK", praviteKey,"RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(jsonObject.toString());
        String aliRes = "";
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            aliRes = response.getCode();
        } catch (Exception e) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        if (StringUtils.isEmpty(aliRes)) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        if (aliRes.equals("10000")) {
            return;
        }
        if (aliRes.equals("20000")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION , "invalid service");
        }
        if (aliRes.equals("20001")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION , "no auth to payback");
        }
        if (aliRes.equals("40001")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION , "param miss");
        }
        if (aliRes.equals("40002")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION , "illegal param");
        }
        if (aliRes.equals("40004")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION , "service fails");
        }
        if (aliRes.equals("40006")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION , "another: no auth to payback");
        }
    }

    @Override
    public void wxinPayBack(boolean miniApp, Order order) {



    }

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
