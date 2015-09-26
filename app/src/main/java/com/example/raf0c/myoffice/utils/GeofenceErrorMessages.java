package com.example.raf0c.myoffice.utils;

/**
 * Created by raf0c on 25/09/15.
 */

import android.content.Context;
import android.content.res.Resources;

import com.example.raf0c.myoffice.R;
import com.google.android.gms.location.GeofenceStatusCodes;

/**
 * Geofence error codes mapped to error messages.
 */
public class GeofenceErrorMessages {
    /**
     * Prevents instantiation.
     */
    private GeofenceErrorMessages() {}

    /**
     * Returns the error string for a geofencing error code.
     */
    public static String getErrorString(Context context, int errorCode) {
        Resources mResources = context.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return mResources.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return mResources.getString(R.string.geofence_too_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return mResources.getString(R.string.geofence_too_many_pending_intents);
            default:
                return mResources.getString(R.string.unknown_geofence_error);
        }
    }

    public static String getGoogleMapsKey(Context context){
        Resources mResources = context.getResources();
        return mResources.getString(R.string.google_maps_key);
    }

    public static String getGoogleMapsBrowserKey(Context context){
        Resources mResources = context.getResources();
        return mResources.getString(R.string.google_maps_browser_key);
    }
}
