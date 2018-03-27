package com.example.jamie.dissertation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

public class settings extends MainActivity {
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = mSettings.edit();
        super.onCreate(savedInstanceState);
        //     setContentView(R.layout.activity_settings2);

        LinearLayout dynamicContent, bottonNavBar;
        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar = (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_settings2, null);

        dynamicContent.addView(wizard);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.settings);
        rb.setTextColor(Color.parseColor("#A9A9A9"));

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //keeps keyboard from opening on startup


        String userName = mSettings.getString("userName", "missing");
        final EditText nameField = (EditText) findViewById(R.id.nameField);
        nameField.setText(userName);
        Button saveName = (Button) findViewById(R.id.nameButton);
        saveName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    String userName = nameField.getText().toString();
                    System.out.println(userName);
                    editor.putString("userName", userName);
                    editor.apply();
            }
        });

        checkAllRadioButtons();

    }

    public void checkAllRadioButtons(){
        checkSendRadioButtons();
        checkPanicRadioButtons();
        checkStateRadioButtons();
    }
    public void checkSendRadioButtons(){
        String checkDisplay = mSettings.getString("checkSend", "missing");
        RadioButton display = (RadioButton) findViewById(R.id.display);
        RadioButton nodisplay = (RadioButton) findViewById(R.id.nodisplay);

        if (checkDisplay.equals("display")){
            display.setChecked(true);
        }
        else {
            nodisplay.setChecked(true);
        }
    }

    public void checkStateRadioButtons(){
        String checkState = mSettings.getString("state", "missing");
        RadioButton standard = (RadioButton) findViewById(R.id.std);
        RadioButton alert = (RadioButton) findViewById(R.id.alert);

            standard.setChecked(true);
        }
    public void checkPanicRadioButtons(){
        RadioButton call999 = (RadioButton) findViewById(R.id.Call999);
        RadioButton batchLoc = (RadioButton) findViewById(R.id.batchSend);
        RadioButton recordVideo = (RadioButton) findViewById(R.id.openVideo);
        RadioButton alertSound = (RadioButton) findViewById(R.id.alertSound);
        String checkPanic = mSettings.getString("checkPanicSettings", "missing");
        if (checkPanic.equals("phone")){
            call999.setChecked(true);
        }
        else if (checkPanic.equals("sendLoc")){
            batchLoc.setChecked(true);
        }
        else if (checkPanic.equals("record")){
            recordVideo.setChecked(true);
        }
        else if (checkPanic.equals("alertSound")){
            alertSound.setChecked(true);
        }
    }


    public void checkSendSettings(View view) {
        switch (view.getId()) {
            case R.id.display:
                this.checkSend = "display";
                editor.putString("checkSend", "display");
                editor.apply();

                break;

            case R.id.nodisplay:
                this.checkSend = "nodisplay";
                editor.putString("checkSend", "nodisplay");
                editor.apply();
                break;
        }
    }

    public void aboutApp(View view) {
        Intent in = new Intent(getBaseContext(), AboutPage.class);
        startActivity(in);
        overridePendingTransition(0, 0);

    }

    public void checkState(View view) {
        switch (view.getId()) {
            case R.id.std:
                this.state = "Standard";
                editor.putString("state", "Standard");
                editor.apply();
                changeState();
                break;
            case R.id.alert:
                this.state = "Alert";
                editor.putString("state", "Alert");
                editor.apply();
                changeState();
                break;
        }
    }

    public void checkPanic(View view) {

        // Is the button now checked?
        // boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clickedz
        switch (view.getId()) {
            case R.id.Call999:
                this.checkPanicSettings = "phone";
                editor.putString("checkPanicSettings", "phone");
                editor.apply();
                break;

            case R.id.batchSend:
                this.checkPanicSettings = "sendLoc";
                editor.putString("checkPanicSettings", "sendLoc");
                editor.apply();
                break;
            case R.id.openVideo:
                this.checkPanicSettings = "record";
                editor.putString("checkPanicSettings", "record");
                editor.apply();
                break;
            case R.id.alertSound:
                this.checkPanicSettings = "alertSound";
                editor.putString("checkPanicSettings", "alertSound");
                editor.apply();
                break;
        }
    }
}

