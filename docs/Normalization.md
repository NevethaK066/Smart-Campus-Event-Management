# Database Normalization

## Smart Campus Event Management and Student Participation Analysis System

**Department:** Information Technology, Velalar College of Engineering and Technology, Erode
**Class:** 25IT3A&B | **Academic Year:** 2026-27 (Odd) | **Scenario:** 6

---

## 1. What is Normalization?

**Normalization** is the process of organizing data in tables so that:

1. Each fact is stored in only one place.
2. Data is not repeated unnecessarily.
3. Updating, adding, or deleting data does not cause errors or lost information.

Normalization is done in steps called **normal forms**. This document shows the three most common ones: **1NF, 2NF, and 3NF**.

**Simple idea:** Imagine one big notebook where you write everything about students and events together. If a student's department changes, you have to fix it in many places. Normalization splits the notebook into separate, well-organized sheets so each fact is written once.

---

## 2. Starting Point: One Big Table (Unnormalized)

Before normalization, the college might store all registration data in one big table, like this:

| student_id | student_name | department | year_of_study | event_names | event_dates | venues | attendance |
|---|---|---|---|---|---|---|---|
| 25IT001 | Arun Kumar | Information Technology | 2 | AI Workshop, Code Sprint | 2026-12-05, 2026-12-10 | Seminar Hall A, Computer Lab 1 | Present, Absent |
| 25IT002 | Priya Lakshmi | Information Technology | 2 | AI Workshop | 2026-12-05 | Seminar Hall A | Absent |

**Problems with this table:**

- The `event_names` column holds **several values in one cell** (for example, "AI Workshop, Code Sprint"). This makes searching and counting very hard.
- The event details are written in a list that is hard to match with the attendance list.
- If "AI Workshop" moves to a new venue, the venue must be fixed in every row that mentions it.

We fix these problems step by step below.

---

## 3. First Normal Form (1NF)

### 3.1 Rule for 1NF

A table is in **1NF** when:

1. Every cell holds **one single value** (no lists, no groups inside one cell).
2. Every row can be identified by a **primary key**.
3. There are **no repeating groups** of columns.

### 3.2 Applying 1NF

We split the list into separate rows. Each row now shows **one student with one event**.

| student_id | student_name | department | year_of_study | event_id | event_name | event_date | venue | max_participants | attendance_status | participation_status |
|---|---|---|---|---|---|---|---|---|---|---|
| 25IT001 | Arun Kumar | Information Technology | 2 | 1 | AI Workshop | 2026-12-05 | Seminar Hall A | 60 | Present | Participated |
| 25IT001 | Arun Kumar | Information Technology | 2 | 2 | Code Sprint | 2026-12-10 | Computer Lab 1 | 30 | Absent | Winner |
| 25IT002 | Priya Lakshmi | Information Technology | 2 | 1 | AI Workshop | 2026-12-05 | Seminar Hall A | 60 | Absent | Registered |

**Primary key for this table:** (**student_id**, **event_id**). Together, these two columns uniquely identify each row.

**Result:** Every cell now holds one value, so the table is in **1NF**.

### 3.3 Problem Still Left After 1NF

Look at the repeated data:

- "Arun Kumar" appears in **two rows**.
- "AI Workshop" venue "Seminar Hall A" appears in **two rows**.

Some columns depend only on part of the key. This is what 2NF fixes.

---

## 4. Second Normal Form (2NF)

### 4.1 Rule for 2NF

A table is in **2NF** when:

1. It is already in **1NF**.
2. Every non-key column depends on the **whole** primary key, not just part of it.

This matters only when the primary key has **two or more columns** (a composite key), like (student_id, event_id) here.

### 4.2 Finding Partial Dependencies

Our composite key is (**student_id**, **event_id**). Check each column:

| Column | Depends on | Problem? |
|---|---|---|
| student_name | student_id only | **Yes**. It depends on part of the key. |
| department | student_id only | **Yes**. It depends on part of the key. |
| year_of_study | student_id only | **Yes**. It depends on part of the key. |
| event_name | event_id only | **Yes**. It depends on part of the key. |
| event_date | event_id only | **Yes**. It depends on part of the key. |
| venue | event_id only | **Yes**. It depends on part of the key. |
| max_participants | event_id only | **Yes**. It depends on part of the key. |
| attendance_status | student_id + event_id | No. It depends on the whole key. |
| participation_status | student_id + event_id | No. It depends on the whole key. |

**Example:** A student's name depends only on the student ID. It does not change when the event changes. So it should not be stored in the registration rows.

### 4.3 Applying 2NF

We split the 1NF table into three tables:

**STUDENT** (student details, depends on student_id)

| student_id (PK) | student_name | department | year_of_study |
|---|---|---|---|
| 25IT001 | Arun Kumar | Information Technology | 2 |
| 25IT002 | Priya Lakshmi | Information Technology | 2 |

**EVENT** (event details, depends on event_id)

