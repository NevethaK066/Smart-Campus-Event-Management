# Testing Documentation

## Smart Campus Event Management and Student Participation Analysis System

**Department:** Information Technology, Velalar College of Engineering and Technology, Erode
**Class:** 25IT3A&B | **Academic Year:** 2026-27 (Odd) | **Scenario:** 6


---

## 1. Testing Objectives

1. Check that every functional requirement (FR-01 to FR-19 in the SRS) works as described.
2. Check that duplicate registrations are blocked, both in the Java program and in the database.
3. Check that registration is blocked when an event is full.
4. Check that invalid input (wrong year, zero capacity, bad date, empty text) is rejected with a clear message.
5. Check that Java, MySQL, and Python all work with the same database and the same table names.
6. Check that the Python program retrieves, checks, cleans, and summarizes the data correctly.
7. Check that the six charts are created.

---

## 2. Testing Techniques

| Technique | What it means | Where it is used in this project |
|---|---|---|
| Black-box testing | Test the program by its inputs and outputs, without looking at the code | Most test cases in Section 5 |
| White-box testing | Test the code paths inside a method (for example, both branches of an if statement) | Duplicate check and capacity check in `RegistrationDAO` |
| Equivalence partitioning | Group inputs into valid and invalid groups, and test one value from each group | Year of study (valid 1 to 4, invalid anything else), capacity (valid 1 to 1000) |
| Boundary value analysis | Test the values at the edges of a valid range | Year of study: test 0, 1, 4, and 5. Capacity: test 0, 1, and 1000 |
| Error guessing | Test inputs that users often enter by mistake | Empty name, letters in a number field, date 2026-13-45 |
| Manual testing | A person runs the program and checks the screen | All Java console tests |
| Automated checks | The program checks its own results | Python data checks (missing values, duplicates) |

---

## 3. Testing Levels

### 3.1 Unit Testing

Unit testing checks one small part of the code on its own.

| Unit | What is tested | How it is tested | Test cases |
|---|---|---|---|
| Student, Event, Registration (models) | Constructors, getters, setters, and toString() | Add a student or event and view it in the list. The values shown must match the values entered. | TC-01, TC-05, TC-06, TC-09 |
| DatabaseConnection | Connection opens with the correct password | Run any menu option. No "Database error" message appears. | All Java tests |
| StudentDAO.studentExists, addStudent, getAllStudents | Correct insert, read, and existence check | Test cases for adding and viewing students | TC-01, TC-02, TC-05 |
| EventDAO.addEvent, updateEvent, getEventById, eventExists | Correct insert, update, read, and existence check | Test cases for adding, updating, and viewing events | TC-06, TC-10, TC-11 |
| RegistrationDAO.isDuplicate | Returns true for an existing registration and false for a new one | Test cases for valid and duplicate registration | TC-12, TC-13 |
| RegistrationDAO.hasSeatsLeft | Returns false when the event is full | Test case for a full event | TC-16 |
| RegistrationDAO.updateAttendance, updateParticipation | Correct status is saved | Test cases for attendance and participation | TC-17, TC-19 |
| RegistrationDAO.getEventReport | Correct counts and seats left | Test case for the report | TC-21 |
| Python: check_missing_values, check_duplicates | Correct counts | Run the Python program and compare the counts | TC-23, TC-24 |
| Python: clean_data | Correct types, correct attendance counts, correct attendance rate | Run the Python program and compare the summary | TC-25 |

### 3.2 Integration Testing

Integration testing checks that two or more parts work together.

| Integration | What is tested | Test cases |
|---|---|---|
| Java and MySQL | Java saves data to the database, and reads it back correctly | TC-01, TC-12, TC-17, TC-20 |
| Python and MySQL | Python reads the same data that Java saves | TC-22 |
| Java DAO and database rules | The Java checks and the database UNIQUE, FOREIGN KEY, and CHECK rules work together | TC-13, TC-27, TC-28, TC-29 |
| Python and charts | Cleaned data is used to create the charts | TC-26 |

### 3.3 System Testing

System testing checks the whole system from start to finish, as a user would use it.

| Test | Description | Test case |
|---|---|---|
| End-to-end flow | Add a student, add an event, register the student, record attendance, record the result, view the report, then run the Python analysis and charts | TC-30 |

---

## 4. Test Environment and Setup

**Software:**

| Software | Version |
|---|---|
| Windows 10/11 (or Ubuntu 22.04) | Any |
| MySQL Server | 8.0 or later |
| Java JDK | 17 or later |
| MySQL Connector/J | 8.x |
| Python | 3.9 or later |
| mysql-connector-python, pandas, matplotlib | Latest |

**Reset the database before a test run:**

1. Open MySQL Workbench.
2. Run `database/schema.sql`.
3. Run `database/sample_data.sql`.

The database now has 9 students, 5 events, and 20 registrations.

