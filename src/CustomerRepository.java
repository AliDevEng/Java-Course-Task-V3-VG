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
        } catch (SQLException e) {
            System.err.println("SQL-fel vid hämtning av alla kunder: " + e.getMessage());
            System.err.println("SQL-felkod: " + e.getErrorCode());
            throw new SQLException("Kunde inte hämta kundlistan från databasen: " + e.getMessage(), e);
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
            } catch (SQLException e) {
                System.err.println("SQL-fel vid tillägg av ny kund: " + e.getMessage());
                System.err.println("SQL-felkod: " + e.getErrorCode());


                // Mer specifik felmeddelande
                if (e.getMessage().contains("UNIQUE constraint failed")) {
                    throw new SQLException("En kund med denna Email finns redan", e);
                } else {
                    throw new SQLException("Kunde inte lägga till kunden i databasen: " + e.getMessage(), e );
                }
            }
        }

        // 12. Metod för att uppdatera en kunds e-postadress
        public boolean updateEmail(int customerId, String email) throws SQLException {

            String sql = "UPDATE customers SET email = ? WHERE customer_id = ?";

            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, email);
                pstmt.setInt(2, customerId);

                try {
                    int rowsAffected = pstmt.executeUpdate();
                    return rowsAffected > 0;

                } catch (SQLException e) {
                    System.err.println("SQL-fel vid uppdatering av Email: " + e.getMessage());
                    System.err.println("SQL-felkod: " + e.getErrorCode());

                    if (e.getMessage().contains("UNIQUE constraint failed")) {
                        throw new SQLException("En annan kund använder redan denna Email", e);
                    } else {
                        throw new SQLException("Kunde inte uppdatera kundens Email: " + e.getMessage(), e);
                    }
                }
            } catch (SQLException e) {
                System.err.println("SQL-fel vid förberedelse av uppdatering: " + e.getMessage());
                throw new SQLException("Databasfel vid uppdatering av kundens Email: " + e.getMessage(), e);
            }
        }

        // 15. Metod för att ta bort en kund från databasen
        public boolean deleteCustomer (int customerId ) throws SQLException {

            String sql = "DELETE FROM customers WHERE customer_id = ?";


            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, customerId);

                try {
                    int rowsAffected = pstmt.executeUpdate();
                    return rowsAffected > 0;

                } catch (SQLException e) {
                    System.err.println("SQL-fel vid borttagning av kund: " + e.getMessage());
                    System.err.println("SQL-felkod: " + e.getErrorCode());

                    if (e.getMessage().contains("foreign key constraint")) {
                        throw new SQLException("Kunden kan inte tas bort eftersom den har kopplade ordrar", e);
                    } else {
                        throw new SQLException("Kunde inte ta bort kunden från databasen: " + e.getMessage(), e);
                    }
                }

            } catch (SQLException e) {
                System.err.println("SQL-fel vid förberedelse av borttagning: " + e.getMessage());
                throw new SQLException("Databasfel vid borttagning av kund: " + e.getMessage(), e);
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

            } catch (SQLException e) {
                System.err.println("SQL-fel vid hämtning av kunddata: " + e.getMessage());
                throw new SQLException("Kunde inte läsa kunddata: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            System.err.println("SQL-fel vid förberedelse av kundhämtning: " + e.getMessage());
            throw new SQLException("Databasefel vid sökning efter kund: " + e.getMessage(), e);
        }
    }


    // Metod för at logga in med email-password
    public Customer loginCustomer (String email, String password) throws SQLException {

        String sql = "SELECT * FROM customers WHERE email = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Om en kund hittades, returnera den
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("password")
                );
            }
        }

        // Om ingen kund hittas, returnera null
        return null;
    }
}

