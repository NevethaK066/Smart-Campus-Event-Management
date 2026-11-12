# Software Requirements Specification (SRS)

## Smart Campus Event Management and Student Participation Analysis System

**Department:** Information Technology, Velalar College of Engineering and Technology, Erode
**Class:** 25IT3A&B
**Academic Year:** 2026-27 (Odd)
**Scenario:** 6

---

## 1. Project Title

Smart Campus Event Management and Student Participation Analysis System

## 2. Introduction

Colleges hold many events every year, such as technical events, workshops, seminars, and cultural programs. Each event needs a list of students, a record of who attended, and a final report. Doing this on paper or in loose spreadsheets is slow and error-prone.

This project is a software system that stores event and student data in a MySQL database. Students can register for events through a Java console application. Python reads the same database, cleans the data, and creates charts that show participation patterns.

## 3. Purpose

The purpose of this system is to:

- Keep a single, reliable record of students and events.
- Stop students from registering for the same event twice.
- Stop registrations once an event is full.
- Record attendance and participation results.
- Produce event reports quickly.
- Show participation patterns through charts to help the college plan future events.

## 4. Scope

**In scope:**

- Storing student details (ID, name, department, year of study).
- Adding and managing events (name, date, venue, maximum participants).
- Registering students for events, with duplicate and capacity checks.
- Recording attendance (Present or Absent) and participation result (Registered, Participated, Winner, Runner-up).
- Displaying registered participants.
- Generating event-wise reports.
- Retrieving data in Python, cleaning it, and creating six charts.

**Out of scope:**

- A web or mobile interface (the user interface is a console menu).
- Online payments for events.
- Login and passwords for students.
- Sending emails or messages to students.
- Machine learning or prediction.

## 5. Problem Statement

Event records are managed manually, which causes the following problems:

- A student may register for the same event more than once.
- Events may be overbooked because nobody checks the seat limit.
- Attendance is hard to track and easy to lose.
- Preparing a report takes many hours of manual counting.
- The college has no easy way to see which departments or years take part most.

## 6. Objectives

1. Build a database that stores students, events, and registrations.
2. Build a Java console application that performs all day-to-day tasks.
3. Prevent duplicate registrations at both the application level and the database level.
4. Prevent registrations after the maximum participant count is reached.
5. Record attendance and participation results for each registration.
6. Generate event-wise reports from the database.
7. Use Python to clean the data and show six meaningful charts.

## 7. Existing System

In the existing system:

- Event details are written on notice boards or in separate files.
- Registrations are collected on paper or in simple spreadsheets.
- Duplicate entries are found by hand, if at all.
- Attendance is marked on a sheet and typed in later, often with mistakes.
- Reports take days to prepare, and the college has little data for analysis.

## 8. Proposed System

The proposed system is a three-part solution:

1. **MySQL database:** Stores all records in three linked tables: STUDENT, EVENT, and REGISTRATION. Rules such as UNIQUE and CHECK keep the data correct.
2. **Java console application:** Lets the administrator add students and events, lets students register, records attendance and results, and shows reports. Before each registration, the program checks for duplicates and capacity.
3. **Python analysis program:** Reads the same database, checks for missing values and duplicates, cleans the data, shows summary statistics, and saves six charts as image files.

## 9. Functional Requirements

| ID | Requirement |
|---|---|
| FR-01 | The system shall store each student's ID, name, department, and year of study. |
| FR-02 | The system shall allow the administrator to add a new student. |
| FR-03 | The system shall display the list of all students. |
| FR-04 | The system shall store each event's ID, name, date, venue, and maximum participant count. |
| FR-05 | The system shall allow the administrator to add a new event. |
| FR-06 | The system shall allow the administrator to update an event's details. |
| FR-07 | The system shall display the list of all events. |
| FR-08 | The system shall allow a student to register for an event. |
| FR-09 | The system shall prevent a student from registering for the same event twice. |
| FR-10 | The system shall prevent registration when the event has reached its maximum participant count. |
| FR-11 | The system shall record attendance (Present or Absent) for each registration. |
| FR-12 | The system shall record the participation status (Registered, Participated, Winner, or Runner-up). |
| FR-13 | The system shall display all participants registered for a chosen event. |
| FR-14 | The system shall generate an event-wise report showing registered count, attended count, and seats left. |
| FR-15 | The Python program shall retrieve participation data from the MySQL database. |
| FR-16 | The Python program shall check for missing values and duplicate records. |
| FR-17 | The Python program shall clean the data and convert it to suitable types. |
| FR-18 | The Python program shall display summary statistics. |
| FR-19 | The Python program shall generate six charts (event registrations, department participation, attendance distribution, participation status, year-wise participation, and top events). |

