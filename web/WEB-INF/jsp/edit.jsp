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
                            <button data-type="${type}" class="add-organization-button-js" type="button">
                                Add new organization
                            </button>
                            <br>
                            <c:forEach var="organization" items="${organizations}" varStatus="counter">
                                <c:if test="${counter.index > 0}">
                                    <hr>
                                </c:if>
                                <c:set var="homePage" value="${organization.getHomePage()}"/>
                                <c:set var="organizationName" value="${homePage.getName()}"/>
                                <c:set var="url" value="${homePage.getUrl()}"/>
                                Название: <input type="text" size="80" value="${organizationName}" name="${type}">
                                <br>
                                Ссылка: <input type="text" size="80" value="${url}" class="link-input"
                                               name="${type}-url">
                                <br>
                                <button data-type="${type}" data-index="${counter.index}"
                                        class="add-position-button-js" type="button">
                                    Add new position
                                </button>
                                <br>
                                <c:forEach var="position" items="${organization.getPositions()}">
                                    <c:set var="startDate" value="${position.getStartDate()}"/>
                                    <c:set var="endDate" value="${position.getEndDate()}"/>
                                    <c:set var="title" value="${position.getTitle()}"/>
                                    <c:set var="description" value="${position.getDescription()}"/>
                                    Начало работы: <input type="text" value="${DateUtil.format(startDate)}"
                                                          placeholder="mm/yyyy"
                                                          name="${type}-${counter.index}-startDate">
                                    Окончание: <input type="text" value="${DateUtil.format(endDate)}"
                                                      name="${type}-${counter.index}-endDate" placeholder="mm/yyyy">
                                    <br>
                                    Заголовок: <input type="text" size="80" value="${title}"
                                                      name="${type}-${counter.index}-title">
                                    <br>
                                    Описание: <textarea cols="82" rows="5"
                                                        name="${type}-${counter.index}-description">${description}</textarea>
                                    <br>
                                </c:forEach>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                    <span id="end-dd-js"></span>
                </dd>
            </dl>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragment/footer.jsp"/>
<template id="position-template">
    <br>Начало работы: <input type="text" name="-startDate" placeholder="mm/yyyy">
    Окончание: <input type="text" name="-endDate" placeholder="mm/yyyy">
    <br>
    Заголовок: <input type="text" size="80" name="-title">
    <br>
    Описание: <textarea cols="82" rows="5" name="-description"></textarea>
    <br>
</template>
<template id="organization-template">
    <hr>
    <br>Название: <input type="text" size="80" name="-name">
    <br>
    Ссылка: <input type="text" size="80" class="link-input" name="-url">
    <br>
    <button class="add-position-button-js" type="button">Add new position</button>
    <br>
</template>
<script>
    const positionTemplate = document.querySelector("#position-template");
    const organizationTemplate = document.querySelector("#organization-template");

    const onAddPositionButtonClick = evt => {
        const newPositionElement = positionTemplate.content.cloneNode(true);
        const type = evt.target.dataset.type;
        const index = evt.target.dataset.index;
        const prefix = type + "-" + index;
        newPositionElement.querySelector("[name='-startDate']").name = prefix + "-startDate";
        newPositionElement.querySelector("[name='-endDate']").name = prefix + "-endDate";
        newPositionElement.querySelector("[name='-title']").name = prefix + "-title";
        newPositionElement.querySelector("[name='-description']").name = prefix + "-description";
        evt.target.parentNode.insertBefore(newPositionElement, evt.target.nextSibling);
    };

    document.querySelectorAll(".add-position-button-js")
        .forEach(link => link.addEventListener("click", onAddPositionButtonClick));

    document.querySelectorAll(".add-organization-button-js")
        .forEach(link => link.addEventListener("click", evt => {
            const newOrganizationElement = organizationTemplate.content.cloneNode(true);
            const type = evt.target.dataset.type;
            newOrganizationElement.querySelector("[name='-name']").name = type;
            newOrganizationElement.querySelector("[name='-url']").name = type + "-url";
            const button = newOrganizationElement.querySelector("[type='button']");
            button.dataset.type = type;
            const index = document.querySelectorAll("input[name='" + type + "']").length + "";
            button.dataset.index = index;
            button.addEventListener("click", onAddPositionButtonClick);
            evt.target.parentNode.insertBefore(newOrganizationElement, document.querySelector("#evt.target.nextSibling"));
            button.click();
            document.querySelector("textarea[name='" + type + "-" + index + "-description']").focus();
        }))
</script>
</body>
</html>
