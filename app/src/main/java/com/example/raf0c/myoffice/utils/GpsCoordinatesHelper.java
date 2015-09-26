package com.example.raf0c.myoffice.utils;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.raf0c.myoffice.constants.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

/**
 * Created by raf0c on 25/09/15.
 */
public class GpsCoordinatesHelper extends Service implements LocationListener {

    private final Context mContext;

    private boolean mIsGpsEnabled = false;
    private boolean mIsNetworkEnabled = false;
    private boolean mCanGetLocation = false;

    private Location mLocation;

    private double mLatitude;
    private double mLongitude;
    private String mCityName;
    private GoogleMap mMap;

    protected LocationManager mLocationManager;

    public GpsCoordinatesHelper(Context mContext, GoogleMap map){
        this.mContext = mContext;
        this.mMap = map;
        getmLocation();
    }

    public Location getmLocation(){
        try{
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            mIsGpsEnabled = mLocationManager.isProviderEnabled((LocationManager.GPS_PROVIDER));
            mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!mIsGpsEnabled && !mIsNetworkEnabled){
                Toast.makeText(mContext,"GPS and Network is turned off!", Toast.LENGTH_LONG).show();
            }else{
                mCanGetLocation = true;

                if(mIsNetworkEnabled){
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.MIN_TIME_BTWN_UPDATES,
                            Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                    if(mLocationManager != null){
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(mLocation != null){
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();

                            //get city name
                            Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
                            //only need the first result
                            List<Address> addresses = gcd.getFromLocation(mLatitude, mLongitude,1);
                            mCityName = addresses.get(0).getLocality();
                        }
                    }
                }

                if(mIsGpsEnabled){
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            Constants.MIN_TIME_BTWN_UPDATES,
                            Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                    if(mLocationManager != null){
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(mLocation != null){
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();

                            //get city name
                            Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
                            //only need the first result
                            List<Address> addresses = gcd.getFromLocation(mLatitude, mLongitude,1);
                            mCityName = addresses.get(0).getLocality();
                        }
                    }
                }
            }
        }catch (Exception e){
            //TODO effectively catch exception later.
            Log.e(GpsCoordinatesHelper.class.getName(), e.toString());
            e.printStackTrace();
        }
        return mLocation;
    }


    public void stopUsingGPS(){
        if(mLocationManager != null){
            mLocationManager.removeUpdates(GpsCoordinatesHelper.this);
        }
    }

    public double getmLatitude(){
        if(mLocation != null){
            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    public double getmLongitude(){
        if(mLocation != null){
            mLongitude = mLocation.getLongitude();
        }
        return mLongitude;
    }

    public String getmCityName(){
        return mCityName;
    }

    public boolean ismCanGetLocation(){
        return mCanGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled.  Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();;
            }
        });
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}