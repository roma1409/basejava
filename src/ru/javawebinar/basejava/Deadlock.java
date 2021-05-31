package ru.javawebinar.basejava;

public class Deadlock {
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();

        Thread thread1 = new MyThread(lock1, lock2);
        Thread thread2 = new MyThread(lock2, lock1);

        thread1.start();
        thread2.start();
    }
}

class MyThread extends Thread {
    private final Object lock1;
    private final Object lock2;

    public MyThread(Object lock1, Object lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
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
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
