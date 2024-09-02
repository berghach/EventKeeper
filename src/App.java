import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import entities.*;
import enums.*;


public class App {

    static void index(){
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            System.out.println("=============Wellcome, to Event Keeper!=============");
            System.out.println("You are a:");
            System.out.println("1. Guest (First time in this app)");
            System.out.println("2. User");
            System.out.println("3. Admin");
            System.out.println("====================================================");
            System.out.print("Entrez votre choix : ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Create your account.");
                    break;
                case 2:
                    System.out.println("You already have an account.");
                    break;
                case 3:
                    // admin verification
                    break;
                default:
                System.out.println("Invalid choice! try one of the choices above.");
                    break;
            }
        }while(choice < 1 || choice > 3);
        scanner.close();
    }

    /**
     * The main entry point of the program.
     *
     * @param args unused
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception {
        User adminUser = new User("Admin", "User", Role.ADMIN, "adminpass");
        User user1 = new User("John", "Doe", Role.ADMIN, "password123");
        User user2 = new User("Jane", "Smith", Role.USER, "password456");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        // index();
        System.out.println(adminUser);
        users.forEach(System.out::println);
    }
}
