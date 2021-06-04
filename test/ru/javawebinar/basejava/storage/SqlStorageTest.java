package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;

import java.util.Properties;

public class SqlStorageTest extends AbstractStorageTest {
    private static final Properties properties;
    private static final String url;
    private static final String user;
    private static final String password;

    static {
        properties = Config.get().getProperties();
        url = properties.getProperty("db.url");
        user = properties.getProperty("db.user");
        password = properties.getProperty("db.password");
    }

    public SqlStorageTest() {
        super(new SqlStorage(url, user, password));
    }
}
