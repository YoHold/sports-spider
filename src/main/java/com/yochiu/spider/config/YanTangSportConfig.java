package com.yochiu.spider.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class YanTangSportConfig implements SportConfig{

    private static final String CG_CODE_YAN_TANG = "0020C062910";
    private static final String CG_ID_YAN_TANG = "8a42f487543cab890154a2a69e024362";
    private static final String CG_TYPE_YAN_TANG = "1";
    private static final String ORDER_TOTAL_YAN_TANG = "80.00";
    private static final String YAN_TANG_SAVE_ORDER_URL = "https://tytapp.quntitong.cn/sportinterNew/androidnorder/saveNewOrder.do";


    @Override
    public String getCgCode() {
        return CG_CODE_YAN_TANG;
    }

    @Override
    public String getCgId() {
        return CG_ID_YAN_TANG;
    }

    @Override
    public String getCgType() {
        return CG_TYPE_YAN_TANG;
    }

    @Override
    public String getOrderTotal() {
        return ORDER_TOTAL_YAN_TANG;
    }

    @Override
    public String getOrderUrl() {
        return YAN_TANG_SAVE_ORDER_URL;
    }
}
