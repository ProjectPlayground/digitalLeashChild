package com.geogehigbie.digitalleashchild;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.xml.datatype.Duration;

import layout.FragmentDataChild;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GoogleApiClient client;
    private LocationManager locationManager;
    private LocationRequest locationRequest;

    private Location lastLocation;
    private String lastLongitudeString;
    private String lastLatitudeString;

    private TextView output;

    private String reportingString = "Your location is being reported as: ";

    private Handler handler1;
    private Runnable runnable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadFirstFragment();

        createGoogleAPIClient();

    }


    public void loadFirstFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, new FragmentDataChild());
        fragmentTransaction.commit();

    }



    @Override
    protected void onStart() {
        super.onStart();

        client.connect();
    }

    @Override
    protected void onStop() {

        client.disconnect();

        super.onStop();
    }

    //used to get last location - might not use this in this app;
    @Override
    public void onConnected(Bundle connectionHint) {

        //used to get last location - might not use this in this app;
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        if (lastLocation != null) {
            lastLatitudeString = String.valueOf(lastLocation.getLatitude());
            lastLongitudeString = String.valueOf(lastLocation.getLongitude());
        }

        LocationRequest request = new LocationRequest();
        request.setPriority(1);

        handler1 = new Handler();
        runnable1 = new Runnable(){
            @Override
            public void run(){

            }
        };
        handler1.postDelayed(runnable1, 1000);

    }



    public void createGoogleAPIClient(){
       client = new GoogleApiClient.Builder(this)
               .addApi(LocationServices.API)
               .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
               .build();
    }

    public void onClickReportLocation(){
        String location = "SOMEWHERE";
        reportingString = reportingString + location;

        Toast toast = Toast.makeText(this, reportingString, Toast.LENGTH_LONG);
        toast.show();

        constantlyReportLocation();

    }

    public void checkLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location = (Location) locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


    }

    public void constantlyReportLocation(){

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
        }
        //going to have handler here

    }










}
