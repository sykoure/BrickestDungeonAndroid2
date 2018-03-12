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
import android.widget.TextView;
import android.widget.Toast;

import com.app.remi.test.R;
import com.app.remi.test.network.backend.Displayable;
import com.app.remi.test.network.backend.NetworkReceiver;
import com.app.remi.test.network.backend.services.NetworkBackendService;

import java.util.ArrayList;

/**
 * Activity where the player choose his class for his future games
 */
public class ClassesActivity extends Activity implements Displayable {

    public final static String FILTER_CLASSES = "com.app.remi.test.activities.ClassesActivity.FILTER_CLASSES";
    private TextView heroDescriptionView;
    private ImageView heroImagaView1, heroImagaView2, heroImagaView3;
    private Button goToMatchMakingActivityButton;
    private ArrayList<String> listHeroesName;
    private ArrayList<ImageView> listHeroesImage;
    private ArrayList<Boolean> listSelectedImageView;
    private ArrayList<Boolean> listVisibleImageView;
    public static final String TAG_LIST_HERO = "com.app.remi.test.ClassesActivity.TAG_LIST_HERO";
    private Button goToSpellActitivy;
    private LocalBroadcastManager localBroadcastManager;
    private NetworkBackendService networkBackendService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        this.goToSpellActitivy = (Button) findViewById(R.id.goToSpellbutton);

        this.heroDescriptionView = (TextView) findViewById(R.id.heroDescriptionView);

        this.heroImagaView1 = (ImageView) findViewById(R.id.heroImageView1);
        this.heroImagaView2 = (ImageView) findViewById(R.id.heroImageView2);
        this.heroImagaView3 = (ImageView) findViewById(R.id.heroImageView3);
        this.goToMatchMakingActivityButton = (Button) findViewById(R.id.goToSpellbutton);

        // All the heroes start unchecked
        this.heroImagaView1.setImageAlpha(90);
        this.heroImagaView2.setImageAlpha(90);
        this.heroImagaView3.setImageAlpha(90);

        this.listHeroesName = getIntent().getStringArrayListExtra(ConnectionActivity.HERO_LIST_TAG);

        this.listHeroesImage = new ArrayList<ImageView>();
        this.listVisibleImageView = new ArrayList<Boolean>();
        this.listSelectedImageView = new ArrayList<Boolean>();

        this.listInstantiation();
        this.setHeroImageVisibility();
        this.setHeroImageContent();

//        this.heroDescriptionView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.hero_selection_background, 0, 0, 0);

        this.localBroadcastManager = LocalBroadcastManager.getInstance(this);                                       // Get an instance of a broadcast manager
        BroadcastReceiver myReceiver = new NetworkReceiver(this);                                        // Create a class and set in it the behavior when an information is received
        IntentFilter intentFilter = new IntentFilter(FILTER_CLASSES);                                            // The intentFilter action should match the action of the intent send
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
     * The number of visible image will depend of how many heroes name have been passed
     */
    public void listInstantiation() {

        this.listHeroesImage.add(this.heroImagaView1);
        this.listHeroesImage.add(this.heroImagaView2);
        this.listHeroesImage.add(this.heroImagaView3);


        for (int index = 0; index < 3; index++)
            this.listSelectedImageView.add(new Boolean(false));

        for (int index2 = 0; index2 < 3; index2++) {
            if (index2 < this.listHeroesName.size()) {
                this.listVisibleImageView.add(new Boolean(true));
            } else {
                this.listVisibleImageView.add(new Boolean(false));
            }
        }
    }

