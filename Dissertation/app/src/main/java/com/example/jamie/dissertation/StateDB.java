package com.example.jamie.dissertation;

/**
 * Created by jamie on 01/03/18.
 */

public class StateDB {

    private int _id;
    private String _choice;

    public StateDB(){

    }

    public StateDB(int id, String choice){
        this._id = id;
        this._choice = choice;
    }


    public void setID(int id){
        this._id = id;
    }

    public int getID(){
        return this._id;
    }

    public void setStateName(String choice){
        this._choice = choice;
    }

    public String getStateName(){
        return this._choice;
    }

}
