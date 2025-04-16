/**
 * ProductService-klassen utgör affärslogiklagret för produkthantering.
 * Den fungerar som en förmedlare mellan controller och repository.
 */

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductService {

    // Skapa en instans av ProduktRepository för att kunna anropa dess metoder
    ProductRepository productRepository = new ProductRepository();

    // 26. Metod för att hämta alla produkter
    public ArrayList<Product> getAllProducts() throws SQLException {
        System.out.println("ProductService hämtar alla produkter från repository");
        return productRepository.getAllProducts();
    }

    // 33. Metod för att söka produkter efter namn
    public ArrayList<Product> getProductsByName(String searchTerm) throws SQLException {
        System.out.println("ProductService söker produkter med namn som innehåller: " + searchTerm);

        // Validera söktermen - enkel validering för att undvika tomma söktermer
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            System.out.println("Söktermen kan inte vara tom");
            return new ArrayList<>();
        }

        return productRepository.getProductsByName(searchTerm.trim());
    }

    // 38. Metod för att hämta produkt med specifikt ID
    public Product getProductById(int id) throws SQLException {
        return productRepository.getProductById(id);
    }

    // 39. Metod för att söka produkter på kategori
    public void getProductsByCategoryName(String categoryName) throws SQLException {
        ArrayList<Product> products = productRepository.getProductsByCategoryName(categoryName);
        for (Product p : products){
            System.out.println(p.toString());
        }

    }

    // 43. Metod för att uppdatera pris på en produkt
    public boolean updatePrice(int productId, double newPrice) throws SQLException {
        System.out.println("ProductService uppdaterar pris för produkt med ID: " + productId);

        // Validering så att priset är positivt
        if (newPrice <= 0) {
            System.out.println("Priset måste vara större än 0");
            return false;
        }

        return productRepository.updatePrice(productId, newPrice);
    }

    // 47. Metod för att uppdatera lagersaldo på en produkt
    public boolean updateStockQuantity(int productId, int newQuantity) throws SQLException {
        System.out.println("ProductService uppdaterar lagersaldo för produkt med ID: " + productId);

        // Validera att lagersaldot inte är negativt
        if (newQuantity < 0) {
            System.out.println("Lagersaldot kan inte vara negativt");
            return false;
        }

        return productRepository.updateStockQuantity(productId, newQuantity);
    }

}
