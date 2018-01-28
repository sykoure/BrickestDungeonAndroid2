package com.app.remi.test.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.app.remi.test.engine.BasicEngine;
import com.app.remi.test.engine.Engine;
import com.app.remi.test.data.Player;
import com.app.remi.test.network.backend.Displayable;
import com.app.remi.test.network.backend.NetworkReceiver;
import com.app.remi.test.network.backend.services.NetworkBackendService;

import java.util.ArrayList;

/**
 * Activity which serve as a placeholder for drawing the game elements
 */
public class MainActivity extends Activity implements Displayable {

    // Engine will be used as a view
    public final static String FILTER_GAME = "com.app.remi.test.activities.MainActivity.FILTER_GAME";
    public static final String TAG_FIGHT_RESULT = "com.app.remi.test.activities.MainActivity.TAG_FIGHT_RESULT";

    private BasicEngine engine;
    private LocalBroadcastManager localBroadcastManager;
    private NetworkBackendService networkBackendService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
            Intent intentService = new Intent(this, NetworkBackendService.class);
            bindService(intentService, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            MainActivity.this.startGame();
        }

    }


    /**
     * Defines callbacks for service binding, passed to bindService()
     * when the service is connected start the game
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkBackendService.LocalBinder binder = (NetworkBackendService.LocalBinder) service;
            networkBackendService = binder.getService();
            mBound = true;
            MainActivity.this.startGame();
            engine.resume();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (MainMenuActivity.BRICKEST_OFFLINE_MODE || this.mBound)      // If the game is in offline mode or if the service is bound
            engine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MainMenuActivity.BRICKEST_OFFLINE_MODE || this.mBound)      // If the game is in offline mode or if the service is bound
            engine.pause();
    }

    /**
     * Generate a player instance from a string containing all the necessary data
     * TODO Remove useless components
     *
     * @param playerData Data received from the server (BMATCH)
     * @return the Player structure for the opponent
     */
    public Player generatePlayer(String playerData) {
        Player player;
        int hp, shield, ballsNb;
        float ballsSpeed, ballsSize, buttonSize, paddleSize;
        String login, classPicked;
        String[] parsedData = playerData.split(";");
        hp = Integer.parseInt(parsedData[0]);
        shield = Integer.parseInt(parsedData[1]);
        ballsNb = Integer.parseInt(parsedData[2]);
        ballsSpeed = Float.parseFloat(parsedData[3]);
        ballsSize = Float.parseFloat(parsedData[4]);
        buttonSize = Float.parseFloat(parsedData[5]);
        paddleSize = Float.parseFloat(parsedData[6]);
        classPicked = parsedData[7];
        login = parsedData[8];

        player = new Player(hp, shield, classPicked, login);
        return player;
    }

    /**
     * Generate a player instance from a string containing all the necessary data
     * Used for the client Player structure, only one to have a spell list
     *
     * @param playerData Data received from the server (BMATCH)
     * @return the Player structure for the client
     */
    public Player generatePlayerOwn(String playerData, ArrayList<String> spellList) {
        Player player;
        int hp, shield, ballsNb;
        float ballsSpeed, ballsSize, buttonSize, paddleSize;
        String login, classPicked;
        String[] parsedData = playerData.split(";");
        hp = Integer.parseInt(parsedData[0]);
        shield = Integer.parseInt(parsedData[1]);
        ballsNb = Integer.parseInt(parsedData[2]);
        ballsSpeed = Float.parseFloat(parsedData[3]);
        ballsSize = Float.parseFloat(parsedData[4]);
        buttonSize = Float.parseFloat(parsedData[5]);
        paddleSize = Float.parseFloat(parsedData[6]);
        classPicked = parsedData[7];
        login = parsedData[8];

        player = new Player(hp, shield, ballsNb, ballsSpeed, ballsSize, buttonSize, paddleSize, classPicked, login, spellList);
        return player;
    }

    /**
     * This will be called once the service has been connected
     * This instantiate core element of this activity
     * such as the broadcast manager and the engine itself
     * TODO add offline mode
     */
    public void startGame() {
        Intent intent = getIntent();

        int number = intent.getIntExtra("SPELL_BLOCKS_NUMBER", 3);
        //Check if the sensorbutton is on or off
        Boolean playWithSensor = intent.getBooleanExtra("BOOLEAN_CHECK", false);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //TODO remove french comments
        //tab[number] qui sera le tableau contenant les sorts
        Player ownPlayer;
        Player oppPlayer;
        //TODO remove this test
        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {

            ownPlayer = this.generatePlayerOwn(getIntent().getStringExtra(SpellSelectionActivity.TAG_PLAYER_OWN_INFO), getIntent().getStringArrayListExtra((SpellSelectionActivity.TAG_SPELL_LIST)));
            oppPlayer = this.generatePlayer(getIntent().getStringExtra(SpellSelectionActivity.TAG_PLAYER_OPP_INFO));

            this.localBroadcastManager = LocalBroadcastManager.getInstance(this);                                       // Get an instance of a broadcast manager
            BroadcastReceiver myReceiver = new NetworkReceiver(this);                                        // Create a class and set in it the behavior when an information is received
            IntentFilter intentFilter = new IntentFilter(FILTER_GAME);                                                  // The intentFilter action should match the action of the intent send
            localBroadcastManager.registerReceiver(myReceiver, intentFilter);                                           // We register the receiver for the localBroadcastManager
            engine = new BasicEngine(this, playWithSensor, sensorManager, number, ownPlayer, oppPlayer, networkBackendService);

        } else {
            ownPlayer = new Player(10, 10, "offLineClass", "Yourself");
            oppPlayer = new Player(10, 10, "offLineClass", "Opponent");
            engine = new BasicEngine(this, playWithSensor, sensorManager, number, ownPlayer, oppPlayer, null);

        }
        engine = new BasicEngine(this, playWithSensor, sensorManager, number, ownPlayer, oppPlayer, networkBackendService);
        setContentView(engine);
    }

    /**
     * This activity will have to handle data update from server in addition of combat result
     * Parse the data and send it to the engine
     *
     * @param textReceived Will be BREF,arg1,arg2,.. or BWIN/BLOSE
     */
    @Override
    public void handleReception(String textReceived) {

        String[] dataReceived = textReceived.split(",");

        if (dataReceived[0].equals("BREF")) {

            String[] ownPlayerData = dataReceived[1].split(";");
            String[] oppPlayerData = dataReceived[2].split(";");

            int hp = Integer.parseInt(ownPlayerData[0]);
            int shield = Integer.parseInt(ownPlayerData[1]);
            int ballsNb = Integer.parseInt(ownPlayerData[2]);
            float ballsSpeed = Float.parseFloat(ownPlayerData[3]);
            float ballsSize = Float.parseFloat(ownPlayerData[4]);
            float buttonSize = Float.parseFloat(ownPlayerData[5]);
            float paddleSize = Float.parseFloat(ownPlayerData[6]);
            int oppHp = Integer.parseInt(oppPlayerData[0]);
            int oppShield = Integer.parseInt(oppPlayerData[1]);
            this.engine.changePlayersInfos(hp, shield, ballsNb, ballsSpeed, ballsSize, buttonSize, paddleSize, oppHp, oppShield);
            /*
            for (int index = 1; index < ballsNb; index++) {
                this.engine.splitBall();
            }*/
            if(ballsSpeed != 1.0){
                //this.engine.changeBallSpeed(ballsSpeed);
            }
            if(ballsSize != 1.0){
                //this.engine.changeBallSize(ballsSize);
            }

        } else {
            // TODO instead go to Matchmaking activity (currently SpellSelectionActivity)
            Intent intent = new Intent(this, SpellSelectionActivity.class);
            intent.putExtra(TAG_FIGHT_RESULT, textReceived);
            startActivity(intent);
        }
    }

    /***
     * The service is only unbound onDestroy, this allow the service to persist between activities
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /**
     * Disable the back button when the user is in a game
     */
    @Override
    public void onBackPressed() {
    }
}
