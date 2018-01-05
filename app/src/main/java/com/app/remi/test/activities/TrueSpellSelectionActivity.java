package com.app.remi.test.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.remi.test.R;
import com.app.remi.test.network.backend.Displayable;
import com.app.remi.test.network.backend.NetworkReceiver;
import com.app.remi.test.network.backend.services.NetworkBackendService;

import java.util.ArrayList;

/**
 * Activity where the user will select a list of spells to use in further games.
 */
public class TrueSpellSelectionActivity extends Activity implements Displayable {
    private ImageView spellImagaView1, spellImagaView2, spellImagaView3, spellImagaView4, spellImagaView5, spellImagaView6;
    private Button goToMatchMakingActivityButton;
    private ArrayList<String> listSpellsName;
    private ArrayList<ImageView> listSpellsImage;
    private ArrayList<Boolean> listSelectedImageView;
    private ArrayList<Boolean> listVisibleImageView;
    ArrayList<String> spellListToSend;
    public static final String TAG_LIST_SPELL = "com.app.remi.test.TrueSpellSelectionActivity.TAG_LIST_SPELL";
    public final static String FILTER_SPELLS = "com.app.remi.test.activities.TrueSpellSelectionActivity.FILTER_SPELLS";


    private LocalBroadcastManager localBroadcastManager;
    private NetworkBackendService networkBackendService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_spell_selection);

        this.spellImagaView1 = (ImageView) findViewById(R.id.spellImageView1);
        this.spellImagaView2 = (ImageView) findViewById(R.id.spellImageView2);
        this.spellImagaView3 = (ImageView) findViewById(R.id.spellImageView3);
        this.spellImagaView4 = (ImageView) findViewById(R.id.spellImageView4);
        this.spellImagaView5 = (ImageView) findViewById(R.id.spellImageView5);
        this.spellImagaView6 = (ImageView) findViewById(R.id.spellImageView6);
        this.goToMatchMakingActivityButton = (Button) findViewById(R.id.goToSpellbutton);

        // All the spells start unchecked
        this.spellImagaView1.setImageAlpha(90);
        this.spellImagaView2.setImageAlpha(90);
        this.spellImagaView3.setImageAlpha(90);
        this.spellImagaView4.setImageAlpha(90);
        this.spellImagaView5.setImageAlpha(90);
        this.spellImagaView6.setImageAlpha(90);

        this.listSpellsName = getIntent().getStringArrayListExtra(TAG_LIST_SPELL);

        this.listSpellsImage = new ArrayList<ImageView>();
        this.listVisibleImageView = new ArrayList<Boolean>();
        this.listSelectedImageView = new ArrayList<Boolean>();

        this.listInstantiation();
        this.setSpellImageVisibility();
        this.setSpellImageContent();

        this.localBroadcastManager = LocalBroadcastManager.getInstance(this);                                       // Get an instance of a broadcast manager
        BroadcastReceiver myReceiver = new NetworkReceiver(this);                                        // Create a class and set in it the behavior when an information is received
        IntentFilter intentFilter = new IntentFilter(FILTER_SPELLS);                                                // The intentFilter action should match the action of the intent send
        localBroadcastManager.registerReceiver(myReceiver, intentFilter);                                           // We register the receiver for the localBroadcastManager

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkBackendService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     * Source : https://developer.android.com/guide/components/bound-services.html#Binder
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkBackendService.LocalBinder binder = (NetworkBackendService.LocalBinder) service;
            networkBackendService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    /**
     * Instantiate everyList of this activity
     * Used to make to code more readable
     * The number of visible image will depend of how many spell name have been passed
     */
    public void listInstantiation() {

        this.listSpellsImage.add(this.spellImagaView1);
        this.listSpellsImage.add(this.spellImagaView2);
        this.listSpellsImage.add(this.spellImagaView3);
        this.listSpellsImage.add(this.spellImagaView4);
        this.listSpellsImage.add(this.spellImagaView5);
        this.listSpellsImage.add(this.spellImagaView6);

        for (int index = 0; index < 6; index++)
            this.listSelectedImageView.add(new Boolean(false));

        for (int index2 = 0; index2 < 6; index2++) {
            if (index2 < this.listSpellsName.size()) {
                this.listVisibleImageView.add(new Boolean(true));
            } else {
                this.listVisibleImageView.add(new Boolean(false));
            }
        }
    }

    /**
     * Set the visibility of the spells images
     * Depend of how many spell name have been passed
     */
    public void setSpellImageVisibility() {
        for (int index = 0; index < this.listSpellsImage.size(); index++) {
            if (this.listVisibleImageView.get(index)) {
                this.listSpellsImage.get(index).setVisibility(View.VISIBLE);
            } else {
                this.listSpellsImage.get(index).setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Used when you click on an image spell
     * Ff not selected, will select it
     * If already selected, unselect it
     *
     * @param index Index of the spell to select/unselect
     */
    public void selectionOfSpell(int index) {
        if (this.listSelectedImageView.get(index)) {
            this.listSelectedImageView.set(index, false);
            this.listSpellsImage.get(index).setImageAlpha(90);
        } else {
            this.listSelectedImageView.set(index, true);
            this.listSpellsImage.get(index).setImageAlpha(255);
        }

    }

    /**
     * Set the content of every spell image according to the spell name passed
     * Get the resources in the drawable package
     */
    public void setSpellImageContent() {
        for (int index = 0; index < this.listSpellsName.size(); index++) {
            this.listSpellsImage.get(index).setImageResource(getResources().getIdentifier(this.listSpellsName.get(index), "drawable", getPackageName()));
        }
    }

    public ArrayList<String> putSelectedSpellInArray() {
        ArrayList<String> arrayToReturn = new ArrayList<>();
        for (int index = 0; index < this.listSpellsName.size(); index++) {
            if (this.listSelectedImageView.get(index))
                arrayToReturn.add(this.listSpellsName.get(index));
        }
        return arrayToReturn;
    }

    public void behaviorSpellImage1(View view) {
        this.selectionOfSpell(0);
    }


    public void behaviorSpellImage2(View view) {
        this.selectionOfSpell(1);

    }

    public void behaviorSpellImage3(View view) {
        this.selectionOfSpell(2);
    }


    public void behaviorSpellImage4(View view) {
        this.selectionOfSpell(3);
    }

    public void behaviorSpellImage5(View view) {
        this.selectionOfSpell(4);
    }

    public void behaviorSpellImage6(View view) {
        this.selectionOfSpell(5);
    }

    /**
     * This will start the next activity and give it the list of selected spells
     * Makes sure the user chose at least 3 spells
     *
     * @param view Context
     */
    public void goToSpellSelectionActivity(View view) {
        this.spellListToSend = this.putSelectedSpellInArray();
        if (spellListToSend.size() < 3) {
            Toast.makeText(this, "Choose at least 3 spells", Toast.LENGTH_SHORT).show();
        } else {

            String stringToSend = "BSPELLSC";
            for (int index = 0; index < spellListToSend.size(); index++) {
                stringToSend += "," + spellListToSend.get(index);
            }
            networkBackendService.sendMessageToServer(stringToSend);

        }
    }

    /**
     * When the service receive an acknowledgement of the chosen spells, it will call this method and start the next activity
     *
     * @param textReceived Will be "BSPELLSACK"
     */
    @Override
    public void handleReception(String textReceived) {
        Intent intent = new Intent(this, SpellSelectionActivity.class);
        intent.putExtra(TAG_LIST_SPELL, this.spellListToSend);
        startActivity(intent);
    }

    /***
     * The service is only unbound onDestroy, this allow the service to persist between activities
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        mBound = false;
    }
}
