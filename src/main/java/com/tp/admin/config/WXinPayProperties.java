package com.tp.admin.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = WXinPayProperties.PREFIX)
@Data
public class WXinPayProperties {

    public static final String PREFIX = "wxin-pay";



}
