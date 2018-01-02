package com.app.remi.test.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.remi.test.R;
import com.app.remi.test.network.backend.Displayable;
import com.app.remi.test.network.backend.NetworkReceiver;
import com.app.remi.test.network.backend.services.NetworkBackendService;

/**
 * Main menu of the application
 * TODO display the server connection status
 */
public class MainMenuActivity extends Activity implements Displayable {

    public static final boolean BRICKEST_DEBUG_MODE = true;
    public final static String FILTER_MAIN_MENU = "com.app.remi.test.activities.MainMenuActivity.FILTER_MAIN_MENU";
    private Button goToConnectionActivity, goToEngine, forceConnectionButton;
    private ImageView titleview;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.goToConnectionActivity = (Button) findViewById(R.id.goToConnectionButton);
        this.goToEngine = (Button) findViewById(R.id.debugModeButton);
        this.forceConnectionButton = (Button) findViewById(R.id.networkDebugModeButton);
        if (!BRICKEST_DEBUG_MODE) {
            this.goToEngine.setVisibility(View.INVISIBLE);
            this.forceConnectionButton.setVisibility(View.INVISIBLE);
        }
        this.titleview = (ImageView) findViewById(R.id.titleView);

        this.localBroadcastManager = LocalBroadcastManager.getInstance(this);                                       // Get an instance of a broadcast manager
        BroadcastReceiver myReceiver = new NetworkReceiver(this);                                        // Create a class and set in it the behavior when an information is received
        IntentFilter intentFilter = new IntentFilter(FILTER_MAIN_MENU);                                             // The intentFilter action should match the action of the intent send
        localBroadcastManager.registerReceiver(myReceiver, intentFilter);                                           // We register the receiver for the localBroadcastManager
        this.establishConnection();

    }

    public void goToConnectionScreen(View view) {
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }

    /**
     * Used only in debug mode, will skip every part of the connection and go directly into a game alone
     *
     * @param view Context
     */
    public void goToEngineBehavior(View view) {
        if (BRICKEST_DEBUG_MODE) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Will notify the user of the result of the connection to the server
     *
     * @param textReceived Text received from the server, the normal answer to "BPING" is "BPONG"
     */
    @Override
    public void handleReception(String textReceived) {
        Toast.makeText(this, "Connection established", Toast.LENGTH_SHORT).show();

    }

    /**
     * Force the connection with the server
     *
     * @param view
     */
    public void forceConnection(View view) {
        if (BRICKEST_DEBUG_MODE) {
            this.establishConnection();
        }

    }

    /**
     * Method use to call the network background service to establish a connection with the server
     */
    public void establishConnection() {
        Toast.makeText(this, "Trying to establish connection", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, NetworkBackendService.class);                                // Start the service responsible for client/server exchanges
        intent.putExtra(NetworkBackendService.NETWORK_INTENT_TAG, 0);
        startService(intent);
    }
}
