package com.yufan.aop;

import org.aspectj.lang.annotation.Aspect;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 资源aop
 * @author: lirf
 * @time: 2021/7/10
 */
@Aspect
public class ResourceAop {


//    @Before()
//    public

    public static void main(String[] args) {
        Map<String, Object> classMap = new HashMap<>();
        String name = "aa";
        String v = "bb";
        classMap.put(name, v);
        //
        Map<String, String> map = new HashMap<>();
        map.put("name", "value");
        map.put("name2", "value2");
        classMap.put("methodResource", map);

        //
        for (Map.Entry<String, Object> mm : classMap.entrySet()) {
            String key = mm.getKey();
            Object value = mm.getValue();
            if (value instanceof Map) {
                Map<String, String> m2 = (Map<String, String>) value;
                for (Map.Entry<String, String> mmm : m2.entrySet()) {
                    System.out.println(mmm.getKey()+"==="+mmm.getValue());
                }
            }
        }

    }


}
