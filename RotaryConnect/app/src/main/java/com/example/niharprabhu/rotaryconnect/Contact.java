package com.android.aquagiraffe.rotaryconnect;

public class Contact {
    //private variables
    int _id;
    String _fname;
    byte[] _img;

    // Empty constructor
    public Contact(){

    }
    // constructor
    public Contact(int id, String fname, byte[] img){
        this._id = id;
        this._fname = fname;
        this._img = img;

    }

    // constructor
    public Contact(String fname, byte[] img){

        this._fname = fname;
        this._img = img;

    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting first name
    public String getFName(){
        return this._fname;
    }

    // setting first name
    public void setFName(String fname){
        this._fname = fname;
    }

    //getting profile pic
    public byte[] getImage(){
        return this._img;
    }

    //setting profile pic

    public void setImage(byte[] b){
        this._img=b;
    }
}
