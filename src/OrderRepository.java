/**
 * OrderRepository-klassen utgör datalagret för orderhantering.
 * Den ansvarar för all direkt kommunikation med databasen gällande orderdata.
 */

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
}
