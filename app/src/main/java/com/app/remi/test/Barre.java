package com.app.remi.test;

import android.graphics.RectF;

public class Barre {

    private RectF rect;
    private float length;
    private float height;
    private float x;
    private float y;
    private float speed;


    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int paddleMoving = STOPPED;


    public Barre(int screenX, int screenY){
        length = 500;
        height = 100;

        x = screenX / 2;
        y = screenY - 20;

        rect = new RectF(x, y, x + length, y + height);

        speed = 350;
    }

    public RectF getRect(){

        return rect;
    }


    public void setMovementState(int state,float screen){
        if((state==1)&&(rect.left>0)) {
            paddleMoving = 1;
        }
        else if((state==2)&&(rect.right<screen)){
            paddleMoving = 2;
        }
        else{
            paddleMoving = 0;
        }
    }


    public void update(long fps,float screen){
        if((paddleMoving == LEFT)&&(rect.left>0)){
            x = x - speed / fps;
        }

        if((paddleMoving == RIGHT)&&(rect.right<screen)){
            x = x + speed / fps;
        }

        rect.left = x;
        rect.right = x + length;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getLength(){
        return this.length;
    }

    public float getHeight(){
        return this.height;
    }

}