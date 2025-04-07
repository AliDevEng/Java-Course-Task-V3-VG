public class Customer {

    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String adress;
    private String password;

    public Customer(int customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public void introduce () {
        System.out.println("Welcome");
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}



