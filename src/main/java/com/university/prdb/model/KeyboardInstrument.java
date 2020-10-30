package com.university.prdb.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class KeyboardInstrument extends MusicalInstrument {
    int keysQuantity;
    List<Feature> features;

    public KeyboardInstrument() {
    }

    public KeyboardInstrument(int keysQuantity, List<Feature> features) {
        this.keysQuantity = keysQuantity;
        this.features = features;
    }

    public KeyboardInstrument(String name, Manufacturer manufacturer, int keysQuantity, List<Feature> features) {
        super(name, manufacturer);
        this.keysQuantity = keysQuantity;
        this.features = features;
    }

    public int getKeysQuantity() {
        return keysQuantity;
    }

    public void setKeysQuantity(int keysQuantity) {
        this.keysQuantity = keysQuantity;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public void addFeatures(Feature feature) {
        if (features == null) {
            features = new ArrayList<>();
        };
        features.add(feature);
    }

    @Override
    public String toString() {
        return "KeyboardInstrument{" +
                "keysQuantity=" + keysQuantity +
                ", features=" + features +
                ", manufacturer=" + manufacturer +
                '}';
    }

    @Override
    public String play() {
        return this.toString() + " makes doremi'";
    }
}
