package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.XmlSerializationStrategy;

public class XmlPathStorageTest extends AbstractStorageTest {
    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlSerializationStrategy()));
    }
}
