package com.example.jamie.dissertation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class AlertPage extends AppCompatActivity {


    Button sendLoc;
    Button settings;
    Button phone999;
    Button recordVideo;

    CountDownTimer countdown;
    private static final int VIDEO_CAPTURE = 101;
    SharedPreferences.Editor editor;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    myDBHandler dbHandler = new myDBHandler(this, null, null, 1);
    String[] savedContacts;
    String textContacts;

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000000000; // in Milliseconds
    private TextView mTextField;
    protected LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alert_page);
        savedContacts = dbHandler.getContacts();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLUE);
        }
        textContacts = Arrays.deepToString(savedContacts);
        //  System.out.println("On this page  " + Arrays.deepToString(savedContacts));
        int indexOfOpenBracket = textContacts.indexOf("[");
        int indexOfLastBracket = textContacts.lastIndexOf("]");

        textContacts = textContacts.substring(indexOfOpenBracket + 1, indexOfLastBracket);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sendLoc = findViewById(R.id.sendLoc);
        settings = findViewById(R.id.settings);
        phone999 = findViewById(R.id.phone999);
        recordVideo = findViewById(R.id.recordVideo);

        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        editor = prefs.edit();

        mTextField = (TextView) findViewById(R.id.countdownTimer);
        mTextField.setText("Enter a time (in seconds) below and start a countdown to send location to all emergency contacts");

        final EditText secondTimer = (EditText) findViewById(R.id.seconds);

        Button saveTime = (Button) findViewById(R.id.saveTime);
        saveTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               int  x = (Integer.parseInt(secondTimer.getText().toString()) * 1000);
                countdown(x);
                secondTimer.setText("0");

            }
        });

        Button stopTimer = (Button) findViewById(R.id.stopTimer);
        stopTimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                stopTimer();
                mTextField.setText("Update cancelled!");

            }
        });


   /*     final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendLoc();
            }
        }, i);
*/
    /*    new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("done!");
            }
        }
                .start();*/



    }
    public void stopTimer(){
        if(countdown != null) {
            countdown.cancel();
            countdown = null;
        }
    }
    public void buttonSendLoc(View view){
        sendLoc();
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

    public void phone999(View view){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:123456789"));
        startActivity(callIntent);
    }

    public void recordVideo(View view){

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Uri videoUri = Uri.fromFile(mediaFile);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    public void openStandard(View view){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void countdown(int x){
        countdown = new CountDownTimer(x, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("Update sent!");
                sendLoc();

            }
        }
        .start();

    }
}
