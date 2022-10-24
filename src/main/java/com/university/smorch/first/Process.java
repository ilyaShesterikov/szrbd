package com.university.smorch.first;

public class Process {
    private String name;
    private Status status;
    private long time;

    public Process() {
    }

    public Process(String name, Status status, long time) {
        this.name = name;
        this.status = status;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return name + '\'' +
                ", status: " + status +
                ", time left: " + time;
    }
}