    /**
     * Set the visibility of the heroes images
     * Depend of how many heroes name have been passed
     */
    public void setHeroImageVisibility() {
        for (int index = 0; index < this.listHeroesImage.size(); index++) {
            if (this.listVisibleImageView.get(index)) {
                this.listHeroesImage.get(index).setVisibility(View.VISIBLE);
            } else {
                this.listHeroesImage.get(index).setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Used when you click on an image hero
     * Ff not selected, will select it
     * If already selected, unselect it
     *
     * @param index Index of the hero to select/unselect
     */
    public void selectionOfHero(int index) {
        if (this.listSelectedImageView.get(index)) {
            this.listSelectedImageView.set(index, false);
            this.listHeroesImage.get(index).setImageAlpha(90);
        } else {
            this.listSelectedImageView.set(index, true);
            this.listHeroesImage.get(index).setImageAlpha(255);
            unselectOther(index);
            setDescription(this.listHeroesName.get(index));
        }

    }

    /**
     * Unselect all other hero
     *
     * @param index Index of the hero selected
     */
    public void unselectOther(int index) {
        for (int i = 0; i < listSelectedImageView.size(); i++) {
            if (i != index) {
                this.listSelectedImageView.set(i, false);
                this.listHeroesImage.get(i).setImageAlpha(90);
            }
        }
    }

    /**
     * Set the content of every hero image according to the hero name passed
     * Get the resources in the drawable package
     */
    public void setHeroImageContent() {
        for (int index = 0; index < this.listHeroesName.size(); index++) {
            this.listHeroesImage.get(index).setImageResource(getResources().getIdentifier(this.listHeroesName.get(index), "drawable", getPackageName()));
        }
    }

    public void setDescription(String heroName) {
        String descriptionToDisplay;
        if (heroName.equals("paladin")) {
            descriptionToDisplay = "\nThe Paladin :\nProtected behind his shields, slower than the others is however the most resilient one.";

        } else if (heroName.equals("wizard")) {
            descriptionToDisplay = "\nThe Wizard :\nComplex and mysterious, his techniques require an utter mastery.";
        } else if (heroName.equals("warrior")) {
            descriptionToDisplay = "\nThe Warrior :\nA powerful fighter capable of dealing huge damages at his expense.";
        } else {
            descriptionToDisplay = "";
        }

        this.heroDescriptionView.setText(descriptionToDisplay);

    }


    public ArrayList<String> putSelectedHeroInArray() {
        ArrayList<String> arrayToReturn = new ArrayList<>();
        for (int index = 0; index < this.listHeroesName.size(); index++) {
            if (this.listSelectedImageView.get(index))
                arrayToReturn.add(this.listHeroesName.get(index));
        }
        return arrayToReturn;
    }

    public void behaviorHeroImage1(View view) {
        this.selectionOfHero(0);
    }


    public void behaviorHeroImage2(View view) {
        this.selectionOfHero(1);

    }

    public void behaviorHeroImage3(View view) {
        this.selectionOfHero(2);
    }

    public void goToSpellActivity(View view) {
        ArrayList<String> heroListToSend = this.putSelectedHeroInArray();

        if (heroListToSend.size() != 1) {
            Toast.makeText(this, "Choose only one hero", Toast.LENGTH_SHORT).show();
        } else {
            String formattedAnswer = heroListToSend.get(0).substring(0, 1).toUpperCase() + heroListToSend.get(0).substring(1);  // It's necessary to capitalise the first letter
            this.networkBackendService.sendMessageToServer("BCLASSESC," + formattedAnswer);
        }

    }

    /**
     * Reception of the list of spells available for the chosen classes
     * Parse it and pass it to the next activity
     *
     * @param textReceived
     */
    @Override
    public void handleReception(String textReceived) {

        String[] slicedMessage = textReceived.split(",");
        ArrayList<String> spellsList = new ArrayList<>();
        for (int index = 1; index < slicedMessage.length; index++) {                                   // Parsing of received spells
            spellsList.add(slicedMessage[index].toLowerCase());
        }
        //Toast.makeText(this, spellsList.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TrueSpellSelectionActivity.class);
        intent.putExtra(TrueSpellSelectionActivity.TAG_LIST_SPELL, spellsList);                        // We put in the intent the list of available classes
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
