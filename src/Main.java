/**
 * Main-klassen är applikationens startpunkt.
 * Den skapar en instans av CustomerController och startar menyn.
 */

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

       // Scanner scanner = new Scanner(System.in);

        //System.out.println("Ange din email: ");
        //String email = scanner.nextLine();
        //System.out.println("Ange ditt lösenord: ");
        //String password = scanner.nextLine();

        //Customer loggedInCustomer = customerRepository.login(email, password);


        // 1. Skapa en instans av CustomerController
        CustomerController customerController = new CustomerController();
        ProductController productController = new ProductController();
        OrderController orderController = new OrderController();


        // 30. Visa huvudmeny och hantera valet
        showMainMenu(customerController, productController, orderController);

        try {
            // 2. Starta menyhanteringen
            customerController.runMenu();
        } catch (SQLException e) {
            // Hantera SQL-fel genom att skriva ut felmeddelande och avsluta programmet
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    // 31. Metod för att visa huvudmeny och dirigera till rätt controller
    private static void showMainMenu
                                    (CustomerController customerController,
                                     ProductController productController,
                                     OrderController orderController) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== WEBSHOP HUVUDMENY ===");
            System.out.println("1. Kundhantering");
            System.out.println("2. Produkthantering");
            System.out.println("3. Orderhantering");
            System.out.println("0. Avsluta programmet");
            System.out.print("Ange dit val här: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // Gå till kundhanterings meny
                    customerController.runMenu();
                    break;

                case "2":
                    // Gå till produkthanterings meny
                    productController.runMenu();
                    break;

                case "3":
                    // Gå till orderhanteringsmenyn
                    orderController.runMenu();
                    break;

                case "0":
                    // Avsluta programmet
                    System.out.println("Programmet avslutas nu!");
                    running = false;
                    break;

                default:
                    System.out.println("Ej godtagbart val. Försök igen!");
                    break;
            }
        }
    }
}