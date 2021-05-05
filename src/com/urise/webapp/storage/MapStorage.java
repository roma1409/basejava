package com.urise.webapp.storage;

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
    protected boolean checkResumePresence(Resume resume) {
        return storage.containsValue(resume);
    }

    @Override
    protected void addToStorage(Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Object getIndexOrOptional(String uuid) {
        return null;
    }

    @Override
    protected boolean checkResumeAbsence(Object indexOrOptional, String uuid) {
        return !storage.containsKey(uuid);
    }

    @Override
    protected void removeFromStorage(Object indexOrOptional, String uuid) {
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
    protected void updateInStorage(Object indexOrOptional, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getFromStorage(Object indexOrOptional, String uuid) {
        return storage.get(uuid);
    }
}
