package com.android.aquagiraffe.rotaryconnect.model;



public class Past {
    private String title, thumbnailUrl,yearImageUrl,year;
    private String strMemberId;

    public Past() {
    }

    public Past(String name, String thumbnailUrl, String year, String strMemberId,
                 String yearImageUrl) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.strMemberId = strMemberId;
        this.thumbnailUrl = yearImageUrl;
    }

    // Name of Memmber
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    // Thumbnail url
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    // Year of President
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    //Member ID
    public String getRating() {
        return strMemberId;
    }

    public void setRating(String rating) {
        this.strMemberId = rating;
    }


   // image url of year
    public String getYearImageUrl() {
        return yearImageUrl;
    }

    public void setYearImageUrl(String yearImageUrl) {
        this.yearImageUrl = yearImageUrl;
    }
}
