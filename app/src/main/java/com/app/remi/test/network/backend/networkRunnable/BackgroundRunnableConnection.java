package com.app.remi.test.network.backend.networkRunnable;


import com.app.remi.test.network.backend.ClientInterfaceTCP;
import com.app.remi.test.network.backend.services.NetworkBackendService;

import java.io.IOException;

/**
 * It's mandatory to use thread to use socket
 * So this class will allow a connection to be established with the server
 * Created by Mat on 17/12/2017.
 */

public class BackgroundRunnableConnection implements Runnable {
    private ClientInterfaceTCP clientInterfaceTCP;
    private NetworkBackendService networkBackendService;

    public BackgroundRunnableConnection(NetworkBackendService networkBackendService, ClientInterfaceTCP clientInterfaceTCP) {
        this.clientInterfaceTCP = clientInterfaceTCP;
        this.networkBackendService = networkBackendService;
    }

    /**
     * If the TCP client is not already connected, connect it
     * And send a ping to the server
     */
    @Override
    public void run() {

        try {
            if (!this.clientInterfaceTCP.getConnected())
                this.clientInterfaceTCP.setConnected(this.clientInterfaceTCP.connection());
            new Thread(new BackgroundRunnableSendString(this.clientInterfaceTCP, this.networkBackendService, "BPING")).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
