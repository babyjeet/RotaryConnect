package com.android.aquagiraffe.rotaryconnect.model;



public class Past {
    private String title, thumbnailUrl,yearImageUrl,year;
    private String strMemberId;

    public Past(String title, String thumbnailUrl, String yearImageUrl, String year, String strMemberId) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.yearImageUrl = yearImageUrl;
        this.year = year;
        this.strMemberId = strMemberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYearImageUrl() {
        return yearImageUrl;
    }

    public void setYearImageUrl(String yearImageUrl) {
        this.yearImageUrl = yearImageUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStrMemberId() {
        return strMemberId;
    }

    public void setStrMemberId(String strMemberId) {
        this.strMemberId = strMemberId;
    }

}
