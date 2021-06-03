package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainStreams {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9, 8}));

        System.out.println(oddOrEven(Arrays.asList(1, 2, 3, 4, 5)));
        System.out.println(oddOrEven(Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
    }

    /**
     * реализовать метод через стрим int minValue(int[] values).
     * Метод принимает массив цифр от 1 до 9, надо выбрать уникальные и вернуть минимально возможное число,
     * составленное из этих уникальных цифр. Не использовать преобразование в строку и обратно. Например {1,2,3,3,2,3}
     * вернет 123, а {9,8} вернет 89
     */
    static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (acc, x) -> acc * 10 + x);
    }

    /**
     * реализовать метод List<Integer> oddOrEven(List<Integer> integers) если сумма всех чисел нечетная - удалить все нечетные,
     * если четная - удалить все четные. Сложность алгоритма должна быть O(N). Optional - решение в один стрим.
     */
    static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream()
                .mapToInt(Integer::intValue)
                .sum();
        Predicate<Integer> predicate = (sum % 2 == 0) ? (x -> x % 2 == 0) : (x -> x % 2 != 0);
        return integers.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
