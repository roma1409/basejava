package ru.javawebinar.basejava;

public class Deadlock {
    private final static String lock1 = "lock1";
    private final static String lock2 = "lock2";

    public static void main(String[] args) {
        new Thread(() -> ownRun(lock1, lock2)).start();
        new Thread(() -> ownRun(lock2, lock1)).start();
    }

    private static void ownRun(String lock1, String lock2) {
        System.out.printf("%s tries to capture %s%n", getCurrentThreadName(), lock1);
        synchronized (lock1) {
            System.out.printf("%s has captured %s%n", getCurrentThreadName(), lock1);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s tries to capture %s%n", getCurrentThreadName(), lock2);
            synchronized (lock2) {
                System.out.printf("%s has captured %s%n", getCurrentThreadName(), lock2);
            }
        }
    }

    private static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }
}
