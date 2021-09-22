package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    protected final int port;
    protected ServerSocket serverSocket;

    protected final ExecutorService executorService = Executors.newFixedThreadPool(64);
    protected static final List<String> VALID_PATHS = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public Server(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            Thread server = new Thread(this::processConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processConnection() {
        while (!serverSocket.isClosed()) {
            try (final Socket socket = serverSocket.accept()) {
                executorService.execute(new RequestHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }
}


