import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.util.Base64;

public class CryptoUtils {
    public static String generateKey(String masterPassword) throws Exception {
        // Utiliser un "salt" fixe (par exemple, un tableau de zéros de 16 octets)
        byte[] salt = new byte[16]; // Salt fixe : 16 octets, tous initialisés à 0

        // Spécifier la clé dérivée
        KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, 100000, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] key = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(key);
    }



    public static String encrypt(String data, String aesKey) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(aesKey), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String data, String aesKey) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(aesKey), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(decrypted);
    }
}
