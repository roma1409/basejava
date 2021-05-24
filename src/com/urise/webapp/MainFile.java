package com.urise.webapp;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainFile {
    public static void main(String[] args) throws IOException {
        showFiles(".");
    }

    public static void showFiles(String path) throws IOException {
        File dir = new File(path);
        File[] files = dir.listFiles();

        if (Objects.nonNull(files)) {
            for (File file : files) {
                System.out.println(file.getName());

                if (file.isDirectory()) {
                    showFiles(file.getCanonicalPath());
                }
            }
        }
    }
}
