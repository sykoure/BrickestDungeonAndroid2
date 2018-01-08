package com.app.remi.test.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.remi.test.R;
import com.app.remi.test.network.backend.Displayable;
import com.app.remi.test.network.backend.NetworkReceiver;
import com.app.remi.test.network.backend.services.NetworkBackendService;

import java.util.ArrayList;

/**
 * Activity used to identify the player
 */
public class ConnectionActivity extends Activity implements Displayable {

    public final static String FILTER_CONNECTION = "com.app.remi.test.activities.ConnectionActivity.FILTER_CONNECTION";
    public final static String HERO_LIST_TAG = "com.app.remi.test.activities.ConnectionActivity.HERO_LIST_TAG";
    public final static String LOGIN_TAG = "com.app.remi.test.activities.ConnectionActivity.LOGIN_TAG";
    public final static String PASSWORD_TAG = "com.app.remi.test.activities.ConnectionActivity.PASSWORD_TAG";


    private LocalBroadcastManager localBroadcastManager;
    private EditText loginField, passwordField;
    private Button gotoClasses;
    private NetworkBackendService networkBackendService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        this.loginField = (EditText) findViewById(R.id.loginField);
        this.passwordField = (EditText) findViewById(R.id.passwordField);
        this.gotoClasses = (Button) findViewById(R.id.goToClassesbutton);


        this.localBroadcastManager = LocalBroadcastManager.getInstance(this);                                       // Get an instance of a broadcast manager
        BroadcastReceiver myReceiver = new NetworkReceiver(this);                                        // Create a class and set in it the behavior when an information is received
        IntentFilter intentFilter = new IntentFilter(FILTER_CONNECTION);                                            // The intentFilter action should match the action of the intent send
        localBroadcastManager.registerReceiver(myReceiver, intentFilter);                                           // We register the receiver for the localBroadcastManager

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);                                   // We set the login and password on screen with last validated password/login
        this.loginField.setText(sharedPref.getString(LOGIN_TAG, "login"));
        this.passwordField.setText(sharedPref.getString(PASSWORD_TAG, ""));


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkBackendService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    /**
     * If the client is already authentified go to the next activity
     * else identify him
     *
     * @param view
     */
    public void connectionButtonBehavior(View view) {
        if (networkBackendService.isAuthentified()) {                                                      // If already authentified
            networkBackendService.sendMessageToServer("BCLASSESR");                          // Request of the classes available
        } else {
            networkBackendService.sendMessageToServer("BAUTH," + this.loginField.getText().toString() + "," + this.passwordField.getText().toString());
        }

    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     * Source : https://developer.android.com/guide/components/bound-services.html#Binder
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkBackendService.LocalBinder binder = (NetworkBackendService.LocalBinder) service;
            networkBackendService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    /***
     * The service is only unbound onDestroy, this allow the service to persist between activities
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        mBound = false;
    }

    @Override
    public void handleReception(String textReceived) {
        if (textReceived.equals("0")) {
            Toast.makeText(this, "Authentification Successful", Toast.LENGTH_SHORT).show();
            networkBackendService.sendMessageToServer("BCLASSESR");                          // Request of the classes available
        } else if (textReceived.equals("1"))
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
        else if (textReceived.equals("2"))
            Toast.makeText(this, "Login Not Registered", Toast.LENGTH_SHORT).show();

        else {                                                                                             // List of spells sended by the network service
            //Toast.makeText(this, textReceived, Toast.LENGTH_SHORT).show();
            String[] slicedMessage = textReceived.split(",");
            ArrayList<String> heroList = new ArrayList<>();
            for (int index = 1; index < slicedMessage.length; index++) {                                   // Parsing of received classes (Heroes)
                heroList.add(slicedMessage[index].toLowerCase());
            }

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);                      // If the connection is authentified we save the login and password
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(LOGIN_TAG, this.loginField.getText().toString());
            editor.putString(PASSWORD_TAG, this.passwordField.getText().toString());
            editor.commit();
            //Toast.makeText(this, heroList.toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ClassesActivity.class);
            intent.putExtra(HERO_LIST_TAG, heroList);                                                      // We put in the intent the list of available classes
            startActivity(intent);
        }

    }
}
