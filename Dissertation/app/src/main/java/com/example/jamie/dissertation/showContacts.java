package com.example.jamie.dissertation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class showContacts extends AppCompatActivity {
    ListView alv;
    ArrayList<String> appcontacts = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts);
        //  alv = findViewById(R.id.contactsView);
        Intent intent = getIntent();
        appcontacts = intent.getStringArrayListExtra("savedContacts");
        this.setTitle("Saved contacts");
        if (appcontacts == null)
        {
            System.out.println("No added contacts yet");

        } else {
            for (int i =0; i < appcontacts.size(); i++){
                printToScreen();
            }

        }
        System.out.println(appcontacts);

    }


    public void printToScreen(){
        System.out.println("Test");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                appcontacts);


            for (int i = 0; i < appcontacts.size(); i++) {

                System.out.println("This is a test");
                System.out.println(appcontacts);
                alv = (ListView) findViewById(R.id.showcontactsView);
                alv.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            }
    }
    }



