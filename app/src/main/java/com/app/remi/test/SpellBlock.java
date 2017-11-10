package com.app.remi.test;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Remi on 07/11/2017.
 */

public class SpellBlock {

    private RectF rect,leftSide,rightSide,topSide,botSide;

    private int width;
    private int height;

    private double xPosition,yPosition;
    private int xScreen;
    private int yScreen;

    // icon file path
    private final static String LINK = "";

    public SpellBlock(int xScreen, int yScreen,double xPosition,double yPosition){
        this.xScreen = xScreen;
        this.yScreen = yScreen;

        this.xPosition = xPosition;
        this.yPosition = yPosition;

        width = 200;
        height = 200;

        rect = new RectF((float)xPosition, (float)yPosition, (float)xPosition + width,(float) yPosition +height);
        leftSide = new RectF((float)xPosition,(float)yPosition,(float)xPosition+1,(float)yPosition+height);
        rightSide = new RectF((float)xPosition + width - 1,(float)yPosition,(float)xPosition + width,(float)yPosition+height);
        topSide = new RectF((float)xPosition,(float)yPosition+1,(float)xPosition+width,(float)yPosition);
        botSide = new RectF((float)xPosition,(float)yPosition+height-1,(float)xPosition+width,(float)yPosition+height);
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    public RectF getLeftSide(){
        return leftSide;
    }

    public RectF getRightSide(){
        return rightSide;
    }

    public RectF getTopSide(){
        return topSide;
    }

    public RectF getBotSide(){
        return botSide;
    }
}
