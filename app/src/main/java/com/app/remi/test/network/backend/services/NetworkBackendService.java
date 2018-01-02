package com.app.remi.test.network.backend.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.remi.test.network.backend.ClientInterfaceTCP;
import com.app.remi.test.network.backend.networkRunnable.BackgroundRunnableConnection;
import com.app.remi.test.network.backend.networkRunnable.BackgroundRunnableDisconnection;
import com.app.remi.test.network.backend.networkRunnable.BackgroundRunnableSendString;


public class NetworkBackendService extends Service {
    public final static String MESSAGE_SEND_TAG = "com.example.mat.networktestclient.NetworkBackendService.MESSAGE_SEND_TO_RECEIVER";
    public final static String CLASS_TAG = "NetworkBackendService";
    public final static String NETWORK_INTENT_TAG = "com.example.mat.networktestclient.backend.services.NetworkBackendService.NETWORK_VALUE";
    public final static String NETWORK_MESSAGE_TAG = "com.example.mat.networktestclient.backend.services.NetworkBackendService.MESSAGE_VALUE";

    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used
    private ClientInterfaceTCP clientInterfaceTCP; // Backend class used for connection
    private LocalBroadcastManager localBroadcastManager;


    public NetworkBackendService() {

    }

    /**
     * At the creation instantiate a TCP client
     * we do it here to no ensure that the client has the same lifetime has this service
     * the same goes for the local broadcast manager
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.clientInterfaceTCP = new ClientInterfaceTCP(this);
        this.localBroadcastManager = LocalBroadcastManager.getInstance(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Will execute one of this class method, choosen by the value passed by the intent
     * This will allow every class to start/end connection and Send string to the server
     *
     * @param intent  the intent containing the action to make and possibly the message to send
     * @param flags   flags
     * @param startId startId
     * @return mStartMode
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getIntExtra(NETWORK_INTENT_TAG, -1)) {
            case 0:
                this.establishConnection();
                break;
            case 1:
                this.sendMessageToServer(intent.getStringExtra(NETWORK_MESSAGE_TAG));
                break;
            case 2:
                this.terminateConnection();
                break;
            case 3:
                this.stopSelf();    // This will stop the service once this valued is passed
            default:
                Log.e(CLASS_TAG, "ERROR SWITCH VALUE IS INVALID");
        }

        //Displayable displayable = (Displayable) intent.getSerializableExtra(MainActivity.MAINACTIVITYTAG);

        //localBroadcastManager.sendBroadcast(new Intent("com.example.mat.testfirstactivity.TRANSMISSION"));


        return mStartMode;
    }

    /**
     * Used to send a string to the receiver
     *
     * @param message Message to send to the receiver
     */
    public void sendMessageToReceiver(String message) {
        Intent intent = new Intent("com.example.mat.testfirstactivity.TRANSMISSION");
        intent.putExtra(MESSAGE_SEND_TAG, message);
        this.localBroadcastManager.sendBroadcast(intent);
    }

    /**
     *     * Used to send a string to the receiver with specification of the filter
     *
     * @param message Message to send to the receiver
     * @param filter Filter wich determine wich activity can see this message
     */
    public void sendMessageToReceiver(String message, String filter) {
        Intent intent = new Intent(filter);
        intent.putExtra(MESSAGE_SEND_TAG, message);
        this.localBroadcastManager.sendBroadcast(intent);
    }

    /**
     * Will use a thread to start the connection with the server
     * and send a string to establish a pseudo
     * TODO remove the pseudo once the server is updated.
     */
    public void establishConnection() {
        Log.d(CLASS_TAG, "Trying To connect");
        new Thread(new BackgroundRunnableConnection(this, this.clientInterfaceTCP)).start();
        this.sendMessageToServer("Pseudo");
    }

    /**
     * Will use a thrad to send a message to the server
     *
     * @param messageToSend String to send to the server
     */
    public void sendMessageToServer(String messageToSend) {
        String msg = "Sending " + messageToSend + " to server";
        Log.d(CLASS_TAG, msg);
        new Thread(new BackgroundRunnableSendString(this.clientInterfaceTCP, this, messageToSend)).start();
    }

    /**
     * Will use a thread to end the connection with the server
     */
    public void terminateConnection() {
        Log.d(CLASS_TAG, "Trying To disconnect");
        new Thread(new BackgroundRunnableDisconnection(this, this.clientInterfaceTCP)).start();
    }

}
