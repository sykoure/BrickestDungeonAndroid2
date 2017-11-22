package com.app.remi.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.app.remi.test.soundServices.BallBounceService;
import com.app.remi.test.soundServices.BallDropService;
import com.app.remi.test.soundServices.BallStartService;


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

    int life = 10;
    int shield = 10;

    SpellBlock spellBlock;
    Barre paddle;
    Boule ball;
    int numBricks = 0;

    private Boolean playWithSensor;
    private float initialSensorValue;
    private Context mainActivityContext;


    public Moteur(Context context, Boolean playWithSensor, SensorManager sensorManager) {


        super(context);
        // We will need the context for many services
        this.mainActivityContext = context;
        this.playWithSensor = playWithSensor;
        // initialSensorValue is always set a 0 the first time, it allow to detect difference in Z axis positions
        initialSensorValue = 0;
        if (playWithSensor) {
            // We initialise the sensor only if the toggleButton has been checked
            Sensor accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // Here we choose the behavior for the sensor and his delay
            sensorManager.registerListener(mSensorEventListener, accelerometre, SensorManager.SENSOR_DELAY_UI);
        }

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
        spellBlock = new SpellBlock(screenX, screenY, screenX * 0.1, screenY * 0.3);

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

        paddle.update(fps, screenX);
        ball.update(fps);
        collisions();
        Log.d("SCREEN Y", String.valueOf(paddle.getHeight()));
    }

    public void draw() {

        Bitmap bal = BitmapFactory.decodeResource(getResources(), R.drawable.skull);

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(100, 0, 0, 0));
            paint.setColor(Color.argb(100, 255, 255, 255));


            canvas.drawRect(paddle.getRect(), paint);
            canvas.drawRect(spellBlock.getRect(), paint);
            canvas.drawRect(ball.getRect(), paint);
            canvas.drawRect(0, (float) (screenY * 0.2), screenX, 0, paint);

            paint.setColor(Color.argb(255, 0, 247, 255));

            int saut = 0;
            for (int i = 0; i < shield; i++) {
                canvas.drawOval((float) (screenX * 0.1 + saut), (float) (screenY * 0.1), (float) (screenX * 0.1 + saut + 20), (float) (screenY * 0.1 + 50), paint);
                saut = saut + 50;
            }

            saut = 0;
            paint.setColor(Color.argb(255, 255, 76, 76));
            for (int i = 0; i < life; i++) {
                canvas.drawOval((float) (screenX * 0.1 + saut), (float) (screenY * 0.15), (float) (screenX * 0.1 + saut + 20), (float) (screenY * 0.15 + 50), paint);
                saut = saut + 50;
            }

            float startX = ball.getRect().left - ball.getBallWidth();
            float startY = ball.getRect().top - ball.getBallHeight();
            float endX = ball.getRect().right + (bal.getWidth() / 10);
            float endY = ball.getRect().bottom + (bal.getHeight() / 5);

            //canvas.drawBitmap(bal, null, new RectF(startX, startY, endX, endY), null);

            paint.setColor(Color.argb(255, 0, 255, 255));
            canvas.drawRect(spellBlock.getLeftSide(), paint);
            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawRect(spellBlock.getRightSide(), paint);
            paint.setColor(Color.argb(255, 255, 255, 0));
            canvas.drawRect(spellBlock.getBotSide(), paint);
            paint.setColor(Color.argb(255, 0, 255, 0));
            canvas.drawRect(spellBlock.getTopSide(), paint);


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
                if (!playWithSensor) {
                    if (motionEvent.getX() > screenX / 2) {
                        paddle.setMovementState(paddle.RIGHT, screenX);
                    } else {
                        paddle.setMovementState(paddle.LEFT, screenX);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!playWithSensor) {
                    paddle.setMovementState(paddle.STOPPED, screenX);
                }
                break;
        }

        return true;
    }

    /**
     * Behavior for the sensor
     * TODO add precision to the sensor mouvement (the speed of the bar should be the difference between the last value and the new value)
     */
    final SensorEventListener mSensorEventListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        /**
         * If the value has changed the bar is moved, else the bar stop
         * @param sensorEvent
         */
        public void onSensorChanged(SensorEvent sensorEvent) {
            // Que faire en cas d'évènements sur le capteur ?
            if (sensorEvent.values[0] != initialSensorValue) {
                String toDisplay = "Accelerometer Z Value : " + sensorEvent.values[0];
                Log.d("ACCELEROMETRE", toDisplay);
                if (sensorEvent.values[0] <= 0) {
                    paddle.setMovementState(paddle.RIGHT, screenX);
                } else {
                    paddle.setMovementState(paddle.LEFT, screenX);
                }
                initialSensorValue = sensorEvent.values[0];
            }
            /*
            else {
                paddle.setMovementState(paddle.STOPPED, screenX);
            }*/

        }
    };

    void collisions() {
        if (RectF.intersects(spellBlock.getRect(), ball.getRect())) {
            if ((RectF.intersects(spellBlock.getLeftSide(), ball.getRect())) || (RectF.intersects(spellBlock.getRightSide(), ball.getRect()))) {
                if ((RectF.intersects(spellBlock.getTopSide(), ball.getRect())) || (RectF.intersects(spellBlock.getBotSide(), ball.getRect()))) {
                    ball.reverseXVelocity();
                    ball.reverseYVelocity();
                } else {
                    ball.reverseXVelocity();
                }

            } else {
                if ((RectF.intersects(spellBlock.getLeftSide(), ball.getRect())) || (RectF.intersects(spellBlock.getRightSide(), ball.getRect()))) {
                    ball.reverseXVelocity();
                    ball.reverseYVelocity();
                } else {
                    ball.reverseYVelocity();
                }
            }
        }
        if (RectF.intersects(paddle.getRect(), ball.getRect())) {
            //penser à prendre en compte la barre
            ball.reverseYVelocity();
        }
        if (ball.getRect().bottom > screenY) {

            life--;
            RectF rect = new RectF(paddle.getX() + (paddle.getLength() / 2) - (ball.getBallWidth() / 2)
                    , screenY - paddle.getHeight() + 10
                    , paddle.getX() + (paddle.getLength() / 2) + (ball.getBallWidth() / 2)
                    , screenY - paddle.getHeight() - ball.getBallHeight() + 10);
            ball.setRect(rect);
            float value = paddle.getX() + (paddle.getLength() / 2) - (ball.getBallWidth() / 2);

            Log.d("LEFT POSITION", String.valueOf(value));

            value = paddle.getHeight() + ball.getBallHeight() + 100;
            Log.d("TOP POSITION", String.valueOf(value));

            value = paddle.getX() + (paddle.getLength() / 2) + (ball.getBallWidth() / 2);
            Log.d("RIGHT POSITION", String.valueOf(value));

            value = paddle.getHeight() + 100;
            Log.d("BOTTOM POSITION", String.valueOf(value));


            paused = true;
        }
        if (ball.getRect().left < ball.getBallWidth() / 2) {
            ball.reverseXVelocity();
        } else if (ball.getRect().right > screenX - ball.getBallWidth() / 2) {
            ball.reverseXVelocity();
        } else if (ball.getRect().top < 0 + screenY * 0.2 + ball.getBallHeight()) {
            ball.reverseYVelocity();
        }
    }

    /**
     * Call this method to launch the "onStartCommand" method of the associated service
     */
    void playBallBounceSound() {
        Intent intent = new Intent(this.mainActivityContext, BallBounceService.class);
        mainActivityContext.startService(intent);
    }
    /**
     * Call this method to launch the "onStartCommand" method of the associated service
     */
    void playBallDropSound() {
        Intent intent = new Intent(this.mainActivityContext, BallDropService.class);
        mainActivityContext.startService(intent);
    }
    /**
     * Call this method to launch the "onStartCommand" method of the associated service
     */
    void playBallStartSound() {
        Intent intent = new Intent(this.mainActivityContext, BallStartService.class);
        mainActivityContext.startService(intent);
    }
}


