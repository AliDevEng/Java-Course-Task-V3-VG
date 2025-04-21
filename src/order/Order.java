package order;
import common.UserSession;
import customer.Customer;
import customer.CustomerRepository;
import product.Product;
import product.ProductRepository;
/**
 * order.Order-klassen representerar en order i webshop-systemet.
 * Den innehåller information om en order enligt databasstrukturen.
 */

public class Order {

    private int order_id;
    private int customer_id;
    private String order_date;
    private String customerName; // För att visa kundens namn i orderhistoriken

    // Produktinformation för orderrader
    private String productName;
    private int quantity;
    private double unit_price;
    private int product_id;

    // 56. Konstruktor för en order
    public Order(int order_id, int customer_id, String order_date) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.order_date = order_date;
    }

    // 57. Konstruktor för en order med kundnamn
    public Order(int order_id, int customer_id, String order_date, String customerName) {
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.order_date = order_date;
        this.customerName = customerName;
    }

    // 58. Konstruktor för en orderrad (används för att visa produkter i en order)
    public Order(int order_id, int product_id, String productName, int quantity, double unit_price) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.productName = productName;
        this.quantity = quantity;
        this.unit_price = unit_price;
    }

    // Getter- och setter-metoder
    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    // 59. Metod för att beräkna totalpris för en orderrad
    public double getTotalPrice() {
        return quantity * unit_price;
    }

    // 60. toString-metod för att visa en orderrad
    public String getOrderItemString() {
        if (productName != null) {
            return productName + " - Antal: " + quantity + " - Pris: " + unit_price + " kr/st - Totalt: " + getTotalPrice() + " kr";
        }
        return null;
    }

    // 61. toString-metod för att visa en order
    @Override
    public String toString() {
        return "order.Order #" + order_id + " - Datum: " + order_date +
                (customerName != null ? " - Kund: " + customerName : "");
    }
}
