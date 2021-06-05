package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class SqlHelper {
    private static final String UNIQUE_VIOLATION_STATE = "23505";
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void execute(String sql) {
        execute(sql, PreparedStatement::execute);
    }

    public <T> T execute(String sql, Command<T> command) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            return command.execute(statement);
        } catch (SQLException e) {
            if (Objects.equals(UNIQUE_VIOLATION_STATE, e.getSQLState())) {
                throw new ExistStorageException(null);
            }
            throw new StorageException(e);
        }
    }
}
