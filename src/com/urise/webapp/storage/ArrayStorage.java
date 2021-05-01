package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ArrayStorage extends AbstractArrayStorage {
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
    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            showAbsenceResumeError("DELETE", uuid);
        } else {
            shiftArray(index);
        }
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected int findIndex(String uuid) {
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
}
