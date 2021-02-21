package com.yufan.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/6 10:09
 * 功能介绍:
 */
public class CommonMethod {

    /**
     * 处理结果参数
     *
     * @param code
     * @return
     */
    public static JSONObject packagMsg(String code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", OutCode.getMsg(code));
        return json;
    }

    /**
     * 处理结果参数
     *
     * @param code
     * @return
     */
    public static JSONObject packagMsg(String code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", OutCode.getMsg(code).replace("{}", msg));
        return json;
    }

    public static List<String> replaceSpecialChar(String parmas) {
        String separator = "；|，|;|, |,|; |(\\r\\n)|(\\n)";
        List<String> list = Arrays.asList(parmas.split(separator));
        if (list.size() == 0) {
            return list;
        } else {
            ArrayList<String> resultList = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                if (!StringUtils.isEmpty((String) list.get(i))) {
                    resultList.add(list.get(i).trim());
                }
            }
            return resultList;
        }
    }

}
