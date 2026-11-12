import java.sql.*;
import java.util.ArrayList;

/**
 * RegistrationDAO.java
 * Business logic and database access for the REGISTRATION table.
 * Contains the duplicate check and the capacity check.
 */
public class RegistrationDAO {

    // Saves a new registration. Returns true if saved.
    // The database sets attendance = 'Absent' and participation = 'Registered' by default.
    public boolean registerStudent(String studentId, int eventId) {
        String sql = "INSERT INTO REGISTRATION (student_id, event_id) VALUES (?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setInt(2, eventId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Database error while registering: " + e.getMessage());
            return false;
        }
    }

    // Duplicate check: true if this student is already registered for this event
    public boolean isDuplicate(String studentId, int eventId) {
        String sql = "SELECT COUNT(*) FROM REGISTRATION WHERE student_id = ? AND event_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error while checking duplicate: " + e.getMessage());
        }
        return false;
    }

    // Counts how many students are registered for an event
    public int countRegistrations(int eventId) {
        String sql = "SELECT COUNT(*) FROM REGISTRATION WHERE event_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error while counting registrations: " + e.getMessage());
        }
        return 0;
    }

    // Capacity check: true if registered count is less than the maximum participants
    public boolean hasSeatsLeft(int eventId) {
        EventDAO eventDAO = new EventDAO();
        int maxParticipants = eventDAO.getMaxParticipants(eventId);
        int registered = countRegistrations(eventId);
        return registered < maxParticipants;
    }

    // Returns all registrations for one event
    public ArrayList<Registration> getParticipantsByEvent(int eventId) {
        ArrayList<Registration> list = new ArrayList<>();
        String sql = "SELECT registration_id, student_id, event_id, registration_date, "
                   + "attendance_status, participation_status "
                   + "FROM REGISTRATION WHERE event_id = ? ORDER BY registration_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Registration(
                            rs.getInt("registration_id"),
                            rs.getString("student_id"),
                            rs.getInt("event_id"),
                            rs.getString("registration_date"),
                            rs.getString("attendance_status"),
                            rs.getString("participation_status")));
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error while reading participants: " + e.getMessage());
        }
        return list;
    }

    // Sets attendance to "Present" or "Absent". Returns true if changed.
    public boolean updateAttendance(int registrationId, String status) {
        String sql = "UPDATE REGISTRATION SET attendance_status = ? WHERE registration_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, registrationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Database error while updating attendance: " + e.getMessage());
            return false;
        }
    }

    // Sets participation status. Returns true if changed.
    public boolean updateParticipation(int registrationId, String status) {
        String sql = "UPDATE REGISTRATION SET participation_status = ? WHERE registration_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, registrationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Database error while updating participation: " + e.getMessage());
            return false;
        }
    }

    // Event-wise report: one formatted line per event
    // Columns: event ID, name, max, registered, attended, seats left
    public ArrayList<String> getEventReport() {
        ArrayList<String> lines = new ArrayList<>();
        String sql = "SELECT e.event_id, e.event_name, e.max_participants, "
                   + "COUNT(r.registration_id) AS registered_count, "
                   + "SUM(CASE WHEN r.attendance_status = 'Present' THEN 1 ELSE 0 END) AS attended_count "
                   + "FROM EVENT e LEFT JOIN REGISTRATION r ON e.event_id = r.event_id "
                   + "GROUP BY e.event_id, e.event_name, e.max_participants "
                   + "ORDER BY e.event_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int max = rs.getInt("max_participants");
                int registered = rs.getInt("registered_count");
                int attended = rs.getInt("attended_count");
                int seatsLeft = max - registered;

                lines.add(String.format("%-4d %-28s %-10d %-12d %-10d %-10d",
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        max,
                        registered,
                        attended,
                        seatsLeft));
            }

        } catch (SQLException e) {
            System.out.println("Database error while building report: " + e.getMessage());
        }
        return lines;
    }
}
