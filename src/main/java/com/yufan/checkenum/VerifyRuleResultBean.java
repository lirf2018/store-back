package com.yufan.checkenum;

/**
 * @description: 校验结果
 * @author: lirf
 * @time: 2021/8/24
 */
public class VerifyRuleResultBean {
    private boolean resultFlag = true;
    private String code = "00000";
    private String errorMsg;

    public VerifyRuleResultBean() {
    }

    public VerifyRuleResultBean(boolean resultFlag, String code, String errorMsg) {
        this.resultFlag = resultFlag;
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public VerifyRuleResultBean(String code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }
}
