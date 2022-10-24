package com.university.smorch;

public class Main {
    public static void main(String[] args) {
        ResouceAllocator resouceAllocator = new ResouceAllocator();
        for (int i = 0; i < 4; i++) {
            Process process = new Process((i + 1), resouceAllocator);
            process.start();
        }
    }
}
