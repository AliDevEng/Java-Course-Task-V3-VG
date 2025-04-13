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

}
