package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * gkislin
 * 21.07.2016
 */
public class MainFile {
    public static void main(String[] args) {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/ru/javawebinar/basejava");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        printDirectoryDeeply(dir, "");
    }

    public static void printDirectoryDeeply(File dir, String tabulation) {
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                System.out.printf("%s%s%n", tabulation, file.getName());
                if (file.isDirectory()) {
                    printDirectoryDeeply(file, tabulation.concat("\t"));
                }
            }
        }
    }
}
