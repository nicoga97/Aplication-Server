package edu.escuelaing.arep.aplicationServer;

import java.io.IOException;

public class WebAplicationService {

    public WebAplicationService() throws ReflectiveOperationException {
        ListURLHandler handler = new ListURLHandler();
        handler.loadWebAplications();

        initializeServer(35000, handler);


    }

    public void initializeServer(int port, ListURLHandler handler) throws ReflectiveOperationException {
        try {
            new Server(port, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
