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

    Thread gameThread = null;   // The Engine class thread

    //Thanks to the SurfaceView class, we will be able to draw all the objects that we want.
    //The surfaceHolder is a under-coat created by the SurfaceView class to draw objects.

    SurfaceHolder ourHolder;    // This is the under-coat

    boolean playing;            // If the game is frozen or not
    boolean paused = true;      // If the game is paused or not

    Canvas canvas;              // The canvas if the displayer of the SurfaceView Class

    Paint paint;                // The Paint is the paintbrush of the SurfaceView

    long fps;                   // The number of time that we will update the thread
    private long timeThisFrame;

    int screenX;                // The length of the screen
    int screenY;                // The height of the screen

    int life = 10;              // The number of life
    int shield = 10;            // The number of shield

    //TODO Create a List of spellBlock and a List of Ball
    SpellBlock spellBlock;      // The spellBlock
    Barre paddle;               // The paddle
    Boule ball;                 // The ball

    private Boolean playWithSensor;
    private float initialSensorValue;       // The value with which the first sensor value will be compared
    private Context mainActivityContext;    // The Context of the mainActivity used for Services

    /**
     *
     * @param context
     * @param playWithSensor Boolean value, define the playstyle, activate or not the accelerometer, desactivate the touch screen.
     * @param sensorManager The sensor manager used to manage the accelerometer
     */
    public Moteur(Context context, Boolean playWithSensor, SensorManager sensorManager) {

        super(context);

        this.mainActivityContext = context;         // We will need the context for many services
        this.playWithSensor = playWithSensor;
        initialSensorValue = 0;                     // initialSensorValue is always set a 0 the first time, it allow to detect difference in Z axis positions
        if (playWithSensor) {
            // We initialise the sensor only if the toggleButton has been checked
            Sensor accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // Here we choose the behavior for the sensor and his delay
            sensorManager.registerListener(mSensorEventListener, accelerometre, SensorManager.SENSOR_DELAY_UI);
        }


        this.ourHolder = getHolder();   //Initializing the ourHolder objecet
        this.paint = new Paint();       //Initializing the paint object

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

    /**
     * This method will launch the method update() for each objects and the method collisions()
     */
    public void update() {
        paddle.update(fps, screenX);
        ball.update(fps);
        collisions();
    }

    /**
     * This method will render the canvas on the ourHolder
     */
    public void draw() {

        //bal is the object that will take care of the image of the skull.
        Bitmap bal = BitmapFactory.decodeResource(getResources(), R.drawable.skull);

        if (ourHolder.getSurface().isValid()) {

            //we set the canvas as the "drawer" of our canvas
            canvas = ourHolder.lockCanvas();

            //we put the background of the game in the canvas, a big black rectangle
            canvas.drawColor(Color.argb(100, 0, 0, 0));

            //the paint (paintbrush) will now has a white color
            paint.setColor(Color.argb(100, 255, 255, 255));

            //we are putting each objects in the canvas
            canvas.drawRect(paddle.getRect(), paint);
            canvas.drawRect(spellBlock.getRect(), paint);
            canvas.drawRect(ball.getRect(), paint);

            //this is the HUD
            canvas.drawRect(0, (float) (screenY * 0.2), screenX, 0, paint);

            //the paint (paintbrush) will now has a teal color
            paint.setColor(Color.argb(255, 0, 247, 255));

            int saut = 0;

            //this will draw as many oval as the number or shield remaining
            for (int i = 0; i < shield; i++) {
                canvas.drawOval((float) (screenX * 0.1 + saut), (float) (screenY * 0.1), (float) (screenX * 0.1 + saut + 20), (float) (screenY * 0.1 + 50), paint);
                saut = saut + 50;
            }

            saut = 0;

            //the paint (paintbrush) will now has a red color
            paint.setColor(Color.argb(255, 255, 76, 76));

            //this will draw as many oval as the number or life remaining
            for (int i = 0; i < life; i++) {
                canvas.drawOval((float) (screenX * 0.1 + saut), (float) (screenY * 0.15), (float) (screenX * 0.1 + saut + 20), (float) (screenY * 0.15 + 50), paint);
                saut = saut + 50;
            }

            float startX = ball.getRect().left - ball.getBallWidth();
            float startY = ball.getRect().top - ball.getBallHeight();
            float endX = ball.getRect().right + (bal.getWidth() / 10);
            float endY = ball.getRect().bottom + (bal.getHeight() / 5);

            //canvas.drawBitmap(bal, null, new RectF(startX, startY, endX, endY), null);

            //we are drawing each side for the Spellblocks
            paint.setColor(Color.argb(255, 0, 255, 255));
            canvas.drawRect(spellBlock.getLeftSide(), paint);
            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawRect(spellBlock.getRightSide(), paint);
            paint.setColor(Color.argb(255, 255, 255, 0));
            canvas.drawRect(spellBlock.getBotSide(), paint);
            paint.setColor(Color.argb(255, 0, 255, 0));
            canvas.drawRect(spellBlock.getTopSide(), paint);


            paint.setColor(Color.argb(255, 249, 129, 0));

            //Display all the canvas
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
        playing = true;                      // game is not freeze anymore
        gameThread = new Thread(this); // a new thread is on
        gameThread.start();
    }

    /**
     * The method touchEvent will translate our action with the phone
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched the screen

            case MotionEvent.ACTION_DOWN:
                paused = false;

                //if the player is touching the left part of the screen or the right part of the screen
                if (!playWithSensor) {
                    if (motionEvent.getX() > screenX / 2) {
                        paddle.setMovementState(paddle.RIGHT, screenX);
                    } else {
                        paddle.setMovementState(paddle.LEFT, screenX);
                    }
                }
                break;

            //If the player is not touching the phone anymore
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

    /**
     * This method is checking the collisions between the ball and the others object
     */
    void collisions() {

        //collisions between the ball and the spellblocks
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

        //Collision between the ball and the the paddle
        if (RectF.intersects(paddle.getRect(), ball.getRect())) {
            //penser à prendre en compte la barre
            ball.reverseYVelocity();
        }

        //If the ball is hitting the bottom of the screen
        if (ball.getRect().bottom > screenY) {

            life--;

            //update the ball location to put it on the paddle
            RectF rect = new RectF(paddle.getX() + (paddle.getLength() / 2) - (ball.getBallWidth() / 2)
                    , screenY - paddle.getHeight() + 10
                    , paddle.getX() + (paddle.getLength() / 2) + (ball.getBallWidth() / 2)
                    , screenY - paddle.getHeight() - ball.getBallHeight() + 10);
            ball.setRect(rect);

            paused = true;      //freeze the game
        }

        //if the ball hits the right, left or the top side of the screen
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


