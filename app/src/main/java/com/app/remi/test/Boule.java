package com.app.remi.test;

/**
 * Created by Remi on 24/09/2017.
 */

import android.graphics.Rect;
import android.graphics.RectF;
import java.util.Random;

public class Boule {

    private RectF rect;
    private float xSpeed;
    private float ySpeed;
    private float ballWidth = 10;
    private float ballHeight = 10;

    public Boule(int screenX, int screenY){

        xSpeed = 200;
        ySpeed = -400;

        rect = new RectF();

    }

    public RectF getRect(){
        return rect;
    }

    public void setRect(RectF rect){
       this.rect=rect;
    }

    public void update(long fps){
        rect.left = rect.left + (xSpeed / fps);
        rect.top = rect.top + (ySpeed / fps);
        rect.right = rect.left + ballWidth;
        rect.bottom = rect.top - ballHeight;
    }

    public void reverseYVelocity(){
        ySpeed = -ySpeed;
    }

    public void reverseXVelocity(){
        xSpeed = - xSpeed;
    }

    public void setRandomXVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0){
            reverseXVelocity();
        }
    }

    public void reset(int x, int y){
        rect.left = x / 2;
        rect.top = y - 20;
        rect.right = x / 2 + ballWidth;
        rect.bottom = y - 20 - ballHeight;
    }

    public float getBallWidth(){
        return ballWidth;
    }

    public float getBallHeight(){
        return ballHeight;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }
    public void setySpeed(float ySpeed){
        this.ySpeed = ySpeed;
    }

}