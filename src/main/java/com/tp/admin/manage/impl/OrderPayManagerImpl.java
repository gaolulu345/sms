package com.tp.admin.manage.impl;

import ch.qos.logback.core.joran.spi.XMLUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.tp.admin.common.ConfigUtil;
import com.tp.admin.common.Constant;
import com.tp.admin.common.TestConstant;
import com.tp.admin.config.AliPayProperties;
import com.tp.admin.config.WXinPayProperties;
import com.tp.admin.data.entity.Order;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.OrderPayManagerI;
import com.tp.admin.utils.ClientCustomSSL;
import com.tp.admin.utils.MD5Util;
import net.sf.json.JSONObject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderPayManagerImpl implements OrderPayManagerI {

    private static final Logger logger = LoggerFactory.getLogger(OrderPayManagerImpl.class);

    @Autowired
    AliPayProperties aliPayProperties;

    @Autowired
    WXinPayProperties wXinPayProperties;

    @Override
    public void aliPayBack(Order order) {
        logger.info("订单号：{}生成支付宝支付码", order.getAlipayStr());
        String aliURL = "https://openapi.alipay.com/gateway.do";
        String appId = TestConstant.ALiMiniAppID;
        String privateKey = TestConstant.ALiMiniAppPrivateKey;
        String praviteKey = TestConstant.ALiMiniAppPublicKey;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", order.getAlipayStr());
        jsonObject.put("refund_amount", String.valueOf(Float.valueOf(order.getAmount()) / 100));
        AlipayClient alipayClient = new DefaultAlipayClient(aliURL, appId, privateKey, "json", "GBK", praviteKey, "RSA2");
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
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "invalid service");
        }
        if (aliRes.equals("20001")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "no auth to payback");
        }
        if (aliRes.equals("40001")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "param miss");
        }
        if (aliRes.equals("40002")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "illegal param");
        }
        if (aliRes.equals("40004")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "service fails");
        }
        if (aliRes.equals("40006")) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "another: no auth to payback");
        }
    }

    @Override
    public void wxinPayBack(Order order) {
        logger.info("订单号：{} 生成微信支付码", order.getWxpayStr());
        try {
            // 账号信息
            String mch_id = TestConstant.WXMiniAppID; // 商业号
            String key = TestConstant.WXMiniAppApiKey; // key
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            commonParams(packageParams);
            packageParams.put("out_trade_no", String.valueOf(order.getId()));// 商户订单号
            packageParams.put("out_refund_no", order.getWxpayStr());//商户退款单号
            String totalFee = String.valueOf(Float.valueOf(order.getAmount()) / 100);
            packageParams.put("total_fee", totalFee);// 总金额
            packageParams.put("refund_fee", totalFee);//退款金额
            packageParams.put("op_user_id", mch_id);//操作员帐号, 默认为商户号
            String sign = createSign("UTF-8", packageParams, key);
            packageParams.put("sign", sign);// 签名
            String requestXML = getRequestXml(packageParams);
            logger.info("requestXML {} ", requestXML);
            String weixinPost = ClientCustomSSL.doRefund(ConfigUtil.REFUND_URL, requestXML).toString();
            Map map = doXMLParse(weixinPost);
            String returnCode = (String) map.get("return_code");
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = (String) map.get("result_code");
                if ("SUCCESS".equals(resultCode)) {
                    logger.info("订单号：{} 支付凭证 {}  微信退款成功并删除二维码", order.getId(), order.getWxpayStr());
                    throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
                } else {
                    String errCodeDes = (String) map.get("err_code_des");
                    logger.info("订单号：{} 支付凭证 {}  微信退款失败:{}", order.getId(), order.getWxpayStr(), errCodeDes);
                    throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
                }
            } else {
                String returnMsg = (String) map.get("return_msg");
                logger.info("订单号：{}微信退款失败:{}", order.getWxpayStr(), returnMsg);
                throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
            }
        } catch (Exception e) {
            logger.error("订单号：{} 支付凭证 {} 微信支付失败(系统异常)", order.getId(), order.getWxpayStr(), e.getMessage());
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
    }

    private void commonParams(SortedMap<Object, Object> packageParams) {
        // 账号信息
        String appid = TestConstant.WxAppID; // appid
        String mch_id = TestConstant.WxMchID; // 商业号
        // 生成随机字符串
        String currTime = getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = buildRandom(4) + "";
        String nonce_str = strTime + strRandom;
        packageParams.put("appid", appid);// 公众账号ID
        packageParams.put("mch_id", mch_id);// 商户号
        packageParams.put("nonce_str", nonce_str);// 随机字符串
    }


    /**
     * 取出一个指定长度大小的随机正整数.
     */
    private int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 获取制定格式的时间字符串
     * @return
     */
    private String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    /**
     * 信息签名
     * @param characterEncoding
     * @param packageParams
     * @param API_KEY
     * @return
     */
    public String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }


    /**
     * 将请求参数转换为xml格式的string
     */
    @SuppressWarnings({ "rawtypes"})
    public String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }


    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        // 关闭流
        in.close();

        return m;
    }

    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    @SuppressWarnings({ "rawtypes" })
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

}
