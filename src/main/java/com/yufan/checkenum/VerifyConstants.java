package com.yufan.checkenum;

/**
 * @description:
 * @author: lirf
 * @time: 2021/8/24
 */
public class VerifyConstants {

    /**
     * 数据类型
     */
    public static class ValueTypeClass {
        public static String VALUE_TYPE_STRING = "string";
        public static String VALUE_TYPE_INT = "int";
    }

    /**
     * 数据规则类型
     */
    public static class ValueRuleTypeClass {

        /**
         * 范围,包含两端
         */
        public static String BW = "bw";
        /**
         * 固定内容
         */
        public static String IN = "in";
    }
}
