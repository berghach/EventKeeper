package entities;

import enums.Role;
import java.util.List;
import java.util.ArrayList;


public class User {
    // Static counter to track the next ID
    private static int counter = 1;

    // Instance variables
    private int id;
    private String firstName;
    private String lastName;
    private Role role;
    private String email;
    private String password;
    private List<Registration> registrations;

    // Constructor
    public User(String firstName, String lastName, Role role, String email, String password) {
        this.id = counter++;  // Assign current value of counter to id, then increment counter
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.password = password;
        this.registrations = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    // No setter for ID to prevent manual modification

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
 // Method to add a registration for the user
 public void addRegistration(Registration registration) {
    this.registrations.add(registration);
}

// Method to get user registrations
public List<Registration> getRegistrations() {
    return registrations;
}

// Method to get events which the user is registered to
public List<Event> getEvents() {
    List<Event> events = new ArrayList<>();
    for (Registration registration : registrations) {
        events.add(registration.getEvent());
    }
    return events;
}

    // toString method
    @Override
    public String toString() {
        return "User{\n" +
                "\tid=" + id +",\n"+
                "\tfirstName= '" + firstName + '\'' +",\n"+
                "\tlastName= '" + lastName + '\'' +",\n"+
                "\trole= " + role +",\n"+
                "\temail= " + email +",\n"+
                '}';
    }
}
