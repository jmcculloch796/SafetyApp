package com.example.jamie.dissertation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;


public class RequestLocation extends MainActivity {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000000000; // in Milliseconds


    protected LocationManager locationManager;

    protected Button retrieveLocationButton;


    //myDBHandler db = new myDBHandler(this,null,null ,1);
    myDBHandler dbHandler = new myDBHandler(this, null, null, 1);
    String[] savedContacts;
    String textContacts;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        savedContacts = dbHandler.getContacts();


        textContacts = Arrays.deepToString(savedContacts);
        //  System.out.println("On this page  " + Arrays.deepToString(savedContacts));
        int indexOfOpenBracket = textContacts.indexOf("[");
        int indexOfLastBracket = textContacts.lastIndexOf("]");

        textContacts = textContacts.substring(indexOfOpenBracket + 1, indexOfLastBracket);
        LinearLayout dynamicContent, bottonNavBar;
        //    System.out.println("TEST---------------------");
        //  System.out.println(savedContacts);
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_request_location);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar = (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_request_location, null);

        dynamicContent.addView(wizard);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.requestLocation);
        rb.setTextColor(Color.parseColor("#3F51B5"));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
        TextView helper = (TextView) findViewById(R.id.Helper);

        helper.setText("You will have already sent a location update by clicking the location button but you" +
                " can remain on this page if you'd like to send your location again using the button below. Remember " +
                "you can change your settings to modify the text before it sends, or sending it automatically on button click");

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new myLocationListener()
        );
        openSend();

        retrieveLocationButton.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {

                openSend();

            }

        });
    }

    public void openSend() {
        //  settings st = new settings();
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        checkSend = prefs.getString("checkSend", "missing");
        System.out.println(checkSend);
        switch (checkSend) {
            case "nodisplay":
                showCurrentLocationNoDisplay();
                break;
            case "display":
                showCurrentLocationWithDisplay();
                break;
        }

    }


    protected void showCurrentLocationNoDisplay() {

        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = sp.getString("userName", "missing");
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (location != null) {
            String message = "A MESSAGE FROM KEEPSAFE: \n You can track " + userName + "'s location here: http://maps.google.com/maps?q=loc:" + String.format("%f,%f", location.getLatitude(), location.getLongitude());


            SmsManager.getDefault().sendTextMessage(textContacts, null, message, null, null);


        }
    }

    public void showCurrentLocationWithDisplay() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String userName = sp.getString("userName", "missing");

        if (location != null) {
            String message = "A MESSAGE FROM KEEPSAFE \n You can track " + userName + "'s location here: http://maps.google.com/maps?q=loc:" + String.format("%f,%f", location.getLatitude(), location.getLongitude()); //String.format(


            Uri sms_uri = Uri.parse("smsto:" + textContacts);
            Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, sms_uri);
            //intent.setType("vnd.android-dir/mms-sms");
            intent.putExtra("sms_body", message);
            startActivity(intent);

        }
    }
}