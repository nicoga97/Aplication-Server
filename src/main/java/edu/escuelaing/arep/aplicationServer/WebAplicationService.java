package edu.escuelaing.arep.aplicationServer;

import java.io.IOException;

public class WebAplicationService {

    public WebAplicationService() throws ReflectiveOperationException {
        ListURLHandler handler = new ListURLHandler();
        handler.loadWebAplications();

        initializeServer(getPort(), handler);
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    private void initializeServer(int port, ListURLHandler handler) throws ReflectiveOperationException {
        try {
            new Server(port, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
