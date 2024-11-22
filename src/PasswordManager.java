import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordManager {
    private DatabaseManager dbManager = new DatabaseManager();

    public void addPassword(String username, String label, String password) throws Exception {
        // Vérifier si l'utilisateur existe
        if (!dbManager.userExists(username)) { throw new Exception("Error: User not found."); }

        // Vérifier si le mot de passe est correct
        if (!checkPassword(username)) {throw new Exception();}

        // Si le mot de passe est correct, chiffrer le nouveau mot de passe
        String storedHashedPassword = dbManager.getMasterPassword(username);
        String aesKey = CryptoUtils.generateKey(storedHashedPassword);
        String encryptedPassword = CryptoUtils.encrypt(password, aesKey);

        // Sauvegarder le mot de passe chiffré
        dbManager.savePassword(username, label, encryptedPassword);
        System.out.println("Password " + label + " successfully saved!");
    }

    public void showPassword(String username, String label) throws Exception {
        // Vérifier si l'utilisateur existe
        if (!dbManager.userExists(username)) { throw new Exception("Error: User not found."); }

        // Vérifier si le mot de passe est correct
        if (!checkPassword(username)) {throw new Exception();}

        String masterPassword = dbManager.getMasterPassword(username);
        String aesKey = CryptoUtils.generateKey(masterPassword);

        String encryptedPassword = dbManager.getPassword(username, label);
        String plainPassword = CryptoUtils.decrypt(encryptedPassword, aesKey);
        System.out.println("Password " + label + " is: " + plainPassword);
    }

    public boolean checkPassword(String username) throws Exception {
        // Demander le mot de passe maître
        System.out.print("Enter "+ username +" master password: ");
        String masterPassword = new java.util.Scanner(System.in).nextLine();

        // Récupérer le mot de passe maître haché et le sel de l'utilisateur puis hasher le mot de passe Récupéré
        String storedHashedPassword = dbManager.getMasterPassword(username);
        String storedSalt = dbManager.getUserSalt(username);
        String hashedInputPassword = UserManager.hashPassword(masterPassword, Base64.getDecoder().decode(storedSalt));

        // Vérifier si le mot de passe maître haché correspond à celui stocké
        if(hashedInputPassword.equals(storedHashedPassword)) { return true; }
        throw new Exception("Error: Master password is incorrect.");
    }
}
