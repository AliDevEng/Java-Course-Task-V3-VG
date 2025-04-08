import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerService {

    CustomerRepository customerRepository = new CustomerRepository();

    public ArrayList<Customer> getAllCustomers() throws SQLException {
        System.out.println("Detta är vår logiska lager");
        System.out.println("Här ordnar vi med uträkningar och nåt kul");
        return customerRepository.getAll();
    }



    public boolean updateEmail (int customer_id, String email) throws SQLException {
        System.out.println("Service skickar vidare update");
        return customerRepository.updateEmail(customer_id,email);

    }

    public boolean deleteCustomer (int id) throws SQLException {
        System.out.println("Service skickar vidare id");
        return customerRepository.deleteCustomer(id);
    }

    public void insertUser(String name, String email, String password) throws SQLException {
        System.out.println("Service skickar vidare User");
        customerRepository.insertCustomer(name,email,password);
    }
}
