package com.app.remi.test.network.backend.networkRunnable;


import com.app.remi.test.network.backend.ClientInterfaceTCP;
import com.app.remi.test.network.backend.services.NetworkBackendService;

import java.io.IOException;

/**
 * It's mandatory to use thread to use socket
 * So this class will end the connection with the server
 * Created by Mat on 17/12/2017.
 */

public class BackgroundRunnableDisconnection implements Runnable {
    private ClientInterfaceTCP clientInterfaceTCP;
    private NetworkBackendService networkBackendService;

    public BackgroundRunnableDisconnection(NetworkBackendService networkBackendService, ClientInterfaceTCP clientInterfaceTCP) {
        this.clientInterfaceTCP = clientInterfaceTCP;
        this.networkBackendService = networkBackendService;
    }

    /**
     * If the TCP client is  connected, disconnect it
     */
    @Override
    public void run() {

        try {
            if (this.clientInterfaceTCP.getConnected())
                this.clientInterfaceTCP.setConnected(this.clientInterfaceTCP.disconect());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
