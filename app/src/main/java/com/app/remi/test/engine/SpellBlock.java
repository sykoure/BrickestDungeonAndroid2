package com.app.remi.test.engine;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Remi on 07/11/2017.
 */

public class SpellBlock {

    /**
     * rect is the global hitbox of the SpellBlock
     * leftSide is the left hitbox
     * rightSide is the right hitbox
     * topSide is the top hitbox
     * Botside is the bottom hitbox
     */
    private RectF rect,leftSide,rightSide,topSide,botSide;

    //The spell of a Spellblock
    private String spell;               // Nom du spell
    private float cooldown = 0;         // Cooldown actuel
    private float cooldownDuration = 5; // Temps de recharge d'un spell

    //This is the dimension of the Spellblock
    private int width;
    private int height;

    //This is the position of a SpellBlock
    private double xPosition,yPosition;

    //This is the length of the screen and the heigth of the screen
    private int xScreen;
    private int yScreen;

    /**
     * This is the method to create a object SpellBlock
     * It will set its position, screen dimension, dimension and its hitbox according to its current position.
     * @param xScreen is the lenght of the screen
     * @param yScreen is the height of the screen
     * @param xPosition is the xPosition of the Spellblock, the xposition of the upper-left corner
     * @param yPosition is the yPosition of the Spellblock, the yposition of the upper-left corner
     */
    public SpellBlock(int xScreen, int yScreen,double xPosition,double yPosition,int width,int height,String spell){
        this.xScreen = xScreen;
        this.yScreen = yScreen;

        this.spell = spell;

        this.xPosition = xPosition;
        this.yPosition = yPosition;

        this.width = width;
        this.height = height;

        //We set the hitbox sides of the SpellBlock
        rect = new RectF((float)xPosition, (float)yPosition, (float)xPosition + width,(float) yPosition +height);
        leftSide = new RectF((float)xPosition,(float)yPosition+1,(float)xPosition+3,(float)yPosition+height-1);
        rightSide = new RectF((float)xPosition + width - 3,(float)yPosition+1,(float)xPosition + width,(float)yPosition+height-1);
        topSide = new RectF((float)xPosition,(float)yPosition+3,(float)xPosition+width,(float)yPosition);
        botSide = new RectF((float)xPosition,(float)yPosition+height-3,(float)xPosition+width,(float)yPosition+height);
    }

    /**
     * @return the actual hitbox of the spellblock
     */
    public RectF getRect() {
        return rect;
    }

    /**
     * @return the actual left hitbox of the spellblock
     */
    public RectF getLeftSide(){
        return leftSide;
    }

    /**
     * @return the actual right hitbox of the spellblock
     */
    public RectF getRightSide(){
        return rightSide;
    }

    /**
     * @return the actual top hitbox of the spellblock
     */
    public RectF getTopSide(){
        return topSide;
    }

    /**
     * @return the actual bottom hitbox of the spellblock
     */
    public RectF getBotSide(){
        return botSide;
    }

    /**
     * Return the spell of the spellBlock
     * @return spell
     */
    public String getSpell(){
        return spell;
    }

    /**
     * Return the cooldown of the spell block which is the remaining time for the block in order to
     * use its spell again
     * @return cooldown
     */
    public float getCooldown() {
        return cooldown;
    }

    /**
     * This method is useful to modify the cooldown and prevent the player use a spell before some time
     * @param cooldown
     */
    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * This method is used when the player hits a spellblock with the ball, in that case, we modify the cooldown
     * of a spellblock with its cooldownDuration
     * @return cooldownDuration
     */
    public float getCooldownDuration() {
        return cooldownDuration;
    }

}
