package com.university.smorch;

import java.util.ArrayList;

public class ResouceAllocator {
    private int numbOfRecourse = 9;

    public ResouceAllocator() {
    }

    public int getRecourses(int demand, Process process) {
        if (demand > numbOfRecourse) {
            System.out.println("Resource distributor: All recourses is busy");
            return -1;
        } else {
            System.out.println("Resource distributor: Given " + demand + " resources");
            numbOfRecourse -= demand;
            return demand;
        }
    }

    public void returnRecourses(int numb) {
        System.out.println("Resource distributor: Return received " + numb + " resources");
        numbOfRecourse += numb;
    }
}
