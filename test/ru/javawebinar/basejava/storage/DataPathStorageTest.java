package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.DataSerializationStrategy;

public class DataPathStorageTest extends AbstractStorageTest {
    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataSerializationStrategy()));
    }
}