package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new TreeMap<>();

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void save(Resume resume) {
        if (storage.containsValue(resume)) {
            throw new ExistStorageException(resume.getUuid());
        }
        storage.put(resume.getUuid(), resume);
    }

    @Override
    public void delete(String uuid) {
        if (!storage.containsKey(uuid)) {
            throw new NotExistStorageException(uuid);
        }
        storage.remove(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        Resume[] resumes = storage.values().toArray(new Resume[0]);
        System.out.println(Arrays.toString(resumes));
        return resumes;
    }

    @Override
    public void update(Resume resume) {
        if (!storage.containsKey(resume.getUuid())) {
            throw new NotExistStorageException(resume.getUuid());
        }
        storage.put(resume.getUuid(), resume);
    }

    @Override
    public Resume get(String uuid) {
        if (!storage.containsKey(uuid)) {
            throw new NotExistStorageException(uuid);
        }
        return storage.get(uuid);
    }
}
