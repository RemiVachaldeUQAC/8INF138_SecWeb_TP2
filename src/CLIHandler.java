import java.util.Scanner;

public class CLIHandler {
    private UserManager userManager = new UserManager();
    private PasswordManager passwordManager = new PasswordManager();

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("$> ");
            String command = scanner.nextLine();
            String[] args = command.split(" ");

            try {
                if (args[0].equals("passmanager")) {
                    switch (args[1]) {
                        case "-r":
                            userManager.registerUser(args[2]);
                            break;
                        case "-u":
                            String username = args[2];
                            switch (args[3]) {
                                case "-a": // add a password
                                    passwordManager.addPassword(username, args[4], args[5]);
                                    break;
                                case "-s": // show a password
                                    passwordManager.showPassword(username, args[4]);
                                    break;
                                case "-d": // delete a password
                                    passwordManager.deletePassword(username, args[4]);
                                    break;
                                case "-ul": // change label's name
                                    passwordManager.updateLabel(username, args[4]);
                                    break;
                                case "-up": // change label's password
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
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
