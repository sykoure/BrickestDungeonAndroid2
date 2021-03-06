package com.app.remi.test.data;

/**
 * Created by Remi on 22/01/2018.
 */

public class BattleMessage {

    public static class BattleMessageBuilder{

        private String messageCombat;            // This message will be shown when the players will use a spell
        private Boolean hasMessage;              // If the BattleMessage contains something
        private String pseudo;                   // Contain the pseudo of one of the two players

        public BattleMessageBuilder(){
            this.messageCombat = null;
            this.hasMessage = null;
            this.pseudo = null;
        }

        public BattleMessageBuilder setMessageCombat(String messageCombat){
            this.messageCombat = messageCombat;
            return this;
        }

        public BattleMessageBuilder setHadMessage(Boolean hasMessage){
            this.hasMessage = hasMessage;
            return this;
        }

        public BattleMessageBuilder setPseudo(String pseudo){
            this.pseudo = pseudo;
            return this;
        }

        public BattleMessage build(){
            return new BattleMessage(this);
        }

    }

    private String messageCombat;            // This message will be shown when the players will use a spell
    private Boolean hasMessage;              // If the BattleMessage contains something
    private String pseudo;                   // Contain the pseudo of one of the two players

    /**
     * This method creates a BattleMessage object, which contains a message
     * hasMessage is false to prevent it from beeing showing up by the history in the Engine class
     * @param battleMessageBuilder contains all the data for the BattleMessage object
     */
    private BattleMessage(BattleMessageBuilder battleMessageBuilder) {
        this.messageCombat = battleMessageBuilder.messageCombat;
        this.hasMessage = battleMessageBuilder.hasMessage;
        this.pseudo = battleMessageBuilder.pseudo;
    }

    /**
     * this method return the spell used that will be shown by the history of a BattleMessage
     *
     * @return messageCombat
     */
    public String getMessageCombat() {
        return messageCombat;
    }

    /**
     * This method allows us to modify or create a messageCombat for a
     * BattleMessage that will be shown by the history
     *
     * @param messageCombat
     */
    public void setMessageCombat(String messageCombat) {
        this.messageCombat = messageCombat;
    }

    /**
     * This message allows the BattleMessage to show its messageCombat or not
     *
     * @return hasMessage
     */
    public Boolean getHasMessage() {
        return hasMessage;
    }

    /**
     * This message allows the BattleMessage to show its messageCombat or not
     *
     * @param hasMessage
     */
    public void setHasMessage(Boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    /**
     * This method gives us the pseudo of the player who used a spell
     *
     * @return pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * This method allows us, when we have created a BattleMessage,to assign a pseudo
     * to a BattleMessage
     *
     * @param pseudo
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}