import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordManager {
    private DatabaseManager dbManager = new DatabaseManager();

    public void addPassword(String username, String label, String password) throws Exception {
        String masterPassword = dbManager.getMasterPassword(username);
        System.out.println("masterPassword: " + masterPassword);
        String aesKey = CryptoUtils.generateKey(masterPassword);
        System.out.println("aesKey: " + aesKey);

        String encryptedPassword = CryptoUtils.encrypt(password, aesKey);
        dbManager.savePassword(username, label, encryptedPassword);
        System.out.println("Password " + label + " successfully saved!");
    }

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
