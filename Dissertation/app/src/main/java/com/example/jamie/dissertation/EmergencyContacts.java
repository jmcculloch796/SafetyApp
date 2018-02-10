package com.example.jamie.dissertation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.provider.ContactsContract;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmergencyContacts extends Activity {


    List<String> names = new ArrayList<String>();
    List<String> numbers = new ArrayList<String>();
    ArrayList<String> appcontacts = new ArrayList<String>();

    ListView lv;
    ListView alv;
        /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        Intent intent = getIntent();
      //  appcontacts = intent.getStringArrayListExtra("savedContacts");
        readContacts();
    }

    public void openMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void addContact(View view){
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        startActivity(intent);
    }

    public void showSavedContacts(View view){
        Intent intent = new Intent(this, showContacts.class);
        intent.putStringArrayListExtra("savedContacts", appcontacts);
        startActivity(intent);
    }
    public void readContacts(){
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                   // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //  System.out.println("phone" + phone);
                        names.add(name);
                        numbers.add(phone);
                    }


                    pCur.close();

                }

            }
            int length = names.size();
            if (length != numbers.size()) { // Too many names, or too many numbers
                // Fail
            }
            final ArrayList<String> contacts = new ArrayList<String>(length); // Make a new list
            for (int i = 0; i < length; i++) { // Loop through every name/phone number combo
                contacts.add(names.get(i) + " " + numbers.get(i)); // Concat the two, and add it
            }
           // System.out.println(contacts);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    contacts);
            for (int i = 0; i < contacts.size(); i++) {

                lv = (ListView) findViewById(R.id.contactsView);


                lv.setAdapter(arrayAdapter);
            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(EmergencyContacts.this);
                    adb.setTitle("Add contact?");
                    adb.setMessage("Are you sure you want to add " + contacts.get(position) + " to emergency contacts?");
                    final int positionToAdd = position;
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            appcontacts.add(contacts.get(position));
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });
                    adb.show();
                    //System.out.println(appcontacts);
                }
            });

        }


            }
           // textout.append(contacts.toString());
           // textout.append("\n");
        }





