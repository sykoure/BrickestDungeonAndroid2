package com.app.remi.test;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity {


    private Moteur moteur;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moteur = new Moteur(this);
        setContentView(moteur);

        MediaPlayer ring= MediaPlayer.create(MainActivity.this,R.raw.level7);
        ring.start();

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
