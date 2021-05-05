package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    @Override
    public void save(Resume resume) {
        if (checkResumePresence(resume)) {
            throw new ExistStorageException(resume.getUuid());
        }
        addToStorage(resume);
    }

    protected abstract boolean checkResumePresence(Resume resume);

    protected abstract void addToStorage(Resume resume);

    @Override
    public void delete(String uuid) {
        Object indexOrOptional = checkResumeAbsence(uuid);
        removeFromStorage(indexOrOptional, uuid);
    }

    private Object checkResumeAbsence(String uuid) {
        Object indexOrOptional = getIndexOrOptional(uuid);
        if (checkResumeAbsence(indexOrOptional, uuid)) {
            throw new NotExistStorageException(uuid);
        }
        return indexOrOptional;
    }

    protected abstract Object getIndexOrOptional(String uuid);

    protected abstract boolean checkResumeAbsence(Object indexOrOptional, String uuid);

    protected abstract void removeFromStorage(Object indexOrOptional, String uuid);

    @Override
    public void update(Resume resume) {
        Object indexOrOptional = checkResumeAbsence(resume.getUuid());
        updateInStorage(indexOrOptional, resume);
    }

    protected abstract void updateInStorage(Object indexOrOptional, Resume resume);

    @Override
    public Resume get(String uuid) {
        Object indexOrOptional = checkResumeAbsence(uuid);
        return getFromStorage(indexOrOptional, uuid);
    }

    protected abstract Resume getFromStorage(Object indexOrOptional, String uuid);
}
