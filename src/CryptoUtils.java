import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.util.Base64;

public class CryptoUtils {

    // Génère une clé de chiffrement à partir d'un mot de passe maître
    public static String generateKey(String masterPassword) throws Exception {
        // Utiliser un "salt" fixe (par exemple, un tableau de zéros de 16 octets)
        byte[] salt = new byte[16]; // Salt fixe : 16 octets, tous initialisés à 0

        // Spécifier la clé dérivée en utilisant PBKDF2 avec HMAC-SHA256
        KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, 100000, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        // Générer la clé secrète
        byte[] key = factory.generateSecret(spec).getEncoded();

        // Encoder la clé en Base64 pour une représentation en chaîne de caractères
        return Base64.getEncoder().encodeToString(key);
    }

    // Chiffre les données avec la clé AES
    public static String encrypt(String data, String aesKey) throws Exception {
        // Convertir la clé AES de Base64 en bytes
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(aesKey), "AES");

        // Obtenir une instance de Cipher pour le chiffrement AES
        Cipher cipher = Cipher.getInstance("AES");

        // Initialiser le Cipher en mode chiffrement avec la clé spécifiée
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // Chiffrer les données
        byte[] encrypted = cipher.doFinal(data.getBytes());

        // Encoder les données chiffrées en Base64 pour une représentation en chaîne de caractères
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Déchiffre les données avec la clé AES
    public static String decrypt(String data, String aesKey) throws Exception {
        // Convertir la clé AES de Base64 en bytes
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(aesKey), "AES");

        // Obtenir une instance de Cipher pour le déchiffrement AES
        Cipher cipher = Cipher.getInstance("AES");

        // Initialiser le Cipher en mode déchiffrement avec la clé spécifiée
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        // Déchiffrer les données
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(data));

        // Convertir les données déchiffrées en chaîne de caractères
        return new String(decrypted);
    }

    // Vérifie si un mot de passe entré correspond à un mot de passe haché stocké
    public static boolean verifyPassword(String enteredPassword, String storedHashedPassword) throws Exception {
        // Générer le hachage de `enteredPassword`
        String hashedEnteredPassword = generateKey(enteredPassword);

        // Comparer les hachages
        return hashedEnteredPassword.equals(storedHashedPassword);
    }
}
