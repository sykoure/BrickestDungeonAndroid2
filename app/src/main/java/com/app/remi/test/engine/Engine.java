package com.app.remi.test.engine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.remi.test.R;
import com.app.remi.test.activities.MainMenuActivity;
import com.app.remi.test.network.backend.services.NetworkBackendService;
import com.app.remi.test.soundServices.BallBounceService;
import com.app.remi.test.soundServices.BallDropService;
import com.app.remi.test.soundServices.BallStartService;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Remi on 25/10/2017.
 */
public class Engine extends SurfaceView implements Runnable {

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

    boolean leftTouched;        // Does the user touch the left or right side of the screen left = true
    boolean firstTouched = true;// First collision after a freeze

    Player player;              // This is the current player
    Player foe;                 // This is the foe

    SpellBlock spellBlock;     // The spellBlock
    Paddle paddle;             // The paddle
    Ball ball;                 // The ball

    List<SpellBlock> listeS = new ArrayList<SpellBlock>();          // List of spellblocks
    List<Ball> listeB = new ArrayList<Ball>();                // List of balls
    List<Ball> removeBall = new ArrayList<>();             // List of the balls which need to be removed after the method collision()
    BattleMessage[] history;                               // History of the spells used


    private int numberSpellBlocks;          // The number of spellBlocks
    private Boolean playWithSensor;         // If the player has to use the sensor or a touche on the screen to move the paddle
    private float initialSensorValue;       // The value with which the first sensor value will be compared
    private Context mainActivityContext;    // The Context of the mainActivity used for Services
    private Vibrator vibrator;              // Reference to the vibrator manager
    private NetworkBackendService networkBackendService; // Reference to the network service

    /**
     * @param context        Activity holding this view
     * @param playWithSensor Boolean value, define the playstyle, activate or not the accelerometer, desactivate the touch screen.
     * @param sensorManager  The sensor manager used to manage the accelerometer
     */
    public Engine(Context context, Boolean playWithSensor, SensorManager sensorManager, int numberSpellBlocks, Player ownPlayer, Player oppPlayer, NetworkBackendService networkBackendService) {

        super(context);

        this.numberSpellBlocks = numberSpellBlocks; // We set the number of spellblock
        this.mainActivityContext = context;         // We will need the context for many services
        this.playWithSensor = playWithSensor;       // We set the boolean playWithSensor
        initialSensorValue = 0;                     // initialSensorValue is always set a 0 the first time, it allow to detect difference in Z axis positions
        if (playWithSensor) {
            // We initialise the sensor only if the toggleButton has been checked
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // Here we choose the behavior for the sensor and his delay
            sensorManager.registerListener(mSensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        this.vibrator = (Vibrator) this.mainActivityContext.getSystemService(Context.VIBRATOR_SERVICE); // Instantiation of a vibrator manager

        //If we are playing online, we set the networkBackendService
        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE)
            this.networkBackendService = networkBackendService;

        this.ourHolder = getHolder();   //Initializing the ourHolder object
        this.paint = new Paint();       //Initializing the paint object

        // Retrieving the screen size
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenX = size.x;
        screenY = size.y;

        this.player = ownPlayer;
        this.foe = oppPlayer;

        //We initialize the list which will be the history of the spell that was used
        history = new BattleMessage[5];
        for (int i = 0; i < 5; i++) {
            history[i] = new BattleMessage("");
            history[i].setPseudo(null);
        }

        paddle = new Paddle(screenX, screenY);      // We set the paddle position
        ball = new Ball(screenX, screenY);          // We initialize the first ball
        listeB.add(ball);                           // We add the first ball to the list of balls

        // Depending of the number of spellblock, the size and the position for each one will be different
        for (int i = 0; i < numberSpellBlocks; i++) {
            double xposition = screenX * 0.1 + (i * (70 / numberSpellBlocks * 3) + (i * (150 / numberSpellBlocks * 3)));
            if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
                String name = ownPlayer.getSelectedSpells().get(i);

                spellBlock = new SpellBlock(screenX, screenY, xposition, screenY * 0.3, 150 / numberSpellBlocks * 3, 150 / numberSpellBlocks * 3, name);


            } else {
                spellBlock = new SpellBlock(screenX, screenY, xposition, screenY * 0.3, 150 / numberSpellBlocks * 3, 150 / numberSpellBlocks * 3, "spellblock" + i + 1);

            }
            listeS.add(spellBlock);               // We add the spellBlocks
        }

        reset(0);                               // We put the position of the ball
    }

