package com.yufan.checkenum;

/**
 * @description:
 * @author: lirf
 * @time: 2021/8/24
 */
public class TestMain {

    public static void main(String[] args) {

        String key = "TEST_KEY";
        String value = "3";
        if(VerifyRuleEnumImpl.valueOf(key).getCheckVerifyDataEnum() == null){
            System.out.println("===============");
        }
        System.out.println(VerifyRuleEnumImpl.valueOf(key).getCheckVerifyDataEnum().verifyData(key, value));
    }
}
