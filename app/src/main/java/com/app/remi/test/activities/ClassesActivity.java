package com.app.remi.test.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.remi.test.R;

import java.util.ArrayList;

/**
 * Activity where the player choose his class for his future games
 * TODO add the connection and the choosing part
 */
public class ClassesActivity extends Activity {

    private ImageView heroImagaView1, heroImagaView2, heroImagaView3;
    private Button goToMatchMakingActivityButton;
    private ArrayList<String> listHeroesName;
    private ArrayList<ImageView> listHeroesImage;
    private ArrayList<Boolean> listSelectedImageView;
    private ArrayList<Boolean> listVisibleImageView;
    public static final String TAG_LIST_HERO = "com.app.remi.test.ClassesActivity.TAG_LIST_HERO";
    private Button goToSpellActitivy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        this.goToSpellActitivy = (Button) findViewById(R.id.goToSpellbutton);

        this.heroImagaView1 = (ImageView) findViewById(R.id.heroImageView1);
        this.heroImagaView2 = (ImageView) findViewById(R.id.heroImageView2);
        this.heroImagaView3 = (ImageView) findViewById(R.id.heroImageView3);
        this.goToMatchMakingActivityButton = (Button) findViewById(R.id.goToSpellbutton);

        // All the heroes start unchecked
        this.heroImagaView1.setImageAlpha(90);
        this.heroImagaView2.setImageAlpha(90);
        this.heroImagaView3.setImageAlpha(90);

        // TODO remove this, the list will be sent by the server or passed as an argument
        this.listHeroesName = new ArrayList<String>();

        this.listHeroesImage = new ArrayList<ImageView>();
        this.listVisibleImageView = new ArrayList<Boolean>();
        this.listSelectedImageView = new ArrayList<Boolean>();

        // TODO remove this, the list will be sent by the server or passed as an argument
        this.listHeroesName.add("warrior");
        this.listHeroesName.add("paladin");
        this.listHeroesName.add("wizard");


        this.listInstantiation();
        this.setHeroImageVisibility();
        this.setHeroImageContent();
    }

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


    public ArrayList<String> putSelectedHeroInArray() {
        ArrayList<String> arrayToReturn = new ArrayList<>();
        for (int index = 0; index < this.listHeroesName.size(); index++) {
            if(this.listSelectedImageView.get(index))
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

        if(heroListToSend.size() != 1){
            Toast.makeText(this,"Choose only one hero",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, TrueSpellSelectionActivity.class);
            intent.putExtra(TAG_LIST_HERO, heroListToSend);
            startActivity(intent);
        }

    }
}
