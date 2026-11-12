-- ============================================================
-- Smart Campus Event Management and Student Participation
-- Analysis System
-- File: queries.sql
-- Purpose: SELECT, JOIN, aggregate, and report queries.
-- Run this file THIRD, after schema.sql and sample_data.sql.
-- Each query has a comment that explains what it does.
-- ============================================================

USE smart_campus_db;

-- ------------------------------------------------------------
-- 1. SELECT: list all students
-- ------------------------------------------------------------
SELECT student_id, student_name, department, year_of_study
FROM STUDENT
ORDER BY student_id;

-- ------------------------------------------------------------
-- 2. SELECT: list all events
-- ------------------------------------------------------------
SELECT event_id, event_name, event_date, venue, max_participants
FROM EVENT
ORDER BY event_date;

-- ------------------------------------------------------------
-- 3. SELECT with WHERE: students in one department
-- ------------------------------------------------------------
SELECT student_id, student_name, year_of_study
FROM STUDENT
WHERE department = 'Information Technology'
ORDER BY student_name;

-- ------------------------------------------------------------
-- 4. Duplicate check (used by RegistrationDAO.isDuplicate)
-- Returns 1 if the student is already registered, 0 if not.
-- ------------------------------------------------------------
SELECT COUNT(*) AS already_registered
FROM REGISTRATION
WHERE student_id = '25IT001' AND event_id = 1;

-- ------------------------------------------------------------
-- 5. Seats left for one event (used by RegistrationDAO.hasSeatsLeft)
-- ------------------------------------------------------------
SELECT e.event_name,
       e.max_participants,
       COUNT(r.registration_id) AS registered_count,
       e.max_participants - COUNT(r.registration_id) AS seats_left
FROM EVENT e
LEFT JOIN REGISTRATION r ON e.event_id = r.event_id
WHERE e.event_id = 2
GROUP BY e.event_id, e.event_name, e.max_participants;

-- ------------------------------------------------------------
-- 6. JOIN: all registrations with student name and event name
-- (INNER JOIN across the three tables)
-- ------------------------------------------------------------
SELECT r.registration_id,
       s.student_name,
       s.department,
       e.event_name,
       e.event_date,
       r.attendance_status,
       r.participation_status
FROM REGISTRATION r
INNER JOIN STUDENT s ON r.student_id = s.student_id
INNER JOIN EVENT e   ON r.event_id = e.event_id
ORDER BY e.event_date, s.student_name;

-- ------------------------------------------------------------
-- 7. JOIN: registered participants for one event
-- (used by RegistrationDAO.getParticipantsByEvent and View Registered Participants)
-- ------------------------------------------------------------
SELECT s.student_id,
       s.student_name,
       s.department,
       r.attendance_status,
       r.participation_status
FROM REGISTRATION r
INNER JOIN STUDENT s ON r.student_id = s.student_id
WHERE r.event_id = 1
ORDER BY s.student_name;

-- ------------------------------------------------------------
-- 8. Aggregate: total registrations for each event
-- LEFT JOIN keeps events with zero registrations (Web Development Seminar shows 0).
-- ------------------------------------------------------------
SELECT e.event_name,
       COUNT(r.registration_id) AS total_registrations
FROM EVENT e
LEFT JOIN REGISTRATION r ON e.event_id = r.event_id
GROUP BY e.event_id, e.event_name
ORDER BY total_registrations DESC, e.event_name;

-- ------------------------------------------------------------
-- 9. EVENT-WISE PARTICIPANT REPORT (used by RegistrationDAO.getEventReport)
-- Shows, for each event: registered count, attended count, and seats left.
-- ------------------------------------------------------------
SELECT e.event_id,
       e.event_name,
       e.venue,
       e.max_participants,
       COUNT(r.registration_id) AS registered_count,
       SUM(CASE WHEN r.attendance_status = 'Present' THEN 1 ELSE 0 END) AS attended_count,
       e.max_participants - COUNT(r.registration_id) AS seats_left
FROM EVENT e
LEFT JOIN REGISTRATION r ON e.event_id = r.event_id
GROUP BY e.event_id, e.event_name, e.venue, e.max_participants
ORDER BY e.event_id;

-- ------------------------------------------------------------
-- 10. STUDENT PARTICIPATION REPORT
-- For each student: events registered, events attended, and prizes won.
-- ------------------------------------------------------------
SELECT s.student_id,
       s.student_name,
       s.department,
       COUNT(r.registration_id) AS events_registered,
       SUM(CASE WHEN r.attendance_status = 'Present' THEN 1 ELSE 0 END) AS events_attended,
       SUM(CASE WHEN r.participation_status IN ('Winner', 'Runner-up') THEN 1 ELSE 0 END) AS prizes_won
FROM STUDENT s
LEFT JOIN REGISTRATION r ON s.student_id = r.student_id
GROUP BY s.student_id, s.student_name, s.department
ORDER BY events_registered DESC, s.student_name;

-- ------------------------------------------------------------
-- 11. Department-wise participation (used by Python chart 2)
-- ------------------------------------------------------------
SELECT s.department,
       COUNT(r.registration_id) AS total_registrations
FROM STUDENT s
LEFT JOIN REGISTRATION r ON s.student_id = r.student_id
GROUP BY s.department
ORDER BY total_registrations DESC;

-- ------------------------------------------------------------
-- 12. Attendance status distribution (used by Python chart 3)
-- ------------------------------------------------------------
SELECT attendance_status,
       COUNT(*) AS total
FROM REGISTRATION
GROUP BY attendance_status;

-- ------------------------------------------------------------
-- 13. Participation status counts (used by Python chart 4)
-- ------------------------------------------------------------
SELECT participation_status,
       COUNT(*) AS total
FROM REGISTRATION
GROUP BY participation_status
ORDER BY total DESC;

-- ------------------------------------------------------------
-- 14. Year-wise participation (used by Python chart 5)
-- ------------------------------------------------------------
SELECT s.year_of_study,
       COUNT(r.registration_id) AS total_registrations
FROM STUDENT s
LEFT JOIN REGISTRATION r ON s.student_id = r.student_id
GROUP BY s.year_of_study
ORDER BY s.year_of_study;

-- ------------------------------------------------------------
-- 15. Top 3 events by registrations (used by Python chart 6)
-- ------------------------------------------------------------
SELECT e.event_name,
       COUNT(r.registration_id) AS total_registrations
FROM EVENT e
LEFT JOIN REGISTRATION r ON e.event_id = r.event_id
GROUP BY e.event_id, e.event_name
ORDER BY total_registrations DESC
LIMIT 3;

-- ------------------------------------------------------------
-- 16. Event-wise registration count (used by Python chart 1)
-- ------------------------------------------------------------
SELECT e.event_name,
       COUNT(r.registration_id) AS total_registrations
FROM EVENT e
LEFT JOIN REGISTRATION r ON e.event_id = r.event_id
GROUP BY e.event_id, e.event_name
ORDER BY e.event_id;

-- NOTE: The last three statements (one INSERT and two UPDATEs) were removed.
-- They change the sample data, so the expected results in docs/Testing.md would not match.
-- The Java program (menu options 6, 8 and 9) does the same work.
