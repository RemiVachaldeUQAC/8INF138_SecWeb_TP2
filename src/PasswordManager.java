import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class PasswordManager {
    // Instance de DatabaseManager pour interagir avec la base de données
    private DatabaseManager dbManager = new DatabaseManager();

    // Méthode pour ajouter un mot de passe pour un utilisateur
//    public void addPassword(String username, String label, String password) throws Exception {
//        // Vérifier si l'utilisateur existe
//        if (!dbManager.userExists(username)) {
//            throw new Exception("Error: User not found.");
//        }
//
//        // Vérifier si le mot de passe maître est correct
//        if (!checkPassword(username)) {
//            throw new Exception();
//        }
//
//        // Si le mot de passe maître est correct, chiffrer le nouveau mot de passe
//        String storedHashedPassword = dbManager.getMasterPassword(username);
//        String aesKey = CryptoUtils.generateKey(storedHashedPassword);
//        String encryptedPassword = CryptoUtils.encrypt(password, aesKey);
//
//        // Sauvegarder le mot de passe chiffré
//        dbManager.savePassword(username, label, encryptedPassword);
//        System.out.println("Password " + label + " successfully saved!");
//    }
    public void addPassword(String username, String label, String password) throws Exception {
        // Vérifier si l'utilisateur existe
        if (!dbManager.userExists(username)) {
            throw new Exception("Error: User not found.");
        }

        // Vérifier si le mot de passe maître est correct
        if (!checkPassword(username)) {
            throw new Exception();
        }

        // Validation et renforcement du mot de passe
        Scanner scanner = new Scanner(System.in);
        while (password == null || !UserManager.isPasswordStrong(password)) {
            if (password != null && !UserManager.isPasswordStrong(password)) {
                System.out.println("Password is not strong enough. It must meet the following criteria:");
                System.out.println("- At least 8 characters long");
                System.out.println("- Contains at least one uppercase letter");
                System.out.println("- Contains at least one lowercase letter");
                System.out.println("- Contains at least one digit");
                System.out.println("- Contains at least one special character");
            }

            System.out.print("Enter a new password (or type 'generate' to get a strong password suggestion): ");
            String input = scanner.nextLine();

            if ("generate".equalsIgnoreCase(input)) {
                password = UserManager.generateStrongPassword();
                System.out.println("Suggested strong password: " + password);
                System.out.print("Do you want to use this password? (yes/no): ");
                String choice = scanner.nextLine();
                if ("yes".equalsIgnoreCase(choice)) {
                    break;
                } else {
                    password = null;
                }
            } else {
                password = input;
            }
        }

        // Chiffrer le mot de passe validé
        String storedHashedPassword = dbManager.getMasterPassword(username);
        String aesKey = CryptoUtils.generateKey(storedHashedPassword);
        String encryptedPassword = CryptoUtils.encrypt(password, aesKey);

        // Sauvegarder le mot de passe chiffré
        dbManager.savePassword(username, label, encryptedPassword);
        System.out.println("Password " + label + " successfully saved!");
    }

    public void updatePassword(String username, String label) throws Exception {
        // Vérifier si l'utilisateur existe
        if (!dbManager.userExists(username)) {
            throw new Exception("Error: User not found.");
        }

        // Vérifier si le mot de passe maître est correct
        if (!checkPassword(username)) {
            throw new Exception("Error: Master password is incorrect.");
        }

        // Demander le nouveau mot de passe
        Scanner scanner = new Scanner(System.in);
        String newPassword = null;
        while (newPassword == null || !UserManager.isPasswordStrong(newPassword)) {
            if (newPassword != null && !UserManager.isPasswordStrong(newPassword)) {
                System.out.println("Password is not strong enough. It must meet the following criteria:");
                System.out.println("- At least 8 characters long");
                System.out.println("- Contains at least one uppercase letter");
                System.out.println("- Contains at least one lowercase letter");
                System.out.println("- Contains at least one digit");
                System.out.println("- Contains at least one special character");
            }

            System.out.print("Enter a new password for the label " + label + " (or type 'generate' to get a strong password suggestion): ");
            String input = scanner.nextLine();

            if ("generate".equalsIgnoreCase(input)) {
                newPassword = UserManager.generateStrongPassword();
                System.out.println("Suggested strong password: " + newPassword);
                System.out.print("Do you want to use this password? (yes/no): ");
                String choice = scanner.nextLine();
                if ("yes".equalsIgnoreCase(choice)) {
                    break;
                } else {
                    newPassword = null;
                }
            } else {
                newPassword = input;
            }
        }

        // Chiffrer le nouveau mot de passe
        String masterPassword = dbManager.getMasterPassword(username);
        String aesKey = CryptoUtils.generateKey(masterPassword);
        String encryptedPassword = CryptoUtils.encrypt(newPassword, aesKey);

        // Mettre à jour le mot de passe dans la base de données
        dbManager.updatePassword(username, label, encryptedPassword);
        System.out.println("Password for label " + label + " is updated.");
    }

    // Méthode pour afficher un mot de passe pour un utilisateur
    public void showPassword(String username, String label) throws Exception {
        // Vérifier si l'utilisateur existe
        if (!dbManager.userExists(username)) {
            throw new Exception("Error: User not found.");
        }

        // Vérifier si le mot de passe maître est correct
        if (!checkPassword(username)) {
            throw new Exception();
        }

        // Récupérer le mot de passe maître et générer la clé AES
        String masterPassword = dbManager.getMasterPassword(username);
        String aesKey = CryptoUtils.generateKey(masterPassword);

        // Récupérer et déchiffrer le mot de passe
        String encryptedPassword = dbManager.getPassword(username, label);
        String plainPassword = CryptoUtils.decrypt(encryptedPassword, aesKey);
        System.out.println("Password " + label + " is: " + plainPassword);
    }

    // Méthode pour supprimer un mot de passe
    public void deletePassword(String username, String label) throws Exception {
        // Vérifier si l'utilisateur existe
        if (!dbManager.userExists(username)) {
            throw new Exception("Error: User not found.");
        }

        // Vérifier si le mot de passe maître est correct
        if (!checkPassword(username)) {
            throw new Exception("Error: Master password is incorrect.");
        }

        // Supprimer le mot de passe de la base de données
        dbManager.deletePassword(username, label);
        System.out.println("Password " + label + " is deleted");
    }

    // Méthode pour mettre à jour un mot de passe
//    public void updatePassword(String username, String label) throws Exception {
//        // Vérifier si l'utilisateur existe
//        if (!dbManager.userExists(username)) {
//            throw new Exception("Error: User not found.");
//        }
//
//        // Vérifier si le mot de passe maître est correct
//        if (!checkPassword(username)) {
//            throw new Exception("Error: Master password is incorrect.");
//        }
//
//        // Demander le nouveau mot de passe
//        System.out.print("Enter the new password for the label " + label + ": ");
//        String newPassword = new Scanner(System.in).nextLine();
//
//        // Chiffrer le nouveau mot de passe
//        String masterPassword = dbManager.getMasterPassword(username);
//        String aesKey = CryptoUtils.generateKey(masterPassword);
//        String encryptedPassword = CryptoUtils.encrypt(newPassword, aesKey);
//
//        // Mettre à jour le mot de passe dans la base de données
//        dbManager.updatePassword(username, label, encryptedPassword);
//        System.out.println("Password for label " + label + " is updated.");
//    }


    // Méthode pour mettre à jour le nom d'une étiquette
    public void updateLabel(String username, String oldLabel) throws Exception {
        // Vérifier si l'utilisateur existe
        if (!dbManager.userExists(username)) {
            throw new Exception("Error: User not found.");
        }

        // Vérifier si le mot de passe maître est correct
        if (!checkPassword(username)) {
            throw new Exception("Error: Master password is incorrect.");
        }

        // Demander le nouveau nom de l'étiquette
        System.out.print("Enter the new name for the label: ");
        String newLabel = new Scanner(System.in).nextLine();

        // Mettre à jour le nom de l'étiquette dans la base de données
        dbManager.updateLabel(username, oldLabel, newLabel);
        System.out.println("Label " + oldLabel + " is updated to " + newLabel);
    }

    // Méthode pour vérifier le mot de passe maître de l'utilisateur
    public boolean checkPassword(String username) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0; // Compteur de tentatives

        while (attempts < 3) {
            System.out.print("Enter " + username + " master password: ");
            String masterPassword = scanner.nextLine();

            // Récupérer le mot de passe maître haché et le sel de l'utilisateur
            String storedHashedPassword = dbManager.getMasterPassword(username);
            String storedSalt = dbManager.getUserSalt(username);
            String hashedInputPassword = UserManager.hashPassword(masterPassword, Base64.getDecoder().decode(storedSalt));

            // Vérifier si le mot de passe maître haché correspond à celui stocké
            if (hashedInputPassword.equals(storedHashedPassword)) {
                return true; // Mot de passe correct
            }

            // Incrémenter les tentatives
            attempts++;
            System.out.println("Incorrect master password. Attempts left: " + (3 - attempts));

            // Si les 3 tentatives sont épuisées, activer un délai
            if (attempts == 3) {
                System.out.println("Too many failed attempts. Please wait 30 seconds before trying again.");
                try {
                    TimeUnit.SECONDS.sleep(30); // Bloquer l'exécution pendant 30 secondes
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                attempts = 0; // Réinitialiser les tentatives après le délai
            }
        }
        throw new Exception("Error: Master password is incorrect."); // Si toujours incorrect après le délai
    }
}
