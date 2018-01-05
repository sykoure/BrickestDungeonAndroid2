package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.app.remi.test.engine.Engine;
import com.app.remi.test.engine.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activity which serve as a placeholder for drawing the game elements
 */
public class MainActivity extends Activity {

    //Moteur will be used as a view
    private Engine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        int number = intent.getIntExtra("SPELL_BLOCKS_NUMBER", 3);
        //Check if the sensorbutton is on or off
        Boolean playWithSensor = intent.getBooleanExtra("BOOLEAN_CHECK", false);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //TODO remove french comments
        //tab[number] qui sera le tableau contenant les sorts
        Player ownPlayer;
        Player oppPlayer;
        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
            //TODO add the class used by players
            //ArrayList<String> spellList = new ArrayList<>(Arrays.asList(getIntent().getStringArrayExtra(SpellSelectionActivity.TAG_SPELL_LIST)));   // Type casting
            //ownPlayer = this.generatePlayerOwn(getIntent().getStringExtra(SpellSelectionActivity.TAG_PLAYER_OWN_INFO), spellList);
            ownPlayer = this.generatePlayerOwn(getIntent().getStringExtra(SpellSelectionActivity.TAG_PLAYER_OWN_INFO), getIntent().getStringArrayListExtra((SpellSelectionActivity.TAG_SPELL_LIST)));
            oppPlayer = this.generatePlayer(getIntent().getStringExtra(SpellSelectionActivity.TAG_PLAYER_OPP_INFO));
        } else {
            ownPlayer = new Player(10, 10, "offLineClass", "Yourself");
            oppPlayer = new Player(10, 10, "offLineClass", "Opponent");
        }
        engine = new Engine(this, playWithSensor, sensorManager, number, ownPlayer, oppPlayer);
        setContentView(engine);


    }

    @Override
    protected void onResume() {
        super.onResume();
        engine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        engine.pause();
    }

    /**
     * Generate a player instance from a string containing all the necessary data
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

}
