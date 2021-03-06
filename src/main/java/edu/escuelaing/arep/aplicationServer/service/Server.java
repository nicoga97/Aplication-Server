package edu.escuelaing.arep.aplicationServer.service;

import edu.escuelaing.arep.aplicationServer.handlers.ListURLHandler;

import java.io.*;
import java.lang.reflect.Method;
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
    private ListURLHandler handler;
    private OutputStream outputSteam;

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
                    {"csv", "text/csv"},
                    {"doc", "application/msword"},
                    {"docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
                    {"ico", "image/vnd.microsoft.icon"},
                    {"json", "application/json"},
                    {"pdf", "application/pdf"},
            }).collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));


    public Server(int port, ListURLHandler handler) throws IOException, ReflectiveOperationException {
        this.handler = handler;
        keepRunning = true;
        this.port = port;
        do {
            startServer();
            handleRequests();
            stopServer();
        } while (keepRunning);
    }

    private static String getErrorHTTPHeader() {
        return "HTTP/1.1 404 \r\n"
                + "Content-Type: text/html;charset=UTF-8\r\n"
                + "\r\n";
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

    private static String getContentType(String path) {
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
        return contentType;
    }

    private static String getHTTPHeader(String contentType) {

        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Server: Nicoga97\r\n"
                + "Status: 200\r\n"
                + "\r\n";
    }

    private void handleRequests() throws IOException, ReflectiveOperationException {
        String firstLine = in.readLine();
        String input;
        outputSteam = clientSocket.getOutputStream();
        if (firstLine == null || firstLine.equals(" ")) {
            return;
        }

        String[] inputLine = firstLine.split(" ");
        System.out.println("Request recived: " + inputLine[0] + " " + inputLine[1] + " " + inputLine[2]);
        String[] params = null;
        if (inputLine[1].contains("?")) {
            inputLine[1] = inputLine[1].split("\\?")[0];
            params = ((firstLine.split(" ")[1]).split("\\?")[1]).split("&");
        }
        byte[] result;
        try {
            if (inputLine[1].equals("/")) {
                String absolutePath = Paths.get("").toAbsolutePath().toString();
                Path filePath = Paths.get(absolutePath, "/src/main/resources/public/welcomePage/index.html");
                result = Files.readAllBytes(filePath);

            } else if (handler.getURLHandlerList().containsKey(inputLine[1])) {
                if (params != null) {
                    params = getAndcheckParams(params, (String[]) handler.getURLHandlerList().get(inputLine[1]).get(1));
                    result = ((Method) handler.getURLHandlerList().get(inputLine[1]).get(0)).invoke(null, (Object[]) params).toString().getBytes();
                    System.out.println(((Method) handler.getURLHandlerList().get(inputLine[1]).get(0)).invoke(null, (Object[]) params).toString());
                } else {
                    result = ((Method) handler.getURLHandlerList().get(inputLine[1]).get(0)).invoke(null, null).toString().getBytes();
                    System.out.println(((Method) handler.getURLHandlerList().get(inputLine[1]).get(0)).invoke(null, null).toString());
                }


            } else {
                String absolutePath = Paths.get("").toAbsolutePath().toString();
                Path filePath = Paths.get(absolutePath, inputLine[1]);
                result = Files.readAllBytes(filePath);
            }
            out.write(getHTTPHeader(getContentType(inputLine[1])));
            out.flush();
            outputSteam.write(result);
            outputSteam.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return404Error();

        }


    }

    public String[] getAndcheckParams(String[] params, String[] paramsNames) throws Exception {
        String[] ordredParams = new String[paramsNames.length];
        if (params == null || params.length != paramsNames.length) {
            throw new Exception("Not found");
        }


        for (int i = 0; i < paramsNames.length; i++) {

            String[] x = params[i].split("=");
            ordredParams[i] = x[1];

        }

        return ordredParams;
    }

    public void return404Error() throws IOException {
        byte[] result;
        out.write(getErrorHTTPHeader());
        out.flush();
        String absolutePath = Paths.get("").toAbsolutePath().toString();
        Path filePath = Paths.get(absolutePath, "/src/main/resources/public/404/index.html");
        result = Files.readAllBytes(filePath);
        outputSteam.write(result);
        outputSteam.flush();
    }

    private void stopServer() throws IOException {
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
        outputSteam.close();
    }
}
