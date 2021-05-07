package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class BulletedListSection implements Section {
    private final List<String> items = new ArrayList<>();

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (String item : items) {
            builder.append("- ").append(item).append("\n");
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }
}
