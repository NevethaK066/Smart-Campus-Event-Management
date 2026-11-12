# Smart Campus Event Management and Student Participation Analysis System

A college project that manages technical events, workshops, seminars, and cultural activities. It stores student and event data in a MySQL database, lets an administrator register students and record attendance through a Java console application, and uses Python to analyze participation patterns and create charts.

**Department:** Information Technology, Velalar College of Engineering and Technology, Erode
**Class:** 25IT3A&B | **Academic Year:** 2026-27 (Odd) | **Scenario:** 6

---

## 1. Project Title

Smart Campus Event Management and Student Participation Analysis System

## 2. Project Description

Managing college events by hand causes many problems. A student may register twice for the same event. An event may get more participants than it has seats. Attendance is hard to track, and reports take days to prepare.

This system solves these problems with three parts that work together:

1. **MySQL database** stores students, events, and registrations, and enforces the rules.
2. **Java console application** lets the administrator add students and events, register students, record attendance and results, and generate reports.
3. **Python analysis program** reads the same database, cleans the data, shows summary statistics, and creates six charts.

## 3. Problem Statement

A college organizes many events every year. Managing event details, student registrations, attendance, and participation records manually can lead to:

- Duplicate registrations
- Events that are overbooked
- Difficulty tracking attendance
- Delays in preparing reports
- No easy way to see which departments and years take part most

## 4. Features

- Add and view students
- Add, view, and update events
- Register a student for an event
- Block duplicate registrations (in the Java program and in the database)
- Block registration when an event is full
- Record attendance (Present or Absent)
- Record participation result (Registered, Participated, Winner, or Runner-up)
- View all participants of an event
- Generate an event-wise report (registered count, attended count, seats left)
- Python retrieval, missing-value and duplicate checks, data cleaning, and summary statistics
- Six charts: event registrations, department participation, attendance distribution, participation status, year-wise participation, and top events

## 5. Technologies Used

| Technology | Version | Purpose |
|---|---|---|
| Java (JDK) | 17 or later | Console application |
| JDBC with MySQL Connector/J | 8.x | Connects Java to MySQL |
| MySQL Server | 8.0.16 or later | Database |
| Python | 3.9 or later | Data retrieval, cleaning, and charts |
| mysql-connector-python | Latest | Connects Python to MySQL |
| pandas | Latest | Loads and cleans data |
| matplotlib | Latest | Creates charts |
| Git and GitHub | Latest | Version control |

## 6. System Architecture

```
User
  |
  v
Java Application (Console)
  Main.java  -->  StudentDAO / EventDAO / RegistrationDAO  (business logic)
                       |
                       v
               DatabaseConnection.java  (JDBC)
                       |
                       v
               MySQL Database: smart_campus_db
               (STUDENT, EVENT, REGISTRATION)
                       ^
                       |
Python Data Analysis (reads the same database)
  participation_analysis.py  -->  cleaned data and summary
  visualizations.py          -->  six charts
                       |
                       v
Outputs: outputs/ (CSV and summary)  and  visualizations/ (PNG charts)
```

**Data flow:** The administrator uses the Java menu to change data. Java saves the data to MySQL. Python reads the same data from MySQL, cleans it, and writes results to the `outputs/` and `visualizations/` folders. Java and Python never talk to each other directly. The database is the shared source of truth.

## 7. Database Design

**Database name:** `smart_campus_db`

| Table | Primary Key | Foreign Keys | Important Rules |
|---|---|---|---|
| STUDENT | student_id | None | year_of_study must be 1 to 4 |
| EVENT | event_id (auto-increment) | None | max_participants must be greater than 0 |
| REGISTRATION | registration_id (auto-increment) | student_id to STUDENT, event_id to EVENT | UNIQUE (student_id, event_id). attendance_status is Present or Absent. participation_status is Registered, Participated, Winner, or Runner-up. |

Full design documents are in the `docs/` folder:

- `docs/ER_Diagram.md` (ER diagram and relational model)
- `docs/Normalization.md` (1NF, 2NF, and 3NF)

## 8. Installation Instructions

### 8.1 Required Software

1. **Java JDK 17 or later:** https://adoptium.net
2. **MySQL Server 8.0 or later:** https://dev.mysql.com/downloads/mysql/
3. **MySQL Connector/J (JDBC driver):** https://dev.mysql.com/downloads/connector/j/
   Download the `.jar` file and place it in the `lib/` folder.
4. **Python 3.9 or later:** https://www.python.org/downloads/
5. **Git (optional):** https://git-scm.com

### 8.2 Get the Project

```bash
git clone https://github.com/YOUR-USERNAME/Smart-Campus-Event-Management.git
cd Smart-Campus-Event-Management
```

