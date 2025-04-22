package shoppingcart;

import common.ShoppingCart;
import common.UserSession;
import product.Product;
import product.ProductService;
import order.OrderService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * ShoppingCartController-klassen hanterar användarinteraktionen för kundvagnen.
 * Den låter användaren se och hantera sin kundvagn.
 */

public class ShoppingCartController {

    // Skapa en instans av ProductService för att kunna hämta produktinformation
    private ProductService productService = new ProductService();

    private OrderService orderService = new OrderService();



    // Visar kundvagnsmenyn och hanterar användarens val
    public void runMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        UserSession userSession = UserSession.getInstance();

        // Kontrollera om användaren är inloggad
        if (!userSession.isLoggedIn()) {
            System.out.println("Du måste vara inloggad för att använda kundvagnen.");
            return;
        }

        ShoppingCart cart = userSession.getShoppingCart();

        System.out.println("=== KUNDVAGN ===");


        // Visa innehåll i kundvagn
        if (cart.isEmpty()) {
            System.out.println("Din kundvagn är tom.");
        } else {
            displayCart(cart);
        }

        System.out.println("\nVad vill du göra?");
        System.out.println("1. Lägg till produkt i kundvagnen");
        System.out.println("2. Ta bort produkt från kundvagnen");
        System.out.println("3. Ändra antal av produkt i kundvagnen");
        System.out.println("4. Slutför köp (omvandla till kundvagn till order)");
        System.out.println("0. Återgå till huvudmenyn");

        System.out.println();

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                addProductToCart(scanner);
                break;

            case "2":
                removeProductFromCart(scanner);
                break;

            case "3":
                updateProductQuantity(scanner);
                break;

            case "4":
                checkoutCart();
                break;

            case "0":
                return;

