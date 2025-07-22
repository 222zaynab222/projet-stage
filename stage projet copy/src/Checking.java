import java.sql.SQLException;

public class Checking {
    public void checkServer(String serverName, FetchAAS fetchAAS, FetchAPS fetchAPS) {
        try {
            boolean hasAAS = fetchAAS.existsAAS(serverName);
            int apsCount = fetchAPS.countAPS(serverName);
            boolean hasAPS = apsCount > 0;
            if (hasAAS && hasAPS) {
                System.out.println("Server '" + serverName + "' 's APS AND AAS are connecté \n et on dispose de " + apsCount + " APS");
            } else if (hasAAS) {
                System.out.println("Server '" + serverName + "' uniquement connecté au AAS et non au APS.");
            } else if (hasAPS) {
                System.out.println("Server '" + serverName + "' uniquement connecté au APS et non au AAS.");
            } else {
                System.out.println("Server '" + serverName + "' n'existe pas.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du serveur: " + e.getMessage());
        }
    }
}

