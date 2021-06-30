package com.yufan.service.verify;

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
public interface IVerifyImgService {

    /**
     * 查询分页数据
     *
     * @param currePage
     * @param condition
     * @return
     */
    PageInfo loadDataPage(int currePage, TbVerifyImg condition);

    TbVerifyImgGroup findVerifyImgGroup(TbVerifyImgGroup imgGroup);

    boolean checkVerifyImgCode(Integer id, String verifyImgCode);

    List<Map<String, Object>> loadVerifyImgList(String verifyCode);

    void updateVerifyGroupStatus(int id, int status);

    void updateVerifyImgStatus(int id, int status);

    void updateVerifyGroup(TbVerifyImgGroup imgGroup, String oldCode);

    void addVerifyImg(TbVerifyImg verifyImg);

}
