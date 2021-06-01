package ru.javawebinar.basejava;

public class Deadlock extends Thread {
    private final Object lock1;
    private final Object lock2;

    public Deadlock(Object lock1, Object lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();

        Thread thread1 = new Deadlock(lock1, lock2);
        Thread thread2 = new Deadlock(lock2, lock1);

        thread1.start();
        thread2.start();
    }

    @Override
    public void run() {
        synchronized (lock1) {
            sleep();
            synchronized (lock2) {
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
