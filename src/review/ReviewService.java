package review;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * ReviewService-klassen utgör affärslogiklagret för recensionshantering.
 */

public class ReviewService {

    // Skapa en instans av ReviewRepository
    private ReviewRepository reviewRepository = new ReviewRepository();


    // Metod för att lägga till en recension
    public boolean addReview(int customerId, int productId, int rating, String comment) throws SQLException {
        System.out.println("ReviewService lägger till en recension");

        // Validera rating
        if (rating < 1 || rating > 5) {
            System.out.println("Betyget måste vara mellan 1 och 5.");
            return false;
        }

        // Validera kommentar
        if (comment == null || comment.trim().isEmpty()) {
            System.out.println("Kommentaren kan inte vara tom.");
            return false;
        }

        // Kontrollera om kunden har köpt produkten
        if (!reviewRepository.hasCustomerPurchasedProduct(customerId, productId)) {
            System.out.println("Du kan bara recensera produkter du har köpt.");
            return false;
        }

        return reviewRepository.addReview(customerId, productId, rating, comment);
    }


    // Metod för att hämta recensioner för en produkt
    public ArrayList<Review> getReviewsByProductId(int productId) throws SQLException {
        System.out.println("ReviewService hämtar recensioner för produkt-ID: " + productId);

        // Validera produktID
        if (productId <= 0) {
            System.out.println("Ogiltigt produkt-ID.");
            return new ArrayList<>();
        }

        return reviewRepository.getReviewsByProductId(productId);
    }


    // Metod för att beräkna genomsnittligt betyg för en produkt
    public double getAverageRatingForProduct(int productId) throws SQLException {
        System.out.println("ReviewService beräknar genomsnittligt betyg för produkt-ID: " + productId);

        // Validera produktID
        if (productId <= 0) {
            System.out.println("Ogiltigt produkt-ID.");
            return 0.0;
        }

        return reviewRepository.getAverageRatingForProduct(productId);
    }


    // Metod för att kontrollera om en kund har köpt en produkt
    public boolean hasCustomerPurchasedProduct(int customerId, int productId) throws SQLException {
        System.out.println("ReviewService kontrollerar om kund " + customerId +
                " har köpt produkt " + productId);

        // Validera indata
        if (customerId <= 0 || productId <= 0) {
            System.out.println("Ogiltigt kund-ID eller produkt-ID.");
            return false;
        }

        return reviewRepository.hasCustomerPurchasedProduct(customerId, productId);
    }
}