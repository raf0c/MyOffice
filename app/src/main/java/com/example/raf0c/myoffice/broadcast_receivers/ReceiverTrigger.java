package com.example.raf0c.myoffice.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.raf0c.myoffice.services.GeofenceTransitionsIntentService;

/**
 * Created by raf0c on 01/10/15.
 */
public class ReceiverTrigger extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Service Stops", "Launch again");
        context.startService(intent);
    }
}
