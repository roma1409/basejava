package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.Month;
import java.util.UUID;

public class TestData {
    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();

    public static final Resume R1;
    public static final Resume R2;
    public static final Resume R3;
    public static final Resume R4;

    static {
        R1 = ResumeTestData.createResume(UUID_1, "Name1");
        R2 = ResumeTestData.createResume(UUID_2, "Name2");
        R3 = ResumeTestData.createResume(UUID_3, "Name3");
        R4 = ResumeTestData.createResume(UUID_4, "Name4");
    }
}
