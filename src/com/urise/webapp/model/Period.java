package com.urise.webapp.model;

import java.time.YearMonth;
import java.util.Objects;

public class Period {
    private final YearMonth from;
    private YearMonth to;
    private final String title;
    private final String description;

    public Period(YearMonth from, String title, String description) {
        this.from = from;
        this.title = title;
        this.description = description;
    }

    public Period(YearMonth from, YearMonth to, String title) {
        this(from, to, title, "");
    }

    public Period(YearMonth from, YearMonth to, String title, String description) {
        this(from, title, description);
        this.to = to;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(from);
        if (Objects.nonNull(to)) {
            builder.append(" to ").append(to);
        }
        builder.append(", ").append(title);
        if (Objects.nonNull(description) && !description.isBlank()) {
            builder.append(", ").append(description);
        }
        builder.append('}');
        return builder.toString();
    }
}
