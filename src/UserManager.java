import java.util.Base64;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class UserManager {
    private DatabaseManager dbManager = new DatabaseManager();

    public void registerUser(String username) throws Exception {
        System.out.print("Enter master password: ");
        String password = new java.util.Scanner(System.in).nextLine();

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String saltBase64 = Base64.getEncoder().encodeToString(salt);

        System.out.println("aaa");
        String hashedPassword = hashPassword(password, salt);
        System.out.println("hashedPassword: " + hashedPassword);
        dbManager.saveUser(username, hashedPassword, saltBase64);
        System.out.println("User " + username + " registered successfully!");
    }

    public static String hashPassword(String password, byte[] salt) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hashed = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashed);
    }
}
