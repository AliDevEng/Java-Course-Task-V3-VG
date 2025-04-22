package common;

import product.Product;
import java.util.HashMap;
import java.util.Map;

/**
 * ShoppingCart-klassen representerar en kundvagn i webshop-systemet.
 * Den innehåller en lista över produkter och deras respektive antal.
 */

public class ShoppingCart {

    // HashMap för att lagra produkter och deras antal
    private Map<Product, Integer> items;


    // Konstruktor för att skapa en tom kundvagn
    public ShoppingCart() {
        this.items = new HashMap<>();
    }



    // Lägger till en produkt i kundvagnen
    public void addProduct(Product product, int quantity) {
        // Validera indata
        if (product == null || quantity <= 0) {
            System.out.println("Fel produkt eller antal!");
            return;
        }

        // Om produkten redan finns, uppdatera antalet
        if (items.containsKey(product)) {
            int currentQuantity = items.get(product);
            items.put(product, currentQuantity + quantity);
        } else {
            // Annars lägg till den nya produkten
            items.put(product, quantity);
        }

        System.out.println(quantity + " st " + product.getName() + " har lagts till i kundvagnen.");
    }

    // Kontrollera om kundvagnen är tom
    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Hämta alla produkter i kundvagnen
    public Map<Product, Integer> getItems() {
        return items;
    }

    // Beräkna totalt pris för alla produkter i kundvagnen
    public double calculateTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }


    // Ta bort en produkt från kundvagnen
    public boolean removeProduct(Product product) {
        // Validera indata
        if (product == null) {
            System.out.println("Ogiltig produkt!");
            return false;
        }

        // Kontrollera om produkten finns i kundvagnen
        if (!items.containsKey(product)) {
            System.out.println("Produkten finns inte i kundvagnen.");
            return false;
        }

        // Ta bort produkten
        items.remove(product);
        System.out.println(product.getName() + " har tagits bort från kundvagnen.");
        return true;
    }


    // Uppdatera antalet av en produkt i kundvagnen
    public boolean updateQuantity(Product product, int newQuantity) {
        // Validera indata
        if (product == null) {
            System.out.println("Ogiltig produkt!");
            return false;
        }

        if (newQuantity <= 0) {
            // Om antalet är 0 eller mindre, ta bort produkten
            return removeProduct(product);
        }

        // Kontrollera om produkten finns i kundvagnen
        if (!items.containsKey(product)) {
            System.out.println("Produkten finns inte i varukorg.");
            return false;
        }

        // Uppdatera antalet
        items.put(product, newQuantity);
        System.out.println("Antalet " + product.getName() + " har ändrats till " + newQuantity + " st.");
        return true;
    }
}