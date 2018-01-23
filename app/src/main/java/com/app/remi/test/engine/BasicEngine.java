package com.app.remi.test.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.app.remi.test.data.Ball;
import com.app.remi.test.data.BattleMessage;
import com.app.remi.test.data.Paddle;
import com.app.remi.test.data.Player;
import com.app.remi.test.data.SpellBlock;
import com.app.remi.test.network.backend.services.NetworkBackendService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Remi on 22/01/2018.
 */





public class BasicEngine extends Engine implements Runnable {


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
     * @param context               Activity holding this view
     * @param playWithSensor        Boolean value, define the playstyle, activate or not the accelerometer, desactivate the touch screen.
     * @param sensorManager         The sensor manager used to manage the accelerometer
     * @param numberSpellBlocks
     * @param ownPlayer
     * @param oppPlayer
     * @param networkBackendService
     */
    public BasicEngine(Context context, Boolean playWithSensor, SensorManager sensorManager, int numberSpellBlocks, Player ownPlayer, Player oppPlayer, NetworkBackendService networkBackendService) {
        super(context, playWithSensor, sensorManager, numberSpellBlocks, ownPlayer, oppPlayer, networkBackendService);
    }

    /**
     * run() is the method call automatically since our class is  implemented by the the interface runnable
     */
    @Override
    public void run() {
        super.run();
    }

    /**
     * This method will launch the method update() for each objects and the method collisions()
     */
    @Override
    public void update() {
        super.update();
    }

    /**
     * This method will render the canvas on the ourHolder
     */
    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    /**
     * The method touchEvent will translate our action with the phone
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    /**
     * This method is checking the collisions between the ball and the others object
     */
    @Override
    void collisions() {
        super.collisions();
    }

    /**
     * Call this method to launch the "onStartCommand" method of the associated service
     */
    @Override
    void playBallBounceSound() {
        super.playBallBounceSound();
    }

    /**
     * Call this method to launch the "onStartCommand" method of the associated service
     */
    @Override
    void playBallDropSound() {
        super.playBallDropSound();
    }

    /**
     * Call this method to launch the "onStartCommand" method of the associated service
     */
    @Override
    void playBallStartSound() {
        super.playBallStartSound();
    }

    /**
     * Make the user phone vibrate
     *
     * @param duration Duration of the vibration in milliseconds
     */
    @Override
    void startVibration(int duration) {
        super.startVibration(duration);
    }

    /**
     * This method split the ball into two balls
     *
     * @param ball       is the ball which is going to be splitted
     * @param indexSpell is the index of the spellblock
     */
    @Override
    public void splitBall(Ball ball, int indexSpell) {
        super.splitBall(ball, indexSpell);
    }

    /**
     * Create a new ball in the same position as the first ball of the list
     */
    @Override
    public void splitBall() {
        super.splitBall();
    }

    /**
     * Reduce the size of the balls in the list of balls
     * TODO remane changeBallSize
     *
     * @param liste      is the list of balls
     * @param indexSpell is the index of the spellblock
     */
    @Override
    public void reduceBall(List<Ball> liste, int indexSpell) {
        super.reduceBall(liste, indexSpell);
    }

    /**
     * Reduce the size of the balls in the list of balls
     *
     * @param multiplier is the variable that will be determinate the new speed of the balls
     */
    @Override
    public void changeBallSize(float multiplier) {
        super.changeBallSize(multiplier);
    }

    /**
     * This method changes the paddleSize
     *
     * @param paddle1    is the paddle
     * @param multiplier is the variable that will be determinate the new size of the paddle
     * @param indexSpell is the index of the spellblock
     */
    @Override
    public void changePaddleSize(Paddle paddle1, double multiplier, int indexSpell) {
        super.changePaddleSize(paddle1, multiplier, indexSpell);
    }

