package com.example.jamie.dissertation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class addContacts extends MainActivity {

    TextView idView;
    EditText contactName;
    EditText contactNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LinearLayout dynamicContent, bottonNavBar;

        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_emerg_contacts);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar = (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_add_contacts, null);

        dynamicContent.addView(wizard);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.emergContacts);
        rb.setTextColor(Color.parseColor("#3F51B5"));
        idView = (TextView) findViewById(R.id.contactID);
        contactName = (EditText) findViewById(R.id.contactName);
        contactNo =
                (EditText) findViewById(R.id.contactNo);
    }

    public void add(View view){
        myDBHandler dbHandler = new myDBHandler(this, null, null, 1);
        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = sp.getString("userName", "missing");
        String contactName1 = contactName.getText().toString();
        String contactNumber = contactNo.getText().toString();
        ContactsDB contact =
                new ContactsDB(contactName1, contactNumber);

        dbHandler.addProduct(contact);
        idView.setText("Contact added");
        SmsManager.getDefault().sendTextMessage(contactNumber, null, "You have been added as an emergency contact by " + userName + " through KeepSafe, expect some location updates!", null,null);

        contactName.setText("");
        contactNo.setText("");
    }

}
