package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import org.junit.jupiter.api.Test;

import static com.urise.webapp.ResumeTestData.createResume;
import static com.urise.webapp.storage.AbstractArrayStorage.STORAGE_LIMIT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    void saveOverflow() {
        try {
            for (int i = storage.size(); i < STORAGE_LIMIT; i++) {
                storage.save(createResume(String.format("%d", i), "Vasya"));
            }
        } catch (Exception e) {
            fail();
        }
        assertSize(STORAGE_LIMIT);
        assertThrows(StorageException.class, () -> storage.save(createResume(UUID_4, "Vasya")));
        assertSize(STORAGE_LIMIT);
    }
}
