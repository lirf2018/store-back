package com.yufan.dao.param;

import com.yufan.pojo.TbParam;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/5 22:21
 * 功能介绍:
 */
public interface IParamCodeDao {

    public PageInfo loadParamCodePage(int currePage, TbParam param);

    public void updateParamCodeStatus(int paramId, int status);

    public List<Map<String,Object>> loadParamGroupName();


}
