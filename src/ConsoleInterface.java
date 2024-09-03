import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import entities.*;
import enums.*;
import repositories.*;;


public class ConsoleInterface {
    private EventRepository eventRepo = new EventRepository();
    private UserRepository userRepo = new UserRepository();
    private RegistrationRepository registrationRepo = new RegistrationRepository();
    private static Scanner scanner = new Scanner(System.in);
    private static User authUser;

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
    // Events management dashboard
    private void EventsDisplay() {
        // Assuming eventRepo is an instance of EventRepository class
        List<Event> events = eventRepo.readAll(); // Fetch all events
    
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            System.out.println("================== Events ==================");
            for (Event event : events) {
                // Display event details
                System.out.println(event.toString());
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
        scanner.nextLine(); // Consume newline left-over
        
        List<Event> results;
        
        switch (choice) {
            case 1:
                System.out.print("\tEnter date (YYYY-MM-DD): ");
                String dateString = scanner.nextLine();
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
                String location = scanner.nextLine();
                results = eventRepo.search(null, location, null);
                break;
            case 3:
                System.out.println("\tEnter event type:");
                for (EventType type : EventType.values()) {
                    System.out.println("\t\t- " + type);
                }
                System.out.print("\tEnter type: ");
                String typeString = scanner.nextLine().toUpperCase();
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
                String dateInput = scanner.nextLine();
                Date date = null;
                if (!dateInput.isEmpty()) {
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
                    } catch (ParseException e) {
                        System.out.println("\tInvalid date format! Proceeding with no date filter.");
                    }
                }
                
                System.out.print("\tEnter location (or leave empty for none): ");
                String locationInput = scanner.nextLine();
                
                System.out.println("\tEnter event type (or leave empty for none):");
                for (EventType type : EventType.values()) {
                    System.out.println("\t\t- " + type);
                }
                String typeInput = scanner.nextLine().toUpperCase();
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
    private void ManageChosenEvent(Event event){

    }
    // Registration management dashboard
    private void RegistrationDisplay(){
        // display Registrations
        if (authUser.getRole().equals(Role.ADMIN)) {
            
        } else {
            
        }
    }
    private void EventRegister(Event event, User user){
        Registration registration = new Registration(new Date(), event, user);
        event.addRegistration(registration);
        user.addRegistration(registration);
    }
}
