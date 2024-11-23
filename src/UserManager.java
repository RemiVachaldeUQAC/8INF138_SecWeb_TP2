
import java.util.Base64;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class UserManager {
    private DatabaseManager dbManager = new DatabaseManager();

    public void registerUser(String username) throws Exception {
        String password = null;

        do {
            System.out.print("Enter master password (or type 'generate' to get a strong password suggestion): ");
            String input = new java.util.Scanner(System.in).nextLine();

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

            if (!isPasswordStrong(password)) {
                System.out.println("Password is not strong enough. It must meet the following criteria:");
                System.out.println("- At least 8 characters long");
                System.out.println("- Contains at least one uppercase letter");
                System.out.println("- Contains at least one lowercase letter");
                System.out.println("- Contains at least one digit");
                System.out.println("- Contains at least one special character");
                password = null; // Reset password to retry
            }
        } while (password == null);

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashPassword(password, salt);
        dbManager.saveUser(username, hashedPassword, saltBase64);
        System.out.println("User " + username + " registered successfully!");
    }

    public static String hashPassword(String password, byte[] salt) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hashed = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashed);
    }

    private boolean isPasswordStrong(String password) {
        if (password.length() < 8) return false;
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private String generateStrongPassword() {
        SecureRandom random = new SecureRandom();
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        String allChars = upperCase + lowerCase + digits + specialChars;

        StringBuilder password = new StringBuilder();
        // Ensure at least one character from each category
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Fill the rest of the password to meet minimum length (8 characters)
        for (int i = 4; i < 12; i++) { // Generate a 12-character password
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Shuffle the characters to randomize their order
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
