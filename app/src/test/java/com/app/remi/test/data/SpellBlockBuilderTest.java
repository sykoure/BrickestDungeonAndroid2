package com.app.remi.test.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mat on 10/02/2018.
 * This class will test the spellBlock object and its builder
 */
public class SpellBlockBuilderTest {

    // The spellBlock object
    private SpellBlock spellBlock = new SpellBlock.SpellBlockBuilder(0,0).
                                        spell("spell").
                                        dimension(0,0).
                                        position(0,0).
                                        cooldownDuration(5).
                                        cooldown(0).
                                        build();
    //test

    @Test
    public void spell() throws Exception {
    }

    @Test
    public void dimension() throws Exception {
    }

    @Test
    public void cooldown() throws Exception {
    }

    @Test
    public void cooldownDuration() throws Exception {
    }

    @Test
    public void position() throws Exception {
    }

    @Test
    public void build() throws Exception {
    }

}