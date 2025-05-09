package order; /**
 * order.OrderService-klassen utgör affärslogiklagret för orderhantering.
 * Den fungerar som en förmedlare mellan controller och repository.
 */

import customer.Customer;
import customer.CustomerRepository;
import product.Product;
import product.ProductRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderService {

    // Skapa en instans av order.OrderRepository för att kunna anropa dess metoder
    OrderRepository orderRepository = new OrderRepository();
    CustomerRepository customerRepository = new CustomerRepository();
    ProductRepository productRepository = new ProductRepository();

    // 64. Metod för att hämta ordrar för en specifik kund
    public ArrayList<Order> getOrdersByCustomerId(int customerId) throws SQLException {
        System.out.println("order.OrderService hämtar ordrar för kund med ID: " + customerId);

        // Kontroll av kund-ID
        if (customerId <= 0) {
            System.out.println("ID måste vara större än 0");
            return new ArrayList<>();
        }

        // Kontrollera om kunden finns
        if (!customerExists(customerId)) {
            System.out.println("Kunde inte hitta kunden med ID: " + customerId);
            return new ArrayList<>();
        }

        return orderRepository.getOrdersByCustomerId(customerId);
    }

    // 65. Metod för att hämta orderdetaljer för en specifik order
    public ArrayList<Order> getOrderItemsByOrderId(int orderId) throws SQLException {
        System.out.println("order.OrderService hämtar orderdetaljer för order med ID: " + orderId);

        // Kontroll av order-ID
        if (orderId <= 0) {
            System.out.println("ID måste vara större än 0");
            return new ArrayList<>();
        }

        // Kontrollera om kunden finns
        /*if (!customerExists(orderId)) {
            System.out.println("Kunde inte hitta kunden med ID: " + orderId);
            return new ArrayList<>();
        }*/

        // Kontrollera om ordern finns
        if (!orderExists(orderId)) {
            System.out.println("Kunde inte hitta ordern med ID: " + orderId);
            return new ArrayList<>();
        }

        return orderRepository.getOrderItemsByOrderId(orderId);
    }

    // 66. Metod för att kontrollera om en kund finns
    public boolean customerExists(int customerId) throws SQLException {

        // Kontroll av kund-ID
        if (customerId <= 0) {
            return false;
        }

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


    // 72. Metod för att skapa en ny order
    public int createOrder(int customerId) throws SQLException {
        System.out.println("order.OrderService skapar en ny order för kund med ID: " + customerId);

        // Validera kund-ID
        if (customerId <= 0) {
            System.out.println("Fel! ID måste vara större än 0");
            return -1; // Returnera -1 för att indikera ett fel
        }

        // Validera att kunden finns
        if (!customerExists(customerId)) {
            System.out.println("Kunden med ID " + customerId + " hittades inte.");
            return -1; // Returnera -1 för att indikera ett fel
        }

        return orderRepository.createOrder(customerId);
    }

    // 78. Metod för att hämta alla ordrar
    public ArrayList<Order> getAllOrders() throws SQLException {
        System.out.println("order.OrderService hämtar alla ordrar");
        return orderRepository.getAllOrders();
    }

    // 79. Metod för att kontrollera om en order existerar
    public boolean orderExists(int orderId) throws SQLException {

        // Validering av order-ID
        if (orderId <= 0) {
            return false;
        }

        return orderRepository.orderExists(orderId);
    }


    // 80. Metod för att lägga till en produkt i en order
    public boolean addProductToOrder(int orderId, int productId, int quantity) throws SQLException {
        System.out.println("order.OrderService validerar och lägger till produkt i order");

        // Grundläggande validering av indata
        if (orderId <= 0) {
            System.out.println("Ogiltigt order-ID: ID måste vara större än 0");
            return false;
        }

        if (productId <= 0) {
            System.out.println("Ogiltigt produkt-ID: ID måste vara större än 0");
            return false;
        }

        if (quantity <= 0) {
            System.out.println("Ogiltig kvantitet: Antalet måste vara större än 0");
            return false;
        }

        // Validera ordern
        if (!orderExists(orderId)) {
            System.out.println("Ordern med ID " + orderId + " hittades inte.");
            return false;
        }

        // Hämta produkten för att få aktuellt pris och kontrollera lager
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            System.out.println("Produkten med ID " + productId + " hittades inte.");
            return false;
        }

        // ========102. Särskild validering av lagersaldo========

        // Kontrollera om produkten redan finns i ordern för att hantera ackumulerad kvantitet
        int currentOrderQuantity = getCurrentOrderQuantity(orderId, productId);


        // Validera att det finns tillräckligt många produkter i lager (inklusive befintlig kvantitet i ordern)
        if (product.getStock_quantity() < (quantity + currentOrderQuantity)) {
            System.out.println("Observera!!! Otillräckligt lagersaldo.");
            System.out.println("Tillgänglig just nu: " + product.getStock_quantity());
            System.out.println("Redan i order: " + currentOrderQuantity);
            System.out.println("Försöker lägga till: " + quantity);
            System.out.println("Total mängd som behövs: " + (quantity + currentOrderQuantity));
            return false;
        }

        // Lägg till produkten i ordern med aktuellt pris
        boolean success = orderRepository.addProductToOrder(orderId, productId, quantity, product.getPrice());

        if (success) {
            // Uppdatera lagersaldot
            int newQuantity = product.getStock_quantity() - quantity;
            productRepository.updateStockQuantity(productId, newQuantity);
        }

        return success;
    }

    // 103. Ny hjälpmetod för att hämta aktuell kvantitet av en produkt i en order
    private int getCurrentOrderQuantity(int orderId, int productId) throws SQLException {
        int currentQuantity = 0;

        ArrayList<Order> orderItems = getOrderItemsByOrderId(orderId);
        for (Order item : orderItems) {
            if (item.getProduct_id() == productId) {
                currentQuantity += item.getQuantity();
            }
        }

        return currentQuantity;
    }

}
