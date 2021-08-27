package com.yufan.exception;

import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/8/27
 */
@RestControllerAdvice
public class RrcExpHandler {

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorExpHandler(Exception ex) {
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ApplicationException.class)
    public JSONObject myErrorHandler(ApplicationException ex) {
        return ex.getOut();
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = DataCheckException.class)
    public JSONObject myErrorHandler(DataCheckException ex) {
        return ex.getOut();
    }
}
