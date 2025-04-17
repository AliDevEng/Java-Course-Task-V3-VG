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
    ProductService productService = new ProductService();

    // 68. Menymetod som visar ordermenyn och hanterar användarens val
    public void runMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        // Visa ordermenyalternativ
        System.out.println(" == ORDERMENY == ");
        System.out.println("1. Se orderhistorik för en kund");
        System.out.println("2. Lägga en order");
        System.out.println("3. Lägga till produkter i order");
        System.out.println("0. Återgå till huvudmenyn");

        // Läs användarens val
        String select = scanner.nextLine();

        // Hantera användarens val genom en switch-sats
        switch (select) {
            case "1":
                // 69. Visa orderhistorik för en kund
                showOrderHistoryForCustomer(scanner);
                break;

            case "2":
                // 73. Skapa en ny order
                createNewOrder(scanner);
                break;

            case "3":
                // 81. Lägga till produkter i en order
                addProductsToOrder(scanner);
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

    // 74. Metod för att skapa en ny order
    private void createNewOrder(Scanner scanner) throws SQLException {
        System.out.println("=== SKAPA NY ORDER ===");

        try {
            // Visa alla kunder så att användaren kan välja en
            ArrayList<Customer> customers = customerService.getAllCustomers();

            if (customers.isEmpty()) {
                System.out.println("Det finns inga kunder i systemet. Kan inte skapa en order.");
                return;
            }

            System.out.println("Tillgängliga kunder:");
            for (Customer customer : customers) {
                System.out.println(customer.getCustomerId() + ". " + customer.getName());
            }

            // Hämta kund-ID från användaren
            System.out.print("Välj kund (ange ID): ");
            int customerId = Integer.parseInt(scanner.nextLine());

            // Skapa ordern
            int orderId = orderService.createOrder(customerId);

            if (orderId > 0) {
                System.out.println("Order skapad med ID: " + orderId);
                System.out.println("Du kan nu lägga till produkter i ordern genom att välja 'Lägga till produkter i order' i ordermenyn.");
            } else {
                System.out.println("Det gick inte att skapa ordern.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Ange ett numeriskt värde för kund-ID.");
        } catch (SQLException e) {
            System.out.println("Ett fel uppstod vid skapande av order: " + e.getMessage());
            throw e;
        }
    }


    // 82. Metod för att lägga till produkter i en order
    private void addProductsToOrder(Scanner scanner) throws SQLException {
        System.out.println("=== LÄGG TILL PRODUKTER I ORDER ===");

        try {
            // Visa alla ordrar så att användaren kan välja en
            ArrayList<Order> orders = orderService.getAllOrders();

            if (orders.isEmpty()) {
                System.out.println("Det finns inga ordrar i systemet. Skapa en order först.");
                return;
            }

            System.out.println("Tillgängliga ordrar:");
            for (Order order : orders) {
                System.out.println(order);
            }

            // Hämta order-ID från användaren
            System.out.print("Välj order (ange ID): ");
            int orderId = Integer.parseInt(scanner.nextLine());

            // Kontrollera om ordern finns
            if (!orderService.orderExists(orderId)) {
                System.out.println("Ordern med ID " + orderId + " hittades inte.");
                return;
            }

            // Visa alla produkter så att användaren kan välja
            ArrayList<Product> products = productService.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("Det finns inga produkter i systemet. Lägg till produkter först.");
                return;
            }

            System.out.println("\nTillgängliga produkter:");
            System.out.println("ID\tNamn\t\tPris\tLagersaldo\tBeskrivning");
            System.out.println("-----------------------------------------------------------");

            for (Product product : products) {
                System.out.printf("%d\t%-15s\t%.2f kr\t%d%n",
                        product.getProduct_id(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock_quantity());
            }

            boolean addMore = true;
            while (addMore) {
                // Hämta produkt-ID från användaren
                System.out.print("\nVälj produkt att lägga till (ange ID): ");
                int productId = Integer.parseInt(scanner.nextLine());

                // Kontrollera att produkten finns
                Product product = productService.getProductById(productId);
                if (product == null) {
                    System.out.println("Produkten med ID " + productId + " hittades inte.");
                    continue;
                }

                // Hämta kvantitet från användaren
                System.out.print("Ange antal (max " + product.getStock_quantity() + "): ");
                int quantity = Integer.parseInt(scanner.nextLine());

                if (quantity <= 0) {
                    System.out.println("Antalet måste vara större än 0.");
                    continue;
                }

                // Lägg till produkten i ordern
                boolean success = orderService.addProductToOrder(orderId, productId, quantity);

                if (success) {
                    System.out.println(quantity + " st " + product.getName() + " har lagts till i ordern.");
                } else {
                    System.out.println("Kunde inte lägga till produkten i ordern.");
                }

                // Fråga om användaren vill lägga till fler produkter
                System.out.print("Vill du lägga till fler produkter i ordern? (ja/nej): ");
                String response = scanner.nextLine().trim().toLowerCase();
                addMore = response.equals("j") || response.equals("ja");
            }

            // Visa orderdetaljer efter att produkter har lagts till
            System.out.println("\nAktuell orderinformation:");
            ArrayList<Order> orderItems = orderService.getOrderItemsByOrderId(orderId);

            if (orderItems.isEmpty()) {
                System.out.println("Ordern innehåller inga produkter.");
            } else {
                System.out.println("Produkter i order #" + orderId + ":");
                for (Order item : orderItems) {
                    System.out.println("  - " + item.getOrderItemString());
                }

                // Visa totalt pris för ordern
                double total = orderService.calculateOrderTotal(orderItems);
                System.out.println("Totalt ordervärde: " + total + " kr");
            }

        } catch (NumberFormatException e) {
            System.out.println("Fel format. Ange korrekta värden för ID och antal.");
        } catch (SQLException e) {
            System.out.println("Ett fel uppstod vid tillägg av produkter: " + e.getMessage());
            throw e;
        }
    }
}
