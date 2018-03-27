package com.example.jamie.dissertation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class settingsFirstTime extends AppCompatActivity {

    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    EditText nameField;
    RadioGroup panic;
    RadioGroup loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = mSettings.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_first_time);
        panic = (RadioGroup) findViewById(R.id.panicButtonGroup);
        loc = (RadioGroup) findViewById(R.id.locationSettingsGroup);


        String userName = mSettings.getString("userName", "missing");
        nameField = (EditText) findViewById(R.id.nameField);
        nameField.setText("");



    }

    public void openHome(View view){
        if (TextUtils.isEmpty(nameField.getText())){
            nameField.setError("You have not filled in everything");
        }
        else if (panic.getCheckedRadioButtonId() != -1 && loc.getCheckedRadioButtonId() != -1){
            String userName = nameField.getText().toString();
            System.out.println(userName);
            editor.putString("userName", userName);
            editor.apply();
            Intent in = new Intent(getBaseContext(), Home.class);
            startActivity(in);
        }
        else {
            Toast.makeText(this, "Ensure you have selected something for all settings!", Toast.LENGTH_SHORT).show();
        }
    }



    public void checkSendSettings(View view) {
        switch (view.getId()) {
            case R.id.display:
                editor.putString("checkSend", "display");
                editor.apply();

                break;

            case R.id.nodisplay:
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


    public void checkPanic(View view) {

        // Is the button now checked?
        // boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clickedz
        switch (view.getId()) {
            case R.id.Call999:
                editor.putString("checkPanicSettings", "phone");
                editor.apply();
                break;

            case R.id.batchSend:
                editor.putString("checkPanicSettings", "sendLoc");
                editor.apply();
                break;
            case R.id.openVideo:
                editor.putString("checkPanicSettings", "record");
                editor.apply();
                break;
            case R.id.alertSound:
                editor.putString("checkPanicSettings", "alertSound");
                editor.apply();
                break;
        }
    }
}
