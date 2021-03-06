package edu.escuelaing.arep.aplicationServer.service;

import edu.escuelaing.arep.aplicationServer.handlers.ListURLHandler;

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
        return 8080;
    }

    private void initializeServer(int port, ListURLHandler handler) throws ReflectiveOperationException {
        try {
            new Server(port, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
