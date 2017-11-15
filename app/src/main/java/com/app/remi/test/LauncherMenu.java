package com.app.remi.test;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Menu activity class
 * NB : maybe contain some error on background
 */
public class LauncherMenu extends Activity {
    private Button startButton;
    private TextView titleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_menu);
        startButton = (Button) findViewById(R.id.nextScreenButton);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
    }

    // Start the game activity
    public void submit(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
