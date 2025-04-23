package review;

import java.sql.*;
import java.util.ArrayList;

/**
 * ReviewRepository-klassen utgör datalagret för recensionshantering.
 * Den ansvarar för all direktkommunikation med databasen angående recensioner.
 */

public class ReviewRepository {

    // Konstant för anslutning till databasen
    public static final String URL = "jdbc:sqlite:webbutiken.db";

    // Metod för att lägga till en recension
    public boolean addReview(int customerId, int productId, int rating, String comment) throws SQLException {
        String sql = "INSERT INTO reviews (customer_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, rating);
            pstmt.setString(4, comment);



            int affectedRows = pstmt.executeUpdate();


            return affectedRows > 0;


        } catch (SQLException e) {
            System.err.println("SQL-fel vid tillägg av recension: " + e.getMessage());
            System.err.println("SQL-felkod: " + e.getErrorCode());

            // Mer specifika felmeddelanden
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new SQLException("Du har redan get recension för denna produkt.", e);
            } else if (e.getMessage().contains("foreign key constraint")) {
                throw new SQLException("Kunden eller produkten finns inte.", e);
            } else {
                throw new SQLException("Kunde inte lägga till recensionen: " + e.getMessage(), e);
            }
        }
    }



    // Metod för att hämta recensioner för en produkt
    public ArrayList<Review> getReviewsByProductId(int productId) throws SQLException {
        ArrayList<Review> reviews = new ArrayList<>();

        String sql = "SELECT r.*, c.name as customer_name, p.name as product_name " +
                "FROM reviews r " +
                "JOIN customers c ON r.customer_id = c.customer_id " +
                "JOIN products p ON r.product_id = p.product_id " +
                "WHERE r.product_id = ? " +
                "ORDER BY r.review_id DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review(
                            rs.getInt("review_id"),
                            rs.getInt("customer_id"),
                            rs.getInt("product_id"),
                            rs.getInt("rating"),
                            rs.getString("comment"),
                            rs.getString("customer_name"),
                            rs.getString("product_name")
                    );

                    reviews.add(review);
                }
            }
        }

        return reviews;
    }



    // Metod för att beräkna genomsnittligt betyg för en produkt
    public double getAverageRatingForProduct(int productId) throws SQLException {
        String sql = "SELECT AVG(rating) as average_rating FROM reviews WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("average_rating");
                }
            }
        }

        return 0.0; // Returnera 0 om det inte finns några recensioner
    }



    // Metod för att kontrollera om en kund har köpt en produkt
    public boolean hasCustomerPurchasedProduct(int customerId, int productId) throws SQLException {
        String sql = "SELECT 1 FROM orders o " +
                "JOIN orders_products op ON o.order_id = op.order_id " +
                "WHERE o.customer_id = ? AND op.product_id = ? " +
                "LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            pstmt.setInt(2, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returnera true om kunden har köpt produkten
            }
        }
    }
}