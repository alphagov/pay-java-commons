package uk.gov.service.payments.commons.testing.port;

import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

public class PortFactory {
    public static int findFreePort() {
        int port;
        try {
            ServerSocket server = new ServerSocket(0);
            port = server.getLocalPort();
            server.close();
            // allow time for the socket to be released
            TimeUnit.MILLISECONDS.sleep(350);
        } catch (Exception e) {
            throw new RuntimeException("Exception while trying to find a free port", e);
        }
        return port;
    }
}
