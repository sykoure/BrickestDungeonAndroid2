package com.app.remi.test.soundServices;

import android.media.MediaPlayer;

/**
 * Created by Mat on 26/12/2017.
 */

public class Soundthread extends Thread {
    private MediaPlayer mediaPlayer;

    public Soundthread(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void run() {
        this.mediaPlayer.start();
    }
}
