import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void save(Resume r) {
        if (get(r.uuid) == null) {
            storage[size++] = r;
        }
    }

    Resume get(String uuid) {
        Resume resume = null;
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].uuid)) {
                resume = storage[i];
                break;
            }
        }
        return resume;
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
