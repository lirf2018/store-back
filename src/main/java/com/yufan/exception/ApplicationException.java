package com.yufan.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * 创建人: lirf
 * 创建时间:  2020/9/6 0:36
 * 功能介绍:
 */
public class ApplicationException extends RuntimeException {

    private JSONObject out;//自定义异常码


    public ApplicationException(JSONObject out) {
        this.out = out;
    }

    public JSONObject getOut() {
        return out;
    }

    public void setOut(JSONObject out) {
        this.out = out;
    }
}
