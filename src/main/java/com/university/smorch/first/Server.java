package com.university.smorch.first;

import java.util.LinkedList;
import java.util.Queue;

public class Server implements Runnable {
    private static final int ONE_SECOND = 1000;
    private static final int PROCESS_SIMULATION_TIME = 3000;
    private Queue<Process> queue;

    public Server() {
        queue = new LinkedList<>();
    }

    public void addProcess(Process process) {
        process.setStatus(Status.WAITING);
        process.setTime(PROCESS_SIMULATION_TIME * queue.size());
        queue.add(process);
    }


    @Override
    public void run() {
        try {
            while (true) {
                if (queue.size() == 0) {
                    Thread.sleep(ONE_SECOND);
                } else {
                    Process process = queue.poll();
                    if (process != null) {
                        process.setStatus(Status.SERVICE);
                        process.setTime(PROCESS_SIMULATION_TIME);
                        Thread.sleep(PROCESS_SIMULATION_TIME); //process simulation
                        process.setTime(0);
                        process.setStatus(Status.FINISHED);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
