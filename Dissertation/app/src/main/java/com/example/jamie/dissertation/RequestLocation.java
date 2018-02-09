package com.example.jamie.dissertation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class RequestLocation extends Activity {

        // Assume thisActivity is the current activity


        private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters

        private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000000000; // in Milliseconds



        protected LocationManager locationManager;



        protected Button retrieveLocationButton;



        @Override

        public void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_request_location);


            retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);


            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            int permissionCheck = ContextCompat.checkSelfPermission(RequestLocation.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MINIMUM_TIME_BETWEEN_UPDATES,
                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                    new MyLocationListener()
            );


            retrieveLocationButton.setOnClickListener(new OnClickListener() {

                @Override

                public void onClick(View v) {

                    showCurrentLocation();

                }

            });

        }


        protected void showCurrentLocation() {

            int permissionCheck = ContextCompat.checkSelfPermission(RequestLocation.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



            if (location != null) {

                String message = String.format(

                        "Current Location \n Longitude: %1$s \n Latitude: %2$s",

                        location.getLongitude(), location.getLatitude()

                );

                Toast.makeText(RequestLocation.this, message,

                        Toast.LENGTH_LONG).show();

            }



        }



        private class MyLocationListener implements LocationListener {



            public void onLocationChanged(Location location) {

                String message = String.format(

                        "New Location \n Longitude: %1$s \n Latitude: %2$s",

                        location.getLongitude(), location.getLatitude()

                );

                Toast.makeText(RequestLocation.this, message, Toast.LENGTH_LONG).show();

            }
            public void onStatusChanged(String s, int i, Bundle b) {

                Toast.makeText(RequestLocation.this, "Provider status changed",

                        Toast.LENGTH_LONG).show();

            }

            public void onProviderDisabled(String s) {
                Toast.makeText(RequestLocation.this,
                        "Provider disabled by the user. GPS turned off",
                        Toast.LENGTH_LONG).show();
            }

            public void onProviderEnabled(String s) {

                Toast.makeText(RequestLocation.this,

                        "Provider enabled by the user. GPS turned on",

                        Toast.LENGTH_LONG).show();

            }

        }

    }


