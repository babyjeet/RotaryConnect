package com.android.aquagiraffe.rotaryconnect.model;

public class PdfDetailsWithImage
{
    String pdf_Name , strMessage;

    public String getPdf_Name()
    {
        return pdf_Name;
    }

//    public String getStrMessage() {
//        return strMessage;
//    }
//
//    public void setStrMessage(String strMessage) {
//        this.strMessage = strMessage;
//    }

    public PdfDetailsWithImage()
    {

    }

    //    @Override
//    public String toString() {
//        return "PdfDetailsWithImage{" +
//                "pdf_Name='" + pdf_Name + '\'' +
//                ", strMessage='" + strMessage + '\'' +
//                '}';
//    }
    @Override
    public String toString() {
        return "PdfDetailsWithImage{" +
                "pdf_Name='" + pdf_Name + '\'' +
                '}';
    }

    public PdfDetailsWithImage(String pdf_Name)
    {
        this.pdf_Name = pdf_Name;
    }

    public void setPdf_Name(String pdf_Name)
    {
        this.pdf_Name = pdf_Name;
    }

}
