package com.urise.webapp.model;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {
    private final String uuid;
    private final String fullName;
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public void addSection(SectionType type, Section section) {
        sections.put(type, section);
    }

    public void removeSection(SectionType type) {
        sections.remove(type);
    }

    public void addContact(ContactType type, String contact) {
        contacts.put(type, contact);
    }

    public void removeContact(ContactType type) {
        contacts.remove(type);
    }


    @Override
    public String toString() {
        StringBuilder contactsBuilder = new StringBuilder();
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            contactsBuilder.append(entry.getKey()).append(": ");
            contactsBuilder.append(entry.getValue()).append("\n");
        }
        StringBuilder sectionBuilder = new StringBuilder();
        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            sectionBuilder.append(entry.getKey()).append(": ");
            sectionBuilder.append(entry.getValue()).append("\n\n");
        }
        sectionBuilder.setLength(sectionBuilder.length() - 2);

        return String.format("Resume: %s %s\n\n%s \n%s", fullName, uuid, contactsBuilder, sectionBuilder);
    }

    @Override
    public int compareTo(Resume other) {
        return Comparator.comparing((Resume r) -> r.fullName)
                .thenComparing(r -> r.uuid)
                .compare(this, other);
    }
}
