package com.yufan.dao.verify.impl;

import com.yufan.bean.VerifyImgGroupCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.verify.IVerifyImgDao;
import com.yufan.pojo.TbVerifyImg;
import com.yufan.pojo.TbVerifyImgGroup;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/28
 */
@Transactional
@Repository
public class VerifyImgDaoImpl implements IVerifyImgDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadDataPage(int currePage, VerifyImgGroupCondition condition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select g.id,g.verify_code,g.verify_title,g.status,DATE_FORMAT(g.createtime,'%Y-%m-%d %T') as createtime,g.similar_type,p.param_value as similar_type_name ");
        sql.append(" ,CONCAT('").append(Constants.IMG_WEB_URL).append("',g.back_img) as back_img_show,g.back_img ");
        sql.append(" from tb_verify_img_group g ");
        sql.append(" left join tb_param p on p.param_code='similar_type' and p.param_key=g.similar_type and p.`status`=1  ");
        sql.append(" where 1=1  ");
        if (!StringUtils.isEmpty(condition.getVerifyCode())) {
            sql.append(" and g.verify_code like '%").append(condition.getVerifyCode().trim()).append("%' ");
        }
        if (!StringUtils.isEmpty(condition.getImgUuid())) {
            sql.append(" and g.verify_code in (select verify_code from tb_verify_img where img_uuid like '%").append(condition.getImgUuid().trim()).append("%' ) ");
        }
        if (condition.getStatus() != null) {
            sql.append(" and g.status = ").append(condition.getStatus()).append(" ");
        }
        if (condition.getSimilarType() != null) {
            sql.append(" and g.similar_type = ").append(condition.getSimilarType()).append(" ");
        }
        sql.append(" ORDER BY g.createtime desc  ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public TbVerifyImgGroup findVerifyImgGroup(TbVerifyImgGroup imgGroup) {
        String hql = " from TbVerifyImgGroup where verifyCode=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, imgGroup.getVerifyCode());
    }

    @Override
    public List<Map<String, Object>> loadVerifyImgList(String verifyCode, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select vi.id,vi.verify_code,CONCAT('").append(Constants.IMG_WEB_URL).append("',vi.verify_img) as verify_img,vi.img_uuid, ");
        sql.append(" vi.status,vi.verify_type,vi.word, vi.verify_img as verify_img_path, ");
        sql.append(" DATE_FORMAT(vi.createtime,'%Y-%m-%d %T') as createtime,ig.id as group_id ");
        sql.append(" from tb_verify_img vi join tb_verify_img_group ig on ig.verify_code = vi.verify_code where 1=1  ");
        sql.append(" and vi.verify_code = '").append(verifyCode.trim()).append("' ");
        if (status != null) {
            sql.append(" and vi.status = ").append(status).append(" ");
        }
        sql.append(" ORDER BY vi.createtime desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void updateVerifyGroupStatus(int id, int status) {
        String sql = " update tb_verify_img_group set status=? where id=?  ";
        iGeneralDao.executeUpdateForSQL(sql, status, id);
    }

    @Override
    public void updateVerifyImgStatus(int id, int status) {
        String sql = " update tb_verify_img set status=? where id=?  ";
        iGeneralDao.executeUpdateForSQL(sql, status, id);
    }

    @Override
    public boolean checkVerifyImgCode(Integer id, String verifyImgCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tb_verify_img_group where 1=1 ");
        sql.append(" and verify_code='").append(verifyImgCode.trim()).append("' ");
        if (id != null && id > 0) {
            sql.append(" and id != ").append(id).append(" ");
        }
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString());
        if (null != list && list.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void updateVerifyGroup(TbVerifyImgGroup imgGroup, String oldCode) {
        if (!StringUtils.isEmpty(oldCode)) {
            String sql = " update tb_verify_img set verify_code=? where verify_code=? ";
            iGeneralDao.executeUpdateForSQL(sql, imgGroup.getVerifyCode(), oldCode);
        }
        iGeneralDao.saveOrUpdate(imgGroup);
    }

    @Override
    public void addVerifyImg(TbVerifyImg verifyImg) {
        iGeneralDao.save(verifyImg);
    }

    @Override
    public void updateVerifyImg(TbVerifyImg verifyImg) {
        iGeneralDao.saveOrUpdate(verifyImg);
    }

    @Override
    public void updateBackImg(int id, String img) {
        String sql = " update tb_verify_img_group set back_img=? where id=? ";
        iGeneralDao.executeUpdateForSQL(sql, img, id);
    }
}
