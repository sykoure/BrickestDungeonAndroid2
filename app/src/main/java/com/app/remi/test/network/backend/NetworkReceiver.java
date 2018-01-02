package com.app.remi.test.network.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.remi.test.network.backend.services.NetworkBackendService;


public class NetworkReceiver extends BroadcastReceiver {
    Displayable displayable;

    public NetworkReceiver(Displayable displayable) {
        super();
        this.displayable = displayable;
    }

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcasted.
     * Use the method display text to let the displayable object handle the string to display
     * @param context Context
     * @param intent Intent broadcasted containing the string to pass to the displayable
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO remove logs
        Log.d("BROADCAST_RECEIVER","MESSAGE_RECEIVED");
        String messageReceived = intent.getStringExtra(NetworkBackendService.MESSAGE_SEND_TAG);
        Log.d("BROADCAST_RECEIVER",messageReceived);
        this.displayable.handleReception(messageReceived);




    }
}
