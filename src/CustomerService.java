/**
 * CustomerService-klassen utgör affärslogiklagret för kundhantering.
 * Den fungerar som en förmedlare mellan controller och repository.
 */

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerService {
    // Skapa en instans av CustomerRepository för att kunna anropa dess metoder
    CustomerRepository customerRepository = new CustomerRepository();

    // 7. Metod för att hämta alla kunder
    public ArrayList<Customer> getAllCustomers() throws SQLException {
        System.out.println("Detta är vår logiska lager");
        System.out.println("Här ordnar vi med uträkningar och nåt kul");
        return customerRepository.getAll();
    }


    // 13. Metod för att uppdatera en kunds e-postadress
    public boolean updateEmail (int customer_id, String email) throws SQLException {
        System.out.println("Service skickar vidare update");
        return customerRepository.updateEmail(customer_id,email);

    }

    // 16. Metod för att ta bort en kund
    public boolean deleteCustomer (int id) throws SQLException {
        System.out.println("Service skickar vidare id");
        return customerRepository.deleteCustomer(id);
    }

    // 10. Metod för att lägga till en ny användare/kund
    public void insertUser(String name, String email, String password) throws SQLException {
        System.out.println("Service skickar vidare User");
        customerRepository.insertCustomer(name,email,password);
    }

    // 20. Metod för att hämta en specifik kund från databasen baserat på ID
    public Customer getCustomerById(int customerId) throws SQLException {
        System.out.println("Service hämtar kund med ID: " + customerId);
        return customerRepository.getCustomerById(customerId);
    }
}
