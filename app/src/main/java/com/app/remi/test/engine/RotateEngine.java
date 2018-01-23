package com.app.remi.test.engine;

import android.content.Context;
import android.hardware.SensorManager;

import com.app.remi.test.data.Player;
import com.app.remi.test.network.backend.services.NetworkBackendService;

/**
 * Created by Remi on 22/01/2018.
 */

public class RotateEngine extends Engine implements Runnable {
    /**
     * @param context               Activity holding this view
     * @param playWithSensor        Boolean value, define the playstyle, activate or not the accelerometer, desactivate the touch screen.
     * @param sensorManager         The sensor manager used to manage the accelerometer
     * @param numberSpellBlocks
     * @param ownPlayer
     * @param oppPlayer
     * @param networkBackendService
     */
    public RotateEngine(Context context, Boolean playWithSensor, SensorManager sensorManager, int numberSpellBlocks, Player ownPlayer, Player oppPlayer, NetworkBackendService networkBackendService) {
        super(context, playWithSensor, sensorManager, numberSpellBlocks, ownPlayer, oppPlayer, networkBackendService);
    }
}
