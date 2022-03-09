package com.yochiu.spider.support;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yochiu.spider.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: yochiu
 * @Description:
 * @Date: 2021/8/17
 */
public class DingTalkMsgUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DingTalkMsgUtil.class);

    public DingTalkMsgUtil() {
    }

    public static boolean postTextMsg(String url, String content, JSONArray atMobiles) {
        if (StringUtils.isNotEmpty(url)) {
            JSONObject data = getTextMsgPostData(content, atMobiles);
            if (data != null) {
                return postMsg(url, data);
            }
        }

        return false;
    }

    public static boolean postMarkDownMsg(String url, String title, String text, JSONArray atMobiles) {
        if (StringUtils.isNotEmpty(url)) {
            JSONObject data = getMarkDownPostData(title, text, atMobiles);
            if (data != null) {
                return postMsg(url, data);
            }
        }

        return false;
    }

    private static boolean postMsg(String url, JSONObject data) {
        try {
            String resultMsg = HttpUtil.postJson(url, data.toString());
            JSONObject resultJson = JSONObject.parseObject(resultMsg);
            if (resultJson.getIntValue("errcode") == 0) {
                return true;
            }
        } catch (Exception var4) {
            LOG.error("postMsg", var4);
        }

        return false;
    }

    private static JSONObject getTextMsgPostData(String content, JSONArray atMobiles) {
        if (StringUtils.isEmpty(content)) {
            return null;
        } else {
            JSONObject result = new JSONObject();
            result.put("msgtype", "text");
            JSONObject text = new JSONObject();
            text.put("content", content);
            result.put("text", text);
            if (atMobiles != null && !atMobiles.isEmpty()) {
                JSONObject at = new JSONObject();
                at.put("atMobiles", atMobiles);
                at.put("isAtAll", false);
                result.put("at", at);
            }

            return result;
        }
    }

    private static JSONObject getMarkDownPostData(String title, String text, JSONArray atMobiles) {
        JSONObject result = new JSONObject();
        result.put("msgtype", "markdown");
        JSONObject markDown = new JSONObject();
        markDown.put("title", title);
        markDown.put("text", text);
        result.put("markdown", markDown);
        if (atMobiles != null && !atMobiles.isEmpty()) {
            JSONObject at = new JSONObject();
            at.put("atMobiles", atMobiles);
            at.put("isAtAll", false);
            result.put("at", at);
        }

        return result;
    }

}
