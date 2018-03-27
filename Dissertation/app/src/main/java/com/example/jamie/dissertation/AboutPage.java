package com.example.jamie.dissertation;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AboutPage extends MainActivity {

    TextView aboutpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinearLayout dynamicContent, bottonNavBar;

        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_emerg_contacts);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar = (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_about_page, null);

        dynamicContent.addView(wizard);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.settings);
        rb.setTextColor(Color.parseColor("#3F51B5"));

    }
}
