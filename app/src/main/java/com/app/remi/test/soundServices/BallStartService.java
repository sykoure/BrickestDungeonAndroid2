package com.app.remi.test.soundServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
/**
 * Service responsible for playing a sound when the ball start
 */
public class BallStartService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    public BallStartService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {


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
        Log.d("BALL_BOUNCE_SERVICE", "PLAY BALL START SOUND");
        return mStartMode;
    }
}
