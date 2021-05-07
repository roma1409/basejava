package com.urise.webapp.model;

import java.time.YearMonth;

public class Place {
    private final String company;
    private final YearMonth from;
    private YearMonth to;
    private final String title;
    private final String description;

    public Place(String company, YearMonth from, String title, String description) {
        this.company = company;
        this.from = from;
        this.title = title;
        this.description = description;
    }

    public Place(String company, YearMonth from, YearMonth to, String title) {
        this(company, from, to, title, "");
    }

    public Place(String company, YearMonth from, YearMonth to, String title, String description) {
        this(company, from, title, description);
        this.to = to;
    }

    public void setTo(YearMonth to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Place{" +
                "company='" + company + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}' + "\n";
    }
}
