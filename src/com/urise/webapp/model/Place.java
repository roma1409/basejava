package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class Place {
    private final String company;
    private final List<Period> periods = new ArrayList<>();

    public Place(String company) {
        this.company = company;
    }

    public void addPeriod(Period period) {
        periods.add(period);
    }

    @Override
    public String toString() {
        return "Place{" +
                "company='" + company + '\'' +
                ", periods=" + periods +
                '}';
    }
}
