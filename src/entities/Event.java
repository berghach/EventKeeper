package entities;

import java.util.Date;
import enums.EventType;

public class Event {
    // Static counter to track the next ID
    private static int counter = 1;

    // Instance variables
    private int id;
    private String title;
    private Date date;
    private String location;
    private EventType type;

    // Constructor
    public Event(String title, Date date, String location, EventType type) {
        this.id = counter++;  // Assign current value of counter to id, then increment counter
        this.title = title;
        this.date = date;
        this.location = location;
        this.type = type;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    // No setter for ID to prevent manual modification

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    // toString method
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", type=" + type +
                '}';
    }
}
