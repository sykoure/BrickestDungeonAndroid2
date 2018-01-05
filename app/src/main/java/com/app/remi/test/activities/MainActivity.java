package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.app.remi.test.engine.Engine;
import com.app.remi.test.engine.Player;

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
        //tab[number] qui sera le tableau contenant les sorts

        //TODO add the class used by players
        Player ownPlayer = this.generatePlayer(getIntent().getStringExtra(SpellSelectionActivity.TAG_PLAYER_OWN_INFO));
        Player oppPlayer = this.generatePlayer(getIntent().getStringExtra(SpellSelectionActivity.TAG_PLAYER_OPP_INFO));

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
     * TODO adjust this in function of server protocols changes
     *
     * @param playerData
     * @return
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

}