            default:
                System.out.println("Ogiltigt val. Försök igen.");
                break;
        }

        // Visa menyn igen
        runMenu();
    }

    // Visar kundvagnens innehåll och totalpris
    private void displayCart(ShoppingCart cart) {
        System.out.println("=== DINA PRODUKTER ===");

        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double totalPrice = product.getPrice() * quantity;

            System.out.printf("%s - %d st x %.2f kr = %.2f kr\n",
                    product.getName(), quantity, product.getPrice(), totalPrice);
        }

        System.out.println("========================");
        System.out.printf("Totalt: %.2f kr\n", cart.calculateTotal());
    }

    // Lägga produkt i kundvagn
    private void addProductToCart(Scanner scanner) throws SQLException {
        // Visa alla produkter
        ArrayList<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("Det finns inga produkter tillgängliga.");
            return;
        }

        System.out.println("\n=== TILLGÄNGLIGA PRODUKTER ===");
        for (Product product : products) {
            System.out.printf("%d. %s - %.2f kr - Lagersaldo: %d\n",
                    product.getProduct_id(), product.getName(),
                    product.getPrice(), product.getStock_quantity());
        }

        try {
            System.out.print("\nVälj produkt (ange ID): ");
            int productId = Integer.parseInt(scanner.nextLine());

            // Hämta produkten
            Product selectedProduct = productService.getProductById(productId);

            if (selectedProduct == null) {
                System.out.println("Produkten hittades inte.");
                return;
            }

            System.out.printf("Du har valt: %s (%.2f kr)\n",
                    selectedProduct.getName(), selectedProduct.getPrice());

            System.out.print("Ange antal (max " + selectedProduct.getStock_quantity() + "): ");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Validera antal
            if (quantity <= 0) {
                System.out.println("Antalet måste vara större än 0.");
                return;
            }

            if (quantity > selectedProduct.getStock_quantity()) {
                System.out.println("Det finns inte tillräckligt många i lager.");
                return;
            }

            // Lägg till produkten i kundvagnen
            UserSession.getInstance().getShoppingCart().addProduct(selectedProduct, quantity);

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Ange ett numeriskt värde.");
        }
    }


    // Låter användaren ta bort en produkt från kundvagnen
    private void removeProductFromCart(Scanner scanner) {
        ShoppingCart cart = UserSession.getInstance().getShoppingCart();

        // Kontrollera om kundvagnen är tom
        if (cart.isEmpty()) {
            System.out.println("Din kundvagn är tom.");
            return;
        }

        // Visa produkterna i kundvagnen med nummer
        Map<Product, Integer> items = cart.getItems();
        Product[] products = items.keySet().toArray(new Product[0]);

        System.out.println("\n=== PRODUKTER I KUNDVAGNEN ===");
        for (int i = 0; i < products.length; i++) {
            Product product = products[i];
            int quantity = items.get(product);
            System.out.printf("%d. %s - %d st - %.2f kr/st\n",
                    i + 1, product.getName(), quantity, product.getPrice());
        }

        // Vi ber användare att välja produkt att ta bort
        System.out.print("\nVälj produkt att ta bort (ange nummer): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());

            // Kontrollera att valet är giltigt
            if (choice < 1 || choice > products.length) {
                System.out.println("Ogiltigt val. Försök igen.");
                return;
            }

            // Ta bort den valda produkten
            Product selectedProduct = products[choice - 1];
            cart.removeProduct(selectedProduct);

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Ange ett heltal.");
        }
    }


    // Låter användaren ändra antalet av en produkt i kundvagnen
    private void updateProductQuantity(Scanner scanner) throws SQLException {
        ShoppingCart cart = UserSession.getInstance().getShoppingCart();

        // Kontrollera om kundvagnen är tom
        if (cart.isEmpty()) {
            System.out.println("Din kundvagn är tom.");
            return;
        }

        // Visa produkterna i kundvagnen med nummer
        Map<Product, Integer> items = cart.getItems();
        Product[] products = items.keySet().toArray(new Product[0]);

        System.out.println("\n=== PRODUKTER I KUNDVAGNEN ===");
        for (int i = 0; i < products.length; i++) {
            Product product = products[i];
            int quantity = items.get(product);
            System.out.printf("%d. %s - %d st - %.2f kr/st\n",
                    i + 1, product.getName(), quantity, product.getPrice());
        }

        // Be användaren välja produkt att ändra
        System.out.print("\nVälj produkt att ändra antal för (ange nummer): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());

            // Kontrollera att valet är giltigt
            if (choice < 1 || choice > products.length) {
                System.out.println("Ogiltigt val. Försök igen.");
                return;
            }

            // Välja produkt
            Product selectedProduct = products[choice - 1];
            int currentQuantity = items.get(selectedProduct);

            System.out.println("Vald produkt: " + selectedProduct.getName());
            System.out.println("Nuvarande antal: " + currentQuantity + " st");

            // Använd produkten från kundvagnen direkt
            int stockQuantity = selectedProduct.getStock_quantity();

            // Be användaren ange nytt antal
            System.out.print("Ange nytt antal (0 för att ta bort, max " +
                    (stockQuantity + currentQuantity) + "): ");
            int newQuantity = Integer.parseInt(scanner.nextLine());

            // Validera nytt antal
            if (newQuantity < 0) {
                System.out.println("Antalet kan inte vara negativt.");
                return;
            }

            if (newQuantity > stockQuantity + currentQuantity) {
                System.out.println("Det finns inte tillräckligt många i lager.");
                return;
            }

            // Uppdatera antalet
            cart.updateQuantity(selectedProduct, newQuantity);

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Ange ett heltal.");
        }
    }


    // Omvandlar kundvagnen till en order
    private void checkoutCart() throws SQLException {
        UserSession session = UserSession.getInstance();
        ShoppingCart cart = session.getShoppingCart();

        // Kontrollera om kundvagnen är tom
        if (cart.isEmpty()) {
            System.out.println("Din kundvagn är tom. Det finns inget att beställa.");
            return;
        }

        // Visa kundvagnens innehåll en sista gång
        displayCart(cart);

        // Fråga användaren om bekräftelse
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nVill du slutföra köpet? (ja/nej): ");
        String confirm = scanner.nextLine().toLowerCase();

        if (!confirm.equals("ja") && !confirm.equals("j")) {
            System.out.println("Köpet avbröts.");
            return;
        }

        try {
            // Skapa en ny order för den inloggade kunden
            int customerId = session.getLoggedInCustomer().getCustomerId();
            int orderId = orderService.createOrder(customerId);

            if (orderId == -1) {
                System.out.println("Kunde inte skapa order. Försök igen senare.");
                return;
            }

            // Lägg till alla produkter från kundvagnen i ordern
            boolean allProductsAdded = true;
            Map<Product, Integer> items = cart.getItems();

            for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                // Försök hämta produkten på nytt från databasen med namn
                Product databaseProduct = null;
                ArrayList<Product> matchingProducts = productService.getProductsByName(product.getName());
                if (!matchingProducts.isEmpty()) {
                    // Använd första matchande produkten
                    databaseProduct = matchingProducts.get(0);
                }

                if (databaseProduct == null) {
                    System.out.println("Kunde inte hitta produkt: " + product.getName() + " i databasen.");
                    allProductsAdded = false;
                    continue;
                }

                // Kontrollera lagersaldo en sista gång
                if (databaseProduct.getStock_quantity() < quantity) {
                    System.out.println("Varning: " + databaseProduct.getName() + " har endast " +
                            databaseProduct.getStock_quantity() + " i lager. Du försöker köpa " + quantity + ".");
                    allProductsAdded = false;
                    continue;
                }

                // Lägg till produkten i ordern
                boolean success = orderService.addProductToOrder(orderId, databaseProduct.getProduct_id(), quantity);

                if (!success) {
                    allProductsAdded = false;
                    System.out.println("Kunde inte lägga till " + databaseProduct.getName() + " i ordern.");
                }
            }

            if (allProductsAdded) {
                System.out.println("Din order har skapats med ID: " + orderId);
                System.out.println("Tack för ditt köp!");

                // Töm kundvagnen
                session.setShoppingCart(new ShoppingCart());
            } else {
                System.out.println("Det uppstod problem med vissa produkter. Kontrollera din orderhistorik.");
            }

        } catch (SQLException e) {
            System.out.println("Ett fel uppstod vid skapande av order: " + e.getMessage());
            throw e;
        }
    }
}