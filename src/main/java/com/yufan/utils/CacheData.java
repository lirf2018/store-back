package com.yufan.utils;

import com.yufan.pojo.TbParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/26 16:46
 * 功能介绍:  缓存数据
 */
public class CacheData {

    /**
     * 参数列表
     */
    public static List<TbParam> PARAMLIST = new ArrayList<>();


    /**
     * 项目检索到的资源(用于权限控制)
     */
    public static List<Map<String, Object>> RESOURCELIST = new ArrayList<>();


}
