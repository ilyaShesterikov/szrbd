package com.university.smorch;

import java.util.ArrayList;

public class Elevator extends Thread {
    public int currentFloor;
    private ArrayList<Passenger> upCalls = new ArrayList<>();
    private ArrayList<Passenger> downCalls = new ArrayList<>();
    private ArrayList<Passenger> inbox = new ArrayList();

    public Elevator() {
        this.currentFloor = 1;
    }

    public synchronized void call(Passenger passenger) {
        if (passenger.floor == 1) {
            downCalls.add(passenger);
        } else {
            upCalls.add(passenger);
        }
    }

    @Override
    public void run() {
        System.out.println("Elevator is started");
        go();
    }

    public synchronized void go() {
        System.out.println(upCalls.size());
        System.out.println(downCalls.size());
        int counter = 0;
        while (upCalls.size() > 0 || downCalls.size() > 0) {
            goUp();
            goDown();
            counter++;
        }
    }

    private void goDown() {
        while (currentFloor != 1) {
            System.out.println("(DOWN)Floor: " + currentFloor);
            for (int i = 0; i < upCalls.size(); i++) {
                if (upCalls.get(i).floor == currentFloor) {
                    System.out.println("(DOWN)Taken " + upCalls.get(0).id);
                    inbox.add(upCalls.get(0));
                    upCalls.remove(0);
                }
                if (inbox.size() == 5) {
                    System.out.println("(DOWN)Inbox full");
                    break;
                }
            }
            if (inbox.size() == 5) {
                System.out.println("(DOWN)Inbox full");
                break;
            }
            currentFloor--;
        }
        System.out.println("(DOWN)Floor: " + currentFloor);
        System.out.println("(DOWN)Inbox empty");
        inbox.clear();
    }

    private void goUp() {
        while (inbox.size() < 5 && downCalls.size() > 0) {
            System.out.println("(UP)Taken " + downCalls.get(0).id);
            inbox.add(downCalls.get(0));
            downCalls.remove(0);
            if (inbox.size() == 5) {
                System.out.println("(UP)Inbox full");
            }
        }
        while (inbox.size() > 0 && currentFloor < 5) {
            System.out.println("(UP)Floor " + currentFloor);
            currentFloor++;
            for (int i = 0; i < inbox.size(); i++) {
                if (inbox.get(i).finFloor == currentFloor) {
                    System.out.println("(UP)Inbox minus: " +
                            inbox.get(i).id);
                    inbox.remove(i);
                }
            }
        }
        currentFloor = 5;
        System.out.println("(UP)Floor " + currentFloor);
    }
}
