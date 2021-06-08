package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SqlStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private SqlStorage storage;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getSqlStorage();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("<div style='width:500px; margin:130px auto 0'>");
        List<Resume> resumes = storage.getAllSorted();
        writer.write(String.format("<h1>There are %d resumes:</h1>\n", resumes.size()));
        if (resumes.size() > 0) {
            writer.write("<table style='border-collapse:collapse;'>");
            writer.write("<tr>");
            writer.write("<th>Full Name</th>");
            writer.write("<th>Uuid</th>");
            writer.write("</tr>");
            for (Resume resume : resumes) {
                writer.write("<tr style='border:1px solid black;'>");
                writer.write(String.format("<td>%s</td>%n<td style='padding-left:50px;'>%s</td>", resume.getFullName(), resume.getUuid()));
                writer.write("</tr>");
            }
            writer.write("</table>");
            writer.write("</div>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
