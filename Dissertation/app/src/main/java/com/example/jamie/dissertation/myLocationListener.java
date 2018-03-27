package com.example.jamie.dissertation;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by jamie on 15/02/18.
 */

public class myLocationListener implements LocationListener {



    public void onLocationChanged(Location location) {

        String message = String.format(

                "New Location \n Longitude: %1$s \n Latitude: %2$s",

                location.getLongitude(), location.getLatitude()

        );


    }
    public void onStatusChanged(String s, int i, Bundle b) {


    }

    public void onProviderDisabled(String s) {

    }

    public void onProviderEnabled(String s) {

}

}
