package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.app.remi.test.R;

/**
 * Main menu of the application
 * TODO display the server connection status
 */
public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void goToConnectionScreen(View view) {
        Intent intent = new Intent(this,SpellSelectionActivity.class);
        startActivity(intent);
    }
}
