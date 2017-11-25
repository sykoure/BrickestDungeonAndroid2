package com.app.remi.test;

import android.graphics.RectF;

public class Barre {

    //Hitbox of the paddle
    private RectF rect;

    //The dimension of the paddle
    private float length;
    private float height;

    //Position of the paddle
    private float x;
    private float y;

    private float speed;

    //State of the paddle's direction
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    //At the beginning, the paddle has to be stopped
    private int paddleMoving = STOPPED;

    /**
     * The method Barre() sets the dimension of the paddle, its hitbox and its speed.
     * @param screenX This is the length of the screen
     * @param screenY This is the height of the screen
     */
    public Barre(int screenX, int screenY) {
        length = 130;
        height = 30;

        x = screenX / 2;
        y = screenY - 20;

        rect = new RectF(x, y, x + length, y + height);

        speed = 350;
    }

    /**
     * @return the hitbox of the paddle
     */
    public RectF getRect() {
        return rect;
    }

    /**
     * The method setMovementState() is setting the paddle's state, if it has to go to the left, right or to be stopped.
     * The parameter screen is used to stop the paddle if it's going to far on the left or on the right.
     * @param state This is the paddle's direction
     * @param screen This is the length of the screen
     */
    public void setMovementState(int state, float screen) {
        if ((state == 1) && (rect.left > 0)) {
            paddleMoving = 1;
        } else if ((state == 2) && (rect.right < screen)) {
            paddleMoving = 2;
        } else {
            paddleMoving = 0;
        }
    }

    /**
     * The method() update will be run fps times per seconds and will update the paddle's hitbox position
     * (and so the position of the paddle too)
     * @param fps The number of update for each second
     * @param screen The length of the screen
     */
    public void update(long fps, float screen) {
        if ((paddleMoving == LEFT) && (rect.left > 0)) {
            x = x - speed / fps;
        }

        if ((paddleMoving == RIGHT) && (rect.right < screen)) {
            x = x + speed / fps;
        }

        rect.left = x;
        rect.right = x + length;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getLength() {
        return this.length;
    }

    public float getHeight() {
        return this.height;
    }

    /**
     * The sensor return a value between -10 and 10
     * And considering that the paddle's speed per second is 13x, we have have to multiply the parameter by 13
     * @param speed is the value passed by the sensor between -10 and 10
     */
    public void setSpeedSensor(float speed) {
        this.speed = speed * 13;
    }
}