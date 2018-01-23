package com.app.remi.test.data;

/**
 * Created by Remi on 24/09/2017.
 */

import android.graphics.RectF;

public class Ball {

    //The hitbox of the ball
    private RectF rect;

    //Speed of the ball
    private float xSpeed;
    private float ySpeed;
    private float sommeSpeed;
    public final static  float SPEEDMAX = 1500;
    public final static  float SPEEDMIN = 250;


    //Dimension of the ball
    private float ballWidth = 10;
    private float ballHeight = 10;
    public final static float BALLWIDTHMIN = 3;
    public final static float BALLHEIGTHMIN = 3;
    public final static float BALLWIDTHMAX = 50;
    public final static float BALLHEIGTHMAX = 50;

    /**
     * The method Ball() is setting the values of the object Ball, its hitbox and its speed
     * @param screenX is the length of the screen
     * @param screenY is the height of the screen
     */
    public Ball(int screenX, int screenY){

        xSpeed = 200;
        ySpeed = -300;
        sommeSpeed = 500;

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

    //The method change the Yspeed in -YSpeed, this is changing the direction
    public void reverseYVelocity(){
        ySpeed = -ySpeed;
    }

    //The method change the Xspeed in -XSpeed, this is changing the direction
    public void reverseXVelocity(){
        xSpeed = - xSpeed;
    }


    /**
     * We give the position of the ball b1 to the ball b2
     * @param b1 is the first ball
     * @param b2 is the second ball
     */
    public void givePosition(Ball b1, Ball b2){
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

    /**
     * Get the ySpeed of the ball
     * @return the ySpeed
     */
    public float getySpeed(){
        return this.ySpeed;
    }

    /**
     * Get the xSpeed of the ball
     * @return the xSpeed
     */
    public float getxSpeed(){
        return this.xSpeed;
    }

    /**
     * This method allows us to modify the ball width
     * @param ballWidth
     */
    public void setBallWidth(float ballWidth){
        this.ballWidth = ballWidth;
    }

    /**
     * This method allows us to modify the ball height
     * @param ballHeight
     */
    public void setBallHeight(float ballHeight){
        this.ballHeight = ballHeight;
    }

    /**
     * this method return the sommeSpeed which is the addition of the xSpeed and the ySpeed
     * @return sommeSpeed
     */
    public float getSommeSpeed() {
        return sommeSpeed;
    }

    /**
     * This method allows us to modify the sommeSpeed, when the player activate a spell like acceleroBall for exemple
     * @param sommeSpeed
     */
    public void setSommeSpeed(float sommeSpeed) {
        this.sommeSpeed = sommeSpeed;
    }
}