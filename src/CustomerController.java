import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerController {

    CustomerService customerService = new CustomerService();

    public void runMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Hämta alla kunder");
        String select = scanner.nextLine();

        switch (select){
            case "1":
                ArrayList<Customer> customers = customerService.getAllCustomers();
                for (Customer customer : customers) {
                    System.out.println("KundId: " + customer.getCustomerId());
                    System.out.println("Namn: " + customer.getName());
                }

        }
    }


}
