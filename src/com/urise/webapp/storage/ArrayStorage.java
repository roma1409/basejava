package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.stream.IntStream;

public class ArrayStorage extends AbstractArrayStorage {
    @Override
    protected void saveToArray(Resume resume, int index) {
        storage[size] = resume;
    }

    @Override
    protected int findIndex(String uuid) {
        return IntStream.range(0, size)
                .filter(i -> uuid.equals(storage[i].getUuid()))
                .findFirst()
                .orElse(-1);
    }
}
