package advancejavaproject4.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for managing MySQL database connections
 * @author yigitt
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private static final String HOST = "127.0.0.1";
    private static final String PORT = "3307";
    private static final String DATABASE = "student_records_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false";

    
    private DatabaseConnection() {
        // Private constructor for Singleton pattern
    }

 
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Hocanızın gösterdiği gibi basit format
                Class.forName("com.mysql.jdbc.Driver");

                // Basit bağlantı - ek parametresiz
                connection = DriverManager.getConnection(URL, USER, PASSWORD);

                System.out.println("Database connection established successfully");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Please add mysql-connector-java.jar to your project libraries.", e);
        } catch (SQLException e) {
            // Print detailed error for debugging
            System.err.println("=== DETAILED SQL ERROR ===");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            System.err.println("========================");

            // Provide user-friendly error messages
            String errorMessage = getReadableErrorMessage(e);
            throw new SQLException(errorMessage, e);
        }
    }

 
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }


    public List<String> getTableNames() throws SQLException {
        List<String> tables = new ArrayList<>();
        String query = "SHOW TABLES";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }

        return tables;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    private String getReadableErrorMessage(SQLException e) {
        int errorCode = e.getErrorCode();
        String sqlState = e.getSQLState();

        switch (errorCode) {
            case 0:
                return "Cannot connect to MySQL server at " + HOST + ":" + PORT +
                        "\nPlease ensure XAMPP is running and MySQL service is started.";
            case 1045:
                return "Authentication failed. Check your username and password.\n" +
                        "Default XAMPP credentials are: User='root', Password=''";
            case 1049:
                return "Database '" + DATABASE + "' does not exist.\n" +
                        "Please create the database using phpMyAdmin or run the schema.sql script.";
            case 1146:
                return "Table does not exist in the database.\n" +
                        "Please run the schema.sql script to create the required tables.";
            default:
                if ("08S01".equals(sqlState)) {
                    return "Communication link failure. MySQL server might have stopped.\n" +
                            "Please check if XAMPP MySQL service is running.";
                }
                return "Database error: " + e.getMessage();
        }
    }

    public String getDatabaseURL() {
        return URL;
    }

 
    public String getDatabaseName() {
        return DATABASE;
    }
}
