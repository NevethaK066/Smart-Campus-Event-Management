/**
 * Registration.java
 * Model class: holds one student's registration for one event.
 * Matches the REGISTRATION table in MySQL.
 */
public class Registration {

    // Private fields (encapsulation)
    private int registrationId;
    private String studentId;
    private int eventId;
    private String registrationDate;      // format: yyyy-MM-dd
    private String attendanceStatus;      // Present or Absent
    private String participationStatus;   // Registered, Participated, Winner, Runner-up

    // Constructor 1: used when registering a NEW student for an event
    // Default values match the database defaults.
    public Registration(String studentId, int eventId) {
        this.studentId = studentId;
        this.eventId = eventId;
        this.attendanceStatus = "Absent";
        this.participationStatus = "Registered";
    }

    // Constructor 2: used when READING a registration from the database
    public Registration(int registrationId, String studentId, int eventId,
                        String registrationDate, String attendanceStatus, String participationStatus) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        this.attendanceStatus = attendanceStatus;
        this.participationStatus = participationStatus;
    }

    // Getters
    public int getRegistrationId() {
        return registrationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public String getParticipationStatus() {
        return participationStatus;
    }

    // Setters (used by Record Attendance and Update Participation Status)
    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public void setParticipationStatus(String participationStatus) {
        this.participationStatus = participationStatus;
    }

    // Returns one printable line for the registration
    @Override
    public String toString() {
        return String.format("%-8d %-10s %-8d %-12s %-12s",
                registrationId, studentId, eventId, attendanceStatus, participationStatus);
    }
}
