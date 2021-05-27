package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.ObjectSerializationStrategy;

public class ObjectPathStorageTest extends AbstractStorageTest {
    private static final PathStorage storage = new PathStorage(STORAGE_DIR.getAbsolutePath());

    public ObjectPathStorageTest() {
        super(storage);
        storage.setSerializableStrategy(new ObjectSerializationStrategy());
    }
}
