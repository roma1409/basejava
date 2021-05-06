package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> map = new HashMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doUpdate(Resume r, Object searchKey) {
        map.put(String.valueOf(searchKey), r);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return map.containsKey(String.valueOf(searchKey));
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        map.put(String.valueOf(searchKey), r);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return map.get(String.valueOf(searchKey));
    }

    @Override
    protected void doDelete(Object searchKey) {
        map.remove(String.valueOf(searchKey));
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Resume[] getAll() {
        return map.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return map.size();
    }
}
