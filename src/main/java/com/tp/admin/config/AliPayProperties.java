package com.tp.admin.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = AliPayProperties.PREFIX)
@Data
public class AliPayProperties {

    public static final String PREFIX = "ali-pay";

    String payId;
    String publickey;
    String privateKey;
    String terminalPrivateKey;
    String terminalPrivateKeyTest;

    String nimiAppId;
    String nimiAppPublicKey;
    String nimiAppPrivateKey;

}
