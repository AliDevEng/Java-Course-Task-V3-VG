/**
 * User-klassen är en basklas som representerar an användare i systemet
 * Innheåller grundläggande information som är gemensamma för alla användartyper
 */

public class User {

    protected int id;
    protected String name;
    protected String email;
    protected String password;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Metod för att visa användarinfo (kan överskridas av sub-klass)
    public void displayInfo () {
        System.out.println("Användare: " + name + " (" + email + ")" );
    }
}
