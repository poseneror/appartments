import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Or on 30/07/2017.
 */
public interface DBObject {
    int getId();
    String insertionStatement();
    String selectStatement();
    void  fetchResults(ResultSet rs) throws SQLException;
}
