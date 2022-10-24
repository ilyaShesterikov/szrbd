package com.university.smorch;

public class Process extends Thread {
    private int id;
    private int demand;
    private long timeout;
    private ResouceAllocator resouceAllocator;

    public Process(int id, ResouceAllocator resouceAllocator) {
        this.id = id;
        this.demand = ((int) (Math.random() * 9) + 1);
        this.resouceAllocator = resouceAllocator;
        this.timeout = ((int) (Math.random() * 100) + 100);
    }

    public void getResource() {
        int resource = -1;
        while (true) {
            System.out.println("Process " + this.id + ": Requested " + demand + " resources");
            resource = resouceAllocator.getRecourses(demand, this);
            if (resource != -1) {
                break;
            }
            w();
        }
        System.out.println("Process " + this.id + ": Got " + resource + " resources");
        timeout();
        System.out.println("Process " + this.id + ": Returned " + resource + " resources");
        resouceAllocator.returnRecourses(resource);
    }

    @Override
    public void run() {
        timeout();
        getResource();
    }

    public synchronized void w() {
        try {
            this.wait(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void timeout() {
        try {
            wait(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
