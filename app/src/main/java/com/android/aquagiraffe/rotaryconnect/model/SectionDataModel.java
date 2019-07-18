package com.android.aquagiraffe.rotaryconnect.model;

import java.util.ArrayList;

public class SectionDataModel
{
    private String headerTitle;
    private ArrayList<SingleItemModel> allItemInSection;
    private String strdate;
    private String strImageName;
    public SectionDataModel() {
    }

    public SectionDataModel(String headerTitle, ArrayList<SingleItemModel> allItemInSection, String strdate, String strImageName) {
        this.headerTitle = headerTitle;
        this.allItemInSection = allItemInSection;
        this.strdate=strdate;
        this.strImageName=strImageName;
    }

    public String getStrImageName() {
        return strImageName;
    }

    public void setStrImageName(String strImageName) {
        this.strImageName = strImageName;
    }

    public String getStrdate() {
        return strdate;
    }

    public void setStrdate(String strdate) {
        this.strdate = strdate;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<SingleItemModel> getAllItemInSection() {
        return allItemInSection;
    }

    public void setAllItemInSection(ArrayList<SingleItemModel> allItemInSection) {
        this.allItemInSection = allItemInSection;
    }
}
