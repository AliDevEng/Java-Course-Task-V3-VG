package customer;
/**
 * customer.CustomerService-klassen utgör affärslogiklagret för kundhantering.
 * Den fungerar som en förmedlare mellan controller och repository.
 */

import common.User;
import common.UserSession;
import product.Product;
import order.Order;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CustomerService {
    // Skapa en instans av customer.CustomerRepository för att kunna anropa dess metoder
    CustomerRepository customerRepository = new CustomerRepository();

    // 83. Email valideringsmönster med regex
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private boolean isValidEmail (String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // 7. Metod för att hämta alla kunder
    public ArrayList<Customer> getAllCustomers() throws SQLException {
        System.out.println("Detta är vår logiska lager");
        System.out.println("Här ordnar vi med uträkningar och nåt kul");
        return customerRepository.getAll();
    }


    // 13. Metod för att uppdatera en kunds e-postadress
    public boolean updateEmail (int customer_id, String email) throws SQLException {
        System.out.println("Service skickar vidare update");

        // Validering av kund
        if (customer_id <= 0) {
            System.out.println("OBS! Kund-ID måste vara större än 0");
            return false;
        }

        // validering av e-post
        if (!isValidEmail(email)) {
            System.out.println("Fel mejl-format! Exempel: xxx@gmail.com");
            return false;
        }


        return customerRepository.updateEmail(customer_id,email);

    }

    // 16. Metod för att ta bort en kund
    public boolean deleteCustomer (int id) throws SQLException {
        System.out.println("Service skickar vidare id");

        // Validering av kund-id
        if (id <= 0) {
            System.out.println("Fel kund-id! Kund-ID måste vara större än 0");
            return false;
        }

        return customerRepository.deleteCustomer(id);
    }

    // 10. Metod för att lägga till en ny användare/kund
    public boolean insertUser(String name, String email, String password) throws SQLException {
        System.out.println("Service skickar vidare common.User");

        // Kontroll av namn
        if (name == null || name.trim().isEmpty()) {
            System.out.println("OBS! Namn kan inte vara tomt.");
            return false;
        }

        // Validera Email
        if (!isValidEmail(email)) {
            System.out.println("Fel mejl-format! Exempel: xxx@gmail.com");
            return false;
        }

        customerRepository.insertCustomer(name,email,password);
        return true;
    }

    // 20. Metod för att hämta en specifik kund från databasen baserat på ID
    public Customer getCustomerById(int customerId) throws SQLException {
        System.out.println("Service hämtar kund med ID: " + customerId);

        if (customerId <= 0) {
            System.out.println("Fel kund-id! Kund-ID måste vara större än 0");
            return null;
        }

        return customerRepository.getCustomerById(customerId);
    }
}
