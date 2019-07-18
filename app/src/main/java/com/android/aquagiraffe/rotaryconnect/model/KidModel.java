package com.android.aquagiraffe.rotaryconnect.model;

public class KidModel {

    String Name,Contact,DOB,Gender,id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    @Override
    public String toString() {
        return "KidModel{" +
                "Name='" + Name + '\'' +
                ", Contact='" + Contact + '\'' +
                ", DOB='" + DOB + '\'' +
                ", Gender='" + Gender + '\'' +
                '}';
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
