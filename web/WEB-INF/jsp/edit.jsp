<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>

<% pageContext.setAttribute("newLineChar", "\n"); %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragment/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}" required></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=50 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <dl>
                <dt>${type.title}</dt>
                <dd>
                    <c:choose>
                        <c:when test="${type eq SectionType.OBJECTIVE}">
                            <input type="text" name="${type.name()}" size=80 value="${section}">
                        </c:when>
                        <c:when test="${type eq SectionType.PERSONAL}">
                            <textarea name="${type.name()}" cols="85" rows="3">${section}</textarea>
                        </c:when>
                        <c:when test="${type eq SectionType.QUALIFICATIONS || type eq SectionType.ACHIEVEMENT}">
                            <c:set var="strings" value="${fn:join(section.getItems().toArray(), newLineChar)}"/>
                            <textarea cols="85" rows="10" name="${type.name()}">${strings}</textarea>
                        </c:when>
                        <c:when test="${type eq SectionType.EXPERIENCE || type eq SectionType.EDUCATION}">
                            <c:set var="organizations" value="${section.getOrganizations()}"/>
                            <%--                            <script>--%>
                            <%--                                alert("${organizations.size()}")--%>
                            <%--                            </script>--%>
                            <c:forEach var="organization" items="${organizations}" varStatus="counter">
                                <c:if test="${counter.index > 0}">
                                    <hr>
                                </c:if>
                                <c:set var="homePage" value="${organization.getHomePage()}"/>
                                <c:set var="organizationName" value="${homePage.getName()}"/>
                                <c:set var="url" value="${homePage.getUrl()}"/>
                                Название: <input type="text" size="80" value="${organizationName}" name="${type}"
                                                 required>
                                <br>
                                Ссылка: <input type="text" size="80" value="${url}" class="link-input"
                                               name="${type}-url">
                                <br>
                                <c:forEach var="position" items="${organization.getPositions()}">
                                    <c:set var="startDate" value="${position.getStartDate()}"/>
                                    <c:set var="endDate" value="${position.getEndDate()}"/>
                                    <c:set var="title" value="${position.getTitle()}"/>
                                    <c:set var="description" value="${position.getDescription()}"/>
                                    Начало работы: <input type="text" value="${DateUtil.format(startDate)}"
                                                          required
                                                          name="${type}-${counter.index}-startDate">
                                    Окончание: <input type="text" value="${DateUtil.format(endDate)}"
                                                      name="${type}-${counter.index}-endDate">
                                    <br>
                                    Заголовок: <input type="text" size="80" value="${title}"
                                                      name="${type}-${counter.index}-title" required>
                                    <br>
                                    Описание: <textarea cols="82" rows="5"
                                                        name="${type}-${counter.index}-description">${description}</textarea>
                                    <br>
                                </c:forEach>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </dd>
            </dl>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragment/footer.jsp"/>
</body>
</html>
