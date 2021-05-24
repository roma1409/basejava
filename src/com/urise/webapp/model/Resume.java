package com.urise.webapp.model;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

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

    public String getFullName() {
        return fullName;
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
        if (!contactsBuilder.isEmpty()) {
            contactsBuilder.insert(0, "\n\n");
        }
        StringBuilder sectionBuilder = new StringBuilder();
        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            sectionBuilder.append(entry.getKey()).append(": ");
            sectionBuilder.append(entry.getValue()).append("\n\n");
        }
        if (!sectionBuilder.isEmpty()) {
            sectionBuilder.setLength(sectionBuilder.length() - 2);
            sectionBuilder.insert(0, "\n");
        }
        return String.format("Resume: %s %s %s %s", fullName, uuid, contactsBuilder, sectionBuilder);
    }

    @Override
    public int compareTo(Resume other) {
        return Comparator.comparing((Resume r) -> r.fullName)
                .thenComparing(r -> r.uuid)
                .compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) && Objects.equals(fullName, resume.fullName) && Objects.equals(sections, resume.sections) && Objects.equals(contacts, resume.contacts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, sections, contacts);
    }
}
