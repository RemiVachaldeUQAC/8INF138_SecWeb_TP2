import java.util.Scanner;

public class CLIHandler {
    // Instances de UserManager et PasswordManager pour gérer les utilisateurs et les mots de passe
    private UserManager userManager = new UserManager();
    private PasswordManager passwordManager = new PasswordManager();

    // Méthode principale pour exécuter le gestionnaire de ligne de commande
    public void run() {
        Scanner scanner = new Scanner(System.in);

        // Boucle infinie pour lire les commandes de l'utilisateur
        while (true) {
            System.out.println("$> ");
            String command = scanner.nextLine();
            String[] args = command.split(" ");

            try {
                // Vérifier si la commande commence par "passmanager"
                if (args[0].equals("passmanager")) {
                    switch (args[1]) {
                        case "-r":
                            // Enregistrer un nouvel utilisateur
                            userManager.registerUser(args[2]);
                            break;
                        case "-u":
                            String username = args[2];
                            switch (args[3]) {
                                case "-a": // Ajouter un mot de passe
                                    passwordManager.addPassword(username, args[4], args[5]);
                                    break;
                                case "-s": // Afficher un mot de passe
                                    passwordManager.showPassword(username, args[4]);
                                    break;
                                case "-d": // Supprimer un mot de passe
                                    passwordManager.deletePassword(username, args[4]);
                                    break;
                                case "-ul": // Changer le nom de l'étiquette
                                    passwordManager.updateLabel(username, args[4]);
                                    break;
                                case "-up": // Changer le mot de passe de l'étiquette
                                    passwordManager.updatePassword(username, args[4]);
                                    break;
                                default:
                                    System.out.println("Invalid command");
                            }
                            break;
                        default:
                            System.out.println("Invalid command");
                    }
                } else {
                    System.out.println("Unknown command");
                }
            } catch (Exception e) {
                // Gérer les exceptions et afficher un message d'erreur
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
