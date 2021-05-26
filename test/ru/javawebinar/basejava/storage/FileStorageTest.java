package ru.javawebinar.basejava.storage;

public class FileStorageTest extends AbstractStorageTest {
    private static final FileStorage storage = new FileStorage(STORAGE_DIR);

    static {
        storage.setSerializableStrategy(new ObjectSerializationStrategy());
    }

    public FileStorageTest() {
        super(storage);
    }
}
