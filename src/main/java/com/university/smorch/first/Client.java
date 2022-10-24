package com.university.smorch.first;

public class Client implements Runnable {
    private Server server;
    private String name;
    private int num;

    public Client(Server server, String name) {
        this.server = server;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            int min = 100;
            int max = 500;
            for (int i = 0; i < 5; i++) {
                int randValue = (int) (Math.random() * (max - min + 1) + min);
                Thread.sleep(randValue);
                Process process = new Process(name + " process#" + (num++), Status.NOT_WORKING, 0);
                System.out.println(process);
                server.addProcess(process);
                System.out.println(process);
                checkStatus(process, Status.SERVICE);
                checkStatus(process, Status.FINISHED);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkStatus(Process process, Status status) throws InterruptedException {
        while (true) {
            if (process.getStatus() == status) {
                System.out.println(process);
                break;
            } else {
                Thread.sleep(10);
            }
        }
    }
}
