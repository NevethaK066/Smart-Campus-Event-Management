/**
 * Event.java
 * Model class: holds one event's details.
 * Matches the EVENT table in MySQL.
 */
public class Event {

    // Private fields (encapsulation)
    private int eventId;
    private String eventName;
    private String eventDate;      // format: yyyy-MM-dd
    private String venue;
    private int maxParticipants;

    // Constructor 1: used when adding a NEW event (the database creates the ID)
    public Event(String eventName, String eventDate, String venue, int maxParticipants) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.venue = venue;
        this.maxParticipants = maxParticipants;
    }

    // Constructor 2: used when READING an event from the database (ID is known)
    public Event(int eventId, String eventName, String eventDate, String venue, int maxParticipants) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.venue = venue;
        this.maxParticipants = maxParticipants;
    }

    // Getters
    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getVenue() {
        return venue;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    // Setters (used by Update Event)
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    // Returns one printable line for the event
    @Override
    public String toString() {
        return String.format("%-4d %-28s %-12s %-22s %d", eventId, eventName, eventDate, venue, maxParticipants);
    }
}
