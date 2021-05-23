package com.urise.webapp;

import com.urise.webapp.model.*;

import java.time.YearMonth;

import static com.urise.webapp.model.ContactType.*;
import static com.urise.webapp.model.SectionType.*;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume grigoriy = init();
        System.out.println(grigoriy);
    }

    private static Resume init() {
        Resume grigoriy = new Resume("uud1", "Grigoriy Kislin");

        grigoriy.addContact(PHONE, "+7 999 999 99 99");
        grigoriy.addContact(SKYPE, "grigory.kislin");
        grigoriy.addContact(EMAIL, "gkislin@yandex.ru");
        grigoriy.addContact(LINKEDIN, "https://www.linkedin.com/in/gkislin");
        grigoriy.addContact(GITHUB, "https://github.com/gkislin");
        grigoriy.addContact(STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        grigoriy.addContact(HOME_PAGE, "http://gkislin.ru/");

        grigoriy.addSection(OBJECTIVE, new SingleLineSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));

        grigoriy.addSection(PERSONAL, new SingleLineSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        BulletedListSection achievements = new BulletedListSection();
        achievements.addItem("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        achievements.addItem("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievements.addItem("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        achievements.addItem("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievements.addItem("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
        achievements.addItem("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        grigoriy.addSection(ACHIEVEMENT, achievements);

        BulletedListSection qualifications = new BulletedListSection();
        qualifications.addItem("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualifications.addItem("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualifications.addItem("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle");
        qualifications.addItem("MySQL, SQLite, MS SQL, HSQLDB");
        qualifications.addItem("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        qualifications.addItem("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        qualifications.addItem("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements)");
        qualifications.addItem("Python: Django");
        qualifications.addItem("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        qualifications.addItem("cala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        qualifications.addItem("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT");
        qualifications.addItem("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
        qualifications.addItem("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer");
        qualifications.addItem("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования");
        qualifications.addItem("Родной русский, английский \"upper intermediate\"");
        grigoriy.addSection(QUALIFICATIONS, qualifications);

        PlaceSection experience = new PlaceSection();
        Place javaOps = new Place("Java Online Projects");
        javaOps.addPeriod(new Period(YearMonth.parse("2013-10"), "Автор проекта", "Создание, организация и проведение Java онлайн проектов и стажировок."));
        experience.addPlace(javaOps);
        Place wrike = new Place("Wrike");
        wrike.addPeriod(new Period(YearMonth.parse("2014-10"), YearMonth.parse("2016-01"), "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO"));
        experience.addPlace(wrike);
        Place rit = new Place("RIT Center");
        rit.addPeriod(new Period(YearMonth.parse("2012-04"), YearMonth.parse("2014-10"), "Java архитектор", "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"));
        experience.addPlace(rit);
        grigoriy.addSection(EXPERIENCE, experience);

        PlaceSection education = new PlaceSection();
        Place coursera = new Place("Coursera");
        coursera.addPeriod(new Period(YearMonth.parse("2013-03"), YearMonth.parse("2013-05"), "\"Functional Programming Principles in Scala\" by Martin Odersky"));
        education.addPlace(coursera);
        Place luxoft = new Place("Luxoft");
        luxoft.addPeriod(new Period(YearMonth.parse("2011-03"), YearMonth.parse("2011-04"), "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\""));
        education.addPlace(luxoft);
        Place siemensAg = new Place("Siemens AG");
        siemensAg.addPeriod(new Period(YearMonth.parse("2005-01"), YearMonth.parse("2005-04"), "3 месяца обучения мобильным IN сетям (Берлин)"));
        education.addPlace(siemensAg);
        Place alcatel = new Place("Alcatel");
        alcatel.addPeriod(new Period(YearMonth.parse("1997-09"), YearMonth.parse("1998-03"), "6 месяцев обучения цифровым телефонным сетям (Москва)"));
        education.addPlace(alcatel);
        Place itmo = new Place("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики");
        itmo.addPeriod(new Period(YearMonth.parse("1993-09"), YearMonth.parse("1996-07"), "Аспирантура (программист С, С++)"));
        itmo.addPeriod(new Period(YearMonth.parse("1987-09"), YearMonth.parse("1993-07"), "Инженер (программист Fortran, C)"));
        education.addPlace(itmo);
        Place mfti = new Place("Заочная физико-техническая школа при МФТИ");
        mfti.addPeriod(new Period(YearMonth.parse("1984-09"), YearMonth.parse("1987-06"), "Закончил с отличием"));
        education.addPlace(mfti);
        grigoriy.addSection(EDUCATION, education);

        return grigoriy;
    }
}
