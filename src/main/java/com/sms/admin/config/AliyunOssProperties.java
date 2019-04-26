package com.sms.admin.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = AliyunOssProperties.PREFIX)
@Data
public class AliyunOssProperties {
    public static final String PREFIX = "aliyun-oss";

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String path;
    private String otherPath;
    private String serverUrl;

}