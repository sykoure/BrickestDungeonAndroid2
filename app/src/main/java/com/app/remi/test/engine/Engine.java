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

    SpellBlock spellBlock;      // The spellBlock
    Paddle paddle;               // The paddle
    Ball ball;                 // The ball

    List<SpellBlock> listeS = new ArrayList<SpellBlock>();  // List of spellblocks
    List<Ball> listeB = new ArrayList<Ball>();            // List of balls
    List<Ball> removeBall = new ArrayList<>();
    BattleMessage[] history;                                // History of the spells used


    private int numberSpellBlocks;          // The number of spellBlocks
    private Boolean playWithSensor;
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

        this.numberSpellBlocks = numberSpellBlocks;
        this.mainActivityContext = context;         // We will need the context for many services
        this.playWithSensor = playWithSensor;
        initialSensorValue = 0;                     // initialSensorValue is always set a 0 the first time, it allow to detect difference in Z axis positions
        if (playWithSensor) {
            // We initialise the sensor only if the toggleButton has been checked
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // Here we choose the behavior for the sensor and his delay
            sensorManager.registerListener(mSensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        this.vibrator = (Vibrator) this.mainActivityContext.getSystemService(Context.VIBRATOR_SERVICE); // Instantiation of a vibrator manager

        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE)
            this.networkBackendService = networkBackendService;

        this.ourHolder = getHolder();   //Initializing the ourHolder objecet
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

        history = new BattleMessage[5];
        for (int i = 0; i < 5; i++) {
            history[i] = new BattleMessage("");
            history[i].setPseudo(null);
        }

        paddle = new Paddle(screenX, screenY);
        ball = new Ball(screenX, screenY);
        listeB.add(ball);                        // We add the first ball²²

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

        reset(0);                     // We put the position of the ball
    }

    public void reset(int i) {
        listeB.get(i).reset(screenX, screenY);
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

        for (int i = 0; i < listeB.size(); i++) {
            listeB.get(i).update(fps);
        }
        for (int j = 0; j < listeS.size(); j++) {
            checkCooldown(listeS.get(j));
        }
        collisions();
        ballRemoved(removeBall);
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


            //we are drawing each side for the Spellblocks
            for (int i = 0; i < listeS.size(); i++) {
                paint.setColor(Color.argb(255, 0, 255, 255));
                canvas.drawRect(listeS.get(i).getLeftSide(), paint);
                paint.setColor(Color.argb(255, 255, 0, 0));
                canvas.drawRect(listeS.get(i).getRightSide(), paint);
                paint.setColor(Color.argb(255, 255, 255, 0));
                canvas.drawRect(listeS.get(i).getBotSide(), paint);
                paint.setColor(Color.argb(255, 0, 255, 0));
                canvas.drawRect(listeS.get(i).getTopSide(), paint);

                if (listeS.get(i).getCooldown() > 0) {
                    paint.setColor(Color.argb(255, 0, 0, 200));
                    canvas.drawRect(listeS.get(i).getRect().left,
                            listeS.get(i).getRect().bottom - (listeS.get(i).getRect().bottom - listeS.get(i).getRect().top) * ((listeS.get(i).getCooldownDuration() - listeS.get(i).getCooldown()) / listeS.get(i).getCooldownDuration()),
                            listeS.get(i).getRect().right,
                            listeS.get(i).getRect().bottom
                            , paint
                    );
                    //Log.w("WARNING",String.valueOf(listeS.get(i).getCooldown()));
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
                        if (firstTouched) {
                            ball.setySpeed(-200);
                            ball.setxSpeed(200);
                            firstTouched = false;
                        }

                    } else {
                        paddle.setMovementState(paddle.LEFT, screenX);
                        leftTouched = true;
                        if (firstTouched) {
                            ball.setySpeed(-200);
                            ball.setxSpeed(-200);
                            firstTouched = false;
                        }
                    }
                }

                paused = false;
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
        for (int i = 0; i < listeB.size(); i++) {
            for (int j = 0; j < listeS.size(); j++) {
                float minimumS;
                if (RectF.intersects(listeS.get(j).getRect(), listeB.get(i).getRect())) {
                    minimumS = minimumCalcul(Math.abs(listeB.get(i).getRect().right - listeS.get(j).getLeftSide().left),
                            Math.abs(listeB.get(i).getRect().left - listeS.get(j).getRightSide().right),
                            Math.abs(listeS.get(j).getBotSide().bottom - listeB.get(i).getRect().top),
                            Math.abs(listeB.get(i).getRect().bottom - listeS.get(j).getTopSide().top)
                    );

                    if ((listeS.get(j).getLeftSide().left < listeB.get(i).getRect().right) && (listeB.get(i).getxSpeed() > 0) && (minimumS == Math.abs(listeB.get(i).getRect().right - listeS.get(j).getLeftSide().left))) {
                        listeB.get(i).reverseXVelocity();
                        this.playBallBounceSound();
                        this.startVibration(100);

                        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE)
                            networkBackendService.sendMessageToServer("BFIGHT," + listeS.get(j).getSpell());

                    } else if ((listeS.get(j).getRightSide().right > listeB.get(i).getRect().left) && (listeB.get(i).getxSpeed() < 0) && (minimumS == Math.abs(listeB.get(i).getRect().left - listeS.get(j).getRightSide().right))) {
                        listeB.get(i).reverseXVelocity();
                        this.playBallBounceSound();
                        this.startVibration(100);

                        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE)
                            networkBackendService.sendMessageToServer("BFIGHT," + listeS.get(j).getSpell());

                    } else if ((listeS.get(j).getBotSide().bottom > listeB.get(i).getRect().top) && (listeB.get(i).getySpeed() < 0) && (minimumS == Math.abs(listeS.get(j).getBotSide().bottom - listeB.get(i).getRect().top))) {
                        listeB.get(i).reverseYVelocity();
                        this.playBallBounceSound();
                        this.startVibration(100);

                        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE)
                            networkBackendService.sendMessageToServer("BFIGHT," + listeS.get(j).getSpell());

                    } else if ((listeS.get(j).getTopSide().top < listeB.get(i).getRect().bottom) && (listeB.get(i).getySpeed() > 0) && (minimumS == Math.abs(listeB.get(i).getRect().bottom - listeS.get(j).getTopSide().top))) {
                        listeB.get(i).reverseYVelocity();
                        this.playBallBounceSound();
                        this.startVibration(100);

                        if (!MainMenuActivity.BRICKEST_OFFLINE_MODE)
                            networkBackendService.sendMessageToServer("BFIGHT," + listeS.get(j).getSpell());

                    }
                }
            }

            //Collision between the ball and the the paddle
            if ((RectF.intersects(paddle.getRect(), listeB.get(i).getRect())) && (listeB.get(i).getySpeed() > 0)) {
                //TODO penser à prendre en compte la barre
                listeB.get(i).reverseYVelocity();
                Log.d("PADDLE", "PADDLE");
                this.playBallBounceSound();
            }

            //If the ball is hitting the bottom of the screen
            else if (((listeB.get(i).getySpeed() / fps) + listeB.get(i).getRect().top > screenY) && (listeB.get(i).getySpeed() > 0)) {
                player.loseLife(1);          // The user loses 1 hp
                if (listeB.size() == 1) {
                    //update the ball location to put it on the paddle
                    RectF rect = new RectF(paddle.getX() + (paddle.getLength() / 2) - (listeB.get(i).getBallWidth() / 2)
                            , screenY - paddle.getHeight() + 10
                            , paddle.getX() + (paddle.getLength() / 2) + (listeB.get(i).getBallWidth() / 2)
                            , screenY - paddle.getHeight() - listeB.get(i).getBallHeight() + 10);
                    listeB.get(i).setRect(rect);
                    this.playBallDropSound();
                    paused = true;                      // Freeze the game
                    firstTouched = true;                // First time before a collision
                } else {
                    if(listeB.size() != listeS.size()+1)
                        removeBall.add(listeB.get(i));
                }
                listeB.get(i).reverseYVelocity();
            }


            //if the ball hits the right, left or the top side of the screen
            else if ((listeB.get(i).getRect().top - (listeB.get(i).getySpeed() / fps) - listeB.get(i).getBallHeight() < screenY * 0.2) && (listeB.get(i).getySpeed() < 0)) {
                listeB.get(i).reverseYVelocity();
                Log.d("SCREEN", "HUD");
                this.playBallBounceSound();
            } else if ((listeB.get(i).getRect().left - (listeB.get(i).getxSpeed() / fps) - listeB.get(i).getBallWidth() < 0) && (listeB.get(i).getxSpeed() < 0)) {
                listeB.get(i).reverseXVelocity();
                Log.d("SCREEN", "LEFT SCREEN");
                this.playBallBounceSound();
            } else if ((listeB.get(i).getRect().right + (listeB.get(i).getxSpeed() / fps) > screenX) && (listeB.get(i).getxSpeed() > 0)) {
                listeB.get(i).reverseXVelocity();
                Log.d("SCR1EEN", "RIGHT SCREEN");
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

    // Split the ball
    public void splitBall(Ball ball,int indexSpell) {
        // New Ball with different trajectories
        Ball ball2 = new Ball(screenX, screenY);

        ball2.givePosition(ball2, ball);
        ball2.setySpeed(ball.getySpeed());
        ball2.setxSpeed(ball.getxSpeed());
        ball2.reverseXVelocity();
        ball2.setBallHeight(ball.getBallHeight());
        ball2.setBallWidth(ball.getBallHeight());

        listeB.add(ball2);
        addHistory(history, listeS.get(indexSpell).getSpell(), true, (player.getLogin()));
    }


    // Reduce the size of the balls
    public void reduceBall(List<Ball> liste,int indexSpell) {
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).getBallHeight() / 2 < liste.get(i).getBallHeightMin()) {
                liste.get(i).setBallWidth(liste.get(i).getBallWidthMin());
                liste.get(i).setBallHeight(liste.get(i).getBallHeightMin());
            } else {
                liste.get(i).setBallHeight(liste.get(i).getBallHeight() / 2);
                liste.get(i).setBallWidth(liste.get(i).getBallWidth() / 2);
            }
        }
        addHistory(history, listeS.get(indexSpell).getSpell(), true, (player.getLogin()));
    }

    // Change paddle size
    public void changePaddleSize(Paddle paddle1,float multiplicateur,int indexSpell){
        if((Paddle.SIZEMAX >= paddle1.getLength() * multiplicateur)&&(multiplicateur >= 1)){
            paddle1.setLength(paddle1.getLength() * multiplicateur);
        }
        else if((Paddle.SIZEMIN <= paddle1.getLength() * multiplicateur)&&(multiplicateur <= 1)){
            paddle1.setLength(paddle1.getLength() * multiplicateur);
        }
        addHistory(history, listeS.get(indexSpell).getSpell(), true, (player.getLogin()));
    }

    // Change ball Size
    public void changeBallSize(List<Ball> liste,float multiplicateur,int indexSpell){
        for (int i = 0; i < liste.size(); i++) {
            if((listeB.get(i).getSommeSpeed() * multiplicateur <= Ball.SPEEDMAX)&&(multiplicateur >= 1)){
                listeB.get(i).setSommeSpeed(listeB.get(i).getSommeSpeed() * multiplicateur);
                listeB.get(i).setxSpeed(listeB.get(i).getxSpeed() * multiplicateur);
                listeB.get(i).setySpeed(listeB.get(i).getySpeed() * multiplicateur);
            }
            else if((listeB.get(i).getSommeSpeed() * multiplicateur >= Ball.SPEEDMIN)&&(multiplicateur <= 1)){
                listeB.get(i).setSommeSpeed(listeB.get(i).getSommeSpeed() * multiplicateur);
                listeB.get(i).setxSpeed(listeB.get(i).getxSpeed() * multiplicateur);
                listeB.get(i).setySpeed(listeB.get(i).getySpeed() * multiplicateur);
            }
        }
        addHistory(history, listeS.get(indexSpell).getSpell(), true, (player.getLogin()));
    }

    // Update the cooldowns
    public void checkCooldown(SpellBlock spellBlock) {
        if (spellBlock.getCooldown() > 0) {
            spellBlock.setCooldown(spellBlock.getCooldown() - 1 / (float) fps); //Reduit le cooldown de 1 par seconde
        } else {
            spellBlock.setCooldown(0);
        }
    }

    // Update the history
    public void addHistory(BattleMessage[] history, String message, Boolean bool, String pseudo) {
        int i = 4;
        while (i > 0) {
            history[i].setPseudo(history[i - 1].getPseudo());
            history[i].setHasMessage(history[i - 1].getHasMessage());
            history[i].setMessageCombat(history[i - 1].getMessageCombat());
            i--;
        }
        history[0].setPseudo(pseudo);
        history[0].setHasMessage(bool);
        history[0].setMessageCombat(message);
    }

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

    public void ballRemoved(List<Ball> liste) {
        for (int i = 0; i < liste.size(); i++) {
            listeB.remove(liste.get(i));
        }
        for (int i = 0; i < liste.size(); i++) {
            liste.get(i);
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


