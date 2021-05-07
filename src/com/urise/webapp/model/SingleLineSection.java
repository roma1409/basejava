package com.urise.webapp.model;

public class SingleLineSection implements Section {
    private final String description;

    public SingleLineSection(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
