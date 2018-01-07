package com.app.remi.test.engine;

import java.util.ArrayList;

/**
 * Created by Remi on 31/12/2017.
 */

public class Player {

    private int life, shield, ballsNb;
    private float ballsSpeed, ballsSize, buttonSize, paddleSize;
    private String nameClass;
    private String login;
    private ArrayList<String> selectedSpells;


    /**
     * Used for opponent structure and offline mode
     *
     * @param life      life point of the player
     * @param shield    Number of shields
     * @param nameClass Name of the selected class
     * @param login     Login of the player
     */
    public Player(int life, int shield, String nameClass, String login) {
        this.life = life;
        this.shield = shield;
        this.nameClass = nameClass;
        this.login = login;
    }

    /**
     * Real constructor
     *
     * @param life           life point of the player
     * @param shield         Number of shields
     * @param ballsNb        Number of balls
     * @param ballsSpeed     Modifier of the balls speed
     * @param ballsSize      Modifier of the balls size
     * @param buttonSize     Modifier of the buttons size
     * @param paddleSize     Modifier of the paddle size
     * @param nameClass      Name of the selected class
     * @param login          Login of the player
     * @param selectedSpells List of spells selected by the player
     */
    public Player(int life, int shield, int ballsNb, float ballsSpeed, float ballsSize, float buttonSize, float paddleSize, String nameClass, String login, ArrayList<String> selectedSpells) {
        this.life = life;
        this.shield = shield;
        this.ballsSpeed = ballsSpeed;
        this.ballsSize = ballsSize;
        this.buttonSize = buttonSize;
        this.paddleSize = paddleSize;
        this.nameClass = nameClass;
        this.login = login;
        this.selectedSpells = selectedSpells;
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    @Deprecated
    public void loseLife(int damage) {
        if (this.life < damage) {
            this.life = 0;
        } else {
            this.life = this.life - damage;
        }
    }
    @Deprecated
    public void loseShield(int damage) {
        if (this.shield < damage) {
            this.shield = 0;
        } else {
            this.shield = this.shield - damage;
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getBallsNb() {
        return ballsNb;
    }

    public void setBallsNb(int ballsNb) {
        this.ballsNb = ballsNb;
    }

    public float getBallsSpeed() {
        return ballsSpeed;
    }

    public void setBallsSpeed(float ballsSpeed) {
        this.ballsSpeed = ballsSpeed;
    }

    public float getBallsSize() {
        return ballsSize;
    }

    public void setBallsSize(float ballsSize) {
        this.ballsSize = ballsSize;
    }

    public float getButtonSize() {
        return buttonSize;
    }

    public void setButtonSize(float buttonSize) {
        this.buttonSize = buttonSize;
    }

    public float getPaddleSize() {
        return paddleSize;
    }

    public void setPaddleSize(float paddleSize) {
        this.paddleSize = paddleSize;
    }

    public ArrayList<String> getSelectedSpells() {
        return selectedSpells;
    }

    public void setSelectedSpells(ArrayList<String> selectedSpells) {
        this.selectedSpells = selectedSpells;
    }
}
