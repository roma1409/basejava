package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

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
    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
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
