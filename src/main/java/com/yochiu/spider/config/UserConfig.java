package com.yochiu.spider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "user")
public class UserConfig {

    private String authString;
    private String authString2;
    private String imsi;
    private String mobilePhone;
    private String userId;
    private String deviceId;

}
