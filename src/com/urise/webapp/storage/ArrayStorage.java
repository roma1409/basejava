package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage implements Storage {
    private final Resume[] storage = new Resume[10_000];
    private int size = 0;

    @Override
    public void update(Resume resume) {
        int index = findIndex(resume.getUuid());
        if (index == -1) {
            showAbsenceResumeError("UPDATE", resume.getUuid());
        } else {
            storage[index] = resume;
        }
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void save(Resume resume) {
        boolean isResumeNew = findIndex(resume.getUuid()) == -1;
        if (!isResumeNew) {
            System.out.printf("You can't 'SAVE' resume due to there is already a resume with this uuid: '%s'.%n", resume.getUuid());
        }
        boolean isEnoughSpaceInStorage = size < storage.length;
        if (!isEnoughSpaceInStorage) {
            System.out.println("You can't 'SAVE' resume due to there isn't enough space to add any new resume.");
        }

        if (isResumeNew && isEnoughSpaceInStorage) {
            storage[size++] = resume;
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            showAbsenceResumeError("GET", uuid);
            return null;
        }
        return storage[index];
    }

    @Override
    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            showAbsenceResumeError("DELETE", uuid);
        } else {
            shiftArray(index);
        }
    }

    private void showAbsenceResumeError(String operation, String uuid) {
        System.out.printf("You can't '%s' resume due to there isn't resume with this uuid: '%s'.%n", operation, uuid);
    }

    private int findIndex(String uuid) {
        return IntStream.range(0, size)
                .filter(i -> uuid.equals(storage[i].getUuid()))
                .findFirst()
                .orElse(-1);
    }

    private void shiftArray(int start) {
        int length = size - 1 - start;
        if (length > 0) {
            System.arraycopy(storage, start + 1, storage, start, length);
        }
        storage[--size] = null;
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public int size() {
        return size;
    }
}
