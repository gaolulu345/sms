package com.tp.admin.config;


import com.tp.admin.common.Constant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = TpProperties.PREFIX)
@Data
public class TpProperties {

    public static final String PREFIX = "tp";

    public static String signKey = Constant.RemoteTer.PUBLIC_KEY;

    public String washManageKey;

    public String washManageServer;


}
