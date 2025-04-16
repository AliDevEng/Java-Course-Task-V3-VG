/**
 * OrderController-klassen utgör presentationslagret för orderhantering.
 * Den hanterar all interaktion med användaren gällande ordrar.
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class OrderController {

    // Skapa en instans av OrderService för att kunna anropa dess metoder
    OrderService orderService = new OrderService();
    CustomerService customerService = new CustomerService();

    // 68. Menymetod som visar ordermenyn och hanterar användarens val
    public void runMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        // Visa ordermenyalternativ
        System.out.println(" == ORDERMENY == ");
        System.out.println("1. Se orderhistorik för en kund");
        System.out.println("0. Återgå till huvudmenyn");

        // Läs användarens val
        String select = scanner.nextLine();

        // Hantera användarens val genom en switch-sats
        switch (select) {
            case "1":
                // 69. Visa orderhistorik för en kund
                showOrderHistoryForCustomer(scanner);
                break;
            case "0":
                // Återgå till huvudmenyn
                return;
            default:
                System.out.println("Ogiltigt val. Försök igen.");
                break;
        }


        runMenu();
    }

    // 70. Metod för att visa orderhistorik för en specifik kund
    private void showOrderHistoryForCustomer(Scanner scanner) throws SQLException {
        System.out.println("=== ORDERHISTORIK FÖR KUND ===");

        try {
            // Visa alla kunder så att användaren kan välja en
            ArrayList<Customer> customers = customerService.getAllCustomers();

            if (customers.isEmpty()) {
                System.out.println("Det finns inga kunder i systemet.");
                return;
            }

            System.out.println("Tillgängliga kunder:");
            for (Customer customer : customers) {
                System.out.println(customer.getCustomerId() + ". " + customer.getName());
            }

            // Hämta kund-ID från användaren
            System.out.print("Ange ID på kunden vars orderhistorik du vill se: ");
            int customerId = Integer.parseInt(scanner.nextLine());

            // Kontrollera om kunden finns
            if (!orderService.customerExists(customerId)) {
                System.out.println("Kunden med ID " + customerId + " hittades inte.");
                return;
            }

            // Hämta kundens ordrar
            ArrayList<Order> orders = orderService.getOrdersByCustomerId(customerId);

            if (orders.isEmpty()) {
                System.out.println("Kunden har inga ordrar.");
                return;
            }

            // Visa ordrar
            System.out.println("\nOrderhistorik för " + orders.get(0).getCustomerName() + ":");
            System.out.println("--------------------------------------");

            for (Order order : orders) {
                System.out.println("\nOrder #" + order.getOrder_id() + " - Datum: " + order.getOrder_date());

                // Hämta orderdetaljer
                ArrayList<Order> orderItems = orderService.getOrderItemsByOrderId(order.getOrder_id());

                if (orderItems.isEmpty()) {
                    System.out.println("  Inga detaljer hittades för denna order.");
                } else {
                    System.out.println("  Orderrader:");
                    for (Order item : orderItems) {
                        System.out.println("  - " + item.getOrderItemString());
                    }

                    // Visa totalt pris för ordern
                    double total = orderService.calculateOrderTotal(orderItems);
                    System.out.println("  Totalt ordervärde: " + total + " kr");
                }
                System.out.println("--------------------------------------");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Ange ett numeriskt värde för kund-ID.");
        } catch (SQLException e) {
            System.out.println("Ett fel uppstod vid hämtning av orderhistorik: " + e.getMessage());
            throw e;
        }
    }
}
