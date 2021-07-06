package com.yufan.service.param;


import com.yufan.pojo.TbParam;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/22 17:37
 * 功能介绍:
 */
public interface IParamCodeService {

    TbParam loadTbParamCodeById(int id);

    List<TbParam> loadTbParamCodeList();

    List<TbParam> loadTbParamCodeList(int status);

    List<TbParam> loadTbParamCodeList(int status, String paramCode);

    List<TbParam> loadTbParamCodeList(int status, String paramCode, String paramKey);

    PageInfo loadParamCodePage(int currePage, TbParam param);

    int saveParamCode(TbParam param);

    public void updateParamCodeStatus(int paramId, int status);

    public List<Map<String, Object>> loadParamGroupName();

     public void refreshCache();

}