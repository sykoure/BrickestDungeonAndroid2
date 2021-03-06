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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.remi.test.R;
import com.app.remi.test.network.backend.Displayable;
import com.app.remi.test.network.backend.NetworkReceiver;
import com.app.remi.test.network.backend.services.NetworkBackendService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Matchmaking activity
 * The client can choose here to play with the accelerometer
 * Bad naming due to early implementation
 * TODO rename properly
 */
public class SpellSelectionActivity extends Activity implements Displayable {

    // Button that will launch the game
    private Button startButton;
    // The textViews
    private TextView accelerometerTextView, matchResultTextView;
    // The button to allow or not the player to use the accelerometer
    private ToggleButton accelerometerToggleButton;

    private LocalBroadcastManager localBroadcastManager;
    private NetworkBackendService networkBackendService;
    private boolean mBound = false;
    // Allow the usage of the back button
    private boolean canGoBack;

    private ArrayList<String> spellsList;
    public final static String MATCHMAKING_SPELLS_LIST = "com.app.remi.test.activities.SpellSelectionActivity";
    public final static String FILTER_MATCHMAKING = "com.app.remi.test.activities.SpellSelectionActivity.FILTER_MATCHMAKING";
    public static final String TAG_PLAYER_OWN_INFO = "com.app.remi.test.SpellSelectionActivity.TAG_PLAYER_OWN_INFO";
    public static final String TAG_PLAYER_OPP_INFO = "com.app.remi.test.SpellSelectionActivity.TAG_PLAYER_OPP_INFO";
    public static final String TAG_SPELL_LIST = "com.app.remi.test.SpellSelectionActivity.TAG_SPELL_LIST";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_selection);

        //all the views
        startButton = (Button) findViewById(R.id.nextScreenButton);
        accelerometerTextView = (TextView) findViewById(R.id.accelerometerTextView);
        matchResultTextView = (TextView) findViewById(R.id.matchResultTextView);
        accelerometerToggleButton = (ToggleButton) findViewById(R.id.accelerometerToggleButton);


        this.localBroadcastManager = LocalBroadcastManager.getInstance(this);                                       // Get an instance of a broadcast manager
        BroadcastReceiver myReceiver = new NetworkReceiver(this);                                        // Create a class and set in it the behavior when an information is received
        IntentFilter intentFilter = new IntentFilter(FILTER_MATCHMAKING);                                           // The intentFilter action should match the action of the intent send
        localBroadcastManager.registerReceiver(myReceiver, intentFilter);                                           // We register the receiver for the localBroadcastManager


        if (getIntent().getStringExtra(MainActivity.TAG_FIGHT_RESULT) == null) {                                    // If the activity has not been restarted by the MainActivity
            this.canGoBack = true;
            this.spellsList = getIntent().getStringArrayListExtra(TrueSpellSelectionActivity.TAG_LIST_SPELL);
            Log.d("SPELL_SELECTION : ", this.spellsList.toString());

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Set<String> spellsSet = new HashSet<>();
            spellsSet.addAll(this.spellsList);                                                                      // Conversion of the array list into a set to allow the usage of SharedPreferences
            editor.putStringSet(TAG_SPELL_LIST, spellsSet);
            editor.commit();
        } else {                                                                                                    // The activity has been started by the MainActivity
            this.canGoBack = false;
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);                               // We retrieve the list of spells previously saved
            Set<String> spellsSet = sharedPref.getStringSet(TAG_SPELL_LIST, null);
            Log.d("SPELLS RETRIEVED", spellsSet.toString());
            this.spellsList = new ArrayList<>();
            this.spellsList.addAll(spellsSet);
            if (getIntent().getStringExtra(MainActivity.TAG_FIGHT_RESULT).equals("BWIN")) {                         // Display the result of the match
                this.matchResultTextView.setText("YOU WON");
            } else {
                this.matchResultTextView.setText("YOU LOST");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        Intent intent = new Intent(this, NetworkBackendService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

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


    /**
     * Send a request to be queued for matchmaking
     *
     * @param view Context
     */
    public void submit(View view) {

        networkBackendService.sendMessageToServer("BMAKE");
    }


    /**
     * Behavior when the server have found an opponent and started a game
     * Parsing of the players info received by the server
     *
     * @param textReceived
     */
    @Override
    public void handleReception(String textReceived) {

        Intent intent = new Intent(this, MainActivity.class);
        boolean buttonState = accelerometerToggleButton.isChecked();

        String own_player_info, opp_player_info;
        String[] playersInfos = textReceived.split(",");        // Parsing of the received informations
        own_player_info = playersInfos[1];
        opp_player_info = playersInfos[2];

        if (MainMenuActivity.BRICKEST_DEBUG_MODE) {
            Toast.makeText(this, "OWN :" + own_player_info, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "OPP : " + opp_player_info, Toast.LENGTH_SHORT).show();
        }

        intent.putExtra(TAG_PLAYER_OWN_INFO, own_player_info);
        intent.putExtra(TAG_PLAYER_OPP_INFO, opp_player_info);
        intent.putExtra(TAG_SPELL_LIST, this.spellsList); // Retrieving of the list of spells
        intent.putExtra("SPELL_BLOCKS_NUMBER", this.spellsList.size());
        intent.putExtra(SpellSelectionActivity.MATCHMAKING_SPELLS_LIST, this.spellsList);
        intent.putExtra("BOOLEAN_CHECK", buttonState);
        startActivity(intent);
    }

    /***
     * The service is only unbound onDestroy, this allow the service to persist between activities
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        mBound = false;
    }

    /**
     * The user can only go back when the previous screen is not the engine
     * If the last screen was the engine, the user back button send him back to title screen
     */
    @Override
    public void onBackPressed() {
        if (this.canGoBack) {
            super.onBackPressed();
        }
        else{
            Intent intent = new Intent(this,MainMenuActivity.class);
            startActivity(intent);
        }
    }
}
