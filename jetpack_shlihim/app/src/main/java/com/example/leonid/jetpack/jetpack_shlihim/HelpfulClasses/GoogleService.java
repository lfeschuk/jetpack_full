package com.example.leonid.jetpack.jetpack_shlihim.HelpfulClasses;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.leonid.jetpack.jetpack_shlihim.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by deepshikha on 24/11/16.
 */

public class GoogleService extends Service implements LocationListener{

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude,longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 20000;
    public static String str_receiver = "MyApplication.DisplayLocation";
    Intent intent;
    FusedLocationProviderClient mFusedLocationClient;
    public final String TAG = "GoogleService";




    public GoogleService() {
        Log.d(TAG,"rrr");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
Log.d(TAG,"onCreate");
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(),10,notify_interval);
        intent = new Intent(str_receiver);
//        fn_getlocation();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_ACCESS_COARSE_LOCATION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // All good!
//                } else {
//                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//        }
//    }


    private void fn_getlocation(){



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED ) {
                    Log.d(TAG,"ff2f");
                    ActivityCompat.requestPermissions(MainActivity.main_activity, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION },
                            0);
                }
                else
        {

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener( new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            Log.d(TAG,"fff");
                            if (location != null) {
                                Log.e(TAG,location.getLatitude()+"");
                                  Log.e(TAG,location.getLongitude()+"");
                                fn_update(location);

                            }
                        }
                    });
        }

//        Log.d(TAG,"fff");
//        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
//        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        if (!isGPSEnable && !isNetworkEnable){
//
//        }else {
//
//
//
//
//            if (isGPSEnable){
//                location = null;
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED ||
//                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                                != PackageManager.PERMISSION_GRANTED ) {
//                    Log.d(TAG,"ff2f");
//                    ActivityCompat.requestPermissions(MainActivity.main_activity, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION },
//                            0);
//                }
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
//                if (locationManager!=null){
//                    Log.d(TAG,"fff3");
//                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    Log.d(TAG,"location:" + location);
//                    if (location!=null){
//                        Log.e("latitude",location.getLatitude()+"");
//                        Log.e("longitude",location.getLongitude()+"");
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                        fn_update(location);
//                    }
//                   // while(location == null);
//                    Log.d(TAG,"location:" + location);
//
//                }
//            }
//
//            else if (isNetworkEnable){
//                location = null;
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.main_activity, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION },
//                            0);
//                }
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
//                if (locationManager!=null){
//                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    if (location!=null){
//
//                        Log.e("latitude",location.getLatitude()+"");
//                        Log.e("longitude",location.getLongitude()+"");
//
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                        fn_update(location);
//                    }
//                }
//
//            }
//
//
//        }

    }

    private class TimerTaskToGetLocation extends TimerTask{
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"ee");
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(Location location){

        intent.putExtra("latutide",location.getLatitude()+"");
        intent.putExtra("longitude",location.getLongitude()+"");
        sendBroadcast(intent);
    }


}