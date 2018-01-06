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
     * Set the new hitbox of the spellblock
     * @param rect is the new hitbox of the SpellBlock
     */
    public void setRect(RectF rect) {
        this.rect = rect;
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

    public String getSpell(){
        return spell;
    }

    public void setSpell(String spell){
        this.spell = spell;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public float getCooldownDuration() {
        return cooldownDuration;
    }

    public void setCooldownDuration(float cooldownDuration) {
        this.cooldownDuration = cooldownDuration;
    }

    public void setLeftSide(RectF leftSide) {
        this.leftSide = leftSide;
    }

    public void setRightSide(RectF rightSide) {
        this.rightSide = rightSide;
    }

    public void setTopSide(RectF topSide) {
        this.topSide = topSide;
    }

    public void setBotSide(RectF botSide) {
        this.botSide = botSide;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getxPosition() {
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public int getxScreen() {
        return xScreen;
    }

    public void setxScreen(int xScreen) {
        this.xScreen = xScreen;
    }

    public int getyScreen() {
        return yScreen;
    }

    public void setyScreen(int yScreen) {
        this.yScreen = yScreen;
    }
}
