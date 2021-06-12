package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.SqlStorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String PROPERTY_FILE_NAME = getConfigPath();
    private static final Config INSTANCE = new Config();

    private final File storageDir;
    private final SqlStorage sqlStorage;

    public static Config get() {
        return INSTANCE;
    }

    private static String getConfigPath() {
//        String projectPath = "Linux".equals(System.getProperty("os.name")) ? "/home/roman/Desktop" : "C:/Users/roman.pogorelov/Desktop/javaops";
//        return projectPath.concat("/basejava/config/resumes.properties");
        return "/resumes.properties";
    }

    private Config() {
        try (InputStream stream = Config.class.getResourceAsStream(PROPERTY_FILE_NAME)) {
            Properties properties = new Properties();
            properties.load(stream);
            String storagePathKey = "Linux".equals(System.getProperty("os.name")) ? "storage.dir.linux" : "storage.dir.win";
            storageDir = new File(properties.getProperty(storagePathKey));

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            sqlStorage = new SqlStorage(url, user, password);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Invalid config file %s", PROPERTY_FILE_NAME));
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public SqlStorage getSqlStorage() {
        return sqlStorage;
    }
}
