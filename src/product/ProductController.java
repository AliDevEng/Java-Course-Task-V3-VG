package product;

/**
 * product.ProductController-klassen utgör presentationslagret för produkthantering.
 * Den hanterar all interaktion med användaren gällande produkter.
 */

import common.UserSession;
import customer.Customer;
import customer.CustomerRepository;
import order.Order;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductController {

    // Skapa en instans av product.ProductService för att kunna anropa dess metoder
    ProductService productService = new ProductService();

    // 27. Meny för att visa produktmeny och hantera användarensval
    public void runMenu () throws SQLException {
        Scanner scanner = new Scanner(System.in);

        // JAg lägger dessa för att skapa tomma rader
        System.out.println();

        // Visa meny
        System.out.println("== Produktmeny ==");
        System.out.println("1. Visa alla produkter");
        System.out.println("2. Sök produkter efter namn");
        System.out.println("3. Sök produkter efter kategori");
        System.out.println("4. Uppdatera pris");
        System.out.println("5. Uppdatera lagersaldo");
        System.out.println("6. Lägg till ny produkt");
        System.out.println("0. Återgå till huvudmeny");
        System.out.print("Ange dit val här: ");


        // Läs användarens val
        String select = scanner.nextLine();

        System.out.println();


        // Switch-sats för att hantera användarens val
        switch (select) {

            case "1":
                // 28. visa alla produkter
                showAllProducts();
                break;

            case "2":
                // 34. Sök produkter efter namn
                getProductsByName(scanner);
                break;

            case "3":
                // 40. Sök produkter efter kategor
                System.out.println("Ange kategori: ");
                String categoryName = scanner.nextLine();
                productService.getProductsByCategoryName(categoryName);
                break;

            case"4":
                // 44. Uppdatera pris på en produkt
                updateProductPrice(scanner);
                break;

            case "5":
                // 48. Uppdatera lagersaldo på en produkt
                updateProductStockQuantity(scanner);
                break;

            case "6":
                // 54. Lägg till ny produkt
                addNewProduct(scanner);
                break;

            case "0":
                // Återgå till huvudmeny
                return;

            default:
                System.out.println("Felaktigt val! Försök igen.");
                break;
        }

        // Anrop för att visa meny igen
        if (!select.equals("0")) {
        runMenu();
        }
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

    // 35. Metod för att söka och visa produkter baserat på namn
    private void getProductsByName(Scanner scanner) throws SQLException {
        System.out.println("=== SÖK PRODUKTER EFTER NAMN ===");
        System.out.print("Ange sökterm: ");
        String searchTerm = scanner.nextLine();

        try {
            ArrayList<Product> products = productService.getProductsByName(searchTerm);

            if (products.isEmpty()) {
                System.out.println("Inga produkter hittades som matchar '" + searchTerm + "'.");
            } else {
                System.out.println("=== SÖKRESULTAT FÖR '" + searchTerm + "' ===");
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
                System.out.println("Antal träffar: " + products.size());
            }
        } catch (SQLException e) {
            System.out.println("Ett fel uppstod vid sökning av produkter: " + e.getMessage());
            throw e;
        }
    }

    // 45. Metod för att uppdatera pris på en produkt
    private void updateProductPrice(Scanner scanner) throws SQLException {
        System.out.println("=== UPPDATERA PRIS ===");

        // Visa alla produkter för användaren för att välja ID
        showAllProducts();

        try {
            // Hämta produkt ID från användare
            System.out.println("Ange ID på produkten som ska uppdateras");
            int productId = Integer.parseInt(scanner.nextLine());

            // Hämta produkt för att visa aktuell pris
            Product product = productService.getProductById(productId);

            if (product == null) {
                System.out.println("Ingen produkt hittades med ID: " +productId);
                return;
            }

            System.out.println("Aktuellt pris för " + product.getName() + ": " + product.getPrice() + " kr");

            // Hämta nytt pris från användaren
            System.out.print("Ange nytt pris: ");
            double newPrice = Double.parseDouble(scanner.nextLine());

            // Uppdatera priset
            boolean success = productService.updatePrice(productId, newPrice);

            if (success) {
                System.out.println("Priset har uppdaterats!");
            } else {
                System.out.println("Kunde inte uppdatera priset.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Ange ett korrekt värde.");
        } catch (SQLException e) {
            System.out.println("Ett fel uppstod vid uppdatering av pris: " + e.getMessage());
            throw e;
        }


    }

    // 49. Metod för att uppdatera lagersaldo på en produkt
    private void updateProductStockQuantity (Scanner scanner ) throws SQLException {

        System.out.println(" == UPDATERA LAGERSALDO == ");

        showAllProducts();

        try {
            // Hämta produkt-ID från användaren
            System.out.print("Ange ID på produkten vars lagersaldo ska uppdateras: ");
            int productId = Integer.parseInt(scanner.nextLine());

            // Hämta produkten för att visa aktuellt lagersaldo
            Product product = productService.getProductById(productId);

            if (product == null) {
                System.out.println("Ingen produkt hittades med ID: " + productId);
                return;
            }

            System.out.println("Aktuellt lagersaldo för " + product.getName() + ": " + product.getStock_quantity());

            // Hämta nytt lagersaldo från användaren
            System.out.print("Ange nytt lagersaldo: ");
            int newQuantity = Integer.parseInt(scanner.nextLine());

            // Uppdatera lagersaldot
            boolean success = productService.updateStockQuantity(productId, newQuantity);

            if (success) {
                System.out.println("Lagersaldot har uppdaterats!");
            } else {
                System.out.println("Kunde inte uppdatera lagersaldot.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Ange ett heltal.");
        } catch (SQLException e) {
            System.out.println("Ett fel uppstod vid uppdatering av lagersaldo: " + e.getMessage());
            throw e;
        }
    }

    // 55. Metod för att lägga till en ny produkt
    private void addNewProduct(Scanner scanner) throws SQLException {
        System.out.println("=== LÄG TILL NY PRODUKT ===");

        try {
            // Visa tillgängliga tillverkare
            productService.getAllManufacturers();

            // Hämta produktuppgifter från användaren
            System.out.print("Ange produktnamn: ");
            String name = scanner.nextLine();

            System.out.print("Ange beskrivning: ");
            String description = scanner.nextLine();

            System.out.print("Ange pris: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Ange lagersaldo: ");
            int stockQuantity = Integer.parseInt(scanner.nextLine());

            System.out.print("Välj tillverkare (ange ID): ");
            int manufacturerId = Integer.parseInt(scanner.nextLine());

            // Lägg till produkten
            boolean success = productService.addProduct(name, description, price, stockQuantity, manufacturerId);

            if (success) {
                System.out.println("Produkten har lagts till!");
            } else {
                System.out.println("Kunde inte lägga till produkten.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt format. Säkerställ att pris, lagersaldo och tillverkar-ID är korrekta värden.");
        } catch (SQLException e) {
            System.out.println("Ett fel uppstod vid tillägg av produkt: " + e.getMessage());
            throw e;
        }
    }
}
