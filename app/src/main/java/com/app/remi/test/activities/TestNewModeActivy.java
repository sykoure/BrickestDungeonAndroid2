package com.app.remi.test.activities;

import android.os.Bundle;
import android.app.Activity;

import com.app.remi.test.R;
import com.app.remi.test.engine.Engine;


//this activity will be used as a test to play our game with its new gameplay
public class TestNewModeActivy extends Activity {

    private Engine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(engine);
    }



}
