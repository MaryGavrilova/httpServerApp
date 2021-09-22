package ru.netology;

public class Main {

    public static final int PORT = 9999;
    public static final int TIME_OUT = 60000;

    public static void main(String[] args) {
        Server httpServer = new Server(PORT);
        httpServer.startServer();

        try {
            Thread.sleep(TIME_OUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        httpServer.stopServer();

    }
}


