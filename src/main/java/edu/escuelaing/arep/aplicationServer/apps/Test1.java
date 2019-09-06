package edu.escuelaing.arep.aplicationServer.apps;

import edu.escuelaing.arep.aplicationServer.service.Web;


public class Test1 {
    

    @Web("method1")
    public static String method1() {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Test 1</title>\n"
                + "</head>"
                + "<body>"
                + "Test 1 is working"
                + "</body>"
                + "</html>";
    }
}

