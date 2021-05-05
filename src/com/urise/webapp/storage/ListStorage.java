package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
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
    public void save(Resume resume) {
        if (storage.contains(resume)) {
            throw new ExistStorageException(resume.getUuid());
        }
        storage.add(resume);
    }

    @Override
    public void delete(String uuid) {
        Optional<Resume> optionalResume = getOptionalResume(uuid);
        if (optionalResume.isEmpty()) {
            throw new NotExistStorageException(uuid);
        }
        storage.remove(optionalResume.get());
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[size()]);
    }

    @Override
    public void update(Resume resume) {
        if (!storage.contains(resume)) {
            throw new NotExistStorageException(resume.getUuid());
        }
        storage.set(storage.indexOf(resume), resume);
    }

    @Override
    public Resume get(String uuid) {
        Optional<Resume> optionalResume = getOptionalResume(uuid);
        if (optionalResume.isEmpty()) {
            throw new NotExistStorageException(uuid);
        }
        return optionalResume.get();
    }

    private Optional<Resume> getOptionalResume(String uuid) {
        return storage.stream()
                .filter(currentResume -> uuid.equals(currentResume.getUuid()))
                .findFirst();
    }
}
