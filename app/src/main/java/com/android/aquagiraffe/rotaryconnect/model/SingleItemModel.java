package com.android.aquagiraffe.rotaryconnect.model;

public class SingleItemModel {
    private String name, url, description,nameArray,strProfileName;

    public SingleItemModel() {

    }

    public String getStrProfileName() {
        return strProfileName;
    }

    public void setStrProfileName(String strProfileName) {
        this.strProfileName = strProfileName;
    }

    public SingleItemModel(String name) {
        this.name = name;

    }


    public String getNameArray() {
        return nameArray;
    }

    public void setNameArray(String nameArray) {
        this.nameArray = nameArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
