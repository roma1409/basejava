package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Command<T> {
    T execute(PreparedStatement statement) throws SQLException;
}
