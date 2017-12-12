package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.remi.test.R;

/**
 * Activity where the player choose his class for his future games
 * TODO add the connection and the choosing part
 */
public class ClassesActivity extends Activity {

    private Button goToSpellActitivy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        
        this.goToSpellActitivy = (Button) findViewById(R.id.goToSpellbutton);
    }

    public void goToSpellActivity(View view) {
        Intent intent = new Intent(this, SpellSelectionActivity.class);
        startActivity(intent);
    }
}
