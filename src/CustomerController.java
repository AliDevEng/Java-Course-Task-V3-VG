import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerController {

    CustomerService customerService = new CustomerService();
    CustomerRepository customerRepository = new CustomerRepository();

    public void runMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Hämta alla kunder");
        System.out.println("2. Hämta en kund efter ID");
        System.out.println("3. Lägg till ny kund");
        System.out.println("4. Uppdatera email");
        System.out.println("5. Ta bort");
        System.out.println("0. Avsluta");


        String select = scanner.nextLine();

        switch (select){
            case "1":
                ArrayList<Customer> customers = customerService.getAllCustomers();
                for (Customer customer : customers) {
                    System.out.println("KundId: " + customer.getCustomerId());
                    System.out.println("Namn: " + customer.getName());
                }
                break;
           /* case "2":
                System.out.println("Hämta en kund");
                int id = scanner.nextInt();
                scanner.nextLine();
                Customer customer = customerRepository.getCustomerById(id);
                System.out.println(customer.getName());
                break;

            */
            case "3":
                System.out.println("Ange ett namn: ");
                String name = scanner.nextLine();
                System.out.println("Ange ett email: ");
                String email = scanner.nextLine();
                System.out.println("Ange ett password: ");
                String password = scanner.nextLine();
                customerService.insertUser(name,email,password);
                break;

            case "4":
                System.out.println("Ange id på den kund som ska uppdateras: ");
                int customerId = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Ange ny mejladress");
                String newEmail = scanner.nextLine();
                boolean success = customerService.updateEmail(customerId,newEmail);
                System.out.println(success ? "Kundmejl uppdaterad" : "Kund hittades ej");
                break;
            case "5":
                System.out.println("Ange id på den kund som ska raderas");
                int idToDelete = scanner.nextInt();
                scanner.nextLine();
                boolean deleteSuccess = customerService.deleteCustomer(idToDelete);
                System.out.println(deleteSuccess ? "Kund raderad" : "Kund hittades ej");
                break;
            case "0":
                return;
        }
    }


}