    /**
     * This method allows us to reset the position of a ball that is in the list of balls
     * @param i is the index of the ball in its list
     */
    public void reset(int i) {
        listeB.get(i).reset(screenX, screenY);
    }

    /**
     * run() is the method call automatically since our class is  implemented by the the interface runnable
     */
    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            // If the variable false isnt false, we keep updating the game
            if (!paused) {
                update();
            }
            // We use the draw() method even if the game is paused
            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                /* The fps is apploximately equal to 30 which means that the game is updating and
                 * draw 30 times per second
                */
                fps = 1000 / timeThisFrame;
            }

        }

    }

    /**
     * This method will launch the method update() for each objects and the method collisions()
     */
    public void update() {

        // We start the method by updating all the objects(the paddle, the balls and the spellblocks)
        paddle.update(fps, screenX);

        for (int i = 0; i < listeB.size(); i++) {
            listeB.get(i).update(fps);
        }
        for (int j = 0; j < listeS.size(); j++) {
            checkCooldown(listeS.get(j));
        }
        collisions();               // Then, we check the collisions of the balls
        ballRemoved(removeBall);    // And we remove all the balls in the list removeBall
    }

    /**
     * This method will render the canvas on the ourHolder
     */
    public void draw() {


        if (ourHolder.getSurface().isValid()) {

            //we set the canvas as the "drawer" of our canvas
            canvas = ourHolder.lockCanvas();

            //we put the background of the game in the canvas, a big black rectangle
            canvas.drawColor(Color.argb(100, 0, 0, 0));

            //the paint (paintbrush) will now has a white color
            paint.setColor(Color.argb(100, 255, 255, 255));

            //we are putting each objects in the canvas
            canvas.drawRect(paddle.getRect(), paint);
            //canvas.drawRect(spellBlock.getRect(), paint);
            //canvas.drawRect(ball.getRect(), paint);

            for (int i = 0; i < listeB.size(); i++)
                canvas.drawRect(listeB.get(i).getRect(), paint);

            //We are puttint the spellblocks and their sprite in the canvas
            for (int i = 0; i < listeS.size(); i++) {
                canvas.drawRect(listeS.get(i).getRect(), paint);
                if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
                    // Drawing of the spells blocks sprites
                    String spriteName = listeS.get(i).getSpell() + "_sprite";
                    Bitmap bitmapSpell = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(spriteName, "drawable", mainActivityContext.getPackageName()));
                    canvas.drawBitmap(bitmapSpell, null, listeS.get(i).getRect(), null);
                }

            }

            //this is the HUD
            canvas.drawRect(0, (float) (screenY * 0.2), screenX, 0, paint);

            //the paint (paintbrush) will now has a teal color
            paint.setColor(Color.argb(255, 0, 247, 255));
            paint.setTextSize(25);


            //Information about the Player 1
            canvas.drawText(player.getLogin(), 20, (float) (screenY * 0.04), paint);

            //the paint (paintbrush) will now has a black color
            paint.setColor(Color.argb(255, 0, 0, 0));

            canvas.drawText(player.getNameClass(), 20, (float) (screenY * 0.09), paint);
            canvas.drawText("Life : ", 20, (float) (screenY * 0.13), paint);
            canvas.drawText(String.valueOf(player.getLife()), 120, (float) (screenY * 0.13), paint);
            canvas.drawText("Shield : ", 20, (float) (screenY * 0.17), paint);
            canvas.drawText(String.valueOf(player.getShield()), 120, (float) (screenY * 0.17), paint);


            //the paint (paintbrush) will now has a red color
            paint.setColor(Color.argb(255, 255, 76, 76));

            //Information about the Player 2

            //the paint (paintbrush) will now has a red color
            paint.setColor(Color.argb(255, 255, 76, 76));
            canvas.drawText(foe.getLogin(), screenX - 200, (float) (screenY * 0.04), paint);

            //the paint (paintbrush) will now has a black color
            paint.setColor(Color.argb(255, 0, 0, 0));

            canvas.drawText(foe.getNameClass(), screenX - 200, (float) (screenY * 0.09), paint);
            canvas.drawText("Life : ", screenX - 200, (float) (screenY * 0.13), paint);
            canvas.drawText(String.valueOf(foe.getLife()), screenX - 100, (float) (screenY * 0.13), paint);
            canvas.drawText("Shield : ", screenX - 200, (float) (screenY * 0.17), paint);
            canvas.drawText(String.valueOf(foe.getShield()), screenX - 100, (float) (screenY * 0.17), paint);

            //We will now paint the history

            for (int i = 1; i < 6; i++) {
                double gap = screenY * 0.04;
                if (history[i - 1].getHasMessage()) {
                    if (history[i - 1].getPseudo().equals(player.getLogin())) {
                        paint.setColor(Color.argb(255, 0, 247, 255));
                    } else {
                        paint.setColor(Color.argb(255, 255, 76, 76));
                    }
                    canvas.drawText(history[i - 1].getPseudo(), (float) (screenX * 0.3), (float) (gap) * i, paint);

                    paint.setColor(Color.argb(255, 0, 0, 0));
                    canvas.drawText("=>", (float) (screenX * 0.45), (float) (gap) * i, paint);

                    paint.setColor(Color.argb(255, 0, 255, 0));
                    canvas.drawText(history[i - 1].getMessageCombat(), (float) (screenX * 0.5), (float) (gap) * i, paint);
                }
            }


            // We are drawing each side for the Spellblocks
            for (int i = 0; i < listeS.size(); i++) {
                paint.setColor(Color.argb(255, 0, 255, 255));
                canvas.drawRect(listeS.get(i).getLeftSide(), paint);
                paint.setColor(Color.argb(255, 255, 0, 0));
                canvas.drawRect(listeS.get(i).getRightSide(), paint);
                paint.setColor(Color.argb(255, 255, 255, 0));
                canvas.drawRect(listeS.get(i).getBotSide(), paint);
                paint.setColor(Color.argb(255, 0, 255, 0));
                canvas.drawRect(listeS.get(i).getTopSide(), paint);


                /* We are putting in the canvas the cooldowns representation which is a blue rectangle
                 * superimposed on the spellblocks
                 */
                if (listeS.get(i).getCooldown() > 0) {
                    paint.setColor(Color.argb(255, 0, 0, 200));
                    canvas.drawRect(listeS.get(i).getRect().left,
                            listeS.get(i).getRect().bottom - (listeS.get(i).getRect().bottom - listeS.get(i).getRect().top) * ((listeS.get(i).getCooldownDuration() - listeS.get(i).getCooldown()) / listeS.get(i).getCooldownDuration()),
                            listeS.get(i).getRect().right,
                            listeS.get(i).getRect().bottom
                            , paint
                    );
                }
            }
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
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched the screen

            case MotionEvent.ACTION_DOWN:
                if (paused == true) {
                    this.playBallStartSound();
                    this.startVibration(250);
                }
                //if the player is touching the left part of the screen or the right part of the screen
                if (!playWithSensor) {
                    if (motionEvent.getX() > screenX / 2) {
                        paddle.setMovementState(paddle.RIGHT, screenX);
                        leftTouched = false;
                        //The player serves the ball to the right
                        if (firstTouched) {
                            ball.setySpeed(-300);
                            ball.setxSpeed(200);
                            firstTouched = false;
                        }

                    } else {
                        paddle.setMovementState(paddle.LEFT, screenX);
                        leftTouched = true;
                        //The player serves the ball to the left
                        if (firstTouched) {
                            ball.setySpeed(-300);
                            ball.setxSpeed(-200);
                            firstTouched = false;
                        }
                    }
                }

                paused = false;         // game is not freeze anymore
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
        }
    };

    /**
     * This method is checking the collisions between the ball and the others object
     */
    void collisions() {
        //collisions between the ball and the spellblocks for each ball in the list and each spellblock in the list
        for (int i = 0; i < listeB.size(); i++) {
            for (int j = 0; j < listeS.size(); j++) {
                float minimumS;                     //minimumS is the minimum length between one of the four sides of the spellBlock and the ball

                //If the hitbox of a ball and the hitbox of a ball are interacting
                if (RectF.intersects(listeS.get(j).getRect(), listeB.get(i).getRect())) {
                    //We calculate the minimum (which is a absolute value since it can be negative
                    minimumS = minimumCalcul(Math.abs(listeB.get(i).getRect().right - listeS.get(j).getLeftSide().left),
                            Math.abs(listeB.get(i).getRect().left - listeS.get(j).getRightSide().right),
                            Math.abs(listeS.get(j).getBotSide().bottom - listeB.get(i).getRect().top),
                            Math.abs(listeB.get(i).getRect().bottom - listeS.get(j).getTopSide().top)
                    );

                    // If the ball touches the left side of a spellblock
                    if ((listeS.get(j).getLeftSide().left < listeB.get(i).getRect().right) && (listeB.get(i).getxSpeed() > 0) && (minimumS == Math.abs(listeB.get(i).getRect().right - listeS.get(j).getLeftSide().left))) {
                        listeB.get(i).reverseXVelocity();
                        this.playBallBounceSound();
                        this.startVibration(100);

                        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
                            networkBackendService.sendMessageToServer("BFIGHT," + listeS.get(j).getSpell());
                        }
                        else {
                            switch(j){
                                case 0:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        changeBallSpeed(listeB, 1.2, j);
                                    }
                                    break;
                                case 1:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        splitBall(listeB.get(i), j);
                                    }
                                    break;
                                case 2:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        reduceBall(listeB, j);
                                    }
                                    break;

                            }
                        }

                    }
                    // If the ball touches the right side of a spellblock
                    else if ((listeS.get(j).getRightSide().right > listeB.get(i).getRect().left) && (listeB.get(i).getxSpeed() < 0) && (minimumS == Math.abs(listeB.get(i).getRect().left - listeS.get(j).getRightSide().right))) {
                        listeB.get(i).reverseXVelocity();
                        this.playBallBounceSound();
                        this.startVibration(100);

                        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
                            networkBackendService.sendMessageToServer("BFIGHT," + listeS.get(j).getSpell());
                        }
                        else{
                            switch(j){
                                case 0:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        changeBallSpeed(listeB, 1.2, j);
                                    }
                                    break;
                                case 1:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        splitBall(listeB.get(i), j);
                                    }
                                    break;
                                case 2:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        reduceBall(listeB, j);
                                    }
                                    break;
                            }
                        }
                    }
                    // If the ball touches the bottom side of a spellblock
                    else if ((listeS.get(j).getBotSide().bottom > listeB.get(i).getRect().top) && (listeB.get(i).getySpeed() < 0) && (minimumS == Math.abs(listeS.get(j).getBotSide().bottom - listeB.get(i).getRect().top))) {
                        listeB.get(i).reverseYVelocity();
                        this.playBallBounceSound();
                        this.startVibration(100);

                        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
                            networkBackendService.sendMessageToServer("BFIGHT," + listeS.get(j).getSpell());
                        }
                        else{
                            switch(j){
                                case 0:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        changeBallSpeed(listeB, 1.2, j);
                                    }
                                    break;
                                case 1:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        splitBall(listeB.get(i), j);
                                    }
                                    break;
                                case 2:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        reduceBall(listeB, j);
                                    }
                                    break;
                            }
                        }
                    }
                    // If the ball touches the top side of a spellblock
                    else if ((listeS.get(j).getTopSide().top < listeB.get(i).getRect().bottom) && (listeB.get(i).getySpeed() > 0) && (minimumS == Math.abs(listeB.get(i).getRect().bottom - listeS.get(j).getTopSide().top))) {
                        listeB.get(i).reverseYVelocity();
                        this.playBallBounceSound();
                        this.startVibration(100);

                        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
                            networkBackendService.sendMessageToServer("BFIGHT," + listeS.get(j).getSpell());
                        }
                        else{
                            switch(j){
                                case 0:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        changeBallSpeed(listeB, 1.2, j);
                                    }
                                    break;
                                case 1:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        splitBall(listeB.get(i), j);
                                    }
                                    break;
                                case 2:
                                    if(listeS.get(j).getCooldown() == 0) {
                                        listeS.get(j).setCooldown(listeS.get(j).getCooldownDuration());
                                        reduceBall(listeB, j);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }

            //Collision between the ball and the the paddle
            if (((listeB.get(i).getySpeed() / fps) + listeB.get(i).getRect().top > paddle.getRect().top) && (listeB.get(i).getySpeed() > 0)&&(listeB.get(i).getRect().left >= paddle.getRect().left)&&(listeB.get(i).getRect().right <= paddle.getRect().right)){
                // The trajectories of the ball are modified relative to the place where the collision happened
                changeTrajectories(listeB.get(i));
                this.playBallBounceSound();
            }

            //If the ball is hitting the bottom of the screen
            else if (((listeB.get(i).getySpeed() / fps) + listeB.get(i).getRect().top > screenY) && (listeB.get(i).getySpeed() > 0)) {
                if (listeB.size() == 1) {
                    //update the ball location to put it on the paddle
                    RectF rect = new RectF(paddle.getX() + (paddle.getLength() / 2) - (listeB.get(i).getBallWidth() / 2)
                            , screenY - paddle.getHeight() + 10
                            , paddle.getX() + (paddle.getLength() / 2) + (listeB.get(i).getBallWidth() / 2)
                            , screenY - paddle.getHeight() - listeB.get(i).getBallHeight() + 10);
                    listeB.get(i).setRect(rect);
                    this.playBallDropSound();
                    paused = true;                          // Freeze the game
                    firstTouched = true;                    // First time before a collision
                } else {
                    if (listeB.size() != 1)
                        removeBall.add(listeB.get(i));      // We add the current ball to the removeBall list
                }
                listeB.get(i).reverseYVelocity();
                if (!MainMenuActivity.BRICKEST_OFFLINE_MODE) {
                    this.networkBackendService.sendMessageToServer("BFALL");
                }
                else{
                    player.loseLife(1);
                }
            }


            //if the ball hits the right, left or the top side of the screen
            else if ((listeB.get(i).getRect().top - (listeB.get(i).getySpeed() / fps) - listeB.get(i).getBallHeight() < screenY * 0.2) && (listeB.get(i).getySpeed() < 0)) {
                listeB.get(i).reverseYVelocity();
                this.playBallBounceSound();
            } else if ((listeB.get(i).getRect().left - (listeB.get(i).getxSpeed() / fps) - listeB.get(i).getBallWidth() < 0) && (listeB.get(i).getxSpeed() < 0)) {
                listeB.get(i).reverseXVelocity();
                this.playBallBounceSound();
            } else if ((listeB.get(i).getRect().right + (listeB.get(i).getxSpeed() / fps) > screenX) && (listeB.get(i).getxSpeed() > 0)) {
                listeB.get(i).reverseXVelocity();
                this.playBallBounceSound();
            }
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

    /**
     * Make the user phone vibrate
     *
     * @param duration Duration of the vibration in milliseconds
     */
    void startVibration(int duration) {
        if (this.vibrator != null) {
            this.vibrator.vibrate(duration);
        }
    }

    /**
     * This method split the ball into two balls
     * @param ball is the ball which is going to be splitted
     * @param indexSpell is the index of the spellblock
     */
    @Deprecated
    public void splitBall(Ball ball, int indexSpell) {
        // New Ball with different trajectories
        Ball ball2 = new Ball(screenX, screenY);

        ball2.givePosition(ball2, ball);
        ball2.setySpeed(ball.getySpeed());
        ball2.setxSpeed(ball.getxSpeed());
        ball2.reverseXVelocity();
        ball2.setBallHeight(ball.getBallHeight());
        ball2.setBallWidth(ball.getBallHeight());

        listeB.add(ball2);
        addHistory(history,"splitBall", true, (player.getLogin()));
    }

    /**
     * Create a new ball in the same position as the first ball of the list
     */
    public void splitBall() {
        // New Ball with different trajectories
        Ball ball2 = new Ball(screenX, screenY);

        ball2.givePosition(ball2, this.listeB.get(0));
        ball2.setySpeed(this.listeB.get(0).getySpeed());
        ball2.setxSpeed(this.listeB.get(0).getxSpeed());
        ball2.reverseXVelocity();
        ball2.setBallHeight(this.listeB.get(0).getBallHeight());
        ball2.setBallWidth(this.listeB.get(0).getBallHeight());

        this.listeB.add(ball2);
        this.player.setBallsNb(this.player.getBallsNb() + 1);
    }


    /**
     * Reduce the size of the balls in the list of balls
     * @param liste is the list of balls
     * @param indexSpell is the index of the spellblock
     */
    public void reduceBall(List<Ball> liste, int indexSpell) {
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).getBallHeight() / 2 < liste.get(i).BALLHEIGTHMIN) {
                liste.get(i).setBallWidth(liste.get(i).BALLWIDTHMIN);
                liste.get(i).setBallHeight(liste.get(i).BALLHEIGTHMIN);
            } else {
                liste.get(i).setBallHeight(liste.get(i).getBallHeight() / 2);
                liste.get(i).setBallWidth(liste.get(i).getBallWidth() / 2);
            }
        }
        addHistory(history,"reduceBall", true, (player.getLogin()));
    }

    /**
     * This method changes the paddleSize
     * @param paddle1 is the paddle
     * @param multiplier is the variable that will be determinate the new size of the paddle
     * @param indexSpell is the index of the spellblock
     */
    public void changePaddleSize(Paddle paddle1, double multiplier, int indexSpell) {
        if ((Paddle.SIZEMAX >= paddle1.getLength() * multiplier) && (multiplier >= 1)) {
            paddle1.setLength((float)(paddle1.getLength() * multiplier));
        } else if ((Paddle.SIZEMIN <= paddle1.getLength() * multiplier) && (multiplier <= 1)) {
            paddle1.setLength((float)(paddle1.getLength() * multiplier));
        }
        addHistory(history, listeS.get(indexSpell).getSpell(), true, (player.getLogin()));
    }

    /**
     * This method changes the speed of the balls
     * @param liste is the list of balls
     * @param multiplier is the variable that will be determinate the new speed of the balls
     * @param indexSpell is the index of the spellblock
     */
    public void changeBallSpeed(List<Ball> liste, double multiplier, int indexSpell) {
        for (int i = 0; i < liste.size(); i++) {
            if ((listeB.get(i).getSommeSpeed() * multiplier <= Ball.SPEEDMAX) && (multiplier >= 1)) {
                listeB.get(i).setSommeSpeed((float)(listeB.get(i).getSommeSpeed() * multiplier));
                listeB.get(i).setxSpeed((float)(listeB.get(i).getxSpeed() * multiplier));
                listeB.get(i).setySpeed((float)(listeB.get(i).getySpeed() * multiplier));
            } else if ((listeB.get(i).getSommeSpeed() * multiplier >= Ball.SPEEDMIN) && (multiplier <= 1)) {
                listeB.get(i).setSommeSpeed((float)(listeB.get(i).getSommeSpeed() * multiplier));
                listeB.get(i).setxSpeed((float)(listeB.get(i).getxSpeed() * multiplier));
                listeB.get(i).setySpeed((float)(listeB.get(i).getySpeed() * multiplier));
            }
        }
        addHistory(history, "acceleroBall", true, (player.getLogin()));
    }

    /**
     * This method update the cooldowns for each spellblock
     * @param spellBlock is the spellblock which the cooldowns will be update
     */
    public void checkCooldown(SpellBlock spellBlock) {
        if (spellBlock.getCooldown() > 0) {
            spellBlock.setCooldown(spellBlock.getCooldown() - 1 / (float) fps); //Reduce the cooldown by per second
        } else {
            spellBlock.setCooldown(0);
        }
    }

    /**
     * This is a management of the history, this method add a Battle
     * @param history is the table of BattleMessage
     * @param message is the spell of the new BattleMessage
     * @param bool    is the hasMessage of the new BattleMessage
     * @param pseudo  is the pseudo of the new BattleMessage
     */
    public void addHistory(BattleMessage[] history, String message, Boolean bool, String pseudo) {
        int i = 4;
        /* while i is greater than 0, the BattleMessage in the history at the index i is equal of
         * the BattleMessage in the history at the index i-1
         */
        while (i > 0) {
            history[i].setPseudo(history[i - 1].getPseudo());
            history[i].setHasMessage(history[i - 1].getHasMessage());
            history[i].setMessageCombat(history[i - 1].getMessageCombat());
            i--;
        }
        //Then we add the new BattleMessage in the history at the index 0
        history[0].setPseudo(pseudo);
        history[0].setHasMessage(bool);
        history[0].setMessageCombat(message);
    }

    /**
     * This method calculate the minimum between four values
     * @param number1 is the first value
     * @param number2 is the second value
     * @param number3 is the third value
     * @param number4 is the fourth value
     * @return the minimum between them
     */
    public float minimumCalcul(float number1, float number2, float number3, float number4) {
        if (number1 > number2) {
            number1 = number2;
        }
        if (number1 > number3) {
            number1 = number3;
        }
        if (number1 > number4) {
            number1 = number4;
        }
        return number1;
    }

    /**
     * This is the method called after the collision one, this will delete the ball in the list removeBall
     * @param list is the list of ball
     */
    public void ballRemoved(List<Ball> list) {
        for (int i = 0; i < list.size(); i++) {
            listeB.remove(list.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i);
        }
    }

    public void changeTrajectories(Ball ball){
        float percentage;
        //if the ball is hitting the right part of the part(the area after the middle of the paddle)
        if((paddle.getLength()/2) + paddle.getX() <= (ball.getRect().right + ball.getRect().left)/2){
            percentage = 100 - (((paddle.getRect().right) - (ball.getRect().right + ball.getRect().left)/2) * 100)/ (paddle.getLength()/2);
            ball.setxSpeed((ball.getxSpeed() + percentage * ball.getSommeSpeed())/200);
            ball.setySpeed(ball.getSommeSpeed() - Math.abs(ball.getxSpeed()));
        }
        //if the ball is hitting the left part of the part(the area before the middle of the paddle)
        else{
            percentage = ((((paddle.getLength()/2) + paddle.getX()) - (ball.getRect().right + ball.getRect().left)/2) * 100)/ (paddle.getLength()/2);
            ball.setxSpeed((ball.getxSpeed() + percentage * (0-ball.getSommeSpeed()))/200);
            ball.setySpeed(ball.getSommeSpeed() - Math.abs(ball.getxSpeed()));
        }

        //Then if the ball was falling, the ball is now bouncing back up to the top of the screen
        if(ball.getySpeed() > 0) {
            ball.reverseYVelocity();
        }
    }

    /**
     * Update data about the players
     * New data are received by the server
     */
    public void changePlayersInfos(int ownHp, int ownShield, int ownBallsNb, float ownBallSpeed, float ownBallsSize, float ownButtonSize, float ownPaddleSize, int oppHp, int oppShield) {
        this.player.setLife(ownHp);
        this.player.setShield(ownShield);
        this.player.setBallsNb(ownBallsNb);
        this.player.setBallsSpeed(ownBallSpeed);
        this.player.setBallsSize(ownBallsSize);
        this.player.setButtonSize(ownButtonSize);
        this.player.setPaddleSize(ownPaddleSize);
        this.foe.setLife(oppHp);
        this.foe.setShield(oppShield);
    }
}


