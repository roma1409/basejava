package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class UnOrderedList implements Section {
    private final List<String> items = new ArrayList<>();

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    @Override
    public String toString() {
        return "" + items + "\n";
    }
}
