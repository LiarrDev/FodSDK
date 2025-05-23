package com.fodsdk.entities;

public class FodPayEntity {
    private String orderId;
    private String goodsId;
    private String goodsName;
    private String goodsDesc;
    private int goodsCount;
    private int price;
    private String ext;
    private FodRole role;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public int getPrice() {
        return price;
    }

    /**
     * 价格，单位：分
     */
    public void setPrice(int price) {
        this.price = price;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public FodRole getRole() {
        return role;
    }

    public void setRole(FodRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "FodPayEntity{" +
                "orderId='" + orderId + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsDesc='" + goodsDesc + '\'' +
                ", goodsCount=" + goodsCount +
                ", price=" + price +
                ", ext='" + ext + '\'' +
                ", role=" + role +
                '}';
    }
}
