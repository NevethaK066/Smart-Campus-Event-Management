import java.sql.*;
import java.util.ArrayList;

/**
 * StudentDAO.java
 * Business logic and database access for the STUDENT table.
 */
public class StudentDAO {

    // Adds a new student. Returns true if saved.
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO STUDENT (student_id, student_name, department, year_of_study) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getStudentId());
            ps.setString(2, student.getStudentName());
            ps.setString(3, student.getDepartment());
            ps.setInt(4, student.getYearOfStudy());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Database error while adding student: " + e.getMessage());
            return false;
        }
    }

    // Returns all students as an ArrayList
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT student_id, student_name, department, year_of_study FROM STUDENT ORDER BY student_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("department"),
                        rs.getInt("year_of_study")));
            }

        } catch (SQLException e) {
            System.out.println("Database error while reading students: " + e.getMessage());
        }
        return students;
    }

    // Returns true if a student with this ID exists
    public boolean studentExists(String studentId) {
        String sql = "SELECT COUNT(*) FROM STUDENT WHERE student_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error while checking student: " + e.getMessage());
        }
        return false;
    }
}
