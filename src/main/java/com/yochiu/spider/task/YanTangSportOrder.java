package com.yochiu.spider.task;

import com.yochiu.spider.badminton.Spider;
import com.yochiu.spider.config.OrderConfig;
import com.yochiu.spider.config.UserConfig;
import com.yochiu.spider.config.YanTangSportConfig;
import com.yochiu.spider.data.BallCourt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class YanTangSportOrder {

    @Autowired
    private Spider spider;

    @Autowired
    private YanTangSportConfig sportConfig;

    @Autowired
    private UserConfig userConfig;

    @Autowired
    private OrderConfig orderConfig;

    @Scheduled(cron = "56 59 8 ? * FRI")
    public void orderSport() {
        String openDate = orderConfig.getOpenDate();
        String sectionTime = orderConfig.getSectionTime();
        List<BallCourt> ballCourts = spider.querySportBall(openDate, sportConfig);
        if (CollectionUtils.isEmpty(ballCourts)) {
            log.error("YanTangSportOrder querySportBall empty");
        }
        spider.orderSportBall(openDate, sectionTime, ballCourts, sportConfig);
    }

}
