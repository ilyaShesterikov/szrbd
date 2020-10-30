package com.university.prdb.model;

public class Feature {
    String type;
    Boolean disconnectable;

    public Feature(String type, Boolean disconnectable) {
        this.type = type;
        this.disconnectable = disconnectable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDisconnectable() {
        return disconnectable;
    }

    public void setDisconnectable(Boolean disconnectable) {
        this.disconnectable = disconnectable;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "type='" + type + '\'' +
                ", disconnectable=" + disconnectable +
                '}';
    }
}
