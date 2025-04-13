/**
 * Main-klassen är applikationens startpunkt.
 * Den skapar en instans av CustomerController och startar menyn.
 */

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        // 1. Skapa en instans av CustomerController
        CustomerController customerController = new CustomerController();

        try {
            // 2. Starta menyhanteringen
            customerController.runMenu();
        } catch (SQLException e) {
            // Hantera SQL-fel genom att skriva ut felmeddelande och avsluta programmet
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}