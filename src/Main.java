import java.io.IOException;
import java.util.Map;

public class Main {
    // Méthode principale pour démarrer l'application
    public static void main(String[] args) {
        try {
            // Charger les variables d'environnement à partir du fichier .env
            Map<String, String> envVars = EnvLoader.loadEnv();

            // Récupérer le mot de passe de la base de données à partir des variables d'environnement
            String dbPassword = envVars.get("DB_PASSWORD");

            // Vérifier si le mot de passe de la base de données est défini
            if (dbPassword == null || dbPassword.isEmpty()) {
                System.out.println("Error: DB_PASSWORD is not set in the .env file.");
                return;
            }

            // Afficher le mot de passe de la base de données (à des fins de débogage)
            System.out.println(dbPassword);

            // Afficher le message de bienvenue du gestionnaire de mots de passe CLI
            System.out.println("Password Manager CLI");

            // Créer une instance de CLIHandler et exécuter la boucle principale
            CLIHandler cli = new CLIHandler();
            cli.run();

        } catch (IOException e) {
            // Gérer les exceptions et afficher un message d'erreur si le fichier .env ne peut pas être chargé
            System.err.println("Failed to load environment variables: " + e.getMessage());
        }
    }
}
