package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

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
        R1 = new Resume(UUID_1, "Name1");
        R1.addContact(ContactType.PHONE, "921-555-55-55");
        R1.addContact(ContactType.SKYPE, "skype-nick");
        R1.addContact(ContactType.MAIL, "mail@my.com");

        R1.addSection(SectionType.PERSONAL, new TextSection("personal"));
        R1.addSection(SectionType.OBJECTIVE, new TextSection("fullstack developer"));

        R1.addSection(SectionType.ACHIEVEMENT, new ListSection("achievement1", "achievement2", "achievement3"));
        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("qualification1", "qualification2", "qualification3"));

        R2 = new Resume(UUID_2, "Name2");
        R3 = new Resume(UUID_3, "Name3");
        R4 = new Resume(UUID_4, "Name4");
    }
}
