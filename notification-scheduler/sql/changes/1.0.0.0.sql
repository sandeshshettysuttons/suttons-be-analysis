-- --------------------------------
--  Notification Tables
-- --------------------------------

CREATE TABLE notification.company (
    id                BIGINT NOT NULL AUTO_INCREMENT,
    CREATEDBY         BIGINT NOT NULL,
    CREATIONTS        DATETIME NOT NULL,
    LASTUPDATETS      DATETIME NOT NULL,
    LASTUPDATEDBY     BIGINT NOT NULL,
    VERSION           BIGINT DEFAULT 1 NOT NULL,
    NAME              VARCHAR(200) NOT NULL,
    STATUS            VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE notification.emaillog (
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    createdby           BIGINT NOT NULL,
    creationts          DATETIME NOT NULL,
    lastupdatets        DATETIME NOT NULL,
    lastupdatedby       BIGINT NOT NULL,
    version             BIGINT DEFAULT 1 NOT NULL,
    status              VARCHAR(20) NOT NULL,
    mailrecipient_id    BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE notification.emaillogemployee (
    id                BIGINT NOT NULL AUTO_INCREMENT,
    createdby         BIGINT NOT NULL,
    creationts        DATETIME NOT NULL,
    lastupdatets      DATETIME NOT NULL,
    lastupdatedby     BIGINT NOT NULL,
    version           BIGINT DEFAULT 1 NOT NULL,
    terminationdate   DATE,
    emaillog_id       BIGINT NOT NULL,
    employee_id       BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE notification.employee (
    id                BIGINT NOT NULL AUTO_INCREMENT,
    createdby         BIGINT NOT NULL,
    creationts        DATETIME NOT NULL,
    lastupdatets      DATETIME NOT NULL,
    lastupdatedby     BIGINT NOT NULL,
    version           BIGINT DEFAULT 1 NOT NULL,
    description       VARCHAR(100),
    employeenumber    VARCHAR(20),
    firstname         VARCHAR(50),
    lastname          VARCHAR(50),
    position          VARCHAR(50),
    status            VARCHAR(20) NOT NULL,
    terminationdate   DATE,
    company_id        BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE notification.employeefile (
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    createdby           BIGINT NOT NULL,
    creationts          DATETIME NOT NULL,
    lastupdatets        DATETIME NOT NULL,
    lastupdatedby       BIGINT NOT NULL,
    version             BIGINT DEFAULT 1 NOT NULL,
    fileurl             VARCHAR(255) NOT NULL,
    filename            VARCHAR(50) NOT NULL,
    status              VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE notification.employeefiledetail (
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    createdby           BIGINT NOT NULL,
    creationts          DATETIME NOT NULL,
    lastupdatets        DATETIME NOT NULL,
    lastupdatedby       BIGINT NOT NULL,
    version             BIGINT DEFAULT 1 NOT NULL,
    description         VARCHAR(100),
    employeenumber      VARCHAR(20),
    firstname           VARCHAR(50),
    lastname            VARCHAR(50),
    position            VARCHAR(50),
    status              VARCHAR(20) NOT NULL,
    terminationdate     DATE,
    employeefile_id     BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE notification.mailrecipient (
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    createdby           BIGINT NOT NULL,
    creationts          DATETIME NOT NULL,
    lastupdatets        DATETIME NOT NULL,
    lastupdatedby       BIGINT NOT NULL,
    version             BIGINT DEFAULT 1 NOT NULL,
    email               VARCHAR(100) NOT NULL,
    name                VARCHAR(100),
    type                VARCHAR(20) NOT NULL,
    status              VARCHAR(20) NOT NULL,
    company_id          BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE notification.terminationnotificationconfig (
    id                    BIGINT NOT NULL AUTO_INCREMENT,
    createdby             BIGINT NOT NULL,
    creationts            DATETIME NOT NULL,
    lastupdatets          DATETIME NOT NULL,
    lastupdatedby         BIGINT NOT NULL,
    version               BIGINT DEFAULT 1 NOT NULL,
    daystoterminationfrom BIGINT NOT NULL,
    daystoterminationto   BIGINT NOT NULL,
    label                 VARCHAR(100) NOT NULL,
    type                  VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE notification.emaillog
    ADD CONSTRAINT fk_emaillog_mailrecipient
    FOREIGN KEY (mailrecipient_id) REFERENCES notification.mailrecipient (id);

ALTER TABLE notification.emaillogemployee
    ADD CONSTRAINT fk_emaillogemployee_emaillog
    FOREIGN KEY (emaillog_id) REFERENCES notification.emaillog (id);

ALTER TABLE notification.emaillogemployee
    ADD CONSTRAINT fk_emaillogemployee_employee
    FOREIGN KEY (employee_id) REFERENCES notification.employee (id);

ALTER TABLE notification.employee
    ADD CONSTRAINT fk_employee_company
    FOREIGN KEY (company_id) REFERENCES notification.company (id);

ALTER TABLE notification.employeefiledetail
    ADD CONSTRAINT fk_employeefiledetail_employeefile
    FOREIGN KEY (employeefile_id) REFERENCES notification.employeefile (id);

ALTER TABLE notification.mailrecipient
    ADD CONSTRAINT fk_mailrecipient_company
    FOREIGN KEY (company_id) REFERENCES notification.company (id);

