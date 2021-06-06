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
            throw handleUniqueViolation(e);
        }
    }

    public <T> T transactionalExecute(SqlTransaction<T> transaction) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T res = transaction.execute(connection);
                connection.commit();
                return res;
            } catch (SQLException e) {
                connection.rollback();
                throw handleUniqueViolation(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private StorageException handleUniqueViolation(SQLException e) throws StorageException {
        if (Objects.equals(UNIQUE_VIOLATION_STATE, e.getSQLState())) {
            throw new ExistStorageException(null);
        }
        return new StorageException(e);
    }
}
