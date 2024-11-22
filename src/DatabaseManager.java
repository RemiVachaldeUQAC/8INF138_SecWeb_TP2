import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    public static final String DB_KEY=getDBKey();

    private static final String DB_URL = "jdbc:sqlite:db/data.sqlite";
    private static final String CREATE_USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                salt TEXT NOT NULL
            );
            """;
    private static final String CREATE_PASSWORDS_TABLE = """
            CREATE TABLE IF NOT EXISTS passwords (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                label TEXT NOT NULL,
                password TEXT NOT NULL,
                FOREIGN KEY(username) REFERENCES users(username)
            );
            """;

    // Establishes a connection to the SQLite database
    private Connection connect() throws Exception {
        // Assurer que le répertoire db existe, sinon on le crée
        File dbDir = new File("db");
        if (!dbDir.exists()) {
            dbDir.mkdir();  // Créer le répertoire db si nécessaire
        }

        // Connecter à la base de données SQLite (le fichier .sqlite sera créé automatiquement si inexistant)
        Connection connection = DriverManager.getConnection(DB_URL);
        initializeDatabase(connection); // Créer les tables si nécessaire
        return connection;
    }

    // Initialisation de la base de données (création des tables si elles n'existent pas)
    private void initializeDatabase(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(CREATE_USERS_TABLE);
            stmt.execute(CREATE_PASSWORDS_TABLE);
        }
    }

    // Sauvegarder un utilisateur dans la base de données
    public void saveUser(String username, String password, String salt) throws Exception {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO users (username, password, salt) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, CryptoUtils.encrypt(username,DB_KEY));
                stmt.setString(2, CryptoUtils.encrypt(password,DB_KEY));
                stmt.setString(3, CryptoUtils.encrypt(salt,DB_KEY));
                stmt.executeUpdate();
            }
        }
    }

    // Sauvegarder un mot de passe pour un utilisateur
    public void savePassword(String username, String label, String encryptedPassword) throws Exception {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO passwords (username, label, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, CryptoUtils.encrypt(username,DB_KEY));
                stmt.setString(2, CryptoUtils.encrypt(label,DB_KEY));
                stmt.setString(3, CryptoUtils.encrypt(encryptedPassword,DB_KEY));
                stmt.executeUpdate();
            }
        }
    }

    // Récupérer un mot de passe pour un utilisateur et une étiquette
    public String getPassword(String username, String label) throws Exception {
        try (Connection conn = connect()) {
            String sql = "SELECT password FROM passwords WHERE username = ? AND label = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, CryptoUtils.encrypt(username,DB_KEY));
                stmt.setString(2, CryptoUtils.encrypt(label,DB_KEY));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return CryptoUtils.decrypt(rs.getString("password"),DB_KEY);
                    } else {
                        throw new Exception("Password not found for the specified label.");
                    }
                }
            }
        }
    }

    // Récupérer le mot de passe de l'utilisateur (mot de passe maître)
    public String getMasterPassword(String username) throws Exception {
        try (Connection conn = connect()) {
            String sql = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, CryptoUtils.encrypt(username,DB_KEY));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return CryptoUtils.decrypt(rs.getString("password"),DB_KEY);
                    } else {
                        throw new Exception("User not found.");
                    }
                }
            }
        }
    }

    public boolean userExists(String username) throws Exception {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, CryptoUtils.encrypt(username,DB_KEY));
                try (ResultSet rs = stmt.executeQuery()) {
                    // Retourne true si au moins une ligne correspond
                    return rs.getInt(1) > 0;
                }
            }
        }
    }

    // Récupère le sel (salt) pour un utilisateur donné
    public String getUserSalt(String username) throws Exception {
        try (Connection conn = connect()) {
            String sql = "SELECT salt FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, CryptoUtils.encrypt(username,DB_KEY));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return CryptoUtils.decrypt(rs.getString("salt"),DB_KEY);
                    } else {
                        throw new Exception("Error: User not found.");
                    }
                }
            }
        }
    }
    public static String getDBKey(){
        Map<String, String> m;
        String s;
        try {
            m = EnvLoader.loadEnv();
            s=CryptoUtils.generateKey(m.get("DB_PASSWORD"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return s;
    }



}
