package edu.escuelaing.arep.aplicationServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Server {
    private boolean keepRunning;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port;
    private PrintWriter out;
    private BufferedReader in;
    private URLHandler handler;

    private static final Map<String, String> EXTENSION_TO_MIMETYPE =
            Arrays.stream(new String[][]{
                    {"txt", "text/plain"},
                    {"html", "text/html"},
                    {"js", "application/javascript"},
                    {"css", "text/css"},
                    {"xml", "application/xml"},
                    {"png", "image/png"},
                    {"gif", "image/gif"},
                    {"jpg", "image/jpeg"},
                    {"jpeg", "image/jpeg"},
                    {"svg", "image/svg+xml"},
            }).collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));


    public Server(int port) throws IOException {
        keepRunning = true;
        this.port = port;
        do {
            startServer();
            handleRequests();
            stopServer();
        } while (keepRunning);
    }

    private void handleRequests() throws IOException {
        String outputLine;
        String[] inputLine = in.readLine().split(" ");
        String absolutePath = Paths.get("").toAbsolutePath().toString();
        Path filePath = Paths.get(absolutePath, inputLine[1]);
        System.out.println("Request recived: " + inputLine[0] + " " + inputLine[1] + " " + inputLine[2]);
        try {
            byte[] file = Files.readAllBytes(filePath);
            out.println(getHTTPHeader(inputLine[1]));
            OutputStream outputSteam = clientSocket.getOutputStream();
            outputSteam.write(file);
            outputSteam.flush();
        } catch (Exception e) {
            out.println(getHTTPHeader(inputLine[1]));
            out.println("<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Not Found</title>\n"
                    + "</head>"
                    + "<body>"
                    + "404 Not Found"
                    + "</body>"
                    + "</html>" );
        }


    }

    private void startServer() throws IOException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(1);
        }
        try {
            System.out.println("Ready to receive request...");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
    }

    private static String getHTTPHeader(String path) {
        String extension = "";
        String contentType;
        for (int i = path.length() - 1; i >= 0; i--) {
            Character j = path.charAt(i);
            if (!j.toString().equals(".")) {
                extension = j + extension;
            } else {
                i = -1;
            }
        }
        if (EXTENSION_TO_MIMETYPE.containsKey(extension)) {
            contentType = EXTENSION_TO_MIMETYPE.get(extension);
        } else {
            contentType = "text/html;charset=UTF-8";
        }
        return "HTTP/1.1 200 OK\n"
                + "Content-Type: " + contentType
                + "\nServer: Nicoga97\n"
                + "Status: 200\n";
    }

    private void stopServer() throws IOException {
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
