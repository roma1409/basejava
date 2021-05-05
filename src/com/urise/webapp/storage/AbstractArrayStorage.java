package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int MAX_SIZE = 10_000;
    protected final Resume[] storage = new Resume[MAX_SIZE];
    protected int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        if (storage.length <= size) {
            throw new StorageException("Storage overflow.", uuid);
        }
        int index = findIndex(uuid);
        if (index > -1) {
            throw new ExistStorageException(uuid);
        }
        saveToArray(resume, index);
        size++;
    }

    @Override
    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        shiftArray(index);
    }

    private void shiftArray(int index) {
        int length = size - 1 - index;
        if (length > 0) {
            System.arraycopy(storage, index + 1, storage, index, length);
        }
        storage[--size] = null;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public void update(Resume resume) {
        int index = findIndex(resume.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(resume.getUuid());
        }
        storage[index] = resume;
    }

    @Override
    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return storage[index];
    }

    protected abstract void saveToArray(Resume resume, int index);

    protected abstract int findIndex(String uuid);
}
