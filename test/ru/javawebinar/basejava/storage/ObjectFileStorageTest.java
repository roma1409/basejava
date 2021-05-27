package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.ObjectSerializationStrategy;

public class ObjectFileStorageTest extends AbstractStorageTest {
    private static final FileStorage storage = new FileStorage(STORAGE_DIR);

    public ObjectFileStorageTest() {
        super(storage);
        storage.setSerializableStrategy(new ObjectSerializationStrategy());
    }
}
