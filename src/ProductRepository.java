/**
 * ProductRepository-klassen utgör datalagret för produkthantering.
 * Den ansvarar för all direkt kommunikation med databasen gällande produktdata
 */

import java.util.ArrayList;
import java.sql.*;

public class ProductRepository {

    // Anslutning till databas
    public static final String URL = "jdbc:sqlite:webbutiken.db";


    // 25. Metod för att hämta alla produkter
    public ArrayList<Product> getAllProducts () throws SQLException {
        ArrayList<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM Products";

        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Skapa ett nytt produkt för varje rad i resultatet
                // med alla kolumner från products-tebellen
                Product product = new Product(
                        rs.getInt("stock_quantity"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("name"),
                        rs.getInt("manufacturer_id"),
                        rs.getInt("product_id")
                );

                products.add(product);

            }
        }

        return products;
    }

    // 32. Metod för att söka produkter efter namn (Produkter punkt 2)
    public ArrayList<Product> getProductsByName(String searchTerm) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();

        // SQL-fråga för att söka produkter baserat på namn, använder LIKE för delvis matchning
        String sql = "SELECT * FROM products WHERE name LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Sätt söktermen med jokertecken för att hitta produkter där namnet innehåller söktermen
            pstmt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Skapa ett nytt produktobjekt för varje resultatrad
                    Product product = new Product(
                            rs.getInt("stock_quantity"),
                            rs.getDouble("price"),
                            rs.getString("description"),
                            rs.getString("name"),
                            rs.getInt("manufacturer_id"),
                            rs.getInt("product_id")
                    );

                    products.add(product);
                }
            }
        }

        return products;
    }

    // 36. Metod för att hämta produkt med specifikt ID
    public Product getProductById(int productId) throws SQLException {

        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return null;
            }
            return new Product(rs.getString("name"), rs.getDouble("price"), rs.getInt("stock_quantity"));
        }
    }

    // 37. Metod för att söka produkter på kategori
    public ArrayList<Product> getProductsByCategoryName(String categoryName) throws SQLException {

        ArrayList<Product> products = new ArrayList<>();

        String sql = "SELECT p.*, c.name\n" +
                "  FROM products p\n" +
                "  JOIN products_categories pc ON p.product_id=pc.product_id\n" +
                "  JOIN categories c ON c.category_id=pc.category_id\n" +
                "  WHERE c.name LIKE ?;";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + categoryName + "%");
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            while (rs.next()) {
                Product product = new Product(rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"));
                products.add(product);
            }
            return products;
        }
    }

    // 42. Metod för att uppdatera pris på en produkt
    public boolean updatePrice (int productId, double newPrice) throws SQLException {
        String sql = "UPDATE products SET price = ? WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }

    }

    // 46. Metod för att uppdatera lagersaldo på en produkt
    public boolean updateStockQuantity(int productId, int newQuantity) throws SQLException {

        String sql = "UPDATE products SET stock_quantity = ? WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
