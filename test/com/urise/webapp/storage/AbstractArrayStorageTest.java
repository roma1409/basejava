package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Test;

import static com.urise.webapp.storage.AbstractArrayStorage.STORAGE_LIMIT;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    void saveOverflow() {
        for (int i = storage.size(); i < STORAGE_LIMIT; i++) {
            storage.save(new Resume(String.format("%d", i)));
        }
        assertSize(STORAGE_LIMIT);
        assertThrows(StorageException.class, () -> storage.save(new Resume(UUID_4)));
        assertSize(STORAGE_LIMIT);
    }
}
