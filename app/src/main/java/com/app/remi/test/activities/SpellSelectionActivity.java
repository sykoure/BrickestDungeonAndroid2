package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.app.remi.test.R;

/**
 * Menu activity class
 * NB : maybe contain some error on background
 * TODO Add the spell selection aspect
 */
public class SpellSelectionActivity extends Activity {

    //Button that will launch the game
    private Button startButton;

    //The textViews
    private TextView accelerometerTextView;

    //The button to allow or not the player to use the accelerometer
    private ToggleButton accelerometerToggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_selection);

        //all the views
        startButton = (Button) findViewById(R.id.nextScreenButton);
        accelerometerTextView = (TextView) findViewById(R.id.accelerometerTextView);
        accelerometerToggleButton = (ToggleButton) findViewById(R.id.accelerometerToggleButton);
    }

    // Start the game activity
    public void submit(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        boolean buttonState = accelerometerToggleButton.isChecked();
        intent.putExtra("BOOLEAN_CHECK",buttonState);
        startActivity(intent);
    }
}
