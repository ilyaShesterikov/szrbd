package com.university.prdb.model;

public class StringsInstrument extends MusicalInstrument {
    public StringsInstrument() {
    }

    public StringsInstrument(String name, Manufacturer manufacturer) {
        super(name, manufacturer);
    }

    @Override
    public String toString() {
        return "StringsInstrument{" +
                "manufacturer=" + manufacturer +
                '}';
    }
}
