package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        helper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return helper.execute("" +
                        "SELECT * FROM resume r " +
                        "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid = ?",
                (statement) -> {
                    statement.setString(1, uuid);
                    ResultSet resultSet = statement.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }

                    Resume resume = new Resume(uuid, resultSet.getString("full_name"));

                    do {
                        addContact(resultSet, resume);
                    } while (resultSet.next());

                    return resume;
                });
    }

    @Override
    public void save(Resume resume) {
        helper.transactionalExecute(connection -> {
            insertResume(resume, connection);
            insertContacts(resume, connection);
            return null;
        });
    }

    private void insertResume(Resume resume, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
            statement.setString(1, resume.getUuid());
            statement.setString(2, resume.getFullName());
            statement.execute();
        }
    }

    @Override
    public void update(Resume resume) {
        helper.transactionalExecute(connection -> {
            updateResume(resume, connection);
            deleteContacts(resume.getUuid(), connection);
            insertContacts(resume, connection);
            return null;
        });
    }

    private void updateResume(Resume resume, Connection connection) throws SQLException {
        final String uuid = resume.getUuid();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
            statement.setString(1, resume.getFullName());
            statement.setString(2, uuid);
            int updatedRowQuantity = statement.executeUpdate();
            checkUpdatedRowQuantity(uuid, updatedRowQuantity);
        }
    }

    @Override
    public void delete(String uuid) {
        helper.transactionalExecute(connection -> {
            deleteResume(uuid, connection);
            return null;
        });
    }

    private void deleteResume(String uuid, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM resume WHERE uuid = ?")) {
            statement.setString(1, uuid);
            int updatedRowQuantity = statement.executeUpdate();
            checkUpdatedRowQuantity(uuid, updatedRowQuantity);
            statement.execute();
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.execute("" +
                        "SELECT * FROM resume r " +
                        "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                        "ORDER BY r.full_name, r.uuid",
                statement -> {
                    ResultSet resultSet = statement.executeQuery();
                    Map<String, Resume> uuidToResume = new LinkedHashMap<>();
                    while (resultSet.next()) {
                        String uuid = resultSet.getString("uuid");
                        Resume resume = uuidToResume.get(uuid);
                        if (Objects.isNull(resume)) {
                            resume = new Resume(uuid, resultSet.getString("full_name"));
                            uuidToResume.put(uuid, resume);
                        }
                        addContact(resultSet, resume);
                    }

                    return new ArrayList<>(uuidToResume.values());
                });
    }

    @Override
    public int size() {
        return helper.execute("SELECT count(*) as total FROM resume", (statement) -> {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("total");
        });
    }

    private void checkUpdatedRowQuantity(String uuid, int updatedRowQuantity) {
        if (updatedRowQuantity == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    private void addContact(ResultSet resultSet, Resume resume) throws SQLException {
        String typeStr = resultSet.getString("type");

        if (Objects.nonNull(typeStr)) {
            ContactType type = ContactType.valueOf(typeStr);
            resume.addContact(type, resultSet.getString("value"));
        }
    }

    private void insertContacts(Resume resume, Connection connection) throws SQLException {
        final String uuid = resume.getUuid();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                statement.setString(1, uuid);
                statement.setString(2, entry.getKey().name());
                statement.setString(3, entry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void deleteContacts(String uuid, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
            statement.setString(1, uuid);
            statement.execute();
        }
    }
}
