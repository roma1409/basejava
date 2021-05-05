package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    public abstract int size();

    public abstract void save(Resume resume);

    public abstract void delete(String uuid);

    public abstract void clear();

    public abstract Resume[] getAll();

    public abstract void update(Resume resume);

    public abstract Resume get(String uuid);
}
