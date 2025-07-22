import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);
        try {
            connection = ConnectionDBB.getConnection();
            if (connection != null) {
                System.out.println("Connected to database.");

                FetchAAS fetchAAS = new FetchAAS(connection);
                FetchAPS fetchAPS = new FetchAPS(connection);

                Checking checker = new Checking();

                System.out.print("Enter server name: ");

                String serverName = scanner.nextLine().trim();
                checker.checkServer(serverName, fetchAAS, fetchAPS);
            } else {
                System.out.println("Failed to connect to database.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            ConnectionDBB.closeConnection(connection);
        }
    }
}
