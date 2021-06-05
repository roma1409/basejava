package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.SqlStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final File PROPERTY_FILE = new File("./config/resumes.properties");
    private static final Config INSTANCE = new Config();

    private final File storageDir;
    private final SqlStorage sqlStorage;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream stream = new FileInputStream(PROPERTY_FILE)) {
            Properties properties = new Properties();
            properties.load(stream);
            String storagePathKey = "Linux".equals(System.getProperty("os.name")) ? "storage.dir.linux" : "storage.dir.win";
            storageDir = new File(properties.getProperty(storagePathKey));

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            sqlStorage = new SqlStorage(url, user, password);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Invalid config file %s", PROPERTY_FILE.getAbsolutePath()));
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public SqlStorage getSqlStorage() {
        return sqlStorage;
    }
}
