package com.yochiu.spider.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TianheSportsCenterConfig implements SportConfig {

    private static final String CG_CODE = "0020C000009";
    private static final String CGID = "402881b441a660630141a716ab710049";
    private static final String CG_TYPE = "3";
    private static final String ORDER_TOTAL = "140.00";
    private static final String SAVE_ORDER_URL = "https://tytapp.quntitong.cn/sportinterNew/androidorder/saveOrder.do";


    @Override
    public String getCgCode() {
        return CG_CODE;
    }

    @Override
    public String getCgId() {
        return CGID;
    }

    @Override
    public String getCgType() {
        return CG_TYPE;
    }

    @Override
    public String getOrderTotal() {
        return ORDER_TOTAL;
    }

    @Override
    public String getOrderUrl() {
        return SAVE_ORDER_URL;
    }
}
