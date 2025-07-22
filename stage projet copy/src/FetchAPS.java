import java.sql.*;

public class FetchAPS extends ConnectionDBB {
    private Connection connection;

    public FetchAPS(Connection connection) {
        this.connection = connection;
    }

    public int countAPS(String serverName) throws SQLException {
        String query = "SELECT COUNT(*) FROM au_property WHERE prop_key LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "presentation-" + serverName + ".%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}
