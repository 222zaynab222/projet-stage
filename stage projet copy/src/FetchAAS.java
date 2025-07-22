import java.util.ArrayList;
import java.sql.*;
import java.util.List;

public class FetchAAS extends ConnectionDBB {
    private Connection connection;

    public FetchAAS(Connection connection) {
        this.connection = connection;
    }

    public boolean existsAAS(String serverName) throws SQLException {
        String query = "SELECT 1 FROM au_property WHERE prop_key = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "aas." + serverName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
