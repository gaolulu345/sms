package com.tp.admin.config;


import com.tp.admin.common.Constant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = AdminProperties.PREFIX)
@Data
public class AdminProperties {

    public static final String PREFIX = "admin-setting";

    public static String signKey = Constant.RemoteTer.PUBLIC_KEY;

    public static final String PRO_ORDER_PREFIX = "";

    public static final String PRE_ORDER_PREFIX = "T_";

    public String washManageKey;

    public String washManageServer;

    /**
     * 是否测试
     */
    private Boolean debug;

    /**
     * 根据是否debug获取当前洗车订单前缀
     * @return
     */
    public String washOrderPrefix(){
        if (debug == null || debug == true) {
            return PRE_ORDER_PREFIX;
        }else{
            return PRO_ORDER_PREFIX;
        }
    }

}
