import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            Map<String, String> envVars = EnvLoader.loadEnv();
            String dbPassword = envVars.get("DB_PASSWORD");

            if (dbPassword == null || dbPassword.isEmpty()) {
                System.out.println("Error: DB_PASSWORD is not set in the .env file.");
                return;
            }

            System.out.println(dbPassword);
            System.out.println("Password Manager CLI");
            CLIHandler cli = new CLIHandler();
            cli.run();

        } catch (IOException e) {
            System.err.println("Failed to load environment variables: " + e.getMessage());
        }
    }
}
