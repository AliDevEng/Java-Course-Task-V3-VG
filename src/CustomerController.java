/**
 * CustomerController-klassen utgör presentationslagret för kundhantering.
 * Den hanterar all interaktion med användaren genom en konsolbaserad meny.
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerController {

    // Skapa instanser av service och repository
    CustomerService customerService = new CustomerService();

    CustomerRepository customerRepository = new CustomerRepository();


    // 2. Menymetod som visar och hanterar användarens val
    public void runMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        // Visa meny
        System.out.println("1. Hämta alla kunder");
        System.out.println("2. Hämta en kund efter ID");
        System.out.println("3. Lägg till ny kund");
        System.out.println("4. Uppdatera email");
        System.out.println("5. Ta bort");
        System.out.println("0. Avsluta");

        // Läs användarens val
        String select = scanner.nextLine();

        // Hantera användarens val genom en switch-sats
        switch (select){
            case "1":
                // 8. Visa alla kunder
                ArrayList<Customer> customers = customerService.getAllCustomers();
                for (Customer customer : customers) {
                    System.out.println("KundId: " + customer.getCustomerId());
                    System.out.println("Namn: " + customer.getName());
                }
                break;

            case "2":
                // 19. Hämta och visa en specifik kund baserat på ID
                System.out.println("Ange id på den kund du vill hämta: ");
                int id = scanner.nextInt();
                scanner.nextLine(); // Rensa scanner-bufferten

                Customer customer = customerService.getCustomerById(id);
                if (customer != null) {
                    System.out.println("Kunduppgifter:");
                    System.out.println("ID: " + customer.getCustomerId());
                    System.out.println("Namn: " + customer.getName());
                    System.out.println("Email: " + customer.getEmail());
                    // Visa endast dessa värden om de finns (inte är null)
                    if (customer.getPhone() != null) {
                        System.out.println("Telefon: " + customer.getPhone());
                    }
                    if (customer.getAddress() != null) {
                        System.out.println("Adress: " + customer.getAddress());
                    }
                } else {
                    System.out.println("Kunden med ID " + id + " hittades inte.");
                }
                break;

            case "3":
                // 11. Lägga till en ny kund
                System.out.println("Ange ett namn och efternamn: ");
                String name = scanner.nextLine();
                System.out.println("Ange ett giltigt email: ");
                String email = scanner.nextLine();
                System.out.println("Ange ett password: ");
                String password = scanner.nextLine();
                customerService.insertUser(name,email,password);
                break;

            case "4":
                // 14. Uppdatera en kunds e-postadress
                System.out.println("Ange id på den kund som ska uppdateras: ");
                int customerId = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Ange ny mejladress");
                String newEmail = scanner.nextLine();
                boolean success = customerService.updateEmail(customerId,newEmail);
                System.out.println(success ? "Kundmejl uppdaterad" : "Kund hittades ej");
                break;

            case "5":
                // 17. Ta bort en kund
                System.out.println("Ange id på den kund som ska raderas");
                int idToDelete = scanner.nextInt();
                scanner.nextLine();
                boolean deleteSuccess = customerService.deleteCustomer(idToDelete);
                System.out.println(deleteSuccess ? "Kund raderad" : "Kund hittades ej");
                break;

            case "0":
                // 18. Avsluta programmet
                return;

            default:
                // Hantera ogiltigt val
                System.out.println("Ogiltigt val. Försök igen.");
                break;
        }

        // Rekursivt anrop för att visa menyn igen efter utfört val
        runMenu();

    }


}
