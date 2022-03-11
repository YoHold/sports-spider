package com.yochiu.spider.badminton;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yochiu.spider.config.DingTalkConfig;
import com.yochiu.spider.config.SportConfig;
import com.yochiu.spider.config.UserConfig;
import com.yochiu.spider.data.BallCourt;
import com.yochiu.spider.data.BallCourtSection;
import com.yochiu.spider.support.DingTalkMsgUtil;
import com.yochiu.spider.utils.SpiderThreadFactory;
import com.yochiu.spider.utils.SportsApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class Spider {

    @Autowired
    private UserConfig userConfig;

    @Autowired
    private DingTalkConfig dingTalkConfig;

    private Random random = new Random();

    private long TEN_FIVE_SECONDS = TimeUnit.SECONDS.toMillis(15);

    private long TWENTY_SECONDS = TimeUnit.MINUTES.toMillis(20);

    /**
     * 场地查询
     */
    public List<BallCourt> querySportBall(String openDate, SportConfig sportConfig) {
        long startTime = System.currentTimeMillis();
        do {
            try {
                List<BallCourt> ballCourts = SportsApiClient.querySite(openDate, sportConfig.getCgCode(), userConfig);
                if (CollectionUtils.isNotEmpty(ballCourts)) {
                    log.info("querySportBall suc, ballCourts:{}", ballCourts);
                    return ballCourts;
                }
            } catch (Exception e) {
                log.error("querySportBall error", e);
            }
        } while (System.currentTimeMillis() - startTime < TEN_FIVE_SECONDS);
        return null;
    }

    public void orderSportBall(String openDate, String filterTime, List<BallCourt> ballCourts, SportConfig sportConfig) {
        if (CollectionUtils.isEmpty(ballCourts)) {
            log.info("ballCourts empty");
            return;
        }

        int ballSize = ballCourts.size();
        ExecutorService executor = Executors.newFixedThreadPool(2, SpiderThreadFactory.create("spiderThreadPool", true));
        executor.submit(() -> {
            long startTime = System.currentTimeMillis();
            do {
                try {
                    boolean suc = orderSportBall(ballCourts, ballSize, filterTime, openDate, sportConfig);
                    if (suc) {
                        break;
                    }
                } catch (Exception e) {
                    log.error("orderSportBall error", e);
                }
            } while (System.currentTimeMillis() - startTime < TWENTY_SECONDS);
        });
    }

    private boolean orderSportBall(List<BallCourt> ballCourts, int ballSize, String filterTime, String openDate, SportConfig sportConfig) {
        int index = random.nextInt(ballSize);
        BallCourt ballCourt = ballCourts.get(index);
        Map<String, BallCourtSection> ballCourtSectionMap = ballCourt.getBallCourtSectionMap();
        BallCourtSection ballCourtSection;
        if (StringUtils.isNotEmpty(filterTime)) {
            ballCourtSection = ballCourtSectionMap.get(filterTime);
        } else {
            List<BallCourtSection> ballCourtSections = Lists.newArrayList(ballCourtSectionMap.values());
            int randomIndex = random.nextInt(ballCourtSections.size());
            ballCourtSection = ballCourtSections.get(randomIndex);
        }
        String body = SportsApiClient.orderSportBall(openDate, ballCourt.getResourceId(), ballCourtSection.getResourceid(), sportConfig, userConfig);
        log.info("orderSportBall, body: {}", body);
        if (StringUtils.isNotEmpty(body)) {
            JSONObject dataJson = JSONObject.parseObject(body);
            if (dataJson.containsKey("flag") && dataJson.getIntValue("flag") == 0) {
                DingTalkMsgUtil.postMarkDownMsg(dingTalkConfig.getUrl(), "测试", "测试信息", null);
                return true;
            }
            if (body.contains("未支付")) {
                DingTalkMsgUtil.postMarkDownMsg(dingTalkConfig.getUrl(), "测试", "测试信息", null);
                return true;
            }
        }
        return false;
    }

}
