package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.app.remi.test.Moteur;

public class MainActivity extends Activity {

    //Moteur will be used as a view
    private Moteur moteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        //Check is the sensorbutton is on or off
        Boolean playWithSensor = intent.getBooleanExtra("BOOLEAN_CHECK",false);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        moteur = new Moteur(this, playWithSensor,sensorManager);
        setContentView(moteur);

        //MediaPlayer ring= MediaPlayer.create(MainActivity.this,R.raw.level7);
        //ring.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        moteur.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        moteur.pause();
    }

}
