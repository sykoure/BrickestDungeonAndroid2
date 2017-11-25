package com.app.remi.test;

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
    public SpellBlock(int xScreen, int yScreen,double xPosition,double yPosition){
        this.xScreen = xScreen;
        this.yScreen = yScreen;

        this.xPosition = xPosition;
        this.yPosition = yPosition;

        width = 100;
        height = 100;

        rect = new RectF((float)xPosition, (float)yPosition, (float)xPosition + width,(float) yPosition +height);
        leftSide = new RectF((float)xPosition,(float)yPosition,(float)xPosition+1,(float)yPosition+height);
        rightSide = new RectF((float)xPosition + width - 1,(float)yPosition,(float)xPosition + width,(float)yPosition+height);
        topSide = new RectF((float)xPosition,(float)yPosition+1,(float)xPosition+width,(float)yPosition);
        botSide = new RectF((float)xPosition,(float)yPosition+height-1,(float)xPosition+width,(float)yPosition+height);
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
}
