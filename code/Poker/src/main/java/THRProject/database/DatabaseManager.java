package THRProject.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // Percorso del DB: Salva i nella cartella "data" del progetto
    private static final String DB_URL = "jdbc:h2:./data/pokerdb"; 
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection connection;

    public DatabaseManager() {
        if (connect()) {
            createTable();
        }
    }

    private boolean connect() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connessione al Database H2 stabilita.");
            return true;
        } catch (SQLException e) {
            System.err.println("Errore di connessione al Database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "username VARCHAR(255) PRIMARY KEY, " +
                     "password VARCHAR(255) NOT NULL)";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Errore creazione tabella: " + e.getMessage());
        }
    }

    public String registerUser(String username, String password) {
        if (userExists(username)) {
            return "account esistente";
        }

        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return "OK";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Errore Database";
        }
    }

    public String loginUser(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        return "OK";
                    } else {
                        return "password errata";
                    }
                } else {
                    return "account inesistente";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Errore Database";
        }
    }

    private boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}