### 8.3 Install Python Libraries

```bash
pip install -r python/requirements.txt
```

## 9. MySQL Setup Instructions

1. Start MySQL Server.
2. Create the database, tables, and sample data. From the project folder, run:

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/sample_data.sql
```

Or open MySQL Workbench, open each file, and run it in this order:

1. `database/schema.sql`
2. `database/sample_data.sql`
3. `database/queries.sql` (optional, to see the reports)

**Important:** Do not run `sample_data.sql` twice, because the student IDs would clash. If you need to start again, run `schema.sql` first, which drops and recreates the tables.

3. Set your MySQL password in **two** files:
   - `java/DatabaseConnection.java`: change `PASSWORD`.
   - `python/db_config.py`: change `"password"`.

## 10. Java Execution Instructions

Run these commands from the project folder (`Smart-Campus-Event-Management`).

**Compile:**

```bash
javac -cp "lib/*" -d out java/Student.java java/Event.java java/Registration.java java/DatabaseConnection.java java/StudentDAO.java java/EventDAO.java java/RegistrationDAO.java java/Main.java
```

**Run:**

- Windows:

```bash
java -cp "out;lib/*" Main
```

- Linux or macOS:

```bash
java -cp "out:lib/*" Main
```

**Main menu:**

```
 1. Add Student
 2. View Students
 3. Add Event
 4. View Events
 5. Update Event
 6. Register Student for Event
 7. View Registered Participants
 8. Record Attendance
 9. Update Participation Status
10. Generate Event Report
11. Exit
```

## 11. Python Execution Instructions

Run these commands from the project folder.

**Step 1: Retrieve, clean, and summarize the data**

```bash
python python/participation_analysis.py
```

This creates:
- `outputs/cleaned_participation_data.csv`
- `outputs/python_summary.txt`

**Step 2: Create the six charts**

```bash
python python/visualizations.py
```

This creates six PNG files in the `visualizations/` folder.

**Note:** Make sure MySQL is running and the password in `python/db_config.py` is correct before running either script.

## 12. Project Folder Structure

```
Smart-Campus-Event-Management/
│
├── README.md
├── .gitignore
│
├── docs/
│   ├── SRS.md
│   ├── Design_Diagrams.md
│   ├── ER_Diagram.md
│   ├── Normalization.md
│   ├── Testing.md
│   └── Visualization_Notes.md
│
├── java/
│   ├── Student.java
│   ├── Event.java
│   ├── Registration.java
│   ├── DatabaseConnection.java
│   ├── StudentDAO.java
│   ├── EventDAO.java
│   ├── RegistrationDAO.java
│   └── Main.java
│
├── lib/
│   └── mysql-connector-j-x.x.x.jar
│
├── database/
│   ├── schema.sql
│   ├── sample_data.sql
│   └── queries.sql
│
├── python/
│   ├── requirements.txt
│   ├── db_config.py
│   ├── participation_analysis.py
│   └── visualizations.py
│
├── visualizations/
│   ├── 01_event_registrations.png
│   ├── 02_department_participation.png
│   ├── 03_attendance_distribution.png
│   ├── 04_participation_status.png
│   ├── 05_year_wise_participation.png
│   └── 06_top_events.png
│
└── outputs/
    ├── cleaned_participation_data.csv
    └── python_summary.txt
```

## 13. Sample Outputs

**Event-wise report (Java option 10), using the sample data:**

```
ID   Event Name                   Max        Registered   Attended   Seats Left
1    AI Workshop                  60         6            4          54
2    Code Sprint                  30         5            4          25
3    Technical Paper Presentation 40         5            3          35
4    Cultural Night               100        4            3          96
5    Web Development Seminar      25         0            0          25
```

**Python summary (excerpt):**

```
Total registrations:  20
Unique students:      9
Unique events:        5
Attendance rate:      70.0%
```

**Sample run of the Java duplicate check (option 6):**

```
Enter your choice: 6
Student ID: 25IT001
Event ID: 1
Duplicate registration: this student is already registered for this event.
```


## 14. Future Enhancements

- Student login, so students can register themselves
- A web or mobile interface
- Email or SMS reminders before events
- Certificate generation for winners and participants
- Export reports to PDF
- A feedback form after each event
- Automatic data backup

## 15. Conclusion

This project shows how a college can replace manual event records with a reliable system. The database rules stop duplicate registrations and overbooking. The Java program gives the administrator a simple menu. The Python program turns the stored data into charts that show participation patterns. Together, Java, MySQL, and Python form a complete, working solution that is easy to set up and run.

---

**Team Members:** _Add your names and register numbers here._

**Guide:** _Add your faculty guide's name here._
