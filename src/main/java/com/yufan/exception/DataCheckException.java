package com.yufan.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * @description:
 * @author: lirf
 * @time: 2021/8/27
 */
public class DataCheckException extends RuntimeException {


    private JSONObject out;//自定义异常码


    public DataCheckException(JSONObject out) {
        this.out = out;
    }

    public JSONObject getOut() {
        return out;
    }

    public void setOut(JSONObject out) {
        this.out = out;
    }
}
