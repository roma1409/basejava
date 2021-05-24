package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.lang.reflect.InvocationTargetException;

import static com.urise.webapp.ResumeTestData.createResume;

public class MainReflection {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println(Resume.class
                .getMethod("toString")
                .invoke(createResume("uuid1", "Vasya"))
        );
    }
}
