import java.sql.*;
import java.util.ArrayList;

/**
 * EventDAO.java
 * Business logic and database access for the EVENT table.
 */
public class EventDAO {

    // Adds a new event. Returns true if saved.
    public boolean addEvent(Event event) {
        String sql = "INSERT INTO EVENT (event_name, event_date, venue, max_participants) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, event.getEventName());
            ps.setString(2, event.getEventDate());
            ps.setString(3, event.getVenue());
            ps.setInt(4, event.getMaxParticipants());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Database error while adding event: " + e.getMessage());
            return false;
        }
    }

    // Updates an existing event. Returns true if changed.
    public boolean updateEvent(Event event) {
        String sql = "UPDATE EVENT SET event_name = ?, event_date = ?, venue = ?, max_participants = ? WHERE event_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, event.getEventName());
            ps.setString(2, event.getEventDate());
            ps.setString(3, event.getVenue());
            ps.setInt(4, event.getMaxParticipants());
            ps.setInt(5, event.getEventId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Database error while updating event: " + e.getMessage());
            return false;
        }
    }

    // Returns all events as an ArrayList
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        String sql = "SELECT event_id, event_name, event_date, venue, max_participants FROM EVENT ORDER BY event_date";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("event_date"),
                        rs.getString("venue"),
                        rs.getInt("max_participants")));
            }

        } catch (SQLException e) {
            System.out.println("Database error while reading events: " + e.getMessage());
        }
        return events;
    }

    // Returns one event, or null if it does not exist
    public Event getEventById(int eventId) {
        String sql = "SELECT event_id, event_name, event_date, venue, max_participants FROM EVENT WHERE event_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                            rs.getInt("event_id"),
                            rs.getString("event_name"),
                            rs.getString("event_date"),
                            rs.getString("venue"),
                            rs.getInt("max_participants"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error while finding event: " + e.getMessage());
        }
        return null;
    }

    // Returns true if an event with this ID exists
    public boolean eventExists(int eventId) {
        String sql = "SELECT COUNT(*) FROM EVENT WHERE event_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error while checking event: " + e.getMessage());
        }
        return false;
    }

    // Returns the maximum participant count for an event (0 if not found)
    public int getMaxParticipants(int eventId) {
        String sql = "SELECT max_participants FROM EVENT WHERE event_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error while reading capacity: " + e.getMessage());
        }
        return 0;
    }
}
