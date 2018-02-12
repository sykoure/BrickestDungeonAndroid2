package com.app.remi.test.data;

import android.graphics.RectF;
import android.util.Log;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Remi on 12/02/2018.
 * This class will test the ball class and its builder
 */
public class BallTest {

    // The ball
    private Ball ball = new Ball.BallBuilder(0,0).
                        dimension(0,0).
                        position(0,0,0,0).
                        build();

    // We are going to test the hitbox of the ball
    @Test
    public void getRect() throws Exception {
        RectF rect = new RectF(0,0,0,0);
        assertEquals("Error, this is not the same rect",rect.left,ball.getRect().left,0);
        assertEquals("Error, this is not the same rect",rect.right,ball.getRect().right,0);
        assertEquals("Error, this is not the same rect",rect.top,ball.getRect().top,0);
        assertEquals("Error, this is not the same rect",rect.bottom,ball.getRect().bottom,0);
    }

    @Test
    public void setRect() throws Exception {
        RectF rect = new RectF(0,0,0,0);
        ball.setRect(rect);
        assertEquals("Error, this is not the same rect",rect.left,ball.getRect().left,0);
        assertEquals("Error, this is not the same rect",rect.right,ball.getRect().right,0);
        assertEquals("Error, this is not the same rect",rect.top,ball.getRect().top,0);
        assertEquals("Error, this is not the same rect",rect.bottom,ball.getRect().bottom,0);
    }

    // We will test some methods to change its directory
    @Test
    public void reverseYVelocity() throws Exception {
        ball.setySpeed((int)(-3.21));
        ball.reverseYVelocity();
        assertEquals(3,ball.getySpeed(),0);
    }

    @Test
    public void reverseXVelocity() throws Exception {
        ball.setxSpeed((int)(-3.21));
        ball.reverseXVelocity();
        assertEquals(3,ball.getxSpeed(),0);
    }

    // We will test if the method givePosition is working
    @Test
    public void givePosition() throws Exception {
        Ball testBall = new Ball.BallBuilder(0,0).
                        position(1,2,3,4).
                        build();
        ball.givePosition(testBall,ball);
        assertEquals("Error, this is not the same rect",testBall.getRect().left,ball.getRect().left,0);
        assertEquals("Error, this is not the same rect",testBall.getRect().right,ball.getRect().right,0);
        assertEquals("Error, this is not the same rect",testBall.getRect().top,ball.getRect().top,0);
        assertEquals("Error, this is not the same rect",testBall.getRect().bottom,ball.getRect().bottom,0);
    }

    // We are going to check if we can have the good values for the ball's size
    @Test
    public void getBallWidth() throws Exception {
        assertEquals(0,ball.getBallWidth(),0);
    }

    @Test
    public void getBallHeight() throws Exception {
        assertEquals(0,ball.getBallHeight(),0);
    }

    // We are going to test the speed of the ball
    @Test
    public void setxSpeed() throws Exception {
        ball.setxSpeed(3);
        assertEquals(3,ball.getxSpeed(),0);
    }

    @Test
    public void getySpeed() throws Exception {
        assertEquals(0,ball.getySpeed(),0);
    }

    @Test
    public void getxSpeed() throws Exception {
        assertEquals(0,ball.getxSpeed(),0);
    }

    @Test
    public void setBallWidth() throws Exception {
        ball.setBallWidth(Float.MAX_VALUE);
        assertEquals(Float.MAX_VALUE,ball.getBallWidth(),0);
    }

    @Test
    public void setBallHeight() throws Exception {
        ball.setBallHeight(Float.MAX_VALUE);
        assertEquals(Float.MAX_VALUE,ball.getBallHeight(),0);
    }

    @Test
    public void getSommeSpeed() throws Exception {
        assertEquals(ball.getxSpeed()+ball.getySpeed(),ball.getSommeSpeed(),0);
    }

    @Test
    public void setSommeSpeed() throws Exception {
        assertEquals(ball.getxSpeed()+ball.getySpeed(),ball.getSommeSpeed(),0);
    }

}