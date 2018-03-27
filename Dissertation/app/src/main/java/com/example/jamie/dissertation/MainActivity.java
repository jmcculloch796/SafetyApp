package com.example.jamie.dissertation;

import android.app.NotificationManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;

import static android.media.RingtoneManager.TYPE_ALARM;


public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "MainActivity";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    public static final String PREFS_NAME = "MyPrefs";
    RadioGroup radiogroup1;
    String state;
    RadioButton deals;
    String checkPanicSettings;
    String checkSend;
    SharedPreferences sharedPreferences;
    SharedPreferences mSettings = null;
    SharedPreferences.Editor editor;
    private static final int VIDEO_CAPTURE = 101;
    //haredPreferences mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        displayLocationSettingsRequest(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        deals = (RadioButton) findViewById(R.id.home);
        mSettings = getSharedPreferences("Settings", MODE_PRIVATE);
        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent in;
                switch (checkedId) {
                    case R.id.navigation_home:
                        in = new Intent(getBaseContext(), Home.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.emergContacts:
                        in = new Intent(getBaseContext(), emergContacts.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_camera:
                        //File mediaFile =
                        //      new File(context.getFilesDir(),"/myvideo.mp4");
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        // Uri videoUri = Uri.fromFile(mediaFile);
                        //intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                        startActivityForResult(intent, VIDEO_CAPTURE);
                        break;
                    case R.id.settings:
                        in = new Intent(getBaseContext(), settings.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.requestLocation:
                        in = new Intent(getBaseContext(), RequestLocation.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.maps:
                        in = new Intent(getBaseContext(), MapsActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(mSettings.getBoolean("firstrun", true)){
            Intent in = new Intent(getBaseContext(), settingsFirstTime.class);
            startActivity(in);
            mSettings.edit().putBoolean("firstrun", false).apply();
        }
    }

    public void changeState() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        state = prefs.getString("state", "missing");

        switch (state) {
            case "Alert":
                Intent in = new Intent(this, AlertPage.class);
                startActivity(in);
                break;
            case "Standard":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.BLACK);
                }
                break;
        }

    }

    public void openAlert(View view){
        Intent in = new Intent(this, AlertPage.class);
        startActivity(in);
    }

    public void openPanic(View view) throws IOException {
        //  settings st = new settings();
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        checkPanicSettings = prefs.getString("checkPanicSettings", "missing");
        System.out.println(checkPanicSettings);
        switch (checkPanicSettings) {
            case "phone":
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:123456789"));
                startActivity(callIntent);
                break;
            case "sendLoc":
                Intent in;
                in = new Intent(getBaseContext(), RequestLocation.class);
                startActivity(in);
                break;
            case "record":
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                // Uri videoUri = Uri.fromFile(mediaFile);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                startActivityForResult(intent, VIDEO_CAPTURE);
                break;
            case "alertSound":
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.alarm);
                mp.start();
                /*try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                break;
        }
    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
}

