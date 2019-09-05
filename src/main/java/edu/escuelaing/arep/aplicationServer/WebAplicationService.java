package edu.escuelaing.arep.aplicationServer;

import java.io.IOException;

public class WebAplicationService {

    public WebAplicationService() {
        initializeServer(35000);

    }
    public void initializeServer(int port){
        try {
            new Server(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
