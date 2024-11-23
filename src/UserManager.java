////import java.util.Base64;
////import java.security.MessageDigest;
////import java.security.SecureRandom;
////
////public class UserManager {
////    private DatabaseManager dbManager = new DatabaseManager();
////
////    public void registerUser(String username) throws Exception {
////        System.out.print("Enter master password: ");
////        String password = new java.util.Scanner(System.in).nextLine();
////
////        SecureRandom random = new SecureRandom();
////        byte[] salt = new byte[16];
////        random.nextBytes(salt);
////        String saltBase64 = Base64.getEncoder().encodeToString(salt);
////        String hashedPassword = hashPassword(password, salt);
////        dbManager.saveUser(username, hashedPassword, saltBase64);
////        System.out.println("User " + username + " registered successfully!");
////    }
////
////    public static String hashPassword(String password, byte[] salt) throws Exception {
////        MessageDigest digest = MessageDigest.getInstance("SHA-256");
////        digest.update(salt);
////        byte[] hashed = digest.digest(password.getBytes());
////        return Base64.getEncoder().encodeToString(hashed);
////    }
////}
import java.util.Base64;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class UserManager {
    private DatabaseManager dbManager = new DatabaseManager();

    public void registerUser(String username) throws Exception {
        String password = null;

        do {
            System.out.print("Enter master password: ");
            password = new java.util.Scanner(System.in).nextLine();

            if (!isPasswordStrong(password)) {
                System.out.println("Password is not strong enough. It must meet the following criteria:");
                System.out.println("- At least 8 characters long");
                System.out.println("- Contains at least one uppercase letter");
                System.out.println("- Contains at least one lowercase letter");
                System.out.println("- Contains at least one digit");
                System.out.println("- Contains at least one special character");
            }
        } while (!isPasswordStrong(password));

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
}
