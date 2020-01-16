package com.yufan.service.info;

import com.yufan.bean.ComplainCondition;
import com.yufan.pojo.TbInfo;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/16 14:35
 * 功能介绍:
 */
public interface IInfoService {


    /**
     * 查询分页数据
     *
     * @param currePage
     * @param info
     * @return
     */
    PageInfo loadDataPage(int currePage, TbInfo info);

    public TbInfo loadInfo(int id);

    public int updateInfoStatus(int id, int status);

    public int saveInfo(TbInfo info);

}
