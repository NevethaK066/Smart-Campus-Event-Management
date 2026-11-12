-- ============================================================
-- Smart Campus Event Management and Student Participation
-- Analysis System
-- File: sample_data.sql
-- Purpose: Inserts sample students, events, and registrations.
-- Run this file SECOND, after schema.sql.
-- ============================================================

USE smart_campus_db;

-- ------------------------------------------------------------
-- STUDENT sample data (9 students, 3 departments)
-- ------------------------------------------------------------
INSERT INTO STUDENT (student_id, student_name, department, year_of_study) VALUES
('25IT001', 'Arun Kumar',       'Information Technology',                   2),
('25IT002', 'Priya Lakshmi',    'Information Technology',                   2),
('25IT003', 'Karthik Raja',     'Information Technology',                   2),
('25IT004', 'Divya Shree',      'Information Technology',                   2),
('25CS001', 'Mohamed Ali',      'Computer Science and Engineering',         2),
('25CS002', 'Sneha Rani',       'Computer Science and Engineering',         2),
('25CS003', 'Vignesh S',        'Computer Science and Engineering',         2),
('24EC001', 'Ravi Teja',        'Electronics and Communication Engineering', 3),
('24EC002', 'Kavya Nair',       'Electronics and Communication Engineering', 3);

-- ------------------------------------------------------------
-- EVENT sample data (5 events, past dates for demo)
-- event_id is created automatically: 1, 2, 3, 4, 5
-- ------------------------------------------------------------
INSERT INTO EVENT (event_name, event_date, venue, max_participants) VALUES
('AI Workshop',                 '2026-09-05', 'Seminar Hall A',     60),
('Code Sprint',                 '2026-09-18', 'Computer Lab 1',     30),
('Technical Paper Presentation','2026-10-02', 'Main Auditorium',    40),
('Cultural Night',              '2026-10-15', 'Open Air Theatre',  100),
('Web Development Seminar',     '2026-10-28', 'Seminar Hall B',     25);

-- ------------------------------------------------------------
-- REGISTRATION sample data (20 registrations)
-- registration_id is created automatically: 1 to 20
-- Event 5 has no registrations, to show zero counts in reports.
-- ------------------------------------------------------------
INSERT INTO REGISTRATION (student_id, event_id, registration_date, attendance_status, participation_status) VALUES
-- Event 1: AI Workshop
('25IT001', 1, '2026-08-28', 'Present', 'Participated'),
('25IT002', 1, '2026-08-29', 'Present', 'Runner-up'),
('25IT003', 1, '2026-08-30', 'Absent',  'Registered'),
('25CS001', 1, '2026-08-30', 'Present', 'Winner'),
('25CS002', 1, '2026-08-31', 'Absent',  'Registered'),
('24EC001', 1, '2026-09-01', 'Present', 'Participated'),
-- Event 2: Code Sprint
('25IT001', 2, '2026-09-10', 'Present', 'Winner'),
('25IT003', 2, '2026-09-10', 'Present', 'Participated'),
('25CS001', 2, '2026-09-11', 'Absent',  'Registered'),
('25CS003', 2, '2026-09-11', 'Present', 'Runner-up'),
('25IT004', 2, '2026-09-12', 'Present', 'Participated'),
-- Event 3: Technical Paper Presentation
('25IT002', 3, '2026-09-25', 'Present', 'Participated'),
('24EC002', 3, '2026-09-26', 'Absent',  'Registered'),
('24EC001', 3, '2026-09-27', 'Present', 'Winner'),
('25CS002', 3, '2026-09-27', 'Present', 'Participated'),
('25IT001', 3, '2026-09-28', 'Absent',  'Registered'),
-- Event 4: Cultural Night
('25IT002', 4, '2026-10-08', 'Present', 'Participated'),
('25IT004', 4, '2026-10-08', 'Present', 'Participated'),
('25CS003', 4, '2026-10-09', 'Absent',  'Registered'),
('24EC002', 4, '2026-10-10', 'Present', 'Runner-up');
