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

    @Web("method2")
    public static String method2() {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Test 2</title>\n"
                + "</head>"
                + "<body>"
                + "Test 2 is working"
                + "</body>"
                + "</html>";
    }

    @Web("method3")
    public static String addNumbers(String number1, String number2) {
        return Double.toString(Double.parseDouble(number1) + Double.parseDouble(number2));
    }
}

