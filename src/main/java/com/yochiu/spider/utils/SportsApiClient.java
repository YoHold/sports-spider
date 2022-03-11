package com.yochiu.spider.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yochiu.spider.config.SportConfig;
import com.yochiu.spider.config.UserConfig;
import com.yochiu.spider.data.BallCourtSection;
import com.yochiu.spider.data.BallCourt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static com.yochiu.spider.config.Constant.*;
import static com.yochiu.spider.config.Constant.QUERY_STORE_URL;

@Slf4j
public class SportsApiClient {

    /**
     *  [
     *     {
     *         "cdNum":"2",
     *         "sdNum":"2",
     *         "stadiumField":{
     *             "cgCode":"0020C000009",
     *             "cgName":"广州市羽毛球运动管理中心",
     *             "fgtype":"1",
     *             "fieldCode":"0020C00000910321",
     *             "fieldName":"A馆1号场",
     *             "fieldType":"1",
     *             "flgDeleted":"N",
     *             "ispublish":"Y",
     *             "orderDays":0,
     *             "priceComList":[
     *                 {
     *                     "endTime":"22:30",
     *                     "fieldCode":"0020C00000910321",
     *                     "resourceid":"000000007aa95cb4017b28af3dac38c5",
     *                     "startTime":"20:30"
     *                 }
     *             ],
     *             "resourceid":"8a42f49057b2dfd10157d17dbb4643c8"
     *         },
     *         "storeList":[
     *             {
     *                 "accountNum":1,
     *                 "allNum":1,
     *                 "cardName":"",
     *                 "cardType":"",
     *                 "cgCode":"0020C000009",
     *                 "cgName":"广州市羽毛球运动管理中心",
     *                 "costmoney":"",
     *                 "delayLock":"N",
     *                 "endTime":"10:00",
     *                 "fieldCode":"0020C00000910321",
     *                 "fieldName":"A馆1号场",
     *                 "fieldType":"1",
     *                 "flgCancel":"Y",
     *                 "isUseCard":"N",
     *                 "openDate":"2021-11-07",
     *                 "orderNum":0,
     *                 "price0":"72",
     *                 "price1":"120",
     *                 "reason":"锁定",
     *                 "resourceid":"8a42f4877ca8335f017cb307c7820263",
     *                 "settle0":"72",
     *                 "settle7":"50.4",
     *                 "sportCode":"002",
     *                 "sportName":"羽毛球",
     *                 "startTime":"08:00",
     *                 "timeSection":"08:00 - 10:00"
     *             }
     *         ]
     *     }
     * ]
     */
    public static List<BallCourt> querySite(String openDate, String siteCode, UserConfig userConfig) {
        Map<String, String> params = Maps.newHashMap();
        params.put("authstring", userConfig.getAuthString());
        params.put("authstring2", userConfig.getAuthString2());
        params.put("deviceid", userConfig.getDeviceId());
        params.put("imsi", userConfig.getImsi());
        params.put("mobilephone", userConfig.getMobilePhone());
        params.put("booking", "Y");
        params.put("cgCode", siteCode);
        params.put("cityName", CITY_NAME);
        params.put("citys", CITYS);
        params.put("openDate", openDate);
        params.put("os", OS);
        params.put("service", SERVICE);
        params.put("sportCode", "002");
        params.put("version", VERSION);
        params.put("timestamp","1628404860");
        String dataBody = HttpUtil.get(QUERY_STORE_URL, null, 3000, 3000, params);
        log.info("querySite, openDate:{}, siteCode:{}, dataBody:{}", openDate, siteCode, dataBody);

        if (StringUtils.isEmpty(dataBody)) {
            return null;
        }
        JSONArray dataArray = JSONObject.parseArray(dataBody);
        Map<String, BallCourtSection> courtSectionMap = Maps.newHashMap();
        List<BallCourt> ballCourts = Lists.newArrayList();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataJson = dataArray.getJSONObject(i);
            JSONObject stadiumField = dataJson.getJSONObject("stadiumField");
            BallCourt ballCourt = new BallCourt();
            ballCourt.setCgName(stadiumField.getString("cgName"));
            ballCourt.setFileName(stadiumField.getString("fieldName"));
            ballCourt.setResourceId(stadiumField.getString("resourceid"));

            JSONArray storeList = dataJson.getJSONArray("storeList");
            for (int  j = 0; j < storeList.size(); j++) {
                JSONObject storeDataJson = storeList.getJSONObject(j);
                if ("1".equals(storeDataJson.getString("orderNum"))) {
                    BallCourtSection ballCourtSection = new BallCourtSection();
                    ballCourtSection.setCgName(storeDataJson.getString("cgName"));
                    ballCourtSection.setFileName(storeDataJson.getString("fieldName"));
                    ballCourtSection.setResourceid(storeDataJson.getString("resourceid"));
                    ballCourtSection.setTimeSection(storeDataJson.getString("timeSection"));
                    ballCourtSection.setOpenDate(storeDataJson.getString("openDate"));
                    courtSectionMap.put(storeDataJson.getString("timeSection"), ballCourtSection);
                }

            }
            if (MapUtils.isNotEmpty(courtSectionMap)) {
                ballCourt.setBallCourtSectionMap(courtSectionMap);
                ballCourts.add(ballCourt);
            }
        }

        return ballCourts;
    }

    public static String orderSportBall(String openDate, String stadiumResourceId, String storeIds,
                                        SportConfig sportConfig, UserConfig userConfig) {
        Map<String, String> params = Maps.newHashMap();
        params.put("authstring", userConfig.getAuthString());
        params.put("authstring2", userConfig.getAuthString2());
        params.put("deviceid", userConfig.getDeviceId());
        params.put("imsi", userConfig.getImsi());
        params.put("mobilephone", userConfig.getMobilePhone());
        params.put("userID", userConfig.getUserId());
        params.put("cgId", sportConfig.getCgId());
        params.put("cgtype", sportConfig.getCgType());
        params.put("cgCode", sportConfig.getCgCode());
        params.put("ordertotal", sportConfig.getOrderTotal());
        params.put("cityName", CITY_NAME);
        params.put("citys", CITYS);
        params.put("openDate", openDate);
        params.put("os", "ios");
        params.put("queryType", "android");
        params.put("service", SERVICE);
        long time = System.currentTimeMillis() / 1000;
        params.put("timestamp", String.valueOf(time));
        params.put("stadiumResourceId", stadiumResourceId);
        params.put("storeIds", storeIds);
        params.put("num"+storeIds, "1");
        params.put("terminal", "6");
        params.put("sportsNum", "6");
        params.put("version", VERSION);
        params.put("lat", "113.333480");
        params.put("lon", "23.136389");
        params.put("quanid", "");
        params.put("subAdress","");
        String dataBody = HttpUtil.get(sportConfig.getOrderUrl(), null, 1000, 1000, params);
        return dataBody;
    }

}
