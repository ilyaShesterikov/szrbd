package com.university.prdb;

import com.university.prdb.model.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PrdbApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(PrdbApplication.class, args);
//    }

    public static void main(String[] args) {
        List<MusicalInstrument> list = new ArrayList<>();
        list.add(new StringsInstrument());
        Manufacturer manufacturer = new Manufacturer("name", "address", "1");
        List<Feature> features = new ArrayList<>();
        features.add(new Feature("color display", false));
        list.add(new KeyboardInstrument("piani", manufacturer, 20, features));

        for (MusicalInstrument mi : list) {
            System.out.println(mi.play());
        }

    }
}
