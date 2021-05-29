package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DataSerializationStrategy implements SerializationStrategy {
    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            writeCollection(dos, r.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            writeCollection(dos, r.getSections().entrySet(), entry -> writeSection(dos, entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();

            Resume resume = new Resume(uuid, fullName);

            readCollection(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readCollection(dis, () -> {
                        SectionType sectionType = SectionType.valueOf(dis.readUTF());
                        AbstractSection sectionValue = readSection(dis, sectionType);

                        if (!Objects.isNull(sectionValue)) {
                            resume.addSection(sectionType, sectionValue);
                        }
                    }
            );

            return resume;
        }
    }

    private void writeSection(DataOutputStream dos, SectionType sectionType, AbstractSection sectionValue) throws IOException {
        dos.writeUTF(sectionType.name());
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                dos.writeUTF(((TextSection)sectionValue).getContent());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                writeCollection(dos, ((ListSection) sectionValue).getItems(), dos::writeUTF);
                break;
            case EXPERIENCE:
            case EDUCATION:
                writeCollection(dos, ((OrganizationSection) sectionValue).getOrganizations(), organization -> {
                    Link link = organization.getHomePage();
                    dos.writeUTF(link.getName());
                    dos.writeUTF(link.getUrl());

                    writeCollection(dos, organization.getPositions(), position -> {
                        writeDate(dos, position.getStartDate());
                        writeDate(dos, position.getEndDate());
                        dos.writeUTF(position.getTitle());
                        dos.writeUTF(position.getDescription());
                    });
                });
                break;
        }
    }

    private void writeDate(DataOutputStream dos, LocalDate startDate) throws IOException {
        dos.writeInt(startDate.getYear());
        dos.writeInt(startDate.getMonthValue());
    }

    private interface Writer<T> {
        void write(T item) throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            writer.write(item);
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        AbstractSection sectionValue = null;
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                sectionValue = new TextSection(dis.readUTF());
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> strings = new ArrayList<>();
                readCollection(dis, () -> strings.add(dis.readUTF()));
                sectionValue = new ListSection(strings);
                break;
            case EXPERIENCE:
            case EDUCATION:
                List<Organization> organizations = new ArrayList<>();
                readCollection(dis, () -> {
                    Link link = new Link(dis.readUTF(), dis.readUTF());
                    List<Organization.Position> positions = new ArrayList<>();
                    readCollection(dis, () -> {
                        LocalDate startDate = readDate(dis);
                        LocalDate endDate = readDate(dis);
                        String title = dis.readUTF();
                        String description = dis.readUTF();
                        positions.add(new Organization.Position(startDate, endDate, title, description));
                    });
                    organizations.add(new Organization(link, positions));
                });

                sectionValue = new OrganizationSection(organizations);
                break;
        }
        return sectionValue;
    }

    private interface Executor {
        void execute() throws IOException;
    }

    private void readCollection(DataInputStream dis, Executor executor) throws IOException {
        int quantity = dis.readInt();
        for (int i = 0; i < quantity; i++) {
            executor.execute();
        }
    }

    private LocalDate readDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }
}
