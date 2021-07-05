package com.yufan.dao.verify;

import com.yufan.bean.VerifyImgGroupCondition;
import com.yufan.pojo.TbVerifyImg;
import com.yufan.pojo.TbVerifyImgGroup;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/28
 */
public interface IVerifyImgDao {


    /**
     * 查询分页数据
     *
     * @param currePage
     * @param condition
     * @return
     */
    PageInfo loadDataPage(int currePage, VerifyImgGroupCondition condition);

    TbVerifyImgGroup findVerifyImgGroup(TbVerifyImgGroup imgGroup);

    List<Map<String, Object>> loadVerifyImgList(String verifyCode, Integer status);

    void updateVerifyGroupStatus(int id, int status);

    void updateVerifyImgStatus(int id, int status);

    boolean checkVerifyImgCode(Integer id, String verifyImgCode);

    void updateVerifyGroup(TbVerifyImgGroup imgGroup, String oldCode);

    void addVerifyImg(TbVerifyImg verifyImg);

    void updateVerifyImg(TbVerifyImg verifyImg);

    void updateBackImg(int id, String img);

}
