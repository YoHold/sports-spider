package com.yochiu.spider.data;

import lombok.Data;

@Data
public class BallCourtSection {

    /**
     * 场馆
     */
    private String cgName;

    /**
     * 场地
     */
    private String fileName;

    /**
     * 时间
     */
    private String timeSection;

    /**
     * 场地id
     */
    private String resourceid;

    /**
     * 开放时间
     */
    private String openDate;

}
