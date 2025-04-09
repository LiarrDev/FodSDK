package com.fodsdk.entities;

import com.google.gson.annotations.SerializedName;

public class FodGameConfig {

    @SerializedName("gid")
    private String gId;
    @SerializedName("pid")
    private String pId;
    @SerializedName("areaid")
    private String areaId;
    @SerializedName("osid")
    private String osId;
    @SerializedName("client")
    private String client;
    @SerializedName("pkid")
    private String pkId;
    @SerializedName("pcid")
    private String pcId;
    @SerializedName("cid")
    private String cId;
    @SerializedName("adid")
    private String adId;
    @SerializedName("sdkver")
    private String sdkVer;

    public String getGId() {
        return gId;
    }

    public void setGId(String gId) {
        this.gId = gId;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getOsId() {
        return osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getCId() {
        return cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getSdkVer() {
        return sdkVer;
    }

    public void setSdkVer(String sdkVer) {
        this.sdkVer = sdkVer;
    }

    @Override
    public String toString() {
        return "FodGameConfig{" +
                "gId='" + gId + '\'' +
                ", pId='" + pId + '\'' +
                ", areaId='" + areaId + '\'' +
                ", osId='" + osId + '\'' +
                ", client='" + client + '\'' +
                ", pkId='" + pkId + '\'' +
                ", pcId='" + pcId + '\'' +
                ", cId='" + cId + '\'' +
                ", adId='" + adId + '\'' +
                ", sdkVer='" + sdkVer + '\'' +
                '}';
    }
}
