package com.android.aquagiraffe.rotaryconnect.model;

import java.util.ArrayList;

public class PdfTitleModel
{
    private String headerTitle,strMessageAnouns;
    private ArrayList<PdfDetailsWithImage> allItemInSection;

    public PdfTitleModel()
    {
    }

    public PdfTitleModel(String headerTitle,ArrayList<PdfDetailsWithImage> allItemInSection, String strMessageAnouns)
    {
        this.headerTitle = headerTitle;
        this.allItemInSection = allItemInSection;
        this.strMessageAnouns = strMessageAnouns;
    }

    public String getStrMessageAnouns() {
        return strMessageAnouns;
    }

    public void setStrMessageAnouns(String strMessageAnouns) {
        this.strMessageAnouns = strMessageAnouns;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getheaderTitle()
    {
        return headerTitle;

    }

    public ArrayList<PdfDetailsWithImage> getAllItemInSection()
    {
        return allItemInSection;
    }

    public void setAllItemInSection(ArrayList<PdfDetailsWithImage> allItemInSection)
    {
        this.allItemInSection = allItemInSection;
    }
    public void setTitle(String headerTitle)
    {
        headerTitle = headerTitle;
    }

//    @Override
//    public String toString()
//    {
//        return "PdfTitleModel{" +
//                "headerTitle='" + headerTitle + '\'' +
//                ", allItemInSection=" + allItemInSection +
//                '}';
//    }

    @Override
    public String toString()
    {
        return "PdfTitleModel{" +
                "headerTitle='" + headerTitle + '\'' +
                ", strMessageAnouns='" + strMessageAnouns + '\'' +
                ", allItemInSection=" + allItemInSection +
                '}';
    }
}
