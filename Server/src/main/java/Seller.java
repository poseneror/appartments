import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Or on 01/08/2017.
 */
public class Seller implements DBObject {
    private final String tableName = "Sellers";
    private int id;
    private String name;
    private int contact;
    private String email;
    private String password;

    // for fetching existing seller
    public Seller(int id){

    }

    // for creating a new seller
    public Seller(String name, int contact, String email){
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.password = "";
    }

    public Seller(String name, int contact, String email, String password){
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }

    public String getName() {
        return name;
    }

    public int getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String insertionStatement() {
        return "INSERT INTO Sellers " +
                "(name, contact, email, password) " +
                "VALUES ('" + getName() + "', " + getContact() + ", '" +
                getEmail() + "', crypt('" + password + "', gen_salt('bf')))";
    }

    public String selectStatement() {
        return "SELECT name, contact, email FROM " + getTableName() +
                " WHERE id = " + getId();
    }

    public void fetchResults(ResultSet rs) throws SQLException {

    }
}
