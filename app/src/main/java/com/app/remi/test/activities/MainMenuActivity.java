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

    private Button goToConnectionActivity;
    private ImageView titleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.goToConnectionActivity = (Button) findViewById(R.id.goToConnectionButton);
        this.titleview = (ImageView) findViewById(R.id.titleView);
    }

    public void goToConnectionScreen(View view) {
        Intent intent = new Intent(this, ConnectionActivity.class);
        startActivity(intent);
    }
}
