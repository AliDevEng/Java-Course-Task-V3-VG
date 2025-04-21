package product;

/**
 * product.Product-klassen representerar en produkt i webshop-systemet.
 * Den innehåller all information om en produkt enligt databasstrukturen.
 */


import common.UserSession;
import customer.Customer;
import customer.CustomerRepository;
import order.Order;


public class Product {

    private int product_id;
    private int manufacturer_id;
    private String name;
    private String description;
    private double price;
    private int stock_quantity;


    // 22. Konstruktor för att skapa ett produktobjekt med grundläggande information
    public Product
            (int stock_quantity, double price, String description, String name, int manufacturer_id, int product_id) {
        this.stock_quantity = stock_quantity;
        this.price = price;
        this.description = description;
        this.name = name;
        this.manufacturer_id = manufacturer_id;
        this.product_id = product_id;
    }

    public Product(String name, double price, int stock_quantity) {
        this.name = name;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }

    // 24. Metod för att presentera produkten
    public void display() {
        System.out.println
                ("Produkt: " +  this.name + " - Pris: " + this.price + " kr - Lagersaldo: " + this.stock_quantity);
    }

    // Getter- och setter-metoder för att komma åt och ändra produktens attribut
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getManufacturer_id() {
        return manufacturer_id;
    }

    public void setManufacturer_id(int manufacturer_id) {
        this.manufacturer_id = manufacturer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    // 41. tpString-metod för att visa produkten som string (4235@178956)
    @Override
    public String toString() {
        return "product.Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + stock_quantity +
                '}';
    }
}
