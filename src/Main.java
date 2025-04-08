import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        CustomerController customerController = new CustomerController();

        try {
            customerController.runMenu();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}