import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    // Chemin vers le fichier .env
    private static final String ENV_FILE_PATH = "./.env";

    // Méthode pour charger les variables d'environnement à partir du fichier .env
    public static Map<String, String> loadEnv() throws IOException {
        Map<String, String> envVars = new HashMap<>();

        // Utiliser un BufferedReader pour lire le fichier .env
        try (BufferedReader reader = new BufferedReader(new FileReader(ENV_FILE_PATH))) {
            String line;
            // Lire chaque ligne du fichier
            while ((line = reader.readLine()) != null) {
                // Ignorer les lignes vides ou les commentaires
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Diviser la ligne en clé et valeur
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    // Ajouter la clé et la valeur à la map
                    envVars.put(key, value);
                }
            }
        }
        return envVars;
    }

    // Méthode principale pour tester le chargement des variables d'environnement
    public static void main(String[] args) {
        try {
            // Charger les variables d'environnement
            Map<String, String> envVars = loadEnv();

            // Exemple d'utilisation : récupérer le mot de passe de la base de données
            String dbPassword = envVars.get("DB_PASSWORD");

            if (dbPassword != null) {
                System.out.println("Database password loaded: " + dbPassword);
            } else {
                System.out.println("DB_PASSWORD is not defined in the .env file.");
            }

        } catch (IOException e) {
            // Gérer les exceptions et afficher un message d'erreur
            System.err.println("Error loading .env file: " + e.getMessage());
        }
    }
}
