package com.app.remi.test.data;

import android.graphics.RectF;

public class Paddle {

    //Hitbox of the paddle
    private RectF rect;

    //The dimension of the paddle
    private float length;
    private float height;
    public final static float SIZEMAX = 260;
    public final static float SIZEMIN = 50;

    //Position of the paddle
    private float x;
    private float y;

    //Speed of the paddle
    private float speed;

    //At the beginning, the paddle has to be stopped
    private int paddleMoving = 0;

    /**
     * The method Paddle() sets the dimension of the paddle, its hitbox and its speed.
     * @param screenX This is the length of the screen
     * @param screenY This is the height of the screen
     */
    public Paddle(int screenX, int screenY) {
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
     * Return the position X of the paddle
     * @return x
     */
    public float getX() {
        return this.x;
    }

    /**
     * Return the current Length of the paddle
     * @return length
     */
    public float getLength() {
        return this.length;
    }

    /**
     * Return the current Height of the paddle
     * @return height
     */
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

    /**
     * This method allows us to modify the length of the paddle
     * @param length
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     * This method allows us to know the state of the paddle
     * @return the current paddle state
     */
    public int getPaddleMoving(){
        return this.paddleMoving;
    }

    /**
     * This method allows us to change the value of the paddle's state
     * @param paddleMoving is the new state
     */
    public void setPaddleMoving(int paddleMoving){
        this.paddleMoving = paddleMoving;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}