**Test order (important):**

1. Reset the database. Run **TC-22 to TC-26** (Python tests). They expect 20 registrations.
2. Reset the database again.
3. Run **TC-01 to TC-21** in order (Java tests). Some tests depend on earlier ones.
4. Run **TC-27 to TC-29** (database rule tests).
5. Reset the database again. Run **TC-30** (end-to-end test).

**Pre-test setup for TC-16 (full event):**

Use option 5 (Update Event) to change event 5 to these values: name `Web Development Seminar`, date `2026-10-28`, venue `Seminar Hall B`, maximum `1`.

---

## 5. Test Case Table

**Status values:** Pass, Fail, or Not run.

**Important:** The Actual Result and Status columns are blank. Your team must run each test, write the real result, and mark Pass or Fail. Do not mark a test as Pass unless you have run it.

### 5.1 Adding a Student

| Test Case ID | Module | Test Description | Input | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| TC-01 | Add Student | Add a valid new student | Menu 1. ID: 25IT005. Name: Test Student. Department: Information Technology. Year: 2 | Message: "Student added successfully." Menu 2 then shows 25IT005 in the list. | | Not run |
| TC-02 | Add Student | Reject a duplicate student ID | Menu 1. ID: 25IT001. Name: Any. Department: Any. Year: 2 | Message: "A student with this ID already exists. Not added." | | Not run |
| TC-03 | Add Student | Reject a year of study outside 1 to 4 | Menu 1. ID: 25IT006. Name: Test Two. Department: Information Technology. Year: 9, then 2 | Message: "Please enter a number from 1 to 4." Then the year 2 is accepted and the student is added. | | Not run |
| TC-04 | Add Student | Reject an empty name | Menu 1. ID: 25IT007. Name: (press Enter only) | Message: "This field cannot be empty." Then the user is asked again. | | Not run |
| TC-05 | View Students | Show all students | Menu 2 | A table lists 10 students (the 9 sample students plus 25IT005). | | Not run |

### 5.2 Adding and Updating an Event

| Test Case ID | Module | Test Description | Input | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| TC-06 | Add Event | Add a valid new event | Menu 3. Name: Robotics Demo. Date: 2026-11-20. Venue: Lab 2. Maximum: 50 | Message: "Event added successfully." This event gets ID 6. | | Not run |
| TC-07 | Add Event | Reject an invalid date | Menu 3. Date: 2026-13-45, then 2026-11-20 | Message: "Invalid date. Use yyyy-mm-dd, for example 2026-12-05." Then the valid date is accepted. | | Not run |
| TC-08 | Add Event | Reject a maximum of 0 | Menu 3. Maximum: 0 | Message: "Please enter a number from 1 to 1000." | | Not run |
| TC-09 | View Events | Show all events | Menu 4 | A table lists 6 events. | | Not run |
| TC-10 | Update Event | Update an event successfully | Menu 5. Event ID: 6. Name: Robotics Demo. Date: 2026-11-20. Venue: Lab 2. Maximum: 40 | Message: "Event updated successfully." Menu 4 shows maximum 40 for event 6. | | Not run |
| TC-11 | Update Event | Reject a maximum below the number already registered | Menu 5. Event ID: 1. Name: AI Workshop. Date: 2026-09-05. Venue: Seminar Hall A. Maximum: 5 | Message: "Cannot set maximum to 5. 6 students are already registered." | | Not run |

### 5.3 Registration (Valid, Duplicate, Not Found, Full)

| Test Case ID | Module | Test Description | Input | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| TC-12 | Valid Event Registration | Register a student for an event with free seats | Menu 6. Student ID: 25IT005. Event ID: 1 | Message: "Registration successful." Event 1 now has 7 registrations. | | Not run |
| TC-13 | Duplicate Registration | Block a second registration for the same student and event | Menu 6. Student ID: 25IT001. Event ID: 1 | Message: "Duplicate registration: this student is already registered for this event." No new row is saved. | | Not run |
| TC-14 | Registration (student not found) | Block registration for a student who does not exist | Menu 6. Student ID: 25IT999 | Message: "Student not found. Please add the student first (option 1)." | | Not run |
| TC-15 | Registration (event not found) | Block registration for an event that does not exist | Menu 6. Student ID: 25IT005. Event ID: 99 | Message: "Event not found. Please check the event ID (option 4)." | | Not run |
| TC-16 | Registration after maximum capacity | Block registration when the event is full | Setup: event 5 maximum is 1 (see Section 4). Menu 6. Student ID: 25IT001. Event ID: 5. Then Menu 6. Student ID: 25IT005. Event ID: 5 | First attempt: "Registration successful." Second attempt: "Registration closed: this event has reached its maximum participants." | | Not run |

### 5.4 Attendance and Participation

