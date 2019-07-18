package com.android.aquagiraffe.rotaryconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_POTO = "poto";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CONTACTS="CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID +" INTEGER PRIMARY KEY,"
                + KEY_FNAME +" TEXT,"
                + KEY_POTO  +" BLOB" + ")";
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //Insert values to the table contacts
    public void addContacts(Contact contact){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values=new ContentValues();

        values.put(KEY_FNAME, contact.getFName());
        values.put(KEY_POTO, contact.getImage());


        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }


    /**
     *Getting All Contacts
     **/

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setFName(cursor.getString(1));
                contact.setImage(cursor.getBlob(2));


                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    /**
     *Updating single contact
     **/

    public int updateContact(Contact contact, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, contact.getFName());
        values.put(KEY_POTO, contact.getImage());


        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /**
     *Deleting single contact
     **/

    public void deleteContact(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(Id) });
        db.close();
    }

}
