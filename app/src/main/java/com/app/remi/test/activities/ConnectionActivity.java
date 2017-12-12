package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.remi.test.R;

/**
 * Activity used to identify the player
 * TODO add the connection part
 */
public class ConnectionActivity extends Activity {

    private EditText loginField, passwordField;
    private Button gotoClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        this.loginField = (EditText) findViewById(R.id.loginField);
        this.passwordField = (EditText) findViewById(R.id.passwordField);
        this.gotoClasses = (Button) findViewById(R.id.goToClassesbutton);

    }

    public void goToClassesActivity(View view) {
        Intent intent = new Intent(this, ClassesActivity.class);
        startActivity(intent);
    }
}