| event_id (PK) | event_name | event_date | venue | max_participants |
|---|---|---|---|---|
| 1 | AI Workshop | 2026-12-05 | Seminar Hall A | 60 |
| 2 | Code Sprint | 2026-12-10 | Computer Lab 1 | 30 |

**REGISTRATION** (depends on the whole key: student_id + event_id)

| student_id (PK, FK) | event_id (PK, FK) | attendance_status | participation_status |
|---|---|---|---|
| 25IT001 | 1 | Present | Participated |
| 25IT001 | 2 | Absent | Winner |
| 25IT002 | 1 | Absent | Registered |

**Result:** Each non-key column now depends on the whole key of its own table. The tables are in **2NF**.

**Benefit:** If "AI Workshop" moves to a new venue, we change it in **one row** of the EVENT table only.

### 4.4 Note on REGISTRATION Table

The REGISTRATION table in the 2NF step still uses (student_id, event_id) as its key. In our final design (Step 6), we added a separate **registration_id** as the primary key, and kept **UNIQUE (student_id, event_id)**. This is a common and accepted practice. It makes it simpler to update attendance and participation by one ID, and the UNIQUE rule still prevents duplicate registrations.

---

## 5. Third Normal Form (3NF)

### 5.1 Rule for 3NF

A table is in **3NF** when:

1. It is already in **2NF**.
2. No non-key column depends on **another non-key column**. This is called a **transitive dependency**.

**Simple way to think about it:**
If column **A** determines column **B**, and **B** determines column **C**, then **C** depends on **A** only through **B**. The fix is to move **B** and **C** into their own table.

### 5.2 Checking Our Tables for Transitive Dependencies

**STUDENT table:** (student_id, student_name, department, year_of_study)

- student_id determines student_name, department, and year_of_study. ✔
- Does department determine any other column in this table? No. Department does not determine student_name or year_of_study. So there is no transitive dependency. ✔

**EVENT table:** (event_id, event_name, event_date, venue, max_participants)

- event_id determines every other column. ✔
- Does venue determine event_name, event_date, or max_participants? No. Two events can share a venue, and the venue alone does not decide the other details. ✔

**REGISTRATION table:** (registration_id, student_id, event_id, registration_date, attendance_status, participation_status)

- registration_id determines every other column. ✔
- Does attendance_status determine participation_status? No. A student can be "Absent" and still have a result, or "Present" and be "Registered". Each column is independent. ✔

**Result:** All three tables are in **3NF**.

### 5.3 Example of a 3NF Problem (for illustration)

Suppose we had stored the department head in the STUDENT table:

| student_id | student_name | department | department_head |
|---|---|---|---|
| 25IT001 | Arun Kumar | Information Technology | Dr. Suresh |
| 25IT002 | Priya Lakshmi | Information Technology | Dr. Suresh |

Here, **department_head depends on department**, and department depends on student_id. This is a **transitive dependency** (student_id → department → department_head). It breaks 3NF.

**Fix:** Create a separate DEPARTMENT table:

| department (PK) | department_head |
|---|---|
| Information Technology | Dr. Suresh |

Then the STUDENT table keeps only department as a foreign key. Our project does not store the department head, so this problem does not occur in our design. We keep this example to show the principle.

---

## 6. Final Normalized Design

| Table | Primary Key | Foreign Keys | Normal Form |
|---|---|---|---|
| STUDENT | student_id | None | 3NF |
| EVENT | event_id | None | 3NF |
| REGISTRATION | registration_id (with UNIQUE on student_id, event_id) | student_id → STUDENT, event_id → EVENT | 3NF |

---

## 7. Why the Final Design Satisfies Normalization

| Principle | How the design satisfies it |
|---|---|
| **1NF: single values** | Every column holds one value. There are no lists in any cell. |
| **1NF: primary key** | All three tables have a primary key. |
| **2NF: no partial dependency** | Student details are only in STUDENT. Event details are only in EVENT. Registration details are only in REGISTRATION. |
| **3NF: no transitive dependency** | No non-key column in any table determines another non-key column. |
| **No repeated data** | A student's name is stored once, not once per event. An event's venue is stored once, not once per student. |
| **Safe updates** | Changing an event's venue updates one row in EVENT. Changing a student's department updates one row in STUDENT. |
| **Safe deletes** | Deleting a registration does not delete the student or the event. |
| **Duplicate prevention** | UNIQUE (student_id, event_id) on REGISTRATION stops the same student from registering for the same event twice. |

---

## 8. Summary

- **1NF** removed lists and repeating groups by giving every registration its own row.
- **2NF** moved student details and event details out of the registration table, so each fact depends on the whole key of its own table.
- **3NF** checked that no column depends on another non-key column. All three final tables pass this check.

The final design has three tables (STUDENT, EVENT, REGISTRATION), with clear primary keys and foreign keys. The data is stored once, updates are safe, and duplicate registrations are blocked.

---

