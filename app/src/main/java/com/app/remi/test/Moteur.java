package com.app.remi.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


/**
 * Created by Remi on 25/10/2017.
 */

public class Moteur extends SurfaceView implements Runnable {


    Thread gameThread = null;

    SurfaceHolder ourHolder;
    boolean playing;
    boolean paused = true;

    Canvas canvas;
    Paint paint;

    long fps;
    private long timeThisFrame;

    int screenX;
    int screenY;

    SpellBlock spellBlock;
    Barre paddle;
    Boule ball;
    int numBricks = 0;


    public Moteur(Context context) {


        super(context);

        ourHolder = getHolder();
        paint = new Paint();

        //chopper la taille de l'écran sans etre dans une activity
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenX = size.x;
        screenY = size.y;

        paddle = new Barre(screenX, screenY);
        ball = new Boule(screenX, screenY);
        spellBlock = new SpellBlock(screenX,screenY,screenX*0.3,screenY*0.3);

        reset();

    }

    public void reset() {

        ball.reset(screenX, screenY);


    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            if (!paused) {
                update();
            }
            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

        }

    }

    public void update() {

        paddle.update(fps,screenX);
        ball.update(fps);
        collisions();
    }

    public void draw() {

        Bitmap bal = BitmapFactory.decodeResource(getResources(),R.drawable.skull);

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(100, 0, 0, 0));




            paint.setColor(Color.argb(100, 255, 255, 255));


            canvas.drawRect(paddle.getRect(), paint);
            canvas.drawRect(spellBlock.getRect(),paint);
            canvas.drawRect(ball.getRect(), paint);

            float startX = ball.getRect().left- ball.getBallWidth();
            float startY = ball.getRect().top-ball.getBallHeight();
            float endX =  ball.getRect().right +(bal.getWidth()/10) ;
            float endY = ball.getRect().bottom+(bal.getHeight()/5);

            //canvas.drawBitmap(bal, null, new RectF(startX, startY, endX, endY), null);

            paint.setColor(Color.argb(255,0,255,255));
            canvas.drawRect(spellBlock.getLeftSide(),paint);
            paint.setColor(Color.argb(255,255,0,0));
            canvas.drawRect(spellBlock.getRightSide(),paint);
            paint.setColor(Color.argb(255,255,255,0));
            canvas.drawRect(spellBlock.getBotSide(),paint);
            paint.setColor(Color.argb(255,0,255,0));
            canvas.drawRect(spellBlock.getTopSide(),paint);


            paint.setColor(Color.argb(255, 249, 129, 0));

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }


    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        System.out.println("carre gauche :  " + spellBlock.getRect().left);
        System.out.println("boule gauche :  " + ball.getRect().left);

        System.out.println("carre droite :  " + spellBlock.getRect().right);
        System.out.println("boule droite :  " + spellBlock.getRect().right);

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched the screen

            case MotionEvent.ACTION_DOWN:
                paused = false;
                if (motionEvent.getX() > screenX / 2) {
                    paddle.setMovementState(paddle.RIGHT,screenX);
                }
                else {
                    paddle.setMovementState(paddle.LEFT,screenX);
                }

                break;

            case MotionEvent.ACTION_UP:

                paddle.setMovementState(paddle.STOPPED,screenX);
                break;
        }

        return true;
    }

    void collisions(){
        if (RectF.intersects(spellBlock.getRect(),ball.getRect())){
            if((RectF.intersects(spellBlock.getLeftSide(),ball.getRect()))||(RectF.intersects(spellBlock.getRightSide(),ball.getRect()))){
                if((RectF.intersects(spellBlock.getTopSide(),ball.getRect()))||(RectF.intersects(spellBlock.getBotSide(),ball.getRect()))){
                    ball.reverseXVelocity();
                    ball.reverseYVelocity();
                }
                else{
                    ball.reverseXVelocity();
                }

            }
            else {
                if((RectF.intersects(spellBlock.getLeftSide(),ball.getRect()))||(RectF.intersects(spellBlock.getRightSide(),ball.getRect()))){
                    ball.reverseXVelocity();
                    ball.reverseYVelocity();
                }
                else {
                    ball.reverseYVelocity();
                }
            }
        }
        if (RectF.intersects(paddle.getRect(), ball.getRect())) {
            //penser à prendre en compte la barre
            ball.reverseYVelocity();
        }
        if (ball.getRect().bottom > screenY) {
            RectF rect = new RectF(paddle.getX()+(paddle.getLength()/2)-(ball.getBallWidth()/2)
                                   ,paddle.getHeight()+ball.getBallHeight()
                                   ,paddle.getX()+(paddle.getLength()/2)+(ball.getBallWidth()/2)
                                   ,paddle.getHeight()+100);
            paused = true;
        }
        if (ball.getRect().left < ball.getBallWidth()/2) {
            ball.reverseXVelocity();
        }
        else if (ball.getRect().right > screenX - ball.getBallWidth()/2) {
            ball.reverseXVelocity();
        }
        else if (ball.getRect().top < 0) {
            ball.reverseYVelocity();
        }
    }
}

