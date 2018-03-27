package com.example.jamie.dissertation;

/**
 * Created by jamie on 28/02/18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class myDBHandler extends SQLiteOpenHelper {

    public myDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactsDB.db";
    private static final String TABLE_CONTACTS = "contacts";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CONTACTNAME = "contactName";
    public static final String COLUMN_CONTACTSNO = "contactNo";
    public String[] savedContacts;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " +
                TABLE_CONTACTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_CONTACTNAME
                + " TEXT," + COLUMN_CONTACTSNO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        putInArray(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addProduct(ContactsDB contacts) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACTNAME, contacts.getContactName());
        values.put(COLUMN_CONTACTSNO, contacts.getContactNo());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CONTACTS, null, values);
       // db.close();
    }

    public ContactsDB findProduct(String contactname, SQLiteDatabase db) {
        String query = "Select * FROM " + TABLE_CONTACTS + " WHERE " + COLUMN_CONTACTNAME + " =  \"" + contactname + "\"";

        //SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ContactsDB contacts = new ContactsDB();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            contacts.setID(Integer.parseInt(cursor.getString(0)));
            contacts.setContactName(cursor.getString(1));
            contacts.setContactNo(cursor.getString(2));
            cursor.close();
        } else {
            contacts = null;
        }
       // db.close();
        return contacts;
    }



    public boolean deleteProduct(String contactname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_CONTACTS + " WHERE " + COLUMN_CONTACTNAME + " =  \"" + contactname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ContactsDB contact = new ContactsDB();

        if (cursor.moveToFirst()) {
            contact.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(contact.getID())});
            cursor.close();
            result = true;
        }
       // db.close();
        return result;
    }

    public String[] getAll(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT contactName, contactNo FROM contacts", null);
        cursor.moveToFirst();

        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> number = new ArrayList<String>();

        while(!cursor.isAfterLast()) {
            name.add(cursor.getString(cursor.getColumnIndex("contactName")));
            number.add(cursor.getString(cursor.getColumnIndex("contactNo")));
            cursor.moveToNext();
        }
        final ArrayList<String> all = new ArrayList<String>(name.size()); // Make a new list
        for (int i = 0; i < name.size(); i++) { // Loop through every name/phone number combo
            all.add(name.get(i) + " " + number.get(i)); // Concat the two, and add it
        }
        cursor.close();
        System.out.println(all);
        return all.toArray(new String[all.size()]);
    }

    public String[] getContacts(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT contactNo FROM contacts", null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("contactNo")));
            cursor.moveToNext();
        }
        cursor.close();
        System.out.println(names);
        return names.toArray(new String[names.size()]);
    }

    public String[] getContactName(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT contactName FROM contacts", null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("contactName")));
            cursor.moveToNext();
        }
        cursor.close();
        System.out.println(names);
        return names.toArray(new String[names.size()]);
    }

    public ContactsDB putInArray( SQLiteDatabase db) {

        String query = "Select " + COLUMN_CONTACTSNO + " FROM " + TABLE_CONTACTS;

        //SQLiteDatabase db = this.getWritableDatabase();

        ContactsDB contacts = new ContactsDB();
        Cursor cursor = db.rawQuery(query, null);
        savedContacts = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            savedContacts[i] = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACTSNO));
            //avedContacts[i] = savedContactNumber;
            i++;
        }


        for (int j = 0; j < savedContacts.length; j++) {
            System.out.println(savedContacts[j]);
        }
        /*if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            contacts.setID(Integer.parseInt(cursor.getString(0)));
            contacts.setContactName(cursor.getString(1));
            contacts.setContactNo(cursor.getString(2));
            cursor.close();
        } else {
            contacts = null;
        }*/
        //db.close();

        return contacts;


    }}

