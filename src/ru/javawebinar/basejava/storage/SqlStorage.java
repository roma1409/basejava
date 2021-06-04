package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        this.helper = new SqlHelper();
    }

    @Override
    public void clear() {
        helper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return (Resume) helper.execute("SELECT * FROM resume WHERE uuid = ?", (statement) -> {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, resultSet.getString("full_name"));
        });
    }

    @Override
    public void save(Resume r) {
        helper.execute("INSERT INTO resume (uuid, full_name) VALUES (?, ?)", (statement) -> {
            statement.setString(1, r.getUuid());
            statement.setString(2, r.getFullName());
            statement.execute();

            return Optional.empty();
        });
    }

    @Override
    public void update(Resume r) {
        helper.execute("UPDATE resume SET full_name = ? WHERE uuid = ?", (statement) -> {
            statement.setString(1, r.getFullName());
            statement.setString(2, r.getUuid());
            statement.execute();

            return Optional.empty();
        });
    }

    @Override
    public void delete(String uuid) {
        helper.execute("DELETE FROM resume WHERE uuid = ?", (statement) -> {
            statement.setString(1, uuid);
            if (!statement.execute()) {
                throw new NotExistStorageException(String.format("There isn't such resume with uuid: %s", uuid));
            }

            return Optional.empty();
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return (List<Resume>) helper.execute("SELECT * FROM resume", statement -> {
            ResultSet resultSet = statement.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (resultSet.next()) {
                resumes.add(new Resume(resultSet.getString("uuid"), resultSet.getString("full_name")));
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return (int) helper.execute("SELECT count(*) as total FROM resume", (statement) -> {
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException("While counting error.");
            }
            return resultSet.getInt("total");
        });
    }

    private class SqlHelper<T> {
        void execute(String sql) {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.execute();
            } catch (SQLException e) {
                throw new StorageException(e);
            }
        }

        T execute(String sql, Command<T> command) {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                return command.execute(statement);
            } catch (SQLException e) {
                throw new StorageException(e);
            }
        }
    }

    private interface Command<T> {
        T execute(PreparedStatement statement) throws SQLException;
    }
}
