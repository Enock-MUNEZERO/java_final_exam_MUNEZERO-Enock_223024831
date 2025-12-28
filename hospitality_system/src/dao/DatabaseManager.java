package dao;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    
   
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospitality_management";
    private static final String DB_USER = "hotel_admin";
    private static final String DB_PASSWORD = "secure_password_123";
    
    private DatabaseManager() {
        initializeConnection();
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void initializeConnection() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connection properties
            Properties properties = new Properties();
            properties.setProperty("user", DB_USER);
            properties.setProperty("password", DB_PASSWORD);
            properties.setProperty("useSSL", "false");
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("characterEncoding", "UTF-8");
            properties.setProperty("useUnicode", "true");
            properties.setProperty("serverTimezone", "UTC");
            
            // Establish connection
            connection = DriverManager.getConnection(DB_URL, properties);
            
            // Test connection
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection established successfully!");
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() throws SQLException {
        // Check if connection is still valid
        if (connection == null || connection.isClosed()) {
            initializeConnection();
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection!");
            e.printStackTrace();
        }
    }
    
    // Utility method for executing queries
    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = getConnection().prepareStatement(query, 
                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                ResultSet.CONCUR_READ_ONLY);
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            rs = stmt.executeQuery();
            
        } catch (SQLException e) {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            throw e;
        }
        
        // Note: Caller must close the ResultSet and Statement
        return rs;
    }
    
    // Utility method for executing updates
    public int executeUpdate(String query, Object... params) throws SQLException {
        PreparedStatement stmt = null;
        
        try {
            stmt = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            int affectedRows = stmt.executeUpdate();
            
            // Return generated keys for INSERT statements
            if (query.trim().toUpperCase().startsWith("INSERT")) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            
            return affectedRows;
            
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    // Transaction management
    public void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }
    
    public void commitTransaction() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }
    
    public void rollbackTransaction() throws SQLException {
        getConnection().rollback();
        getConnection().setAutoCommit(true);
    }
    
    // Check database health
    public boolean isConnectionHealthy() {
        try {
            return getConnection() != null && 
                   !getConnection().isClosed() && 
                   getConnection().isValid(5); // 5 second timeout
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Backup database
    public boolean backupDatabase(String backupPath) {
        // Implementation for database backup
        // This is a simplified version - in production, use mysqldump
        return false;
    }
}