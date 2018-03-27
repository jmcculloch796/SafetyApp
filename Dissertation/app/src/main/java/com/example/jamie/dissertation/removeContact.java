package com.example.jamie.dissertation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class removeContact extends MainActivity {
    TextView idView;
    EditText contactName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinearLayout dynamicContent, bottonNavBar;

        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_emerg_contacts);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar = (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_remove_contact, null);

        dynamicContent.addView(wizard);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.emergContacts);
        rb.setTextColor(Color.parseColor("#3F51B5"));
        idView = (TextView) findViewById(R.id.contactID);
        contactName = (EditText) findViewById(R.id.contactName);

    }

    public void removeContact(View view){


        myDBHandler dbHandler = new myDBHandler(this, null,
                null, 1);

        boolean result = dbHandler.deleteProduct(
                contactName.getText().toString());

        if (result) {
            idView.setText("Record Deleted");
            contactName.setText("");
        } else
            idView.setText("No Match Found");
    }



}