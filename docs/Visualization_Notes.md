# Data Visualizations: Explanation and Interpretation

## Smart Campus Event Management and Student Participation Analysis System

**Code that creates the charts:** `python/visualizations.py`
**Chart images:** `visualizations/` folder

The numbers below come from the sample data in `database/sample_data.sql`. If your own output differs, use your own numbers from the program.

**Note:** The "Web Development Seminar" event has no registrations in the sample data. So it does not appear in the event charts (Charts 1 and 6). This is expected.

---

## Chart 1: Event-wise Registration Count

**File:** `visualizations/01_event_registrations.png`
**Type:** Bar chart

**What it shows:** The number of students registered for each event, in date order.

**Interpretation:**
- AI Workshop has the most registrations (6 students).
- Code Sprint and Technical Paper Presentation have 5 each.
- Cultural Night has 4.
- The events have similar popularity. The difference between the most and least popular event is only 2 students, which means no single event dominates.

---

## Chart 2: Department-wise Student Participation

**File:** `visualizations/02_department_participation.png`
**Type:** Horizontal bar chart

**What it shows:** The total number of registrations from each department.

**Interpretation:**
- Information Technology has 10 registrations, which is 50% of all registrations.
- Computer Science and Engineering has 6 registrations (30%).
- Electronics and Communication Engineering has 4 registrations (20%).
- The IT department is the most active. The college could use this to decide which department to target with future events.

---

## Chart 3: Attendance Status Distribution

**File:** `visualizations/03_attendance_distribution.png`
**Type:** Pie chart

**What it shows:** The share of registrations marked as Present and Absent.

**Interpretation:**
- 14 registrations (70%) were marked Present.
- 6 registrations (30%) were marked Absent.
- An attendance rate of 70% means that about 3 out of every 10 registered students did not come. The college could send reminders to registered students before future events to reduce this gap.

---

## Chart 4: Participation Status Analysis

**File:** `visualizations/04_participation_status.png`
**Type:** Bar chart

**What it shows:** How many registrations are in each participation status: Registered, Participated, Winner, and Runner-up.

**Interpretation:**
- Participated is the largest group with 8 registrations (40%).
- Registered (no result recorded yet) has 6 registrations (30%).
- Winner and Runner-up have 3 registrations each (15% each).
- A total of 6 registrations (30%) ended with a prize. This shows that prizes are shared among many students, not limited to a few.

---

## Chart 5: Year-wise Student Participation

**File:** `visualizations/05_year_wise_participation.png`
**Type:** Bar chart

**What it shows:** The number of registrations from students in each year of study.

**Interpretation:**
- Year 2 students have 16 registrations (80%).
- Year 3 students have 4 registrations (20%).
- Second-year students take part the most in this sample. This may be because most students in the sample are in their second year. The college could check whether first-year and final-year students need more encouragement to join events.

---

## Chart 6: Top Events Based on Student Registrations

**File:** `visualizations/06_top_events.png`
**Type:** Bar chart (top 5 events)

**What it shows:** The events with the most registrations, ranked from highest to lowest.

**Interpretation:**
- AI Workshop is the top event (6 registrations).
- Code Sprint and Technical Paper Presentation are tied in second place (5 each).
- Cultural Night is in fourth place (4 registrations).
- Technical events (AI Workshop, Code Sprint, Technical Paper Presentation) fill the top three places. This suggests that students in this sample prefer technical events to cultural events.

---

## Summary

| Chart | Main finding |
|---|---|
| 1. Event-wise registrations | AI Workshop is the most popular event. |
| 2. Department participation | IT students register the most (50%). |
| 3. Attendance distribution | 70% of registered students attended. |
| 4. Participation status | 30% of registrations ended with a prize. |
| 5. Year-wise participation | Year 2 students register the most (80%). |
| 6. Top events | Technical events are the top three. |
