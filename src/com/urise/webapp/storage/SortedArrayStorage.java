package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    protected void saveToArray(Resume resume, int index) {
        int indexForNewResume = -index - 1;
        System.arraycopy(storage, indexForNewResume, storage, indexForNewResume + 1, size - indexForNewResume);
        storage[indexForNewResume] = resume;
    }

    @Override
    protected int findIndex(String uuid) {
        Resume searchResume = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchResume);
    }
}
