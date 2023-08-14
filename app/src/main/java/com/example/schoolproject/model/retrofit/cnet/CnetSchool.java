package com.example.schoolproject.model.retrofit.cnet;


import com.google.gson.annotations.SerializedName;

public class CnetSchool {
    @SerializedName("campusName")
    private String campusName;
    @SerializedName("collegeinfourl")
    private String collegeinfourl;
    @SerializedName("schoolType")
    private String schoolType;
    @SerializedName("link")
    private String link;
    @SerializedName("schoolGubun")
    private String schoolGubun;
    @SerializedName("adres")
    private String adres;
    @SerializedName("schoolName")
    private String schoolName;
    @SerializedName("region")
    private String region;
    @SerializedName("totalCount")
    private String totalCount;
    @SerializedName("estType")
    private String estType;
    @SerializedName("seq")
    private String seq;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEstType() {
        return estType;
    }

    public void setEstType(String estType) {
        this.estType = estType;
    }

    public String getSchoolGubun() {
        return schoolGubun;
    }

    public void setSchoolGubun(String schoolGubun) {
        this.schoolGubun = schoolGubun;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getCollegeinfourl() {
        return collegeinfourl;
    }

    public void setCollegeinfourl(String collegeinfourl) {
        this.collegeinfourl = collegeinfourl;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }
}
