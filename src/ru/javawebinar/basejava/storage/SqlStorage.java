package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.Command;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        String query = "" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                "WHERE r.uuid = ?";

        Command<Resume> command = (statement) -> {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, resultSet.getString("full_name"));
            do {
                String value = resultSet.getString("value");
                String typeStr = resultSet.getString("type");
                if (Objects.nonNull(value) && Objects.nonNull(typeStr)) {
                    ContactType type = ContactType.valueOf(typeStr);
                    resume.addContact(type, value);
                }
            } while (resultSet.next());
            return resume;
        };

        return helper.execute(query, command);
    }

    @Override
    public void save(Resume r) {
        final String uuid = r.getUuid();
        helper.transactionalExecute(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                statement.setString(1, uuid);
                statement.setString(2, r.getFullName());
                statement.execute();
            }
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
                for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                    statement.setString(1, uuid);
                    statement.setString(2, entry.getKey().name());
                    statement.setString(3, entry.getValue());
                    statement.addBatch();
                }
                statement.executeBatch();
            }
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        helper.execute("UPDATE resume SET full_name = ? WHERE uuid = ?", (statement) -> {
            statement.setString(1, r.getFullName());
            statement.setString(2, r.getUuid());
            int updatedRowQuantity = statement.executeUpdate();
            if (updatedRowQuantity == 0) {
                throw new NotExistStorageException(r.getUuid());
            }

            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        helper.execute("DELETE FROM resume WHERE uuid = ?", (statement) -> {
            statement.setString(1, uuid);
            int updatedRowQuantity = statement.executeUpdate();
            if (updatedRowQuantity == 0) {
                throw new NotExistStorageException(uuid);
            }

            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.execute("SELECT * FROM resume ORDER BY full_name, uuid", statement -> {
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
        return helper.execute("SELECT count(*) as total FROM resume", (statement) -> {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("total");
        });
    }
}
