/**
 * Main-klassen är applikationens startpunkt.
 */

import common.UserSession;
import customer.Customer;
import customer.CustomerController;
import customer.CustomerRepository;
import order.OrderController;
import product.ProductController;
import shoppingcart.ShoppingCartController;


import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {


        // 1. Skapa instans
        CustomerController customerController = new CustomerController();
        ProductController productController = new ProductController();
        OrderController orderController = new OrderController();
        CustomerRepository customerRepository = new CustomerRepository();
        ShoppingCartController cartController = new ShoppingCartController();

        // Skapa en instans 
        UserSession userSession = UserSession.getInstance();

        // Hantering av inloggning
        handleLogin (customerRepository);

        // 30. Visa huvudmeny och hantera valet
        showMainMenu(customerController, productController, orderController, cartController);


        // Jag hade problem att stänga menyn så använde följande:
        // System.out.println("Programmet avslutas nu!");
        System.exit(0);

        try {
            // 2. Starta menyhanteringen
            customerController.runMenu();
        } catch (SQLException e) {
            // Hantera SQL-fel genom att skriva ut felmeddelande och avsluta programmet
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private static void handleLogin(CustomerRepository customerRepository) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        UserSession userSession = UserSession.getInstance();

        System.out.println("=== VÄLKOMMEN TILL WEBSHOP ===");
        System.out.println("Vill du logga in? (ja/nej)");
        String answer = scanner.nextLine().toLowerCase();

        if (answer.equals("ja") || answer.equals("j")) {
            boolean continueLogin = true;

            while (continueLogin) {
                System.out.print("Ange din email: ");
                String email = scanner.nextLine();
                System.out.print("Ange ditt lösenord: ");
                String password = scanner.nextLine();

                try {
                    Customer loggedInCustomer = customerRepository.loginCustomer(email, password);

                    if (loggedInCustomer != null) {
                        // Inloggningen lyckades
                        userSession.setLoggedInCustomer(loggedInCustomer);
                        System.out.println("Inloggning lyckades!");
                        loggedInCustomer.displayInfo();
                        return;  // Avsluta metoden om inloggningen lyckas
                    } else {
                        // Inloggningen misslyckades
                        System.out.println("Felaktig email eller password!");
                        System.out.println("Vill du försöka igen? (Ja/Nej)");
                        String retry = scanner.nextLine().toLowerCase();

                        if (!(retry.equals("ja") || retry.equals("j"))) {
                            continueLogin = false;  // Avsluta loopen om användaren inte vill försöka igen
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Fel vid inloggning: " + e.getMessage());
                    // Avsluta loppen om användare inte vill försöka igen
                    continueLogin = false;
                }
            }
        } else {
            System.out.println("Fortsätter utan inloggning.");
        }
    }

    // 31. Metod för att visa huvudmeny och dirigera till rätt controller
    private static void showMainMenu(
            CustomerController customerController,
            ProductController productController,
            OrderController orderController,
            ShoppingCartController cartController) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // Hämta common.UserSession-instansen
            UserSession session = UserSession.getInstance();

            // Använd session istället för userSession
            if (session.isLoggedIn()) {
                Customer loggedInCustomer = session.getLoggedInCustomer();
                System.out.println("\n=== INLOGGAD SOM: " + loggedInCustomer.getName() + " ===");
            }

            System.out.println("\n=== WEBSHOP HUVUDMENY ===");
            System.out.println("1. Kundhantering");
            System.out.println("2. Produkthantering");
            System.out.println("3. Orderhantering");

            // Visa olika menyalternativ baserat på inloggningsstatus
            if (session.isLoggedIn()) {
                System.out.println("4. Logga ut");
                System.out.println("5. Kundvagn");
            } else {
                System.out.println("4. Logga in");
            }

            System.out.println("0. Avsluta programmet");
            System.out.print("Ange ditt val här: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // Gå till kundhanteringsmeny
                    customerController.runMenu();
                    break;

                case "2":
                    // Gå till produkthanteringsmeny
                    productController.runMenu();
                    break;

                case "3":
                    // Gå till orderhanteringsmeny
                    orderController.runMenu();
                    break;

                case "4":
                    // Hantera inloggning/utloggning
                    if (session.isLoggedIn()) {
                        session.logout();
                    } else {
                        handleLogin(new CustomerRepository());
                    }
                    break;

                case "5":
                    if (session.isLoggedIn()) {
                        cartController.runMenu();
                    } else {
                        System.out.println("Du måste vara inloggad för att kunna använda kundvagnen.");
                    }
                    break;

                case "0":
                    // Avsluta programmet
                    System.out.println("Programmet avslutas nu!");
                    running = false;
                    return;

                default:
                    System.out.println("Ej godtagbart val. Försök igen!");
                    break;
            }
        }
    }
}