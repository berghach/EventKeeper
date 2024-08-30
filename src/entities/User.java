package entities;

import enums.Role;

public class User {
    // Static counter to track the next ID
    private static int counter = 1;

    // Instance variables
    private int id;
    private String firstName;
    private String lastName;
    private Role role;
    private String password;

    // Constructor
    public User(String firstName, String lastName, Role role, String password) {
        this.id = counter++;  // Assign current value of counter to id, then increment counter
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString method
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }
}
