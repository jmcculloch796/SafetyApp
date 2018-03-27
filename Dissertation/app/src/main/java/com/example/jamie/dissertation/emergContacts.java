package com.example.jamie.dissertation;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class emergContacts extends MainActivity {

    ArrayList<String> appcontacts = new ArrayList<String>();
    TextView idView;
    EditText contactName;
    EditText contactNo;
    ListView lv;
    String phoneno;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        LinearLayout dynamicContent, bottonNavBar;

        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_emerg_contacts);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar = (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_emerg_contacts, null);

        dynamicContent.addView(wizard);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.emergContacts);
        rb.setTextColor(Color.parseColor("#3F51B5"));


       // lv = (ListView) findViewById(R.id.contactsView);
        showContacts();



    }

 public void buttonShowAll(View view){
    showContacts();
 }

 public void showContacts() {

     final myDBHandler dbHandler = new myDBHandler(this, null, null, 1);

     final String[] allContacts = dbHandler.getAll();
     final String[] phoneno = dbHandler.getContacts();
     final String[] contactname = dbHandler.getContactName();
     final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
             this,
             android.R.layout.simple_list_item_1,
             allContacts);

     for (int i = 0; i < allContacts.length; i++) {

         lv = (ListView) findViewById(R.id.contactsView);


         lv.setAdapter(arrayAdapter);
     }
     if (lv != null){
     lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                   public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                                       AlertDialog.Builder adb = new AlertDialog.Builder(emergContacts.this);
                                       adb.setTitle("EmergencyContacts");
                                       adb.setMessage("Would you like to phone " + contactname[position] + "?");
                                       adb.setNegativeButton("Cancel", null);
                                       adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {
                                               String number = phoneno[position];
                                               Intent callIntent = new Intent(Intent.ACTION_CALL);
                                               System.out.println(phoneno);
                                               callIntent.setData(Uri.parse("tel:" + number));
                                               startActivity(callIntent);
                                           }
                                       });
                                       adb.show();
                                   }
                               });
     /*int arraySize = allContacts.length;
     for(int i = 0; i < arraySize; i++) {
         lv.append("Name and Number of Contact: " + allContacts[i] + "\n");
     }

         });
    }*/
 }
 }


 public void addContact(View view){
     AlertDialog.Builder adb = new AlertDialog.Builder(emergContacts.this);
     adb.setTitle("Add contact?");
     adb.setMessage("Would you like to add a contact manually or through existing contacts on the phone?");
     adb.setNegativeButton("Manual", new AlertDialog.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                     newContactByManual();
                 }


             });
     adb.setPositiveButton("Phone contacts", new AlertDialog.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
             newContactByPhone();
         }
     });
     adb.show();
 }
    public void newContactByManual(){
     Intent in = new Intent(this, addContacts.class);
     startActivity(in);
    }

    public void newContactByPhone() {

      Intent in = new Intent(this, showAllContacts.class);
      startActivity(in);
    }

    public void removeContact(View view) {
        Intent in = new Intent (this, removeContact.class);
        startActivity(in);
    }
}