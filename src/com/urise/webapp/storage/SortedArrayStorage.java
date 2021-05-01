package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected void addNew(Resume resume) {
        int indexForNewResume = -Arrays.binarySearch(storage, 0, size, resume) - 1;
        System.arraycopy(storage, indexForNewResume, storage, indexForNewResume + 1, size - indexForNewResume);
        storage[indexForNewResume] = resume;
        size++;
    }

    @Override
    protected int findIndex(String uuid) {
        Resume searchResume = new Resume();
        searchResume.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchResume);
    }
}
