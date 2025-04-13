/**
 * CustomerRepository-klassen utgör datalagret för kundhantering.
 * Den ansvarar för all direkt kommunikation med databasen gällande kunddata.
 */

import java.sql.*;
import java.util.ArrayList;

public class CustomerRepository {

    // Konstant för anslutning till databasen
    public static final String URL = "jdbc:sqlite:webbutiken.db";

    // 6. Metod för att hämta alla kunder från databasen
    public ArrayList<Customer> getAll() throws SQLException {

        ArrayList<Customer> customers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {

            while (rs.next()) {
                // Skapa ett nytt kundobjekt för varje rad i resultatet
                Customer customer = new Customer(rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"));

                customers.add(customer);
            }
        }
        return customers;
    }


    // 9. Metod för att lägga till en ny kund i databasen
    public void insertCustomer(String name, String email, String password) throws SQLException{
            String sql = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql) ) {

                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, password); // Korrigerat från 5 till 3

                pstmt.execute();
            }
        }

        // 12. Metod för att uppdatera en kunds e-postadress
        public boolean updateEmail (int customerId, String email) throws SQLException {
            String sql = "UPDATE customers SET email = ? WHERE customer_id = ?";
            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql) ) {

                pstmt.setString(1,email);
                pstmt.setInt(2,customerId);

                return pstmt.executeUpdate() > 0;
            }

        }

        // 15. Metod för att ta bort en kund från databasen
        public boolean deleteCustomer (int customerId) throws SQLException {
            String sql = "DELETE FROM customers WHERE customer_id = ?";
            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql) ) {

                pstmt.setInt(1,customerId);

                return pstmt.executeUpdate() > 0;

            }
        }

    // 21. Metod för att hämta en specifik kund från databasen baserat på ID
    public Customer getCustomerById(int customerId)  throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Skapa ett Customer-objekt med all tillgänglig information
                    return new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("password")
                    );
                } else {
                    // Returnera null om ingen kund hittades med det angivna ID:t
                    return null;
                }
            }
        }
    }
}

