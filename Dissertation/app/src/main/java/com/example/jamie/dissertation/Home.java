package com.example.jamie.dissertation;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.provider.LiveFolders.INTENT;
import static com.example.jamie.dissertation.showAllContacts.appcontacts;

public class Home extends MainActivity {

    private TextView mTextMessage;
    private TextView mTextField;
    myDBHandler dbHandler = new myDBHandler(this, null, null, 1);
    String[] savedContacts;
    String textContacts;
    CountDownTimer countdown;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000000000; // in Milliseconds
    protected LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_home);
        LinearLayout dynamicContent, bottonNavBar;
        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar = (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_home, null);

        dynamicContent.addView(wizard);

        savedContacts = dbHandler.getContacts();
        textContacts = Arrays.deepToString(savedContacts);
        int indexOfOpenBracket = textContacts.indexOf("[");
        int indexOfLastBracket = textContacts.lastIndexOf("]");

        textContacts = textContacts.substring(indexOfOpenBracket + 1, indexOfLastBracket);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.navigation_home);
        rb.setTextColor(Color.parseColor("#3F51B5"));

        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = sp.getString("userName", "missing");
        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage.setText("Welcome back, " + userName + "! Select an option from the navigation bar above to get going.");


        final EditText text = (EditText)findViewById(R.id.countdownTimer);

        Button start = (Button)findViewById(R.id.startCountdown);
        start.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
              int value = (Integer.parseInt(text.getText().toString()) * 60000);

                startJourneyTimer(value);
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }

        });
        Button stop = (Button) findViewById(R.id.stopCountdown);
        stop.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                mTextField.setText("Countdown cancelled!");

               stopJourneyTimer();



            }

        });         mTextField = (TextView) findViewById(R.id.displayCountdown);


    }
    public void startJourneyTimer(int value){
        countdown =  new CountDownTimer(value, 1000) {
            public void onTick(long millisUntilFinished) {

               // mTextField.setText("Time remaining: " + millisUntilFinished / 60000 + " minutes " +  + " seconds" );
                String text = String.format(Locale.getDefault(), "Time Remaining %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                mTextField.setText(text);
            }

            public void onFinish() {
                mTextField.setText("Update sent!");
               sendLoc();
            }
        }
                .start();
    }

    public void stopJourneyTimer(){
        if(countdown != null) {
            countdown.cancel();
            countdown = null;
        }
    }
    public void sendLoc(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new myLocationListener()
        );

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = sp.getString("userName", "missing");
        if (location != null) {
            String message = "A MESSAGE FROM KEEPSAFE: \n You can track " + userName + "'s location here: http://maps.google.com/maps?q=loc:" + String.format("%f,%f", location.getLatitude(), location.getLongitude());



            SmsManager.getDefault().sendTextMessage(textContacts, null, message, null,null);


        }
    }
}
