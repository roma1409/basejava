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
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (Place item : places) {
            builder.append("- ").append(item).append("\n");
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
}
