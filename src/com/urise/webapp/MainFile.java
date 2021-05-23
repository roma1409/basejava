package com.urise.webapp;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainFile {
    public static void main(String[] args) throws IOException {
        showFiles(".");
    }

    public static void showFiles(String dirName) throws IOException {
        File dir = new File(dirName);
        File[] files = dir.listFiles();

        if (Objects.nonNull(files)) {
            for (File file : files) {
                String fullName = file.getCanonicalPath();
                System.out.println(fullName);

                if (file.isDirectory()) {
                    showFiles(fullName);
                }
            }
        }
    }
}
