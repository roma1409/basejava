package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.urise.webapp.storage.AbstractArrayStorage.MAX_SIZE;
import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractArrayStorageTest {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    private final Storage storage;

    private Resume r1;
    private Resume r2;
    private Resume r3;

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        r1 = new Resume(UUID_1);
        r2 = new Resume(UUID_2);
        r3 = new Resume(UUID_3);
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @Test
    void size() {
        assertEquals(3, storage.size());
    }

    @Test
    void save() {
        assertEquals(3, storage.size());
        storage.save(new Resume("uuid4"));
        assertEquals(4, storage.size());
    }

    @Test
    void saveExist() {
        assertEquals(3, storage.size());
        assertThrows(ExistStorageException.class, () -> storage.save(r1));
        assertEquals(3, storage.size());
    }

    @Test
    void saveOverflow() {
        for (int i = storage.size(); i < MAX_SIZE; i++) {
            storage.save(new Resume(String.format("%d", i)));
        }
        assertEquals(MAX_SIZE, storage.size());
        assertThrows(StorageException.class, () -> storage.save(new Resume("uuid4")));
        assertEquals(MAX_SIZE, storage.size());
    }

    @Test
    void delete() {
        assertEquals(3, storage.size());
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
    }

    @Test
    void deleteNotExist() {
        assertEquals(3, storage.size());
        assertThrows(NotExistStorageException.class, () -> storage.delete("uuid4"));
        assertEquals(3, storage.size());
    }

    @Test
    void clear() {
        assertEquals(3, storage.size());
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    void getAll() {
        Resume[] expected = {r1, r2, r3};
        Resume[] resumes = storage.getAll();

        assertArrayEquals(expected, resumes);
        assertEquals(3, resumes.length);
    }

    @Test
    void update() {
        // TODO find out how to test this method.
    }

    @Test
    void updateNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume("uuid4")));
    }

    @Test
    void get() {
        assertEquals(r1, storage.get(UUID_1));
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }
}
