-- ============================================================
-- Smart Campus Event Management and Student Participation
-- Analysis System
-- File: schema.sql
-- Purpose: Creates the database and the three tables.
-- Run this file FIRST in MySQL (MySQL 8.0.16 or later).
-- It can be run again safely: old tables are dropped first.
-- ============================================================

CREATE DATABASE IF NOT EXISTS smart_campus_db;
USE smart_campus_db;

-- Drop old tables. Child table (REGISTRATION) first, then parents.
DROP TABLE IF EXISTS REGISTRATION;
DROP TABLE IF EXISTS EVENT;
DROP TABLE IF EXISTS STUDENT;

-- ------------------------------------------------------------
-- STUDENT table
-- ------------------------------------------------------------
CREATE TABLE STUDENT (
    student_id     VARCHAR(10)  NOT NULL,
    student_name   VARCHAR(100) NOT NULL,
    department     VARCHAR(50)  NOT NULL,
    year_of_study  INT          NOT NULL,
    CONSTRAINT pk_student PRIMARY KEY (student_id),
    CONSTRAINT chk_year CHECK (year_of_study BETWEEN 1 AND 4)
);

-- ------------------------------------------------------------
-- EVENT table
-- ------------------------------------------------------------
CREATE TABLE EVENT (
    event_id          INT          NOT NULL AUTO_INCREMENT,
    event_name        VARCHAR(100) NOT NULL,
    event_date        DATE         NOT NULL,
    venue             VARCHAR(100) NOT NULL,
    max_participants  INT          NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (event_id),
    CONSTRAINT chk_max CHECK (max_participants > 0)
);

-- ------------------------------------------------------------
-- REGISTRATION table (links STUDENT and EVENT)
-- ------------------------------------------------------------
CREATE TABLE REGISTRATION (
    registration_id       INT         NOT NULL AUTO_INCREMENT,
    student_id            VARCHAR(10) NOT NULL,
    event_id              INT         NOT NULL,
    registration_date     DATE        NOT NULL DEFAULT (CURRENT_DATE),
    attendance_status     VARCHAR(10) NOT NULL DEFAULT 'Absent',
    participation_status  VARCHAR(15) NOT NULL DEFAULT 'Registered',
    CONSTRAINT pk_registration PRIMARY KEY (registration_id),
    CONSTRAINT uq_student_event UNIQUE (student_id, event_id),
    CONSTRAINT fk_reg_student FOREIGN KEY (student_id)
        REFERENCES STUDENT (student_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_reg_event FOREIGN KEY (event_id)
        REFERENCES EVENT (event_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_attendance CHECK (attendance_status IN ('Present', 'Absent')),
    CONSTRAINT chk_participation CHECK (participation_status IN ('Registered', 'Participated', 'Winner', 'Runner-up'))
);
