package com.yufan.checkenum;

/**
 * @description:
 * @author: lirf
 * @time: 2021/8/24
 */
public enum VerifyRuleEnumImpl {

    TEST_KEY("TEST_KEY", "000001", "只能是0到1", "0|1", VerifyConstants.ValueTypeClass.VALUE_TYPE_INT, VerifyConstants.ValueRuleTypeClass.IN);

    private String key;//校验关键key
    private String code;//编码
    private String errorMsg;//错误描述
    private String value; //检验规则
    private String valueType;//校验值数据类型
    private String valueRuleKey;//校验值规则类型
    private CheckVerifyDataEnum checkVerifyDataEnum;

    VerifyRuleEnumImpl(String key, String code, String errorMsg, String value, String valueType, String valueRuleKey) {
        this.key = key;
        this.code = code;
        this.errorMsg = errorMsg;
        this.value = value;
        this.valueType = valueType;
        this.valueRuleKey = valueRuleKey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public CheckVerifyDataEnum getCheckVerifyDataEnum() {
        return checkVerifyDataEnum;
    }

    public void setCheckVerifyDataEnum(CheckVerifyDataEnum checkVerifyDataEnum) {
        this.checkVerifyDataEnum = checkVerifyDataEnum;
    }
}
