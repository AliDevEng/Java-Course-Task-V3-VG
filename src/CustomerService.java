import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerService {

    CustomerRepository customerRepository = new CustomerRepository();

    public ArrayList<Customer> getAllCustomers() throws SQLException {
        System.out.println("Detta är vår logiska lager");
        System.out.println("Här ordnar vi med uträkningar och nåt kul");
        return customerRepository.getAll();
    }
}
