package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
    private TextView numberSpell;

    //The EditText
    private EditText number;

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
        numberSpell = (TextView) findViewById(R.id.NumberofSpell);
        number = (EditText) findViewById(R.id.Number);
    }

    // Start the game activity
    public void submit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        boolean buttonState = accelerometerToggleButton.isChecked();

        //check if we have a good number of spell
        String check = number.getText().toString();
        if(check.matches("")){
            number.setError("No number given");
            Toast.makeText(this,"You did not put a number of spellblock",Toast.LENGTH_SHORT).show();
        }
        else {
            int numbergiven = Integer.parseInt(number.getText().toString());
            if ((numbergiven < 3) || (numbergiven > 6)) {
                number.setError("Bad number given");
                Toast.makeText(this,"You did not put a number between 3 and 6",Toast.LENGTH_SHORT).show();
            } else {
                intent.putExtra("SPELL_BLOCKS_NUMBER", numbergiven);
                intent.putExtra("BOOLEAN_CHECK", buttonState);
                startActivity(intent);
            }
        }
    }
}
