package com.yufan.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/16 15:13
 * 功能介绍:
 */
@Entity
@Table(name = "tb_mendian", schema = "store_db", catalog = "")
public class TbMendian {
    private int storeId;
    private String storeName;
    private String storeDetail;
    private Byte status;
    private String storeLng;
    private String storeLat;
    private String storeTel;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Integer shopId;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "store_id", nullable = false)
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Basic
    @Column(name = "store_name", nullable = true, length = 100)
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Basic
    @Column(name = "store_detail", nullable = true, length = 255)
    public String getStoreDetail() {
        return storeDetail;
    }

    public void setStoreDetail(String storeDetail) {
        this.storeDetail = storeDetail;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "store_lng", nullable = true, length = 50)
    public String getStoreLng() {
        return storeLng;
    }

    public void setStoreLng(String storeLng) {
        this.storeLng = storeLng;
    }

    @Basic
    @Column(name = "store_lat", nullable = true, length = 50)
    public String getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(String storeLat) {
        this.storeLat = storeLat;
    }

    @Basic
    @Column(name = "store_tel", nullable = true, length = 15)
    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time", nullable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
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
        TbMendian tbMendian = (TbMendian) o;
        return storeId == tbMendian.storeId &&
                Objects.equals(storeName, tbMendian.storeName) &&
                Objects.equals(storeDetail, tbMendian.storeDetail) &&
                Objects.equals(status, tbMendian.status) &&
                Objects.equals(storeLng, tbMendian.storeLng) &&
                Objects.equals(storeLat, tbMendian.storeLat) &&
                Objects.equals(storeTel, tbMendian.storeTel) &&
                Objects.equals(createTime, tbMendian.createTime) &&
                Objects.equals(updateTime, tbMendian.updateTime) &&
                Objects.equals(shopId, tbMendian.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, storeName, storeDetail, status, storeLng, storeLat, storeTel, createTime, updateTime, shopId);
    }
}
