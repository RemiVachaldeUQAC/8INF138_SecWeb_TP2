import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordManager {
    private DatabaseManager dbManager = new DatabaseManager();

    public void addPassword(String username, String label, String password) throws Exception {
        // Vérifier si l'utilisateur existe
        if (!dbManager.userExists(username)) {
            throw new Exception("Error: User not found.");
        }

        // Demander le mot de passe maître
        System.out.print("Enter " + username + " master password: ");
        String masterPassword = new java.util.Scanner(System.in).nextLine();

        // Récupérer le mot de passe maître haché et le sel de l'utilisateur
        String storedHashedPassword = dbManager.getMasterPassword(username); // Récupère le mot de passe maître haché
        String storedSalt = dbManager.getUserSalt(username); // Récupère le sel de l'utilisateur

        // Hacher le mot de passe saisi avec le sel stocké
        String hashedInputPassword = UserManager.hashPassword(masterPassword, Base64.getDecoder().decode(storedSalt));

        // Vérifier si le mot de passe maître haché correspond à celui stocké
        if (!hashedInputPassword.equals(storedHashedPassword)) {
            throw new Exception("Error: Master password is incorrect.");
        }

        String aesKey = CryptoUtils.generateKey(storedHashedPassword);

        // Si le mot de passe est correct, chiffrer le nouveau mot de passe
        String encryptedPassword = CryptoUtils.encrypt(password, aesKey);

        // Sauvegarder le mot de passe chiffré
        dbManager.savePassword(username, label, encryptedPassword);
        System.out.println("Password " + label + " successfully saved!");
    }

    /*public void addPassword(String username, String label, String password) throws Exception {
        String masterPassword = dbManager.getMasterPassword(username);
        System.out.println("masterPassword: " + masterPassword);
        String aesKey = CryptoUtils.generateKey(masterPassword);
        System.out.println("aesKey: " + aesKey);

        String encryptedPassword = CryptoUtils.encrypt(password, aesKey);
        dbManager.savePassword(username, label, encryptedPassword);
        System.out.println("Password " + label + " successfully saved!");
    }*/

    public void showPassword(String username, String label) throws Exception {
        String masterPassword = dbManager.getMasterPassword(username);
        System.out.println("masterPassword: " + masterPassword);
        String aesKey = CryptoUtils.generateKey(masterPassword);
        System.out.println("aesKey: " + aesKey);

        String encryptedPassword = dbManager.getPassword(username, label);
        String plainPassword = CryptoUtils.decrypt(encryptedPassword, aesKey);
        System.out.println("Password " + label + " is: " + plainPassword);
    }
}
