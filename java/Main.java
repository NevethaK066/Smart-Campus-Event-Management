import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Main.java
 * Console menu for the Smart Campus Event Management System.
 * Reads input with Scanner and calls the DAO classes.
 * No SQL is written in this file.
 */
public class Main {

    private static final Scanner input = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final EventDAO eventDAO = new EventDAO();
    private static final RegistrationDAO registrationDAO = new RegistrationDAO();

    public static void main(String[] args) {
        System.out.println("=============================================");
        System.out.println("   Smart Campus Event Management System");
        System.out.println("=============================================");

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:  addStudentMenu(); break;
                case 2:  viewStudentsMenu(); break;
                case 3:  addEventMenu(); break;
                case 4:  viewEventsMenu(); break;
                case 5:  updateEventMenu(); break;
                case 6:  registerStudentMenu(); break;
                case 7:  viewParticipantsMenu(); break;
                case 8:  recordAttendanceMenu(); break;
                case 9:  updateParticipationMenu(); break;
                case 10: generateReportMenu(); break;
                case 11: running = false; break;
                default: System.out.println("Invalid choice. Please enter a number from 1 to 11.");
            }
        }

        System.out.println("Thank you. Goodbye!");
        input.close();
    }

    // ------------------------------------------------------------
    // Menu display
    // ------------------------------------------------------------
    private static void showMenu() {
        System.out.println("\n---------------------- MENU ----------------------");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. View Students");
        System.out.println(" 3. Add Event");
        System.out.println(" 4. View Events");
        System.out.println(" 5. Update Event");
        System.out.println(" 6. Register Student for Event");
        System.out.println(" 7. View Registered Participants");
        System.out.println(" 8. Record Attendance");
        System.out.println(" 9. Update Participation Status");
        System.out.println("10. Generate Event Report");
        System.out.println("11. Exit");
        System.out.println("--------------------------------------------------");
    }

    // ------------------------------------------------------------
    // Option 1: Add Student
    // ------------------------------------------------------------
    private static void addStudentMenu() {
        String id = readNonEmpty("Student ID (for example 25IT005): ");

        if (studentDAO.studentExists(id)) {
            System.out.println("A student with this ID already exists. Not added.");
            return;
        }

        String name = readNonEmpty("Student name: ");
        String department = readNonEmpty("Department: ");
        int year = readIntInRange("Year of study (1 to 4): ", 1, 4);

        Student student = new Student(id, name, department, year);
        if (studentDAO.addStudent(student)) {
            System.out.println("Student added successfully.");
        }
    }

    // ------------------------------------------------------------
    // Option 2: View Students
    // ------------------------------------------------------------
    private static void viewStudentsMenu() {
        ArrayList<Student> students = studentDAO.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.printf("%-10s %-25s %-40s %s%n", "Student ID", "Name", "Department", "Year");
        System.out.println("-----------------------------------------------------------------------------------");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    // ------------------------------------------------------------
    // Option 3: Add Event
    // ------------------------------------------------------------
    private static void addEventMenu() {
        String name = readNonEmpty("Event name: ");
        String date = readDate("Event date (yyyy-mm-dd): ");
        String venue = readNonEmpty("Venue: ");
        int max = readIntInRange("Maximum participants (1 to 1000): ", 1, 1000);

        Event event = new Event(name, date, venue, max);
        if (eventDAO.addEvent(event)) {
            System.out.println("Event added successfully.");
        }
    }

    // ------------------------------------------------------------
    // Option 4: View Events
    // ------------------------------------------------------------
    private static void viewEventsMenu() {
        ArrayList<Event> events = eventDAO.getAllEvents();

        if (events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }

        System.out.printf("%-4s %-28s %-12s %-22s %s%n", "ID", "Event Name", "Date", "Venue", "Max");
        System.out.println("-----------------------------------------------------------------------------------");
        for (Event e : events) {
            System.out.println(e);
        }
    }

    // ------------------------------------------------------------
    // Option 5: Update Event
    // ------------------------------------------------------------
    private static void updateEventMenu() {
        int id = readInt("Event ID to update: ");

        Event existing = eventDAO.getEventById(id);
        if (existing == null) {
            System.out.println("Event not found.");
            return;
        }

        System.out.println("Current details: " + existing);
        System.out.println("Enter the new details:");

        String name = readNonEmpty("New event name: ");
        String date = readDate("New event date (yyyy-mm-dd): ");
        String venue = readNonEmpty("New venue: ");
        int max = readIntInRange("New maximum participants (1 to 1000): ", 1, 1000);

        // Do not allow a maximum below the number already registered
        int registered = registrationDAO.countRegistrations(id);
        if (max < registered) {
            System.out.println("Cannot set maximum to " + max + ". "
                    + registered + " students are already registered.");
            return;
        }

        existing.setEventName(name);
        existing.setEventDate(date);
        existing.setVenue(venue);
        existing.setMaxParticipants(max);

        if (eventDAO.updateEvent(existing)) {
            System.out.println("Event updated successfully.");
        }
    }

    // ------------------------------------------------------------
    // Option 6: Register Student for Event
    // Checks: student exists, event exists, no duplicate, seats left
    // ------------------------------------------------------------
    private static void registerStudentMenu() {
        String studentId = readNonEmpty("Student ID: ");
        if (!studentDAO.studentExists(studentId)) {
            System.out.println("Student not found. Please add the student first (option 1).");
            return;
        }

        int eventId = readInt("Event ID: ");
        if (!eventDAO.eventExists(eventId)) {
            System.out.println("Event not found. Please check the event ID (option 4).");
            return;
        }

        // Duplicate check
        if (registrationDAO.isDuplicate(studentId, eventId)) {
            System.out.println("Duplicate registration: this student is already registered for this event.");
            return;
        }

        // Capacity check
        if (!registrationDAO.hasSeatsLeft(eventId)) {
            System.out.println("Registration closed: this event has reached its maximum participants.");
            return;
        }

        if (registrationDAO.registerStudent(studentId, eventId)) {
            System.out.println("Registration successful.");
        }
    }

    // ------------------------------------------------------------
    // Option 7: View Registered Participants
    // ------------------------------------------------------------
    private static void viewParticipantsMenu() {
        int eventId = readInt("Event ID: ");
        if (!eventDAO.eventExists(eventId)) {
            System.out.println("Event not found.");
            return;
        }

        ArrayList<Registration> list = registrationDAO.getParticipantsByEvent(eventId);
        if (list.isEmpty()) {
            System.out.println("No participants registered for this event yet.");
            return;
        }

        printParticipants(list, buildNameMap());
    }

    // ------------------------------------------------------------
    // Option 8: Record Attendance
    // ------------------------------------------------------------
    private static void recordAttendanceMenu() {
        int eventId = readInt("Event ID: ");
        if (!eventDAO.eventExists(eventId)) {
            System.out.println("Event not found.");
            return;
        }

        ArrayList<Registration> list = registrationDAO.getParticipantsByEvent(eventId);
        if (list.isEmpty()) {
            System.out.println("No participants registered for this event yet.");
            return;
        }

        printParticipants(list, buildNameMap());

        int regId = readInt("Registration ID to update (from the table above): ");
        Registration selected = findRegistration(list, regId);
        if (selected == null) {
            System.out.println("That registration ID is not in this event.");
            return;
        }

        System.out.println("1. Present");
        System.out.println("2. Absent");
        int choice = readIntInRange("Choose attendance (1 or 2): ", 1, 2);
        String status = (choice == 1) ? "Present" : "Absent";

        if (registrationDAO.updateAttendance(regId, status)) {
            System.out.println("Attendance recorded as " + status + ".");
        }
    }

    // ------------------------------------------------------------
    // Option 9: Update Participation Status
    // ------------------------------------------------------------
    private static void updateParticipationMenu() {
        int eventId = readInt("Event ID: ");
        if (!eventDAO.eventExists(eventId)) {
            System.out.println("Event not found.");
            return;
        }

        ArrayList<Registration> list = registrationDAO.getParticipantsByEvent(eventId);
        if (list.isEmpty()) {
            System.out.println("No participants registered for this event yet.");
            return;
        }

        printParticipants(list, buildNameMap());

        int regId = readInt("Registration ID to update (from the table above): ");
        Registration selected = findRegistration(list, regId);
        if (selected == null) {
            System.out.println("That registration ID is not in this event.");
            return;
        }

        String[] options = {"Registered", "Participated", "Winner", "Runner-up"};
        System.out.println("1. Registered");
        System.out.println("2. Participated");
        System.out.println("3. Winner");
        System.out.println("4. Runner-up");
        int choice = readIntInRange("Choose participation status (1 to 4): ", 1, 4);
        String status = options[choice - 1];

        if (registrationDAO.updateParticipation(regId, status)) {
            System.out.println("Participation status set to " + status + ".");
        }
    }

    // ------------------------------------------------------------
    // Option 10: Generate Event Report
    // ------------------------------------------------------------
    private static void generateReportMenu() {
        ArrayList<String> lines = registrationDAO.getEventReport();

        if (lines.isEmpty()) {
            System.out.println("No events found.");
            return;
        }

        System.out.println("\n================ EVENT-WISE REPORT ================");
        System.out.printf("%-4s %-28s %-10s %-12s %-10s %-10s%n",
                "ID", "Event Name", "Max", "Registered", "Attended", "Seats Left");
        System.out.println("-----------------------------------------------------------------------------");
        for (String line : lines) {
            System.out.println(line);
        }
    }

    // ------------------------------------------------------------
    // Helper methods
    // ------------------------------------------------------------

    // Shows participants: registration ID, student ID, name, attendance, participation
    private static void printParticipants(ArrayList<Registration> list, HashMap<String, String> names) {
        System.out.printf("%-8s %-10s %-25s %-12s %-14s%n",
                "Reg ID", "Student", "Name", "Attendance", "Participation");
        System.out.println("-----------------------------------------------------------------------------------");
        for (Registration r : list) {
            String name = names.getOrDefault(r.getStudentId(), "(unknown)");
            System.out.printf("%-8d %-10s %-25s %-12s %-14s%n",
                    r.getRegistrationId(),
                    r.getStudentId(),
                    name,
                    r.getAttendanceStatus(),
                    r.getParticipationStatus());
        }
    }

    // Maps student ID to student name, so the participant list can show names
    private static HashMap<String, String> buildNameMap() {
        HashMap<String, String> map = new HashMap<>();
        for (Student s : studentDAO.getAllStudents()) {
            map.put(s.getStudentId(), s.getStudentName());
        }
        return map;
    }

    // Finds a registration in the list by its ID. Returns null if not found.
    private static Registration findRegistration(ArrayList<Registration> list, int regId) {
        for (Registration r : list) {
            if (r.getRegistrationId() == regId) {
                return r;
            }
        }
        return null;
    }

    // Reads a whole number. Keeps asking until the input is valid.
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    // Reads a whole number within a range
    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Please enter a number from " + min + " to " + max + ".");
        }
    }

    // Reads text that cannot be empty
    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    // Reads a date in yyyy-mm-dd format
    private static String readDate(String prompt) {
        while (true) {
            String text = readNonEmpty(prompt);
            try {
                LocalDate.parse(text);
                return text;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use yyyy-mm-dd, for example 2026-12-05.");
            }
        }
    }
}
