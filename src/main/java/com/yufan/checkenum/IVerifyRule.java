package com.yufan.checkenum;

/**
 * @description: 校验接口
 * @author: lirf
 * @time: 2021/8/24
 */
public interface IVerifyRule {

    VerifyRuleResultBean verifyData(String key,String value);
}
