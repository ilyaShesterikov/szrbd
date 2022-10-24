package com.university.smorch.first;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();
        Thread firstClient = new Thread(new Client(server, "Client 1"));
        firstClient.start();
        Thread secondClient = new Thread(new Client(server, "Client 2"));
        secondClient.start();
        Thread thirdClient = new Thread(new Client(server, "Client 3"));
        thirdClient.start();
        Thread fourthClient = new Thread(new Client(server, "Client 4"));
        fourthClient.start();
    }
}
