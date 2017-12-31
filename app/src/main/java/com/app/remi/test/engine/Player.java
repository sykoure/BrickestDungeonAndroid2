package com.app.remi.test.engine;

/**
 * Created by Remi on 31/12/2017.
 */

public class Player {

    private int life;
    private int shield;
    private String nameClass;

    public Player(int life,int shield,String nameClass){
        this.life = life;
        this.shield = shield;
        this.nameClass = nameClass;
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

    public void loseLife(int damage){
        if(this.life < damage){
            this.life = 0;
        }
        else {
            this.life = this.life - damage;
        }
    }

    public void loseShield(int damage){
        if(this.shield < damage){
            this.shield = 0;
        }
        else {
            this.shield = this.shield - damage;
        }
    }
}
