package com.yufan.checkenum;

/**
 * @description: 校验枚举
 * @author: lirf
 * @time: 2021/8/24
 */
public enum CheckVerifyDataEnum implements IVerifyRule {

    BW {
        @Override
        public VerifyRuleResultBean verifyData(String key, String value) {
            return null;
        }
    },
    IN {
        /**
         * 固定值
         * @param key
         * @param value
         * @return
         */
        @Override
        public VerifyRuleResultBean verifyData(String key, String value) {
            // 配置的固定值
            String cfgValue = VerifyRuleEnumImpl.valueOf(key).getValue();
            String[] values = cfgValue.split("|");
            boolean flag = false;
            for (int i = 0; i < values.length; i++) {
                if (value.equals(values[i])) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return new VerifyRuleResultBean(VerifyRuleEnumImpl.valueOf(key).getCode(), VerifyRuleEnumImpl.valueOf(key).getErrorMsg());
            }
            return new VerifyRuleResultBean();
        }
    }
}