## 10. Non-Functional Requirements

| ID | Category | Requirement |
|---|---|---|
| NFR-01 | Accuracy | Duplicate registrations shall never be saved, because the database enforces a UNIQUE rule. |
| NFR-02 | Reliability | Invalid input (such as a negative capacity or a year of 9) shall be rejected with a clear message. |
| NFR-03 | Usability | The console menu shall use numbered options and clear messages. |
| NFR-04 | Performance | Common actions (add, view, register) shall finish in under 2 seconds with up to 1,000 records. |
| NFR-05 | Maintainability | Code shall be split into model classes and DAO classes, with comments on key methods. |
| NFR-06 | Portability | The system shall run on Windows, Linux, or macOS with Java 17 or later, MySQL 8.0 or later, and Python 3.9 or later. |
| NFR-07 | Security | The database password shall be kept in one configuration place, not repeated across files. |
| NFR-08 | Data consistency | Foreign keys shall prevent a registration for a student or event that does not exist. |

## 11. Hardware Requirements

| Item | Minimum |
|---|---|
| Processor | Intel Core i3 or equivalent |
| RAM | 4 GB (8 GB recommended) |
| Storage | 2 GB free space |
| Display | 1366 x 768 or higher |
| Network | Not required (runs locally) |

## 12. Software Requirements

| Software | Version | Purpose |
|---|---|---|
| Operating System | Windows 10/11, Ubuntu 22.04, or macOS | Platform |
| Java JDK | 17 or later | Runs the console application |
| MySQL Server | 8.0 or later | Stores the database |
| MySQL Connector/J | 8.x (JAR file) | Lets Java talk to MySQL (JDBC driver) |
| Python | 3.9 or later | Data retrieval and analysis |
| mysql-connector-python | Latest | Lets Python talk to MySQL |
| pandas | Latest | Data cleaning and summary |
| matplotlib | Latest | Charts |
| Git and GitHub | Latest | Version control and submission |
| Text editor or IDE | Any (VS Code, IntelliJ, NetBeans) | Writing code |

## 13. User Roles

**Administrator (Staff or Coordinator)**

- Adds and manages students and events.
- Registers students for events.
- Records attendance and participation status.
- Views participants and generates reports.
- Runs the Python analysis.

**Student**

- Is a passive user in this version. Students do not log in.
- Registration is done by the administrator at the counter, on behalf of the student.
- Students are the subject of the data that is analyzed.

## 14. System Features

1. **Student Management:** Add students, view all students.
2. **Event Management:** Add events, update events, view all events.
3. **Registration Management:** Register a student for an event, with duplicate and capacity checks.
4. **Attendance and Participation:** Record attendance and results for each registration.
5. **Participant View:** Show everyone registered for an event.
6. **Event Reports:** Show registered count, attended count, and seats left for each event.
7. **Data Analysis (Python):** Retrieve, clean, summarize, and visualize participation data.

## 15. Assumptions and Constraints

**Assumptions:**

- Each student has a unique student ID.
- Each event has a unique event ID that is created automatically.
- One student can register for many events, and one event can have many students.
- The administrator uses the console on one computer at a time.
- The MySQL server runs on the same computer as the Java and Python programs (localhost).

**Constraints:**

- The system uses a console interface, not a graphical one.
- Students cannot log in or change their own registrations.
- Charts are saved as image files, not shown in a web page.
- The project must be completed within the academic deadline, so only the required features are built.
- No advanced technologies (such as frameworks, web servers, or machine learning) are used, as required by the college.
