/**
 * ProductController-klassen utgör presentationslagret för produkthantering.
 * Den hanterar all interaktion med användaren gällande produkter.
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductController {

    // Skapa en instans av ProductService för att kunna anropa dess metoder
    ProductService productService = new ProductService();

    // 27. Meny för att visa produktmeny och hantera användarensval
    public void runMenu () throws SQLException {
        Scanner scanner = new Scanner(System.in);


        // Visa meny
        System.out.println("== Produktmeny ==");
        System.out.println("1. Visa alla produkter");
        System.out.println("0. Återgå till huvudmeny");


        // Läs användarens val
        String select = scanner.nextLine();


        // Switch-sats för att hantera användarens val
        switch (select) {

            case "1":
                // 28. visa alla produkter
                showAllProducts();
                break;

            case "0":
                // Återgå till huvudmeny
                return;
            default:
                System.out.println("Ogilitig val! Försök igen.");
                break;
        }

        // Anrop för att visa meny igen
        runMenu();
        }


        // 29. Metod för att visa alla produkter
        private void showAllProducts () throws SQLException {

            try {
                ArrayList<Product> products = productService.getAllProducts();

                if (products.isEmpty()) {
                    System.out.println("Inga produkter hittades i systemet.");
                } else {
                    System.out.println("=== ALLA PRODUKTER ===");
                    System.out.println("ID\tTillverkare\tNamn\t\tPris\tLagersaldo\tBeskrivning");
                    System.out.println("--------------------------------------------------------------------------------");

                    for (Product product : products) {
                        System.out.printf("%d\t%d\t\t%-15s\t%.2f kr\t%d\t\t%s%n",
                                product.getProduct_id(),
                                product.getManufacturer_id(),
                                product.getName(),
                                product.getPrice(),
                                product.getStock_quantity(),
                                (product.getDescription() != null ? product.getDescription() : "")
                        );
                    }
                    System.out.println("--------------------------------------------------------------------------------");
                    System.out.println("Totalt antal produkter: " + products.size());
                }
            } catch (SQLException e) {
                System.out.println("Ett fel uppstod vid hämtning av produkter: " + e.getMessage());
                throw e;
            }
    }
}
