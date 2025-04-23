package review;

/**
 * Denna klass hanterar all interaktion med användaren gällande recensioner.
 */


import common.UserSession;
import product.Product;
import product.ProductService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;



public class ReviewController {

    // Skapa instanser av service-klasserna
    private ReviewService reviewService = new ReviewService();
    private ProductService productService = new ProductService();

    // Huvudmeny för recensioner
    public void runMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== RECENSIONSMENY ===");
        System.out.println("1. Lämna en recension");
        System.out.println("2. Visa recensioner för en produkt");
        System.out.println("0. Återgå till huvudmenyn");
        System.out.print("Ange ditt val: ");

        System.out.println();

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                addReview(scanner);
                break;

            case "2":
                viewReviewsForProduct(scanner);
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


    // Metod för att lägga till en recension
    private void addReview(Scanner scanner) throws SQLException {
        // Kontrollera om användaren är inloggad
        UserSession session = UserSession.getInstance();
        if (!session.isLoggedIn()) {
            System.out.println("Du måste vara inloggad för att lämna en recension.");
            return;
        }

        try {
            // Visa alla produkter
            ArrayList<Product> products = productService.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("Det finns inga produkter tillgängliga.");
                return;
            }

            System.out.println("\n=== PRODUKTER ===");
            for (Product product : products) {
                System.out.printf("%d. %s\n", product.getProduct_id(), product.getName());
            }

            // Be användaren välja produkt
            System.out.print("\nVilken produkt vill du recensera? (ange ID): ");
            int productId = Integer.parseInt(scanner.nextLine());

            // Kontrollera att produkten finns
            Product product = productService.getProductById(productId);
            if (product == null) {
                System.out.println("Produkten hittades inte.");
                return;
            }

            // Kontrollera om användaren har köpt produkten
            int customerId = session.getLoggedInCustomer().getCustomerId();
            if (!reviewService.hasCustomerPurchasedProduct(customerId, productId)) {
                System.out.println("Du kan bara recensera produkter du har köpt.");
                return;
            }

            // Be användaren ange betyg och kommentar
            System.out.print("Ange betyg (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine());

            if (rating < 1 || rating > 5) {
                System.out.println("Betyget måste vara mellan 1 och 5.");
                return;
            }

            System.out.print("Skriv din recension: ");
            String comment = scanner.nextLine();

            if (comment.trim().isEmpty()) {
                System.out.println("Recensionen kan inte vara tom.");
                return;
            }

            // Lägg till recensionen
            boolean success = reviewService.addReview(customerId, productId, rating, comment);

            if (success) {
                System.out.println("Tack för din recension!");
            } else {
                System.out.println("Kunde inte lägga till recensionen.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Ange ett numeriskt värde.");
        } catch (SQLException e) {
            System.out.println("Ett fel uppstod: " + e.getMessage());
            throw e;
        }
    }


    // Metod för att visa recensioner för en produkt
    private void viewReviewsForProduct(Scanner scanner) throws SQLException {
        try {
            // Visa alla produkter
            ArrayList<Product> products = productService.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("Det finns inga produkter tillgängliga.");
                return;
            }

            System.out.println("\n=== PRODUKTER ===");
            for (Product product : products) {
                System.out.printf("%d. %s\n", product.getProduct_id(), product.getName());
            }

            // Be användaren välja produkt
            System.out.print("\nVilken produkt vill du se recensioner för? (ange ID): ");
            int productId = Integer.parseInt(scanner.nextLine());

            // Kontrollera att produkten finns
            Product product = productService.getProductById(productId);
            if (product == null) {
                System.out.println("Produkten hittades inte.");
                return;
            }

            // Hämta recensioner för produkten
            ArrayList<Review> reviews = reviewService.getReviewsByProductId(productId);

            if (reviews.isEmpty()) {
                System.out.println("Det finns inga recensioner för denna produkt.");
            } else {
                // Visa genomsnittligt betyg
                double averageRating = reviewService.getAverageRatingForProduct(productId);
                System.out.printf("\n=== RECENSIONER FÖR %s ===\n", product.getName());
                System.out.printf("Genomsnittligt betyg: %.1f/5.0\n\n", averageRating);

                // Visa alla recensioner
                for (Review review : reviews) {
                    System.out.println(review.toString());
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Ange ett numeriskt värde.");
        } catch (SQLException e) {
            System.out.println("Ett fel uppstod: " + e.getMessage());
            throw e;
        }
    }
}