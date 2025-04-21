package order;
/**
 * order.OrderRepository-klassen utgör datalagret för orderhantering.
 * Den ansvarar för all direkt kommunikation med databasen gällande orderdata.
 */
import common.UserSession;
import customer.Customer;
import customer.CustomerRepository;
import product.Product;
import product.ProductRepository;
import java.sql.*;
import java.util.ArrayList;

public class OrderRepository {

    // Konstant för anslutning till databasen
    public static final String URL = "jdbc:sqlite:webbutiken.db";

    // 62. Metod för att hämta ordrar för en specifik kund
    public ArrayList<Order> getOrdersByCustomerId(int customerId) throws SQLException {

        ArrayList<Order> orders = new ArrayList<>();

        String sql =
                        "SELECT o.*, c.name as customer_name " +
                        "FROM orders o " +
                        "JOIN customers c ON o.customer_id = c.customer_id " +
                        "WHERE o.customer_id = ? " +
                        "ORDER BY o.order_date DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getString("order_date"),
                            rs.getString("customer_name")
                    );

                    orders.add(order);
                }
            }
        }

        return orders;
    }



    // 63. Metod för att hämta orderdetaljer för en specifik order
    public ArrayList<Order> getOrderItemsByOrderId (int orderId) throws SQLException {

        ArrayList<Order> orderItems = new ArrayList<>();

        String sql =
                "SELECT op.*, p.name as product_name " +
                "FROM orders_products op " +
                "JOIN products p ON op.product_id = p.product_id " +
                "WHERE op.order_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order orderItem = new Order(
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price")
                    );

                    orderItems.add(orderItem);
                }
            }
        }

        return orderItems;
    }

    // 71. Metod för att skapa en ny order
    public int createOrder(int customerId) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, order_date) VALUES (?, CURRENT_TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, customerId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Skapande av order misslyckades, inga rader påverkades.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Returnera det nya order-ID:t
                } else {
                    throw new SQLException("Skapande av order misslyckades, inget ID erhölls.");
                }
            }
        }
    }

    // 75. Metod för att lägga till en produkt i en order
    public boolean
        addProductToOrder(int orderId, int productId, int quantity, double unitPrice) throws SQLException {
        String sql = "INSERT INTO orders_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, unitPrice);

            try {
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.err.println("SQL-fel vid tillägg av produkt till order: " + e.getMessage());
                System.err.println("SQL-felkod: " + e.getErrorCode());

                if (e.getMessage().contains("foreign key constraint")) {
                    throw new SQLException("Kunde inte lägga till produkten i ordern: Produkt eller order existerar inte", e);
                } else if (e.getMessage().contains("UNIQUE constraint")) {
                    throw new SQLException("Denna produkt finns redan i ordern. Använd uppdatering istället.", e);
                } else {
                    throw new SQLException("Kunde inte lägga till produkten i ordern: " + e.getMessage(), e);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL-fel vid förberedelse av produkt-tillägg: " + e.getMessage());
            throw new SQLException("Databasfel vid tillägg av produkt till order: " + e.getMessage(), e);
        }
    }

    // 77. Metod för att hämta alla ordrar (för att visa i en lista)
    public ArrayList<Order> getAllOrders() throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();

        String sql =
                "SELECT o.*, c.name as customer_name " +
                "FROM orders o " +
                "JOIN customers c ON o.customer_id = c.customer_id " +
                "ORDER BY o.order_date DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getString("order_date"),
                        rs.getString("customer_name")
                );

                orders.add(order);
            }
        }

        return orders;
    }


    public boolean orderExists(int orderId) throws SQLException {

        String sql = "SELECT 1 FROM orders WHERE order_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returnera true om ordern finns
            } catch (SQLException e) {
                System.err.println("SQL-fel vid kontroll om order existerar: " + e.getMessage());
                throw new SQLException("Kunde inte kontrollera om ordern existerar: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            System.err.println("SQL-fel vid förberedelse av kontroll: " + e.getMessage());
            throw new SQLException("Databasfel vid kontroll av order: " + e.getMessage(), e);
        }
    }
}
