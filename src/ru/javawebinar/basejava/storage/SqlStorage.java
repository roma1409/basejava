package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        helper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return helper.transactionalExecute(connection -> {
            Resume resume;
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM resume WHERE uuid = ?")) {
                statement.setString(1, uuid);
                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, resultSet.getString("full_name"));
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact WHERE resume_uuid = ?")) {
                statement.setString(1, uuid);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    addContact(resultSet, resume);
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM section WHERE resume_uuid = ?")) {
                statement.setString(1, uuid);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    addSection(resultSet, resume);
                }
            }
            return resume;
        });
    }

    @Override
    public void save(Resume resume) {
        helper.transactionalExecute(connection -> {
            insertResume(resume, connection);
            insertContacts(resume, connection);
            insertSections(resume, connection);
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
            deleteContacts(resume.getUuid(), connection);
            deleteSections(resume.getUuid(), connection);
            updateResume(resume, connection);
            insertContacts(resume, connection);
            insertSections(resume, connection);
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
        return helper.transactionalExecute(connection -> {
            Map<String, Resume> uuidToResume = new LinkedHashMap<>();
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("uuid");
                    uuidToResume.put(uuid, new Resume(uuid, resultSet.getString("full_name")));
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("resume_uuid");
                    Resume resume = uuidToResume.get(uuid);
                    addContact(resultSet, resume);
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM section")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("resume_uuid");
                    Resume resume = uuidToResume.get(uuid);
                    addSection(resultSet, resume);
                }
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

    private void addSection(ResultSet resultSet, Resume resume) throws SQLException {
        String typeString = resultSet.getString("type");
        if (Objects.nonNull(typeString)) {
            SectionType type = SectionType.valueOf(typeString);
            String value = resultSet.getString("value");
            AbstractSection section = null;
            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    section = new TextSection(value);
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    section = new ListSection(value.split("\n"));
                    break;
            }
            if (Objects.nonNull(section)) {
                resume.addSection(type, section);
            }
        }
    }

    private void insertSections(Resume resume, Connection connection) throws SQLException {
        final String uuid = resume.getUuid();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
                SectionType type = entry.getKey();
                String value = null;
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        value = ((TextSection) entry.getValue()).getContent();
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        value = String.join("\n", ((ListSection) entry.getValue()).getItems());
                        break;
                }
                if (Objects.nonNull(value)) {
                    statement.setString(1, uuid);
                    statement.setString(2, type.name());
                    statement.setString(3, value);
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        }
    }

    private void deleteSections(String uuid, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
            statement.setString(1, uuid);
            statement.execute();
        }
    }
}
