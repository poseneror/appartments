import org.postgresql.geometric.PGpoint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Or on 29/07/2017.
 */
public class Apartment implements DBObject{

    private final String tableName = "Apartments";

    private int id;
    private String title;
    private double x;
    private double y;
    private int price;
    private String address;
    private String description;
    private int seller_id;
    private Seller seller;
    private double distance;

    // for getting details on an apartment
    public Apartment(int id){
        this.id = id;
        DataConnection dc = new DataConnection();
        dc.select(this);
    }

    // for inserting a new apartment
    public Apartment(String title, double x, double y, int price, String address, String description, int seller) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.price = price;
        this.address = address;
        this.description = description;
        this.seller_id = seller;
    }

    // for getting list of nearby apartments
    public Apartment(int id, String title, double x, double y, int price, double distance) {
        this.id = id;
        this.title = title;
        this.x = x;
        this.y = y;
        this.price = price;
        this.distance = distance;
    }

    public String getTableName() {
        return tableName;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public String insertionStatement(){
        return "INSERT INTO " + getTableName() +
                " (title, location, price, address, description, seller) " +
                "VALUES ('" + getTitle() + "', point('" + getX() + "','" + getY() + "'), " + getPrice() + ", '" +
                getAddress() + "', '" + getDescription() + "', " + getSeller_id() + ")";
    }

    public String selectStatement() {
        return "SELECT * FROM Apartments JOIN Sellers ON Apartments.seller = Sellers.id WHERE Apartments.id = " + id;
    }
    public void fetchResults(ResultSet rs) throws SQLException{
        title = rs.getString("title");
        PGpoint location = (PGpoint)rs.getObject("location");
        x = location.x;
        y = location.y;
        price = rs.getInt("price");
        address = rs.getString("address");
        description = rs.getString("description");
        seller_id = rs.getInt("seller");
        String seller_name = rs.getString("name");
        int seller_contact = rs.getInt("contact");
        String seller_email = rs.getString("email");
        seller = new Seller(seller_name, seller_contact, seller_email);
    }

    public static List getFromResults(ResultSet rs) throws SQLException{
        List<Apartment> apartments = new ArrayList<Apartment>();
        while(rs.next()){
            int appId = rs.getInt("id");
            String appTitle = rs.getString("title");
            PGpoint location = (PGpoint)rs.getObject("location");
            double appX = location.x;
            double appY = location.y;
            int appPrice = rs.getInt("price");
            double distance = 0.0;
            try{
                distance = rs.getDouble("distance");
            }catch (SQLException e){
                // dosen't have distance
            }
            apartments.add(new Apartment(appId, appTitle, appX, appY, appPrice, distance));
        }
        return apartments;
    }

    public static String tableName(){
        return "Apartments";
    }
}
