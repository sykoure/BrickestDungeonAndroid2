package com.app.remi.test.network.backend.networkRunnable;


import com.app.remi.test.network.backend.ClientInterfaceTCP;
import com.app.remi.test.network.backend.services.NetworkBackendService;

/**
 * It's mandatory to use thread to use socket
 * So this class will allow the client to send a string to the server
 * Created by Mat on 17/12/2017.
 */

public class BackgroundRunnableSendString implements Runnable {
    private ClientInterfaceTCP clientInterfaceTCP;
    private NetworkBackendService networkBackendService;
    private String messageToSend;

    public BackgroundRunnableSendString(ClientInterfaceTCP clientInterfaceTCP, NetworkBackendService networkBackendService, String messageToSend) {
        this.clientInterfaceTCP = clientInterfaceTCP;
        this.networkBackendService = networkBackendService;
        this.messageToSend = messageToSend;
    }

    /**
     * If the TCP client is connected, send a message to the server
     */
    @Override
    public void run() {
        if (this.clientInterfaceTCP.getConnected())
            this.clientInterfaceTCP.sendString(this.messageToSend);
    }
}
