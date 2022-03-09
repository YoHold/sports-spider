package com.yochiu.spider.data;

import lombok.Data;

import java.util.Map;

@Data
public class BallCourt {

    /**
     * 场馆
     */
    private String cgName;

    /**
     * 场地
     */
    private String fileName;

    /**
     * X号场ID
     */
    private String resourceId;


    private Map<String, BallCourtSection> ballCourtSectionMap;

}
