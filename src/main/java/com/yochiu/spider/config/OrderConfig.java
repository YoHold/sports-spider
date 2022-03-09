package com.yochiu.spider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "order")
public class OrderConfig {

    private String openDate;

    private String sectionTime;
}
