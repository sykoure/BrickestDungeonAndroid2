package com.app.remi.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Main_Menu extends AppCompatActivity {

    private Button solo;
    private Button multi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);

        solo = (Button) findViewById(R.id.Solo);
        multi = (Button) findViewById(R.id.Multiplayer);
    }

    public void submit(View view){
        Intent intent =  new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
