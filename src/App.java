import java.util.Scanner;

import entities.*;
import enums.*;


public class App {

    static void index(){
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            System.out.println("=============Wellcome, to Event Keeper!=============");
            System.out.println("You are a:");
            System.out.println("1. Guest");
            System.out.println("2. User");
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
                default:
                System.out.println("Invalid choice! try 1 or 2.");
                    break;
            }
        }while(choice != 1 && choice != 2);
        scanner.close();
    }

    /**
     * The main entry point of the program.
     *
     * @param args unused
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception {
        String myvar = Role.ADMIN;
        User adminUser = new User("Admin", "User", Role.ADMIN, "adminpass");
        User user1 = new User("John", "Doe", Role.ADMIN, "password123");
        User user2 = new User("Jane", "Smith", Role.USER, "password456");
        index();
    }
}
