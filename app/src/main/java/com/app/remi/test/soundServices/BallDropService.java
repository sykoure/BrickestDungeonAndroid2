package com.app.remi.test.soundServices;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.app.remi.test.R;

/**
 * Service responsible for playing a sound when the ball fall below the bar
 */
public class BallDropService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    private MediaPlayer mediaPlayer;

    public BallDropService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final MediaPlayer mpDrop = MediaPlayer.create(this, R.raw.ball_drop);
        this.mediaPlayer = mpDrop;

    }

    /**
     * Add here the behavior for playing the sound
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        Log.d("BALL_BOUNCE_SERVICE", "PLAY BALL DROP SOUND");
        Soundthread soundthread = new Soundthread(this.mediaPlayer);
        soundthread.run();
        return mStartMode;
    }
}
