package com.yufan.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 17:51
 * 功能介绍:
 */
@Entity
@Table(name = "tb_second_goods", schema = "testlirf", catalog = "")
public class TbSecondGoods {
    private int id;
    private String goodsName;
    private BigDecimal truePrice;
    private BigDecimal nowPrice;
    private BigDecimal purchasePrice;
    private Integer readNum;
    private Integer likeNum;
    private Integer newInfo;
    private Integer isPost;
    private Integer aboutPrice;
    private Integer superLike;
    private String goodsInfo;
    private Integer status;
    private Timestamp createTime;
    private String img4;
    private String img3;
    private String img2;
    private String img1;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "goods_name", nullable = true, length = 100)
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Basic
    @Column(name = "true_price", nullable = true, precision = 2)
    public BigDecimal getTruePrice() {
        return truePrice;
    }

    public void setTruePrice(BigDecimal truePrice) {
        this.truePrice = truePrice;
    }

    @Basic
    @Column(name = "now_price", nullable = true, precision = 2)
    public BigDecimal getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(BigDecimal nowPrice) {
        this.nowPrice = nowPrice;
    }

    @Basic
    @Column(name = "purchase_price", nullable = true, precision = 2)
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @Basic
    @Column(name = "read_num", nullable = true)
    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    @Basic
    @Column(name = "like_num", nullable = true)
    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    @Basic
    @Column(name = "new_info", nullable = true)
    public Integer getNewInfo() {
        return newInfo;
    }

    public void setNewInfo(Integer newInfo) {
        this.newInfo = newInfo;
    }

    @Basic
    @Column(name = "is_post", nullable = true)
    public Integer getIsPost() {
        return isPost;
    }

    public void setIsPost(Integer isPost) {
        this.isPost = isPost;
    }

    @Basic
    @Column(name = "about_price", nullable = true)
    public Integer getAboutPrice() {
        return aboutPrice;
    }

    public void setAboutPrice(Integer aboutPrice) {
        this.aboutPrice = aboutPrice;
    }

    @Basic
    @Column(name = "super_like", nullable = true)
    public Integer getSuperLike() {
        return superLike;
    }

    public void setSuperLike(Integer superLike) {
        this.superLike = superLike;
    }

    @Basic
    @Column(name = "goods_info", nullable = true, length = -1)
    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TbSecondGoods that = (TbSecondGoods) o;
        return id == that.id &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(truePrice, that.truePrice) &&
                Objects.equals(nowPrice, that.nowPrice) &&
                Objects.equals(purchasePrice, that.purchasePrice) &&
                Objects.equals(readNum, that.readNum) &&
                Objects.equals(likeNum, that.likeNum) &&
                Objects.equals(newInfo, that.newInfo) &&
                Objects.equals(isPost, that.isPost) &&
                Objects.equals(aboutPrice, that.aboutPrice) &&
                Objects.equals(superLike, that.superLike) &&
                Objects.equals(goodsInfo, that.goodsInfo) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, goodsName, truePrice, nowPrice, purchasePrice, readNum, likeNum, newInfo, isPost, aboutPrice, superLike, goodsInfo, status);
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
    @Column(name = "img4", nullable = true, length = 50)
    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    @Basic
    @Column(name = "img3", nullable = true, length = 50)
    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    @Basic
    @Column(name = "img2", nullable = true, length = 50)
    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    @Basic
    @Column(name = "img1", nullable = true, length = 50)
    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }
}
