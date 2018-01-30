import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Or on 30/07/2017.
 */
public class DataConnection {
    private Connection conn;
    private Statement st;

    public void insert(DBObject obj){
        try {
            conn = getConnection();
            st = conn.createStatement();
            st.executeUpdate(obj.insertionStatement());
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            terminate();
        }
    }

    // the method returns a result set with the object - the object has an id, so we fetch the rest
    public void select(DBObject obj){
        try {
            conn = getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(obj.selectStatement());
            if(rs.next())
                obj.fetchResults(rs);
            rs.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            terminate();
        }
    }

    public List getAllAppartments(){ // make this generic
        List resObjects = new ArrayList();
        String table = "Apartments";
        try {
            conn = getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, title, location, price FROM " + table);
            resObjects = Apartment.getFromResults(rs);
            rs.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            terminate();
        }
        return resObjects;
    }

    // used for calculation:
    // https://www.plumislandmedia.net/mysql/haversine-mysql-nearest-loc/
    // lat = x lan = y
    public List getNearestAppartments(double x, double y, double range){
        List resObjects = new ArrayList();
        try {
            conn = getConnection();
            st = conn.createStatement();
            String sql = "SELECT id, title, location, price, " +
                    "111.045 * DEGREES(ACOS(COS(RADIANS(latpoint)) " +
                    "* COS(RADIANS(location[0]))" +
                    "* COS(RADIANS(longpoint) - RADIANS(location[1]))" +
                    "+ SIN(RADIANS(latpoint))" +
                    "* SIN(RADIANS(location[0])))) AS distance " +
                    "FROM Apartments " +
                    "JOIN (SELECT " + x + " AS latpoint, " + y + " AS longpoint, " +
                    + range + " AS radius, 111.045 AS distance_unit) AS p ON 1=1 " +
                    "WHERE location[0] BETWEEN  latpoint - (radius / distance_unit) " +
                    "AND latpoint + (radius / distance_unit) " +
                    "AND location[1] BETWEEN longpoint - (radius / (distance_unit * COS(RADIANS(latpoint)))) " +
                    "AND longpoint + (radius / (distance_unit * COS(RADIANS(latpoint)))) " +
                    "ORDER BY distance LIMIT 15";
            ResultSet rs = st.executeQuery(sql);
            resObjects = Apartment.getFromResults(rs);
            rs.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            terminate();
        }
        return resObjects;
    }

    public int authenticateSeller(String email, String password){
        // return auth key?
        int userId = -1;
        try {
            conn = getConnection();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id FROM Sellers " +
                    "WHERE email = '"+ email + "' AND password = crypt('" + password + "', password)");
            if(rs.next())
                userId = rs.getInt("id");
            rs.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            terminate();
        }
        return userId;
    }

    private void terminate(){
        try{
            if(st!=null)
                st.close();
        }catch(SQLException se2){}
        try{
            if(conn!=null)
                conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }
    }
    private Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
    }
}
