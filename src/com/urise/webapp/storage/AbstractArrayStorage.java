package com.urise.webapp.storage;

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
    private int currentElementIndex;

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
        currentElementIndex = findIndex(uuid);
        super.save(resume);
    }

    @Override
    protected boolean checkResumePresence(Resume resume) {
        return currentElementIndex > -1;
    }

    @Override
    protected void addToStorage(Resume resume) {
        saveToArray(resume, currentElementIndex);
        size++;
    }

    @Override
    protected Object getIndexOrOptional(String uuid) {
        return findIndex(uuid);
    }

    @Override
    protected boolean checkResumeAbsence(Object indexOrOptional, String uuid) {
        return (int) indexOrOptional < 0;
    }

    @Override
    protected void removeFromStorage(Object indexOrOptional, String uuid) {
        shiftArray((int) indexOrOptional);
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
    protected void updateInStorage(Object indexOrOptional, Resume resume) {
        storage[(int) indexOrOptional] = resume;
    }

    @Override
    protected Resume getFromStorage(Object indexOrOptional, String uuid) {
        return storage[(int) indexOrOptional];
    }

    protected abstract void saveToArray(Resume resume, int index);

    protected abstract int findIndex(String uuid);
}
