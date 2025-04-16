/**
 * OrderService-klassen utgör affärslogiklagret för orderhantering.
 * Den fungerar som en förmedlare mellan controller och repository.
 */

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderService {

    // Skapa en instans av OrderRepository för att kunna anropa dess metoder
    OrderRepository orderRepository = new OrderRepository();
    CustomerRepository customerRepository = new CustomerRepository();

    // 64. Metod för att hämta ordrar för en specifik kund
    public ArrayList<Order> getOrdersByCustomerId(int customerId) throws SQLException {
        System.out.println("OrderService hämtar ordrar för kund med ID: " + customerId);
        return orderRepository.getOrdersByCustomerId(customerId);
    }

    // 65. Metod för att hämta orderdetaljer för en specifik order
    public ArrayList<Order> getOrderItemsByOrderId(int orderId) throws SQLException {
        System.out.println("OrderService hämtar orderdetaljer för order med ID: " + orderId);
        return orderRepository.getOrderItemsByOrderId(orderId);
    }

    // 66. Metod för att kontrollera om en kund finns
    public boolean customerExists(int customerId) throws SQLException {
        Customer customer = customerRepository.getCustomerById(customerId);
        return customer != null;
    }

    // 67. Metod för att beräkna totalt pris för en order
    public double calculateOrderTotal(ArrayList<Order> orderItems) {
        double total = 0;
        for (Order item : orderItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
