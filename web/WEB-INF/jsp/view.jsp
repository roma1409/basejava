<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>

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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.AbstractSection>"/>
            <c:set var="type" value="${sectionEntry.getKey()}"/>
            <c:set var="value" value="${sectionEntry.getValue()}"/>
    <h3>${type.getTitle()} :</h3>
    <c:choose>
        <c:when test="${type.equals(SectionType.PERSONAL) || type.equals(SectionType.OBJECTIVE)}">
            ${value}
        </c:when>
        <c:when test="${type.equals(SectionType.QUALIFICATIONS) || type.equals(SectionType.ACHIEVEMENT)}">
            <c:set var="items" value="${value.getItems()}"/>
            <c:if test="${items.size() > 0}">
                <ul>
                    <c:forEach var="item" items="${items}">
                        <li>${item}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </c:when>
        <c:when test="${type.equals(SectionType.EXPERIENCE) || type.equals(SectionType.EDUCATION)}">
            <c:set var="items" value="${value.getOrganizations()}"/>
            <c:if test="${items.size() > 0}">
                <ul>
                    <c:forEach var="item" items="${items}">
                        <li>
                            <c:set var="homePage" value="${item.getHomePage()}"/>
                            <c:set var="url" value="${homePage.getUrl()}"/>
                            <c:set var="urlPresence" value="${url.isBlank()}"/>
                            <c:set var="organizationName" value="${homePage.getName()}"/>
                            <h4>
                                <c:choose>
                                    <c:when test="${urlPresence}">
                                        <a href="${url}">${organizationName}</a>
                                    </c:when>
                                    <c:when test="${!urlPresence}">
                                        ${organizationName}
                                    </c:when>
                                </c:choose>
                            </h4>
                            <ul>
                                <c:forEach var="position" items="${item.getPositions()}">
                                    <li>
                                        <c:set var="startDate" value="${position.getStartDate()}"/>
                                        <c:set var="endDate" value="${position.getEndDate()}"/>
                                        <c:set var="title" value="${position.getTitle()}"/>
                                        <c:set var="description" value="${position.getDescription()}"/>
                                            ${startDate} - ${endDate.equals(DateUtil.NOW) ? "Настоящее время" : endDate}
                                        <b>${title}</b> ${description}
                                    </li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </c:when>
    </c:choose>
    </c:forEach>
    </p>
</section>
<jsp:include page="fragment/footer.jsp"/>
</body>
</html>
