import java.util.Scanner;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
// import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import entities.*;
import enums.*;
import repositories.*;;


public class ConsoleInterface {
    private EventRepository eventRepo = new EventRepository();
    private UserRepository userRepo = new UserRepository();
    private RegistrationRepository registrationRepo = new RegistrationRepository();
    private static Scanner scanner = new Scanner(System.in);
    private static User authUser; // store the authenticated user

    public ConsoleInterface() {
        // Intializing the project
        // Adding users
        User admin = new User("Admin", "User", Role.ADMIN, "admin@example.com", "adminpass");
        User johnDoe = new User("John", "Doe", Role.USER, "john.doe@mail.com", "password123");
        User janeSmith = new User("Jane", "Smith", Role.USER, "jane.smith@mail.com", "password456");
    
        // Persisting users in the repository
        userRepo.create(admin);
        userRepo.create(johnDoe);
        userRepo.create(janeSmith);
    
        // Adding events
        Event event1 = new Event("Java Conference", new Date(), "New York", EventType.CONFERENCE);
        Event event2 = new Event("Tech Workshop", new Date(), "San Francisco", EventType.WORKSHOP);
        Event event3 = new Event("Startup Pitch", new Date(), "Boston", EventType.MEETING);
    
        // Persisting events in the repository
        eventRepo.create(event1);
        eventRepo.create(event2);
        eventRepo.create(event3);
    
        // Adding registrations
        Registration reg1 = new Registration(new Date(), event1, johnDoe);
        Registration reg2 = new Registration(new Date(), event2, janeSmith);
        Registration reg3 = new Registration(new Date(), event1, janeSmith);
    
        // Persisting registrations in the repository
        registrationRepo.create(reg1);
        registrationRepo.create(reg2);
        registrationRepo.create(reg3);
    
        // Associating registrations with users
        johnDoe.addRegistration(reg1);
        janeSmith.addRegistration(reg2);
        janeSmith.addRegistration(reg3);
    
        // Associating registrations with events
        event1.addRegistration(reg1);
        event2.addRegistration(reg2);
        event1.addRegistration(reg3);
    }
    public void index(){
        authUser = null; // Reset the authenticated user
        System.out.println("=============Wellcome, to Event Keeper!=============");
        System.out.println("\tYou are a:");
        System.out.println("\t\t1. Guest (First time in this app)");
        System.out.println("\t\t2. User (You already have an account)");
        System.out.println("\t\t3. Administrator");
        System.out.println("\t\t4. Exit");
        System.out.println("====================================================");
        System.out.print("\tEnter your choice : ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                Register();
                break;
            case 2:
                Authenticate();
                break;
            case 3:
                Authenticate();
                break;
            case 4:
                break;
            default:
                System.out.println("\tInvalid choice! try one of the choices above.");
                index();
                break;
        }
        scanner.close();
    }
    // User can sign up to the app
    private void Register(){
        System.out.println("============= Create your account =============");
        System.out.println("\tInsert your informations");
        System.out.print("\t\tEnter your first name: ");
        String firstName = scanner.next();
        System.out.print("\t\tEnter your last name: ");
        String lastName = scanner.next();
        System.out.print("\t\tEnter your email: ");
        String email = scanner.next();
        System.out.print("\t\tEnter your password: ");
        String password = scanner.next();

        User newUser = new User(firstName, lastName, Role.USER, email, password);
        userRepo.create(newUser);
        System.out.println("\tAccount created successfully!");
        System.out.println("===============================================");
        // redirection to login
        Authenticate();
    }
    // Verify User's credentials
    private  void Authenticate(){
        System.out.println("============= You already have an account =============");
        System.out.println("\tLogin:");
        System.out.print("\t\tEnter your email: ");
        String email = scanner.next();
        System.out.print("\t\tEnter your password: ");
        String password = scanner.next();
        
        // get a user that match the inserted emeil
        User user = userRepo.findByEmail(email);
        // verify the user's credentials
        if (user != null && user.getPassword().equals(password)) {
            // Successful authentication
            System.out.println("\tLogin successful! Welcome " + user.getFirstName() + " " + user.getLastName());
            System.out.println("=======================================================");
            // Set the authenticated user
            authUser = user;
            // redirect the user based on his role
            if (authUser.getRole().equals(Role.ADMIN)) {
                AdminMenu();
            }else{
                UserMenu();
            }
        } else { // Failed authentication
            System.out.println("\tLogin failed! Invalid email or password.\n\t Try again.");
            System.out.println("=======================================================");
            Authenticate();
        }
    }
    // Admin Dashboard
    private void AdminMenu() {
        System.out.println("============= Admin Menu: =============");
        System.out.println("\t1. Manage Events."); // To manage events and their participants
        System.out.println("\t2. Search for an event."); // Search 
        System.out.println("\t3. Manage Users."); // To manage users and their registrations
        System.out.println("\t4. Manage Registrations."); // To manage registrations
        System.out.println("\t5. Global report.");
        System.out.println("\t6. Log out.");
        System.out.println("=======================================");
        System.out.print("\tEnter your choice : ");
        int choice = scanner.nextInt();
    
        switch (choice) {
            case 1:
                System.out.println("Managing events...");
                EventsDisplay();
                EventManageMenu(authUser.getRole());
                break;
            case 2:
                System.out.println("Search...");
                SearchMenu();
                break;
            case 3:
                System.out.println("Managing users...");
                UsersDisplay();
                UserManageMenu();
                break;
            case 4:
                System.out.println("Managing registrations...");
                RegistrationDisplay();
                break;
            case 5:
                GlobalReport();
                AdminMenu();
                break;
            case 6:
                System.out.println("Logging out...");
                index();
                break;
            default:
                System.out.println("\tInvalid choice! Please try one of the choices above.");
                AdminMenu();
                break;
        }
    }
    private String GlobalReport() {
        int totalUsers = userRepo.readAll().size();
        int totalEvents = eventRepo.readAll().size();
        int totalRegistrations = registrationRepo.readAll().size();

        // Building the report string with the collected data
        String report = "\tStatistics Report:\n" +
                        "\t\tTotal Users: " + totalUsers + "\n" +
                        "\t\tTotal Events: " + totalEvents + "\n" +
                        "\t\tTotal Registrations: " + totalRegistrations;

        return report;
    }
    // User dashboard 
    private  void UserMenu(){
        System.out.println("============= User Menu: =============");
        System.out.println("\t1. Events.");// to see events and register to them
        System.out.println("\t2. Search for an event."); // Search 
        System.out.println("\t3. My Registrations.");// to manage user's own registration
        System.out.println("\t4. Log out.");
        System.out.println("======================================");
        System.out.print("\tEnter your choice : ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Displaying events...");
                EventsDisplay();
                EventManageMenu(authUser.getRole());
                break;
            case 2:
                System.out.println("Search...");
                SearchMenu();
                break;
            case 3:
                System.out.println("Managing your registrations...");
                RegistrationDisplay();
                break;
            case 4:
                System.out.println("Logging out...");
                index();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                UserMenu();
                break;
        }
    }
    // Users management dashboard (for admin only)
    private void UsersDisplay(){
        // Display users list
        List<User> users = userRepo.readAll();
        if (users.isEmpty()) {
            System.out.println("No users available.");
        } else {
            System.out.println("================== Users ==================");
            for (User user : users){
                // Display user details
                System.out.println(user.toString());
            }
            System.out.println("===========================================");
        }
    }
    private void UserManageMenu(){
        // Management choices for admin
        System.out.println("============= User Management Menu: =============");
        System.out.println("\t1. Add a User.");// to add an user
        System.out.println("\t2. Select a desired user to manage."); // to select the user wanted to manage edit, delete, see his registrations
        System.out.println("\t3. Back to Main Menu.");
        System.out.println("======================================");
        System.out.print("\tEnter your choice : ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Add a User...");
                addUser();
                break;
            case 2:
                // Show the selected user along with the user manege menu
                System.out.println("============= Choose an user: =============");
                System.out.println("\tEnter your choice (the user ID): ");
                int userID = scanner.nextInt();
                if (userRepo.read(userID).isPresent()) {
                    ManageChosenUser(userRepo.read(userID).get());
                } else {
                    System.out.println("\tThis is not an available choice.");
                    UserManageMenu();
                }
                break;
            case 3:
                System.out.println("Back to Main Menu...");
                AdminMenu();
                break;
            default:
                System.out.println("\tInvalid choice! Try one of the options above.");
                UserManageMenu();
                break;
        }
    }
    private void ManageChosenUser(User user){
        // display the item 
        System.out.println(user.toString());
        System.out.println("=============== Menu: ===============");
        System.out.println("\t1. See Registrations.");
        System.out.println("\t2. Edit The User.");
        System.out.println("\t3. Delete The User");
        System.out.println("\t4. Back to Main Menu.");
        System.out.println("=====================================");
        System.out.print("\tEnter your choice : ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                UserRegistrations(user);
                break;
            case 2:
                editUser(user);
                break;
            case 3:
                deleteUser(user);
                break;
            case 4:
                UserManageMenu();
                break;
            default:
                System.out.println("\tInvalid choice! Please try again.");
                ManageChosenUser(user);
                break;
        }
    }
    private void addUser() {
        System.out.println("=== Add New User ===");
    
        // Get user details
        System.out.print("Enter first name: ");
        String firstName = scanner.next();
    
        System.out.print("Enter last name: ");
        String lastName = scanner.next();
    
        System.out.print("Enter email: ");
        String email = scanner.next();
    
        System.out.print("Enter password: ");
        String password = scanner.next();
    
        
    
        // Create and add the new user
        User newUser = new User(firstName, lastName, Role.USER, email, password);
        userRepo.create(newUser);
    
        System.out.println("User added successfully!");
        UserManageMenu(); // Return to the User Management Menu
    }
    
    private void editUser(User user) {
        System.out.println("=== Edit User ===");
    
        System.out.println("Current user details:");
        System.out.println(user.toString());
    
        // Edit first name
        System.out.print("Enter new first name (or press Enter to keep '" + user.getFirstName() + "'): ");
        String firstName = scanner.next();
        if (!firstName.isEmpty()) {
            user.setFirstName(firstName);
        }
    
        // Edit last name
        System.out.print("Enter new last name (or press Enter to keep '" + user.getLastName() + "'): ");
        String lastName = scanner.next();
        if (!lastName.isEmpty()) {
            user.setLastName(lastName);
        }
    
        // Edit email
        System.out.print("Enter new email (or press Enter to keep '" + user.getEmail() + "'): ");
        String email = scanner.next();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
    
        // Edit password
        System.out.print("Enter new password (or press Enter to keep the current password): ");
        String password = scanner.next();
        if (!password.isEmpty()) {
            user.setPassword(password);
        }
    
        // Update the user in the repository
        userRepo.update(user);
    
        System.out.println("User updated successfully!");
        ManageChosenUser(user); // Return to managing the chosen user
    }
    
    private void deleteUser(User user) {
        System.out.println("=== Delete User ===");
    
        System.out.println("You are about to delete the following user:");
        System.out.println(user.toString());
    
        System.out.print("Are you sure you want to delete this user? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
    
        switch (confirmation) {
            case "yes":
                if (userRepo.delete(user.getId())) {
                    System.out.println("User deleted successfully!");
                } else {
                    System.out.println("Failed to delete the user. Please try again.");
                }
                break;
            case "no":
                System.out.println("User deletion canceled.");
                break;
            default:
                System.out.println("Invalid choice! Please enter 'yes' or 'no'.");
                deleteUser(user); // Retry confirmation
                break;
        }
    
        UserManageMenu(); // Return to the user management menu
    }
    
    private void UserRegistrations(User user) {
        List<Registration> registrations = user.getRegistrations();
        if (registrations.isEmpty()) {
            System.out.println(user.getFirstName() + " has no event registrations.");
        } else {
            System.out.println(user.getFirstName() + "'s Registrations:");
            for (Registration reg : registrations) {
                System.out.println(reg.toString());  // Assuming the Registration class has a proper toString method.
            }
        }
    }
    
    // Events management dashboard
    private void EventsDisplay() {
        List<Event> events = eventRepo.readAll(); // Fetch all events
    
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            System.out.println("================== Events ==================");
            for (Event event : events) {
                // Display event details
                System.out.println(event.toString());
                if (authUser.getRole().equals(Role.ADMIN)) {
                    System.out.println("Number of Participants: " + event.getParticipants().size() +",\n");
                }
            }
            System.out.println("============================================");
        }
    }
    private void SearchMenu() {
        System.out.println("============= Search Events =============");
        System.out.println("\t1. Search by Date.");
        System.out.println("\t2. Search by Location.");
        System.out.println("\t3. Search by Event Type.");
        System.out.println("\t4. Search by Multiple Criteria.");
        System.out.println("\t5. Back to Main Menu.");
        System.out.println("=========================================");
        System.out.print("\tEnter your choice: ");
        
        int choice = scanner.nextInt();
        
        List<Event> results;
        
        switch (choice) {
            case 1:
                System.out.print("\tEnter date (YYYY-MM-DD): ");
                String dateString = scanner.next();
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                    results = eventRepo.search(date, null, null);
                } catch (ParseException e) {
                    System.out.println("\tInvalid date format! Please try again.");
                    return;
                }
                break;
            case 2:
                System.out.print("\tEnter location: ");
                String location = scanner.next();
                results = eventRepo.search(null, location, null);
                break;
            case 3:
                System.out.println("\tEnter event type:");
                for (EventType type : EventType.values()) {
                    System.out.println("\t\t- " + type);
                }
                System.out.print("\tEnter type: ");
                String typeString = scanner.next().toUpperCase();
                try {
                    EventType type = EventType.valueOf(typeString);
                    results = eventRepo.search(null, null, type);
                } catch (IllegalArgumentException e) {
                    System.out.println("\tInvalid event type! Please try again.");
                    return;
                }
                break;
            case 4:
                System.out.print("\tEnter date (YYYY-MM-DD, or leave empty for none): ");
                String dateInput = scanner.next();
                Date date = null;
                if (!dateInput.isEmpty()) {
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
                    } catch (ParseException e) {
                        System.out.println("\tInvalid date format! Proceeding with no date filter.");
                    }
                }
                
                System.out.print("\tEnter location (or leave empty for none): ");
                String locationInput = scanner.next();
                
                System.out.println("\tEnter event type (or leave empty for none):");
                for (EventType type : EventType.values()) {
                    System.out.println("\t\t- " + type);
                }
                String typeInput = scanner.next().toUpperCase();
                EventType typeInputEnum = null;
                if (!typeInput.isEmpty()) {
                    try {
                        typeInputEnum = EventType.valueOf(typeInput);
                    } catch (IllegalArgumentException e) {
                        System.out.println("\tInvalid event type! Proceeding with no type filter.");
                    }
                }
                
                results = eventRepo.search(date, locationInput, typeInputEnum);
                break;
            case 5:
                System.out.println("Back to main menu...");
                if (authUser.getRole().equals(Role.ADMIN)) {
                    AdminMenu();
                } else {
                    UserMenu();
                }
                return;
            default:
                System.out.println("\tInvalid choice! Try one of the options above.");
                SearchMenu();
                return;
        }
        if (results.isEmpty()) {
            System.out.println("\tNo events found matching your criteria.");
        } else {
            System.out.println("\tSearch Results:");
            for (Event event : results) {
                System.out.println(event.toString());
            }
        }
    }
    private void EventManageMenu(Role role){
        if (role.equals(Role.ADMIN)) {
            // Management choices for admin
            System.out.println("============= Event Management Menu: =============");
            System.out.println("\t1. Add an event.");// to add an event
            System.out.println("\t2. Select a desired event to manage."); // to select the event wanted to manage edit, delete, see its participants or registrations
            System.out.println("\t3. Back to Main Menu.");
            System.out.println("======================================");
            System.out.print("\tEnter your choice : ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    // Add Event method
                    addEvent();
                    break;
                case 2:
                    // Show the selected event along with the event manege menu
                    System.out.println("============= Choose an event: =============");
                    System.out.println("\tEnter your choice (the event ID): ");
                    int eventID = scanner.nextInt();
                    if (eventRepo.read(eventID).isPresent()) {
                        ManageChosenEvent(eventRepo.read(eventID).get());
                    } else {
                        System.out.println("\tThis is not an available choice.");
                        EventManageMenu(authUser.getRole());
                    }
                    break;
                case 3:
                    System.out.println("Back to main menu...");
                    AdminMenu();
                    break;
                default:
                    System.out.println("\tInvalid choice! Try one of the options above.");
                    EventManageMenu(authUser.getRole());
                    break;
            }
        } else {// for User
            System.out.println("=============== Menu: ===============");
            System.out.println("\t1. Select a desired event which you want to register to."); // to select the event wanted to manage edit, delete, see its participants or registrations
            System.out.println("\t2. Back to Main Menu.");
            System.out.println("=====================================");
            System.out.print("\tEnter your choice : ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    // Register to the chosen event
                    System.out.println("============= Choose an event: =============");
                    System.out.println("\tEnter your choice (the event ID): ");
                    int eventID = scanner.nextInt();
                    if (eventRepo.read(eventID).isPresent()) {
                        EventRegister(eventRepo.read(eventID).get(), authUser);
                    } else {
                        System.out.println("\tThis is not an available choice.");
                        EventManageMenu(authUser.getRole());
                    }
                    break;
                case 2:
                    System.out.println("Back to main menu...");
                    UserMenu();
                    break;
                default:
                    System.out.println("\tInvalid choice! Try one of the options above.");
                    EventManageMenu(authUser.getRole());
                    break;
            }
        }
    }
    // Method to add Event
    private void addEvent() {
        System.out.println("=============== Add New Event ===============");
    
        System.out.print("Enter event title: ");
        String title = scanner.nextLine();
    
        Date date = null;
        boolean validDate = false;
        while (!validDate) {
            System.out.print("Enter event date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                validDate = true; // Date is valid, exit loop
            } catch (ParseException e) {
                System.out.println("Invalid date format! Please enter the date in 'yyyy-MM-dd' format.");
            }
        }
    
        System.out.print("Enter event location: ");
        String location = scanner.nextLine();
    
        EventType type = null;
        boolean validType = false;
        while (!validType) {
            // Display event type choices
            System.out.println("Select event type:");
            System.out.println("1. CONFERENCE");
            System.out.println("2. SEMINAR");
            System.out.println("3. MEETING");
            System.out.println("4. WORKSHOP");
            System.out.print("Enter your choice (1-4): ");
            int typeChoice = scanner.nextInt();
            
            switch (typeChoice) {
                case 1:
                    type = EventType.CONFERENCE;
                    validType = true;
                    break;
                case 2:
                    type = EventType.SEMINAR;
                    validType = true;
                    break;
                case 3:
                    type = EventType.MEETING;
                    validType = true;
                    break;
                case 4:
                    type = EventType.WORKSHOP;
                    validType = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                    break;
            }
        }
    
        // Create new Event object
        Event newEvent = new Event(title, date, location, type); // Ensure your Event class has a default constructor
        
        // Add event to the repository
        eventRepo.create(newEvent);
    
        System.out.println("Event added successfully!");
        EventManageMenu(authUser.getRole()); // Return to the event management menu
    }
    // Method to edit a chosen Event
    private void editEvent(Event event) {
        System.out.println("=== Edit Event ===");
    
        System.out.println("Current event details:");
        System.out.println(event.toString());
    
        // Editing event title
        System.out.print("Enter new event title (or press Enter to keep '" + event.getTitle() + "'): ");
        String title = scanner.next();
        if (!title.isEmpty()) {
            event.setTitle(title);
        }
    
        // Editing event date
        Date date = null;
        boolean validDate = false;
        while (!validDate) {
            System.out.print("Enter new event date (yyyy-MM-dd, or press Enter to keep '" + new SimpleDateFormat("yyyy-MM-dd").format(event.getDate()) + "'): ");
            String dateStr = scanner.next();
            if (dateStr.isEmpty()) {
                validDate = true; // No change, so it's valid
            } else {
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                    event.setDate(date);
                    validDate = true; // Date is valid, exit loop
                } catch (ParseException e) {
                    System.out.println("Invalid date format! Please enter the date in 'yyyy-MM-dd' format.");
                }
            }
        }
    
        // Editing event location
        System.out.print("Enter new event location (or press Enter to keep '" + event.getLocation() + "'): ");
        String location = scanner.next();
        if (!location.isEmpty()) {
            event.setLocation(location);
        }
    
        // Editing event type
        EventType type = null;
        boolean validType = false;
        while (!validType) {
            scanner.nextLine();
            System.out.println("Select new event type (or press Enter to keep '" + event.getType() + "'):");
            System.out.println("1. CONFERENCE");
            System.out.println("2. SEMINAR");
            System.out.println("3. MEETING");
            System.out.println("4. WORKSHOP");
            System.out.print("Enter your choice (1-4): ");
            String typeStr = scanner.next();
            if (typeStr.isEmpty()) {
                validType = true; // No change, so it's valid
            } else {
                switch (typeStr) {
                    case "1":
                        type = EventType.CONFERENCE;
                        validType = true;
                        break;
                    case "2":
                        type = EventType.SEMINAR;
                        validType = true;
                        break;
                    case "3":
                        type = EventType.MEETING;
                        validType = true;
                        break;
                    case "4":
                        type = EventType.WORKSHOP;
                        validType = true;
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 4.");
                        break;
                }
            }
        }
        if (type != null) {
            event.setType(type);
        }
    
        // Update the event in the repository
        eventRepo.update(event);
    
        System.out.println("Event updated successfully!");
        ManageChosenEvent(event); // Return to managing the chosen event
    }
    // Method to delete a chosen Event
    private void deleteEvent(Event event) {
        System.out.println("=== Delete Event ===");
    
        System.out.println("You are about to delete the following event:");
        System.out.println(event.toString());
    
        System.out.print("Are you sure you want to delete this event? (Yes/No) Yes: ");
        String confirmation = scanner.next().trim().toLowerCase();
    
        if (confirmation.isBlank()) {
            confirmation = "yes";
        }
        switch (confirmation) {
            case "yes":
                if (eventRepo.delete(event.getId())) {
                    System.out.println("Event deleted successfully!");
                } else {
                    System.out.println("Failed to delete the event. Please try again.");
                }
                break;
            case "no":
                System.out.println("Event deletion canceled.");
                break;
            default:
                System.out.println("Invalid choice! Please enter 'yes' or 'no'.");
                deleteEvent(event); // Retry confirmation
                break;
        }
    
        EventManageMenu(authUser.getRole()); // Return to the event management menu
    }
    // Method to get a chosen Event manage choices
    private void ManageChosenEvent(Event event){
        // display the item 
        System.out.println(event.toString());
        System.out.println("=============== Menu: ===============");
        System.out.println("\t1. See Participants.");
        System.out.println("\t2. See Registrations.");
        System.out.println("\t3. Edit The Event.");
        System.out.println("\t4. Delete The Event");
        System.out.println("\t5. Back to Main Menu.");
        System.out.println("=====================================");
        System.out.print("\tEnter your choice : ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                seeParticipants(event);
                break;
            case 2:
                seeRegistrations(event);
                break;
            case 3:
                editEvent(event);
                break;
            case 4:
                deleteEvent(event);
                break;
            case 5:
                AdminMenu();
                break;
            default:
                System.out.println("\tInvalid choice! Please try again.");
                ManageChosenEvent(event);
                break;
        }
    }
    // Method to see participants of the event
    private void seeParticipants(Event event) {
        List<User> participants = event.getParticipants();
        if (participants.isEmpty()) {
            System.out.println("No participants for this event.");
        } else {
            System.out.println("Participants:");
            for (User participant : participants) {
                System.out.println("\t" + participant.toString());
            }
        }
    }
    // Method to see registrations of the event
    private void seeRegistrations(Event event) {
        List<Registration> registrations = event.getRegistrations();
        if (registrations.isEmpty()) {
            System.out.println("No registrations for this event.");
        } else {
            System.out.println("Registrations:");
            for (Registration registration : registrations) {
                System.out.println("\t" + registration.toString());
            }
        }
    }
    // Registration management dashboard
    private void RegistrationDisplay(){
        // display Registrations
        if (authUser.getRole().equals(Role.ADMIN)) {
            List <Registration> registrations = registrationRepo.readAll();

            if (registrations.isEmpty()) {
                System.out.println("No Registrations available.");
                AdminMenu();
            } else {
                System.out.println("================== registrations ==================");
                for (Registration registration : registrations) {
                    // Display registration details
                    System.out.println(registration.toString());
                }
                System.out.println("============================================");
                RegistrationManageMenu(authUser.getRole());
            }
        } else {
            List <Registration> registrations = authUser.getRegistrations();
            if (registrations.isEmpty()) {
                System.out.println("No Registrations available.");
                UserMenu();
            } else {
                System.out.println("================== My registrations ==================");
                for (Registration registration : registrations) {
                    // Display registration details
                    System.out.println(registration.toString());
                }
                System.out.println("======================================================");
                RegistrationManageMenu(authUser.getRole());
            }
        }
    }
    private void EventRegister(Event event, User user) {
        try {
            // Check if event or user is null
            if (event == null) {
                throw new IllegalArgumentException("Event cannot be null.");
            }
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null.");
            }
    
            // Check if the user is already registered for the event
            boolean alreadyRegistered = event.getRegistrations().stream()
                    .anyMatch(registration -> registration.getParticipant().getId() == user.getId());
    
            if (alreadyRegistered) {
                throw new IllegalStateException("User is already registered for this event.");
            }
    
            // Create and add the registration
            Registration registration = new Registration(new Date(), event, user);
            event.addRegistration(registration);
            user.addRegistration(registration);
    
            System.out.println("Registration successful!");
            EventManageMenu(authUser.getRole());
    
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
    private void UnsubscribeFromEvent(User user, Event event){
        Registration registration = registrationRepo.readAll().stream()
                                    .filter(item -> (item.getEvent().equals(event) && item.getParticipant().equals(user)))
                                    .collect(Collectors.toList()).get(0);
        System.out.println(registration.toString());
        if (authUser.getRole().equals(Role.ADMIN)) { // for the admin 
            System.out.println("=== Unsubscribe the User"+user.getFirstName()+" from this Event "+event.getTitle()+" ===");
        
            System.out.println("You are about to delete the following Registration:");
            System.out.println(event.toString());
        
            System.out.print("Are you sure you want to Delete this registration? (Yes/No) Yes: ");
            String confirmation = scanner.next().trim().toLowerCase();
        
            if (confirmation.isBlank()) {
                confirmation = "yes";
            }
            switch (confirmation) {
                case "yes":
                    if (eventRepo.delete(event.getId())) {
                        System.out.println("Registration deleted successfully!");
                    } else {
                        System.out.println("Failed to delete the registration. Please try again.");
                        RegistrationManageMenu(authUser.getRole());
                    }
                    break;
                case "no":
                    System.out.println("Registration deletion canceled.");
                    AdminMenu();
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 'yes' or 'no'.");
                    UnsubscribeFromEvent(user, event); // Retry confirmation
                    break;
            }
        } else {
            System.out.println("=== Unsubscribe from this Event "+event.getTitle()+" ===");
        
            System.out.println("You are about to delete the following Registration:");
            System.out.println(event.toString());
        
            System.out.print("Are you sure you want to Unsubscribe ? (Yes/No) Yes: ");
            String confirmation = scanner.next().trim().toLowerCase();
        
            if (confirmation.isBlank()) {
                confirmation = "yes";
            }
            switch (confirmation) {
                case "yes":
                    if (eventRepo.delete(event.getId())) {
                        System.out.println("Unsubscribed successfully!");
                    } else {
                        System.out.println("Failed to delete the registration. Please try again.");
                        RegistrationManageMenu(authUser.getRole());
                    }
                    break;
                case "no":
                    System.out.println("Registration deletion canceled.");
                    UserMenu();
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 'yes' or 'no'.");
                    UnsubscribeFromEvent(user, event); // Retry confirmation
                    break;
            }
        }
    }
    private void RegistrationManageMenu(Role role) {
        if (role.equals(Role.ADMIN)) {
            System.out.println("============= Registration Management Menu: =============");
            System.out.println("\t1. Register a User to an Event.");
            System.out.println("\t2. Select a desired event to manage (view participants, etc.).");
            System.out.println("\t3. Back to Main Menu.");
            System.out.println("=========================================================");
            System.out.print("\tEnter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter the User ID: ");
                    int userId = scanner.nextInt();
                    Optional<User> user = userRepo.read(userId);
                    
                    if (user.isPresent()) {
                        System.out.print("Enter the Event ID: ");
                        int eventId = scanner.nextInt();
                        Optional<Event> event = eventRepo.read(eventId);
                        
                        if (event.isPresent()) {
                            EventRegister(event.get(), user.get());
                        } else {
                            System.out.println("Invalid Event ID.");
                            RegistrationManageMenu(role);
                        }
                    } else {
                        System.out.println("Invalid User ID.");
                        RegistrationManageMenu(role);
                    }
                    break;
                case 2:
                    // Manage event registrations or participants
                    System.out.print("Enter the Event ID: ");
                    int eventId = scanner.nextInt();
                    Optional<Event> event = eventRepo.read(eventId);
                    
                    if (event.isPresent()) {
                        ManageChosenEvent(event.get());
                    } else {
                        System.out.println("Invalid Event ID.");
                        RegistrationManageMenu(role);
                    }
                    break;
                case 3:
                    AdminMenu(); // Return to admin menu
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    RegistrationManageMenu(role); // Retry
                    break;
            }
        } else {
            System.out.println("============= Registration Management Menu: =============");
            System.out.println("\t1. Register to an Event.");
            System.out.println("\t2. Unsubscribe from a desired event.");
            System.out.println("\t3. Back to Main Menu.");
            System.out.println("=========================================================");
            System.out.print("\tEnter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter the Event ID: ");
                    int eventId = scanner.nextInt();
                    Optional<Event> event = eventRepo.read(eventId);
                    
                    if (event.isPresent()) {
                        EventRegister(event.get(), authUser);
                    } else {
                        System.out.println("Invalid Event ID.");
                        RegistrationManageMenu(role);
                    }
                    break;
                case 2:
                    // Unsubscribe user from event
                    System.out.print("Enter the Event ID: ");
                    int unsubEventId = scanner.nextInt();
                    Optional<Event> unsubEvent = eventRepo.read(unsubEventId);
                    
                    if (unsubEvent.isPresent()) {
                        UnsubscribeFromEvent(authUser, unsubEvent.get());
                    } else {
                        System.out.println("Invalid Event ID.");
                        RegistrationManageMenu(role);
                    }
                    break;
                case 3:
                    System.out.println("Back to main menu...");
                    UserMenu();
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    RegistrationManageMenu(role); // Retry
                    break;
            }
        }
    }
}
