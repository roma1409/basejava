package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected final Resume[] storage = new Resume[10_000];
    protected int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void save(Resume resume) {
        boolean isResumeNew = findIndex(resume.getUuid()) < 0;
        if (!isResumeNew) {
            System.out.printf("You can't 'SAVE' resume due to there is already a resume with this uuid: '%s'.%n", resume.getUuid());
        }
        boolean isEnoughSpaceInStorage = size < storage.length;
        if (!isEnoughSpaceInStorage) {
            System.out.println("You can't 'SAVE' resume due to there isn't enough space to add any new resume.");
        }

        if (isResumeNew && isEnoughSpaceInStorage) {
            addNew(resume);
        }
    }

    abstract protected void addNew(Resume resume);

    @Override
    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            showAbsenceResumeError("DELETE", uuid);
        } else {
            shiftArray(index);
        }
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
            showAbsenceResumeError("UPDATE", resume.getUuid());
        } else {
            storage[index] = resume;
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            showAbsenceResumeError("GET", uuid);
            return null;
        }
        return storage[index];
    }

    protected abstract int findIndex(String uuid);

    protected void showAbsenceResumeError(String operation, String uuid) {
        System.out.printf("You can't '%s' resume due to there isn't resume with this uuid: '%s'.%n", operation, uuid);
    }
}
