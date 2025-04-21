package common;

import customer.Customer;

/**
 * Hanterar användarens inloggningssession
 * Singleton-mönster för att säkerställa att endas en session existerar
 */

public class UserSession {

    // Singleton-instans
    private static UserSession instance;

    // Kund inloggad
    private Customer loggedInCustomer;

    // Singleton-mönster / private konstruktor
    private UserSession() {
        loggedInCustomer = null;
    }

    // Metod för att få den enda instansen (Singleton)
    public static UserSession getInstance() {

        if (instance == null) {
            instance = new UserSession ();
        }
        return instance;
    }

    public void setLoggedInCustomer(Customer customer) {
        this.loggedInCustomer = customer;
    }

    public Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }


    // Kontroll om kund är inloggad
    public boolean isLoggedIn() {
        return loggedInCustomer != null;
    }

    // Logga ut nuvarande användare
    public void logout () {
        loggedInCustomer = null;
        System.out.println("Du har loggats ut.");
    }
}
