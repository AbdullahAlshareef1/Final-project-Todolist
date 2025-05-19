package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/tododb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "changeme";
    private static Connection connection;

    public static Connection connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected.");
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace(); // Optional: Useful for debugging
            connection = null;
        }
        return connection;
    }
}
