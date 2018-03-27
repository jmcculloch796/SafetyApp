package com.example.jamie.dissertation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import static com.example.jamie.dissertation.showAllContacts.appcontacts;

/**
 * Created by jamie on 17/02/18.
 */

public class getLocation {
    private Context context;
    protected LocationManager locationManager;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000000000; // in Milliseconds

    public void CustomAdapter(Context context) {
        this.context = context;
    }
   public getLocation(LocationManager locationManager){
        CustomAdapter(context);
       locationManager.requestLocationUpdates(
               LocationManager.GPS_PROVIDER,
               MINIMUM_TIME_BETWEEN_UPDATES,
               MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
               new myLocationListener());
    }

    public void showLocation() {
      /*  int permissionCheck = ContextCompat.checkSelfPermission(Home.this,
                Manifest.permission.ACCESS_FINE_LOCATION);*/
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {

            String message ="You can track my location here: http://maps.google.com/maps?q=loc:" + String.format("%f,%f", location.getLatitude() , location.getLongitude() ); //String.format(

            //    "Current Location \n Longitude: %1$s \n Latitude: %2$s",

            //  location.getLongitude(), location.getLatitude()

            // );
            Uri sms_uri = Uri.parse("smsto:" + appcontacts);
            Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, sms_uri);
            //intent.setType("vnd.android-dir/mms-sms");
            intent.putExtra("sms_body", message);
            context.startActivity(intent);

        }


    }
}
