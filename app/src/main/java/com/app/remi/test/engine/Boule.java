package com.app.remi.test.engine;

/**
 * Created by Remi on 24/09/2017.
 */

import android.graphics.Rect;
import android.graphics.RectF;
import java.util.Random;

public class Boule {

    //The hitbox of the ball
    private RectF rect;

    //Speed of the ball
    private float xSpeed;
    private float ySpeed;

    //Dimension of the ball
    private float ballWidth = 10;
    private float ballHeight = 10;

    /**
     * The method Boule() is setting the values of the object Boule, its hitbox and its speed
     * @param screenX is the length of the screen
     * @param screenY is the height of the screen
     */
    public Boule(int screenX, int screenY){

        xSpeed = 100;
        ySpeed = -400;

        rect = new RectF();

    }

    /**
     * @return the hitbox of the ball
     */
    public RectF getRect(){
        return rect;
    }

    /**
     * @param rect is the new hitbox given to the ball (if the ball hits the bottom of the screen for exemple)
     */
    public void setRect(RectF rect){
       this.rect=rect;
    }

    /**
     * The method update() is fixing the position of the hitbox (and so the ball) fps times per second
     * @param fps The number of time the hitbox will be updated
     */
    public void update(long fps){
        rect.left = rect.left + (xSpeed / fps);
        rect.top = rect.top + (ySpeed / fps);
        rect.right = rect.left + ballWidth;
        rect.bottom = rect.top - ballHeight;
    }

    //The method change the Yspeed in -YSpeed, this is changing the direction
    public void reverseYVelocity(){
        ySpeed = -ySpeed;
    }

    //The method change the Xspeed in -XSpeed, this is changing the direction
    public void reverseXVelocity(){
        xSpeed = - xSpeed;
    }

    /**
     * This method is running one time at the beginning to put the right position of the ball
     * @param x is the length of the screen
     * @param y is the height of the screen
     */
    public void reset(int x, int y){
        rect.left = x / 2;
        rect.top = y - 20;
        rect.right = x / 2 + ballWidth;
        rect.bottom = y - 20 - ballHeight;
    }

    public void givePosition(Boule b1,Boule b2){
        b1.rect.left = b2.rect.left;
        b1.rect.right = b2.rect.right;
        b1.rect.top = b2.rect.top;
        b1.rect.bottom = b2.rect.bottom;
    }

    /**
     * @return the width of the ball
     */
    public float getBallWidth(){
        return ballWidth;
    }

    /**
     * @return return the height of the ball
     */
    public float getBallHeight(){
        return ballHeight;
    }

    /**
     * Set the Xspeed of the ball
     * @param xSpeed is the new xSpeed given
     */
    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    /**
     * Set the YSpeed of the ball
     * @param ySpeed is the new ySpeed given
     */
    public void setySpeed(float ySpeed){
        this.ySpeed = ySpeed;
    }

    public float getySpeed(){
        return this.ySpeed;
    }

    public float getxSpeed(){
        return this.xSpeed;
    }

    public void setBallWidth(float ballWidth){
        this.ballWidth = ballWidth;
    }

    public void setBallHeight(float ballHeight){
        this.ballHeight = ballHeight;
    }
}