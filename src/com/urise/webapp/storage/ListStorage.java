package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected boolean checkResumePresence(Resume resume) {
        return storage.contains(resume);
    }

    @Override
    protected void addToStorage(Resume resume) {
        storage.add(resume);
    }

    @Override
    protected Object getIndexOrOptional(String uuid) {
        return getOptionalResume(uuid);
    }

    @Override
    protected boolean checkResumeAbsence(Object indexOrOptional, String uuid) {
        return ((Optional<Resume>) indexOrOptional).isEmpty();
    }

    @Override
    protected void removeFromStorage(Object indexOrOptional, String uuid) {
        storage.remove(((Optional<Resume>) indexOrOptional).get());
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    protected void updateInStorage(Object indexOrOptional, Resume resume) {
        storage.set(storage.indexOf(resume), resume);
    }

    @Override
    protected Resume getFromStorage(Object indexOrOptional, String uuid) {
        return ((Optional<Resume>) indexOrOptional).get();
    }

    private Optional<Resume> getOptionalResume(String uuid) {
        return storage.stream()
                .filter(currentResume -> uuid.equals(currentResume.getUuid()))
                .findFirst();
    }
}
