package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.Organization;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * gkislin
 * 20.07.2016
 */
public class DateUtil {
    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String formatDates(Organization.Position position) {
        LocalDate startDate = position.getStartDate();
        LocalDate endDate = position.getEndDate();
        String endDateStr = endDate.equals(DateUtil.NOW) ? "Настоящее время" : endDate.format(DATE_FORMATTER);
        return String.format("%s по %s.", startDate.format(DATE_FORMATTER), endDateStr);
    }
}
