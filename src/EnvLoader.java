import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    private static final String ENV_FILE_PATH = "./.env"; // Chemin vers le fichier .env

    public static Map<String, String> loadEnv() throws IOException {
        Map<String, String> envVars = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ENV_FILE_PATH))) {
            String line;
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

                    // Ajouter à la map
                    envVars.put(key, value);
                }
            }
        }
        return envVars;
    }

    public static void main(String[] args) {
        try {
            Map<String, String> envVars = loadEnv();
            // Exemple d'utilisation
            String dbPassword = envVars.get("DB_PASSWORD");

            if (dbPassword != null) {
                System.out.println("Database password loaded: " + dbPassword);
            } else {
                System.out.println("DB_PASSWORD is not defined in the .env file.");
            }

        } catch (IOException e) {
            System.err.println("Error loading .env file: " + e.getMessage());
        }
    }
}
