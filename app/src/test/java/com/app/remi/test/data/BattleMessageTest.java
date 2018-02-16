package com.app.remi.test.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Remi on 12/02/2018.
 * This class will test the battleMessage class and its builder
 */
public class BattleMessageTest {

    // The battlemessage object
    private BattleMessage battleMessage = new BattleMessage.BattleMessageBuilder().
            setHadMessage(true).
            setMessageCombat("test").
            setPseudo("pseudo").
            build();

    /**
     * Unit test about getters and setters
     */

    // We will test the message inside the MessageCombat object
    @Test
    public void getMessageCombat() throws Exception {
        assertEquals("test",battleMessage.getMessageCombat());
    }

    @Test
    public void setMessageCombat() throws Exception {
        battleMessage.setMessageCombat("test2");
        assertEquals("test2",battleMessage.getMessageCombat());
    }

    // We will test if the message inside the MessageCombat has to be shown or not
    @Test
    public void getHasMessage() throws Exception {
        assertTrue(battleMessage.getHasMessage());
    }

    @Test
    public void setHasMessage() throws Exception {
        battleMessage.setHasMessage(false);
        assertFalse(battleMessage.getHasMessage());
    }

    // We will test the pseudo of the player for the message combat
    @Test
    public void getPseudo() throws Exception {
        assertEquals("pseudo",battleMessage.getPseudo());
    }

    @Test
    public void setPseudo() throws Exception {
        battleMessage.setPseudo("test");
        assertEquals("test",battleMessage.getPseudo());
    }

}