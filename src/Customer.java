/**
 * Customer-klassen representerar en kund i webshop-systemet.
 * Den innehåller all information om en kund och metoder för att hantera denna information.
 */

public class Customer {

    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;

    // 3. Konstruktor för att skapa ett kundobjekt med grundläggande information
    public Customer(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
    }

    // 4. Konstruktor för att skapa ett kundobjekt med fullständig information
    public Customer
    (int customerId, String name, String email, String phone, String address, String password) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }



    // 5. Metod för att presentera kunden
    public void introduce(){
        System.out.println("Hello! My name is " + this.name);
    }

    // Getter- och setter-metoder för att komma åt och ändra kundens attribut
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}



