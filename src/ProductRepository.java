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
}
