package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.remi.test.R;

/**
 * Main menu of the application
 * TODO display the server connection status
 */
public class MainMenuActivity extends Activity {

    public static final boolean BRICKEST_DEBUG_MODE = true;
    private Button goToConnectionActivity, goToEngine;
    private ImageView titleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.goToConnectionActivity = (Button) findViewById(R.id.goToConnectionButton);
        this.goToEngine = (Button) findViewById(R.id.debugModeButton);
        if (!BRICKEST_DEBUG_MODE)
            this.goToEngine.setVisibility(View.INVISIBLE);
        this.titleview = (ImageView) findViewById(R.id.titleView);
    }

    public void goToConnectionScreen(View view) {
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }

    /**
     * Used only in debug mode, will skip every part of the connection and go directly into a game alone
     * @param view Context
     */
    public void goToEngineBehavior(View view) {
        if(BRICKEST_DEBUG_MODE){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}
