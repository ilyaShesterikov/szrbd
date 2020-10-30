package com.university.prdb.model;

public abstract class MusicalInstrument {
    String name;
    Manufacturer manufacturer;

    public MusicalInstrument() {
    }

    public MusicalInstrument(String name, Manufacturer manufacturer) {
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "MusicalInstrument{" +
                "manufacturer=" + manufacturer +
                '}';
    }

    public String play() {
        return this.toString() + " makes bzyn'";
    }
}
