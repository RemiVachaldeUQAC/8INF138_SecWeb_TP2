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
                            if (args[3].equals("-a")) {
                                if(args.length==6){
                                    passwordManager.addPassword(username, args[4], args[5]);
                                }
                                else {
                                    System.out.println("invalide number of arguments");
                                }
                            } else if (args[3].equals("-s")) {
                                passwordManager.showPassword(username, args[4]);
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
