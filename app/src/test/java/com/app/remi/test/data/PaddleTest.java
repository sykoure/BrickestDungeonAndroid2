package com.app.remi.test.data;

import android.graphics.RectF;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Remi on 12/02/2018.
 * This class will test the ball class and its builder
 */
public class PaddleTest {

    // The paddle object
    private Paddle paddle = new Paddle.PaddleBuilder(0,0).
                                behavior(0).
                                dimension(0,0).
                                position(0,0).
                                speed(0).
                                build();

    // We will test the hitbox of the paddle
    @Test
    public void getRect() throws Exception {
        RectF rect = new RectF(0,0,0,0);
        assertEquals("Error, this is not the same rect",rect.left,paddle.getRect().left,0);
        assertEquals("Error, this is not the same rect",rect.right,paddle.getRect().right,0);
        assertEquals("Error, this is not the same rect",rect.top,paddle.getRect().top,0);
        assertEquals("Error, this is not the same rect",rect.bottom,paddle.getRect().bottom,0);
    }

    // We will test the position of the paddle
    @Test
    public void getX() throws Exception {
        assertEquals(0,paddle.getX(),0);
    }

    @Test
    public void setX() throws Exception {
        paddle.setX(2);
        assertEquals(2,paddle.getX(),0);
    }

    @Test
    public void setY() throws Exception {
        paddle.setY(3);
        assertEquals(3,paddle.getY(),0);
    }

    @Test
    public void getY() throws Exception {
        assertEquals(0,paddle.getY(),0);
    }

    // We will test the size of the paddle
    @Test
    public void getLength() throws Exception {
        assertEquals(0,paddle.getLength(),0);
    }

    @Test
    public void getHeight() throws Exception {
        assertEquals(0,paddle.getHeight(),0);
}

    @Test
    public void setLength() throws Exception {
        paddle.setLength(21);
        assertEquals(21,paddle.getLength(),0);
    }

    @Ignore
    @Test
    public void setSensor() throws Exception {
        fail();
    }

    // We will test the state of the paddle
    @Test
    public void getPaddleMoving() throws Exception {
        assertEquals(0,paddle.getPaddleMoving(),0);
    }

    @Test
    public void setPaddleMoving() throws Exception {
        paddle.setPaddleMoving(0);
        assertEquals(0,paddle.getPaddleMoving());
    }

    // We will test the speed of the paddle
    @Test
    public void getSpeed() throws Exception {
        assertEquals(0,paddle.getSpeed(),0);
    }

    @Test
    public void setSpeed() throws Exception {
        paddle.setSpeed(1);
        assertEquals(1,paddle.getSpeed(),0);
    }

}