package ru.javawebinar.basejava.storage;

public class PathStorageTest extends AbstractStorageTest {
    private static final PathStorage storage = new PathStorage(STORAGE_DIR.getAbsolutePath());

    static {
        storage.setSerializableStrategy(new ObjectSerializationStrategy());
    }

    public PathStorageTest() {
        super(storage);
    }
}
