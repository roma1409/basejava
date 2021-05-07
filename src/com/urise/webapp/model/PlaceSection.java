package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class PlaceSection implements Section {
    private final List<Place> places = new ArrayList<>();

    public void addPlace(Place place) {
        places.add(place);
    }

    public void removePlace(Place place) {
        places.remove(place);
    }

    @Override
    public String toString() {
        return "PlaceSection{" +
                "places=" + places +
                '}';
    }
}
