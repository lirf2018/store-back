package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2021/1/16 23:47
 * 功能介绍:
 */
@Entity
@Table(name = "tb_page_menu", schema = "store_db", catalog = "")
public class TbPageMenu {
    private int id;
    private String menuName;
    private String menuImg;
    private Integer menuSort;
    private Integer menuType;
    private Integer relType;
    private String menuUrl;
    private Integer status;
    private String leve1Ids;
    private String categoryIds;
    private Timestamp createtime;
    private Integer shopId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "menu_name", nullable = true, length = 50)
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Basic
    @Column(name = "menu_img", nullable = true, length = 100)
    public String getMenuImg() {
        return menuImg;
    }

    public void setMenuImg(String menuImg) {
        this.menuImg = menuImg;
    }

    @Basic
    @Column(name = "menu_sort", nullable = true)
    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    @Basic
    @Column(name = "menu_type", nullable = true)
    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    @Basic
    @Column(name = "rel_type", nullable = true)
    public Integer getRelType() {
        return relType;
    }

    public void setRelType(Integer relType) {
        this.relType = relType;
    }

    @Basic
    @Column(name = "menu_url", nullable = true, length = 200)
    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "leve1_ids", nullable = true, length = 100)
    public String getLeve1Ids() {
        return leve1Ids;
    }

    public void setLeve1Ids(String leve1Ids) {
        this.leve1Ids = leve1Ids;
    }

    @Basic
    @Column(name = "category_ids", nullable = true, length = 100)
    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Basic
    @Column(name = "createtime", nullable = true)
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Basic
    @Column(name = "shop_id", nullable = true)
    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbPageMenu that = (TbPageMenu) o;
        return id == that.id &&
                Objects.equals(menuName, that.menuName) &&
                Objects.equals(menuImg, that.menuImg) &&
                Objects.equals(menuSort, that.menuSort) &&
                Objects.equals(menuType, that.menuType) &&
                Objects.equals(relType, that.relType) &&
                Objects.equals(menuUrl, that.menuUrl) &&
                Objects.equals(status, that.status) &&
                Objects.equals(leve1Ids, that.leve1Ids) &&
                Objects.equals(categoryIds, that.categoryIds) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(shopId, that.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuName, menuImg, menuSort, menuType, relType, menuUrl, status, leve1Ids, categoryIds, createtime, shopId);
    }
}
