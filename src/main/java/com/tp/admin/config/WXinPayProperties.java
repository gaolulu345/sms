package com.tp.admin.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = WXinPayProperties.PREFIX)
@Data
public class WXinPayProperties {

    public static final String PREFIX = "wxin-pay";

    String APP_ID;// 服务号的应用ID
    String APP_SECRET;// 服务号的应用密钥
    String TOKEN;// 服务号的配置token
    String MCH_ID;// 商户号
    String API_KEY;// API密钥
    String SIGN_TYPE;// 签名加密方式
    String CERT_PATH = "apiclient_cert.p12" ;//微信支付证书


}
