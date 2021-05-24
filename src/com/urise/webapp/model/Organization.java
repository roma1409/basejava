package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Organization {
    private final String name;
    private final String link;
    private final List<Period> periods = new ArrayList<>();

    public Organization(String name) {
        this(name, null);
    }

    public Organization(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public void addPeriod(Period period) {
        periods.add(period);
    }

    @Override
    public String toString() {
        String linkData = Objects.isNull(link) ? "" : String.format("(%s)", link);
        return String.format("%s%s, %s", name, linkData, periods);
    }
}
