/**
 * Student.java
 * Model class: holds one student's details.
 * Matches the STUDENT table in MySQL.
 */
public class Student {

    // Private fields (encapsulation: other classes use getters)
    private String studentId;
    private String studentName;
    private String department;
    private int yearOfStudy;

    // Constructor: sets all four details
    public Student(String studentId, String studentName, String department, int yearOfStudy) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getDepartment() {
        return department;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    // Returns one printable line for the student
    @Override
    public String toString() {
        return String.format("%-10s %-25s %-40s %d", studentId, studentName, department, yearOfStudy);
    }
}
