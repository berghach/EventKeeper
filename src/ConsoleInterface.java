import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import entities.*;
import enums.*;
import repositories.*;;


public class ConsoleInterface {
    private EventRepository eventRepo = new EventRepository();
    private UserRepository userRepo = new UserRepository();
    private RegistrationRepository registrationRepo = new RegistrationRepository();
    private static Scanner scanner = new Scanner(System.in);
    private static User authUser;

    public ConsoleInterface(){
        userRepo.create(new User("Admin", "User", Role.ADMIN, "admin@exemple.com", "adminpass"));
        userRepo.create(new User("John", "Doe", Role.ADMIN, "john.doe@mail.com", "password123"));
        userRepo.create(new User("Jane", "Smith", Role.USER, "jane.smith@mail.com", "password456"));
}

    public void index(){
        int choice;
        do{
            System.out.println("=============Wellcome, to Event Keeper!=============");
            System.out.println("\tYou are a:");
            System.out.println("\t\t1. Guest (First time in this app)");
            System.out.println("\t\t2. User (You already have an account)");
            System.out.println("\t\t3. Administrator");
            System.out.println("\t\t4. Exit");
            System.out.println("====================================================");
            System.out.print("\tEntrez votre choix : ");
            choice = scanner.nextInt();

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
                    break;
            }
        }while(choice < 1 || choice > 4);
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
        }
    }
    // Admin Dashboard
    private  void AdminMenu(){
        System.out.println("============= Admin Menu: =============");
        System.out.println("\t1. Manage Events.");
        System.out.println("\t2. Manage Users.");
        System.out.println("\t3. Log out.");
        System.out.println("\t4. Exit.");

    }
    private  String Report(){
        return "this is the report";
    }
    // User dashboard
    private  void UserMenu(){
        System.out.println("============= User Menu: =============");
        System.out.println("\t1. My Events.");
        System.out.println("\t2. My Registrations.");
        System.out.println("\t3. Log out.");
        System.out.println("\t4. Exit.");

    }
}