| Test Case ID | Module | Test Description | Input | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| TC-17 | Recording Attendance | Mark a registered student as Present | Menu 8. Event ID: 1. Registration ID: 3. Choose 1 (Present) | Participant table is shown. Message: "Attendance recorded as Present." | | Not run |
| TC-18 | Recording Attendance | Reject a registration ID that is not in this event | Menu 8. Event ID: 1. Registration ID: 999 | Message: "That registration ID is not in this event." | | Not run |
| TC-19 | Updating Participation Status | Set a participation status to Winner | Menu 9. Event ID: 1. Registration ID: 3. Choose 3 (Winner) | Message: "Participation status set to Winner." | | Not run |

### 5.5 Viewing Participants and Reports

| Test Case ID | Module | Test Description | Input | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| TC-20 | View Registered Participants | Show all participants of one event | Menu 7. Event ID: 1 | Table lists 7 participants with names. Registration ID 3 shows Present and Winner. | | Not run |
| TC-21 | Generate Event Report | Show the event-wise report | Menu 10 | Six lines, as follows. AI Workshop: max 60, registered 7, attended 5, seats left 53. Code Sprint: 30, 5, 4, 25. Technical Paper Presentation: 40, 5, 3, 35. Cultural Night: 100, 4, 3, 96. Web Development Seminar: 1, 1, 0, 0. Robotics Demo: 40, 0, 0, 40. | | Not run |

### 5.6 Database Retrieval and Python Preprocessing

| Test Case ID | Module | Test Description | Input | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| TC-22 | Database Retrieval | Python retrieves all registrations | Run `python python/participation_analysis.py` on a freshly reset database | Message: "Rows retrieved: 20". The preview shows the columns from the JOIN query. | | Not run |
| TC-23 | Python Preprocessing (missing values) | Check for missing values | Same run as TC-22 | Message: "No missing values found." | | Not run |
| TC-24 | Python Preprocessing (duplicates) | Check for duplicate records | Same run as TC-22 | All three counts are 0 (full duplicates, duplicate IDs, duplicate student-event pairs). | | Not run |
| TC-25 | Python Preprocessing (cleaning) | Clean the data and summarize it | Same run as TC-22 | "Rows after cleaning: 20". "Attendance rate: 70.0%". Present = 14, Absent = 6. Files `outputs/cleaned_participation_data.csv` and `outputs/python_summary.txt` are created. | | Not run |
| TC-26 | Data Visualization | Create the six charts | Run `python python/visualizations.py` after TC-22 | Six lines "Saved: ..." are printed. Six PNG files exist in the `visualizations/` folder. | | Not run |

### 5.7 Database Rules (Run in MySQL)

| Test Case ID | Module | Test Description | Input (SQL) | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| TC-27 | Database UNIQUE rule | Block a duplicate registration at database level | `INSERT INTO REGISTRATION (student_id, event_id) VALUES ('25IT001', 1);` | Error 1062 "Duplicate entry" for key uq_student_event. No row is added. | | Not run |
| TC-28 | Database FOREIGN KEY rule | Block a registration for a student who does not exist | `INSERT INTO REGISTRATION (student_id, event_id) VALUES ('99XX999', 1);` | Error 1452 "foreign key constraint fails". | | Not run |
| TC-29 | Database CHECK rule | Block a year of study outside 1 to 4 | `UPDATE STUDENT SET year_of_study = 9 WHERE student_id = '25IT001';` | Error 3819 "Check constraint 'chk_year' is violated". | | Not run |

### 5.8 End-to-End (System Test)

| Test Case ID | Module | Test Description | Input | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|---|
| TC-30 | Full system flow | Use the whole system from start to finish | 1. Reset the database. 2. Java: add student 25IT006 (Year 1). 3. Java: add event Hackathon 2026-11-25, Main Hall, maximum 30. 4. Java: register 25IT006 for the new event. 5. Java: record attendance as Present. 6. Java: set participation to Participated. 7. Java: generate the report. 8. Python: run participation_analysis.py. 9. Python: run visualizations.py | Each Java step shows its success message. The report shows the new event with 1 registered and 1 attended. Python shows "Rows retrieved: 21". Six charts are created. | | Not run |

---

## 6. Summary Table (fill in after testing)

| Result | Count |
|---|---|
| Total test cases | 30 |
| Pass | |
| Fail | |
| Not run | |

**Defects found and fixed:**

| Defect ID | Test Case | Description | Fix | Retested? |
|---|---|---|---|---|
| | | | | |

---

## 7. Conclusion

The tests cover all functional requirements in the SRS, the three layers (Java, MySQL, and Python), and the database rules. Testing shows that the system:

- Saves valid data and rejects invalid data.
- Blocks duplicate registrations, both in the Java program and in the database.
- Blocks registration when an event is full.
- Gives the same data to the Java application and the Python analysis.

Record the actual results in Section 5 and the summary in Section 6 before submission.
