package com.university.smorch;

import java.util.ArrayList;

public class Passenger {
    public int id;
    public int floor;
    public boolean inTheElevator;
    public int finFloor;
    private Elevator elevator;

    public Passenger(int id, int floor, int finFloor, Elevator elevator) {
        this.floor = floor;
        this.finFloor = finFloor;
        this.elevator = elevator;
        this.id = id;
        elevator.call(this);
    }
}
