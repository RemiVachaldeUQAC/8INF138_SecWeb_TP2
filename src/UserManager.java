import java.util.Base64;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class UserManager {
    // Instance de DatabaseManager pour interagir avec la base de données
    private DatabaseManager dbManager = new DatabaseManager();

    // Méthode pour enregistrer un nouvel utilisateur
    public void registerUser(String username) throws Exception {
        String password = null;

        // Boucle pour demander et valider le mot de passe maître
        do {
            System.out.print("Enter master password (or type 'generate' to get a strong password suggestion): ");
            String input = new java.util.Scanner(System.in).nextLine();

            // Si l'utilisateur demande une suggestion de mot de passe fort
            if ("generate".equalsIgnoreCase(input)) {
                password = generateStrongPassword();
                System.out.println("Suggested strong password: " + password);
                System.out.print("Do you want to use this password? (yes/no): ");
                String choice = new java.util.Scanner(System.in).nextLine();
                if ("yes".equalsIgnoreCase(choice)) {
                    break;
                } else {
                    password = null;
                    continue;
                }
            } else {
                password = input;
            }

            // Vérifier si le mot de passe est suffisamment fort
            if (!isPasswordStrong(password)) {
                System.out.println("Password is not strong enough. It must meet the following criteria:");
                System.out.println("- At least 8 characters long");
                System.out.println("- Contains at least one uppercase letter");
                System.out.println("- Contains at least one lowercase letter");
                System.out.println("- Contains at least one digit");
                System.out.println("- Contains at least one special character");
                password = null; // Réinitialiser le mot de passe pour réessayer
            }
        } while (password == null);

        // Générer un sel sécurisé
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String saltBase64 = Base64.getEncoder().encodeToString(salt);

        // Hacher le mot de passe avec le sel
        String hashedPassword = hashPassword(password, salt);

        // Sauvegarder l'utilisateur dans la base de données
        dbManager.saveUser(username, hashedPassword, saltBase64);
        System.out.println("User " + username + " registered successfully!");
    }

    // Méthode pour hacher un mot de passe avec un sel
    public static String hashPassword(String password, byte[] salt) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hashed = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashed);
    }

    // Méthode pour vérifier si un mot de passe est suffisamment fort
    public static boolean isPasswordStrong(String password) {
        if (password.length() < 8) return false;
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    // Méthode pour générer un mot de passe fort
    public static String generateStrongPassword() {
        SecureRandom random = new SecureRandom();
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        String allChars = upperCase + lowerCase + digits + specialChars;

        StringBuilder password = new StringBuilder();
        // Assurer au moins un caractère de chaque catégorie
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Remplir le reste du mot de passe pour atteindre la longueur minimale (12 caractères)
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Mélanger les caractères pour randomiser leur ordre
        char[] passwordChars = password.toString().toCharArray();
        for (int i = passwordChars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordChars[i];
            passwordChars[i] = passwordChars[j];
            passwordChars[j] = temp;
        }

        return new String(passwordChars);
    }
}
