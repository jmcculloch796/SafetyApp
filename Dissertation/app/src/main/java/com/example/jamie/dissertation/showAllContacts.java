package com.example.jamie.dissertation;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class showAllContacts extends MainActivity {

    private TextView mTextMessage;
    ListView alv;
    static ArrayList<String> appcontacts = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> numbers = new ArrayList<String>();
    myDBHandler dbHandler = new myDBHandler(this, null, null, 1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinearLayout dynamicContent, bottonNavBar;
        super.onCreate(savedInstanceState);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar = (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_show_all_contacts, null);

        dynamicContent.addView(wizard);

        readContacts();


             /*  Intent intent = getIntent();
        appcontacts = intent.getStringArrayListExtra("savedContacts");
        if (appcontacts == null) {
            System.out.println("No added contacts yet");

        } else {
            for (int i = 0; i < appcontacts.size(); i++) {
                printToScreen();
            }

        }
        System.out.println(appcontacts);
*/
    }

    public void readContacts() {
        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        final String userName = sp.getString("userName", "missing");
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

               alv = (ListView) findViewById(R.id.showcontactsView);


                alv.setAdapter(arrayAdapter);
            }
            alv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(showAllContacts.this);
                    adb.setTitle("Add contact?");
                    adb.setMessage("Are you sure you want to add " + contacts.get(position) + " to emergency contacts?");
                    final int positionToAdd = position;
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String contactName1 = names.get(position);
                            String contactNumber = numbers.get(position);
                            ContactsDB contact =
                                    new ContactsDB(contactName1, contactNumber);

                            dbHandler.addProduct(contact);
                           SmsManager.getDefault().sendTextMessage(contactNumber, null, "You have been added as an emergency contact by " + userName + " through KeepSafe, expect some location updates!", null,null);


                        }
                    });
                    adb.show();
                    //System.out.println(appcontacts);
                }
            });

        }


    }


    public static ArrayList<String> getContacts(){
        return appcontacts;
    }
}



