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
    private static final String UUID_4 = "uuid4";

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
        checkSize(3);
    }

    @Test
    void save() {
        Resume r4 = new Resume(UUID_4);
        checkSize(3);
        storage.save(r4);
        assertEquals(r4, storage.get(UUID_4));
        checkSize(4);
    }

    @Test
    void saveExist() {
        checkSize(3);
        assertThrows(ExistStorageException.class, () -> storage.save(r1));
        checkSize(3);
    }

    @Test
    void saveOverflow() {
        for (int i = storage.size(); i < MAX_SIZE; i++) {
            storage.save(new Resume(String.format("%d", i)));
        }
        checkSize(MAX_SIZE);
        assertThrows(StorageException.class, () -> storage.save(new Resume(UUID_4)));
        checkSize(MAX_SIZE);
    }

    @Test
    void delete() {
        checkSize(3);
        storage.delete(UUID_1);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
        checkSize(2);
    }

    @Test
    void deleteNotExist() {
        checkSize(3);
        assertThrows(NotExistStorageException.class, () -> storage.delete(UUID_4));
        checkSize(3);
    }

    @Test
    void clear() {
        checkSize(3);
        storage.clear();
        checkSize(0);
    }

    @Test
    void getAll() {
        Resume[] expected = {r1, r2, r3};
        Resume[] actual = storage.getAll();

        assertArrayEquals(expected, actual);
        checkSize(3);
    }

    @Test
    void update() {
        Resume r4 = new Resume(UUID_1);
        checkSize(3);
        storage.update(r4);
        assertEquals(r4, storage.get(UUID_1));
        checkSize(3);
    }

    @Test
    void updateNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.update(new Resume(UUID_4)));
    }

    @Test
    void get() {
        assertEquals(r1, storage.get(UUID_1));
    }

    @Test
    void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID_4));
    }

    private void checkSize(int expectedSize) {
        assertEquals(expectedSize, storage.size());
    }
}
