package com.urise.webapp.model;

public class Text implements Section {
    private final String description;

    public Text(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
