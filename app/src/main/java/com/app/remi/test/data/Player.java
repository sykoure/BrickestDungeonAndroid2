package com.app.remi.test.data;

import java.util.ArrayList;

/**
 * Created by Remi on 31/12/2017.
 */


/**
 * This class is only a date class, it will be useful to stock information about the two players
 * during a game.
 */
public class Player {

    public static class PlayerBuilder{

        private int life, shield, ballsNb;
        private float ballsSpeed, ballsSize, buttonSize, paddleSize;
        private String nameClass;
        private String login;
        private ArrayList<String> selectedSpells;

        public PlayerBuilder(int life,int shield,String login,String nameClass){
            this.life = life;
            this.shield = shield;
            this.login = login;
            this.nameClass = nameClass;
        }

        public PlayerBuilder ballsNb(int ballsNb){
            this.ballsNb = ballsNb;
            return this;
        }

        public PlayerBuilder ballsSpeed(float ballsSpeed){
            this.ballsSpeed = ballsSpeed;
            return this;
        }

        public PlayerBuilder ballsSize(float ballsSize){
            this.ballsSize = ballsSize;
            return this;
        }

        public  PlayerBuilder buttonSize(float buttonSize){
            this.buttonSize = buttonSize;
            return this;
        }

        public PlayerBuilder paddleSize(float paddleSize){
            this.paddleSize = paddleSize;
            return this;
        }

        public PlayerBuilder selectedSpells(ArrayList<String> selectedSpells){
            this.selectedSpells = selectedSpells;
            return this;
        }

        public Player build(){
            return new Player(this);
        }

    }

    private int life, shield, ballsNb;
    private float ballsSpeed, ballsSize, buttonSize, paddleSize;
    private String nameClass;
    private String login;
    private ArrayList<String> selectedSpells;


    /**
     * Real constructor
     */
    private Player(PlayerBuilder playerBuilder) {
        this.life = playerBuilder.life;
        this.shield = playerBuilder.shield;
        this.ballsNb = playerBuilder.ballsNb;
        this.ballsSpeed = playerBuilder.ballsSpeed;
        this.ballsSize = playerBuilder.ballsSize;
        this.buttonSize = playerBuilder.buttonSize;
        this.paddleSize = playerBuilder.paddleSize;
        this.nameClass = playerBuilder.nameClass;
        this.login = playerBuilder.login;
        this.selectedSpells = playerBuilder.selectedSpells;
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