    /**
     * This method changes the speed of the balls
     * Only used on offline mode
     *
     * @param liste      is the list of balls
     * @param multiplier is the variable that will be determinate the new speed of the balls
     * @param indexSpell is the index of the spellblock
     */
    @Override
    public void changeBallSpeed(List<Ball> liste, double multiplier, int indexSpell) {
        super.changeBallSpeed(liste, multiplier, indexSpell);
    }

    /**
     * This method changes the speed of the balls
     * Only used on online mode
     *
     * @param multiplier is the variable that will be determinate the new speed of the balls
     */
    @Override
    public void changeBallSpeed(float multiplier) {
        super.changeBallSpeed(multiplier);
    }

    /**
     * This method update the cooldowns for each spellblock
     *
     * @param spellBlock is the spellblock which the cooldowns will be update
     */
    @Override
    public void checkCooldown(SpellBlock spellBlock) {
        super.checkCooldown(spellBlock);
    }

    /**
     * This is a management of the history, this method add a Battle
     *
     * @param history is the table of BattleMessage
     * @param message is the spell of the new BattleMessage
     * @param bool    is the hasMessage of the new BattleMessage
     * @param pseudo  is the pseudo of the new BattleMessage
     */
    @Override
    public void addHistory(BattleMessage[] history, String message, Boolean bool, String pseudo) {
        super.addHistory(history, message, bool, pseudo);
    }

    /**
     * This method calculate the minimum between four values
     *
     * @param number1 is the first value
     * @param number2 is the second value
     * @param number3 is the third value
     * @param number4 is the fourth value
     * @return the minimum between them
     */
    @Override
    public float minimumCalcul(float number1, float number2, float number3, float number4) {
        return super.minimumCalcul(number1, number2, number3, number4);
    }

    /**
     * This is the method called after the collision one, this will delete the ball in the list removeBall
     *
     * @param list is the list of ball
     */
    @Override
    public void ballRemoved(List<Ball> list) {
        super.ballRemoved(list);
    }

    @Override
    public void changeTrajectories(Ball ball) {
        super.changeTrajectories(ball);
    }

    /**
     * Update data about the players
     * New data are received by the server
     *
     * @param ownHp
     * @param ownShield
     * @param ownBallsNb
     * @param ownBallSpeed
     * @param ownBallsSize
     * @param ownButtonSize
     * @param ownPaddleSize
     * @param oppHp
     * @param oppShield
     */
    @Override
    public void changePlayersInfos(int ownHp, int ownShield, int ownBallsNb, float ownBallSpeed, float ownBallsSize, float ownButtonSize, float ownPaddleSize, int oppHp, int oppShield) {
        super.changePlayersInfos(ownHp, ownShield, ownBallsNb, ownBallSpeed, ownBallsSize, ownButtonSize, ownPaddleSize, oppHp, oppShield);
    }

    /**
     * The method update() is fixing the position of the hitbox (and so the ball) fps times per second
     *
     * @param fps  the number of time the hitbox will be updated
     * @param ball is the ball that will be updated
     */
    @Override
    public void updateBall(long fps, Ball ball) {
        super.updateBall(fps, ball);
    }

    /**
     * This method is running one time at the beginning to put the right position of the ball
     *
     * @param x    is the length of the screen
     * @param y    is the height of the screen
     * @param ball is the ball that will be reseted
     */
    @Override
    public void resetBall(int x, int y, Ball ball) {
        super.resetBall(x, y, ball);
    }

    /**
     * The method() update will be run fps times per seconds and will update the paddle's hitbox position
     * (and so the position of the paddle too)
     *
     * @param fps    The number of update for each second
     * @param screen The length of the screen
     */
    @Override
    public void updatePaddle(long fps, float screen) {
        super.updatePaddle(fps, screen);
    }

    /**
     * The method setMovementState() is setting the paddle's state, if it has to go to the left, right or to be stopped.
     * The parameter screen is used to stop the paddle if it's going to far on the left or on the right.
     *
     * @param state  This is the paddle's direction
     * @param screen This is the length of the screen
     */
    @Override
    public void setMovementStatePaddle(int state, float screen) {
        super.setMovementStatePaddle(state, screen);
    }
}
