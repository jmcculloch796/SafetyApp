package com.example.jamie.dissertation;

/**
 * Created by jamie on 28/02/18.
 */

public class ContactsDB {

    private int _id;
    private String _contactname;
    private String _contactNo;

    public ContactsDB(){

    }

    public ContactsDB(int id, String contactname, String contactNo){
        this._id = id;
        this._contactname = contactname;
        this._contactNo = contactNo;
    }

    public ContactsDB(String contactname, String contactNo){
        this._contactname = contactname;
        this._contactNo = contactNo;
    }
    public void setID(int id){
        this._id = id;
    }

    public int getID(){
        return this._id;
    }

    public void setContactName(String contactname){
        this._contactname = contactname;
    }
    public String getContactName(){
        return this._contactname;
    }

    public void setContactNo(String contactNo){
        this._contactNo = contactNo;
    }

    public String getContactNo(){
        return this._contactNo;
    }
}
