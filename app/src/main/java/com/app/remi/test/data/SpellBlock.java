package com.app.remi.test.data;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Remi on 07/11/2017.
 */

public class SpellBlock {

    public static class SpellBlockBuilder{

        private String spell;
        private float cooldown = 0;
        private float cooldownDuration;
        private int width;
        private int height;
        private double xPosition,yPosition;
        private int xScreen;
        private int yScreen;

        public SpellBlockBuilder(int xScreen,int yScreen){
            this.xScreen = xScreen;
            this.yScreen = yScreen;
        }

        public SpellBlockBuilder spell(String spell){
            this.spell = spell;
            return this;
        }

        public SpellBlockBuilder dimension(int width,int height){
            this.height = height;
            this.width = width;
            return this;
        }

        public SpellBlockBuilder cooldown(float cooldown){
            this.cooldown = cooldown;
            return this;
        }

        public SpellBlockBuilder cooldownDuration(float cooldownDuration){
            this.cooldownDuration = cooldownDuration;
            return this;
        }

        public SpellBlockBuilder position(double xPosition,double yPosition){
            this.xPosition = xPosition;
            this.yPosition = yPosition;
            return this;
        }

        public SpellBlock build(){
            return new SpellBlock(this);
        }

    }
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
     */
    private SpellBlock(SpellBlockBuilder spellBlockBuilder){
        this.xScreen = spellBlockBuilder.xScreen;
        this.yScreen = spellBlockBuilder.yScreen;

        this.spell = spellBlockBuilder.spell;

        this.xPosition = spellBlockBuilder.xPosition;
        this.yPosition = spellBlockBuilder.yPosition;

        this.width = spellBlockBuilder.width;
        this.height = spellBlockBuilder.height;

        this.cooldown = spellBlockBuilder.cooldown;
        this.cooldownDuration = spellBlockBuilder.cooldownDuration;

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
