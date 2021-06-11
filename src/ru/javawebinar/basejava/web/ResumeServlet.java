package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.SqlStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class ResumeServlet extends HttpServlet {
    private SqlStorage storage;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getSqlStorage();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

        if (Objects.isNull(action)) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }

        Resume resume;
        switch (action) {
            case "create":
                resume = new Resume("");
                break;
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                resume = storage.get(uuid);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
        request.setAttribute("resume", resume);

        String nextPath = Objects.equals("view", action) ? "WEB-INF/jsp/view.jsp" : "WEB-INF/jsp/edit.jsp";
        request.getRequestDispatcher(nextPath).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        boolean isCreationOperation = false;
        Resume resume;
        try {
            resume = storage.get(uuid);
        } catch (NotExistStorageException e) {
            resume = new Resume(uuid, fullName);
            isCreationOperation = true;
        }
        resume.setFullName(fullName);
        setContactsFromRequest(request, resume);
        setSectionsFromRequest(request, resume);

        if (isCreationOperation) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }

        response.sendRedirect("resume");
    }

    private void setContactsFromRequest(HttpServletRequest request, Resume resume) {
        for (ContactType contactType : ContactType.values()) {
            String contactValue = request.getParameter(contactType.name());
            if (Objects.isNull(contactValue) || contactValue.isBlank()) {
                resume.getContacts().remove(contactType);
            } else {
                resume.addContact(contactType, contactValue);
            }
        }
    }

    private void setSectionsFromRequest(HttpServletRequest request, Resume resume) {
        for (SectionType sectionType : SectionType.values()) {
            String sectionValue = request.getParameter(sectionType.name());
            AbstractSection section = null;
            if (Objects.isNull(sectionValue) || sectionValue.isBlank()) {
                resume.getSections().remove(sectionType);
            } else {
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        section = new TextSection(sectionValue);
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        String[] newListSectionValues = Arrays.stream(sectionValue.split("\n"))
                                .filter(str -> !str.isBlank())
                                .toArray(String[]::new);
                        section = new ListSection(newListSectionValues);
                        break;
                }
                if (Objects.nonNull(section)) {
                    resume.addSection(sectionType, section);
                }
            }
        }
    }
}
