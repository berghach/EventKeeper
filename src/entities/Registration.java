package entities;

import java.util.Date;

public class Registration {
    // Static counter to track the next ID
    private static int counter = 1;

    // Instance variables
    private int id;
    private Date date;
    private Event event;      // The event for which the participant is registering
    private User participant;  // The participant who is registering

    // Constructor
    public Registration(Date date, Event event, User participant) {
        this.id = counter++;  // Assign current value of counter to id, then increment counter
        this.date = date;
        this.event = event;
        this.participant = participant;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    // No setter for ID to prevent manual modification

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    // toString method
    @Override
    public String toString() {
        return "Registration{\n" +
                "\tid=" + id +",\n"+
                "\tdate=" + date +",\n"+
                "\tevent=" + event.toString() +",\n"+
                "\tparticipant=" + participant.toString() +",\n"+
                '}';
    }
}
