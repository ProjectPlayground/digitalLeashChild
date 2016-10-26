package com.geogehigbie.digitalleashchild;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import layout.FragmentDataChild;

import static android.R.attr.radius;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GoogleApiClient client;
    private LocationManager locationManager;
    private LocationRequest locationRequest;

    private Location lastLocation;
    private String lastLongitudeString;
    private String lastLatitudeString;

    private Location location;
    private TextView output;

    private String reportingString = "Your location is being reported as: ";

    private Handler handler1;
    private Runnable runnable1;

    private double longitude;
    private double latitude;

    private String locationCoordinatesString;

    //permission checking variables
    private final static int DISTANCE_UPDATES = 1;
    private final static int TIME_UPDATES = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean LocationAvailable;

    private JSONObject childJSON;
    private String JSONString;

    private String parentUserName;

    //some udacity suggestions
//    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFirstFragment();
    }


    public void loadFirstFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, new FragmentDataChild());
        fragmentTransaction.commit();

    }


    @Override
    public void onConnected(Bundle connectionHint) {

      //  checkPermissionsAndGetLocation();
    }


    public void checkPermissionsAndGetLocation(){

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            getLocation();

//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // Requests the user permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);

            }
        }

    public void getLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationRequest = locationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

      //  LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);

        lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(lastLocation != null){
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            lastLatitudeString = String.valueOf(lastLocation.getLatitude());
            lastLongitudeString = String.valueOf(lastLocation.getLongitude());
        }

        Log.i("LAT", "getLocation: " + lastLatitudeString );
        Log.i("LONG", "getLocation: " + lastLongitudeString);


        handler1 = new Handler();
        runnable1 = new Runnable() {
            @Override
            public void run() {
                getLocation();
                createJSON();

            }
        };
        handler1.postDelayed(runnable1, 10000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 99: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getParentUserName(){
        parentUserName = (String) findViewById(R.id.edit1).toString();
    }

    public String createJSON(){
        childJSON = new JSONObject();

            try {
                childJSON.put("username", parentUserName);
                childJSON.put("radius", radius);
                childJSON.put("longitude", longitude);
                childJSON.put("latitude", latitude);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONString = childJSON.toString();
            return JSONString;
    }



    public void onClickReportLocation(View view){

        checkPermissionsAndGetLocation();

        showLocationUpdateToast();

        getParentUserName();
    }



    public void showLocationUpdateToast(){
        String locationCoordinatesString = lastLongitudeString + " Longitude and " + lastLatitudeString + " Latitude";
        reportingString = reportingString + locationCoordinatesString;

        Toast toast = Toast.makeText(this, reportingString, Toast.LENGTH_LONG);
        toast.show();

    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast toast = Toast.makeText(this, "The connection was suspended.", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(this, "The connection failed.", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
