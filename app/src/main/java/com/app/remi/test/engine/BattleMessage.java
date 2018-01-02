package com.app.remi.test.engine;

/**
 * Created by Remi on 02/01/2018.
 */

public class BattleMessage {

    private String messageCombat;            // This message will be shown when the players will use a spell
    private Boolean hasMessage;              // If the BattleMessage contains something
    private String pseudo;                   // Contain the pseudo of one of the two players

    public BattleMessage(String messageCombat){
        this.messageCombat = messageCombat;
        hasMessage = false;
    }

    public String getMessageCombat() {
        return messageCombat;
    }

    public void setMessageCombat(String messageCombat) {
        this.messageCombat = messageCombat;
    }

    public Boolean getHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(Boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
