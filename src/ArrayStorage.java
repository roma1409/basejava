import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10_000];
    private int size = 0;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void save(Resume resume) {
        if (findIndex(resume.uuid) == -1) {
            storage[size++] = resume;
        }
    }

    Resume get(String uuid) {
        int index = findIndex(uuid);
        return index == -1 ? null : storage[index];
    }

    void delete(String uuid) {
        int index = findIndex(uuid);
        if (index != -1) {
            shiftArray(index);
        }
    }

    private int findIndex(String uuid) {
        return IntStream.range(0, size)
                .filter(i -> uuid.equals(storage[i].uuid))
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

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return size;
    }
}
