// UserDAO.java
package dao;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DatabaseManager dbManager;
    
    public UserDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    // Create a new user
    public int createUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, password_hash, full_name, email, phone, " +
                      "role, department, status, hire_date) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        return dbManager.executeUpdate(query,
            user.getUsername(),
            user.getPasswordHash(),
            user.getFullName(),
            user.getEmail(),
            user.getPhone(),
            user.getRole().toString(),
            user.getDepartment(),
            user.getStatus().toString(),
            user.getHireDate()
        );
    }
    
    // Get user by ID
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, userId);
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get user by username
    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, username);
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get user by email
    public User getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, email);
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users ORDER BY full_name";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query);
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
            return users;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get users by role
    public List<User> getUsersByRole(User.Role role) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = ? ORDER BY full_name";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, role.toString());
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
            return users;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get users by department
    public List<User> getUsersByDepartment(String department) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE department = ? ORDER BY full_name";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, department);
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
            return users;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get active users
    public List<User> getActiveUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE status = 'ACTIVE' ORDER BY full_name";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query);
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
            return users;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Update user
    public boolean updateUser(User user) throws SQLException {
        String query = "UPDATE users SET username = ?, full_name = ?, email = ?, phone = ?, " +
                      "role = ?, department = ?, status = ?, hire_date = ?, updated_at = CURRENT_TIMESTAMP " +
                      "WHERE user_id = ?";
        
        int affectedRows = dbManager.executeUpdate(query,
            user.getUsername(),
            user.getFullName(),
            user.getEmail(),
            user.getPhone(),
            user.getRole().toString(),
            user.getDepartment(),
            user.getStatus().toString(),
            user.getHireDate(),
            user.getUserId()
        );
        
        return affectedRows > 0;
    }
    
    // Update password
    public boolean updatePassword(int userId, String newPasswordHash) throws SQLException {
        String query = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP " +
                      "WHERE user_id = ?";
        
        int affectedRows = dbManager.executeUpdate(query, newPasswordHash, userId);
        return affectedRows > 0;
    }
    
    // Update last login
    public boolean updateLastLogin(int userId) throws SQLException {
        String query = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        int affectedRows = dbManager.executeUpdate(query, userId);
        return affectedRows > 0;
    }
    
    // Delete user
    public boolean deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE user_id = ?";
        int affectedRows = dbManager.executeUpdate(query, userId);
        return affectedRows > 0;
    }
    
    // Deactivate user (soft delete)
    public boolean deactivateUser(int userId) throws SQLException {
        String query = "UPDATE users SET status = 'INACTIVE', updated_at = CURRENT_TIMESTAMP " +
                      "WHERE user_id = ?";
        int affectedRows = dbManager.executeUpdate(query, userId);
        return affectedRows > 0;
    }
    
    // Search users
    public List<User> searchUsers(String searchTerm) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE " +
                      "username LIKE ? OR " +
                      "full_name LIKE ? OR " +
                      "email LIKE ? OR " +
                      "department LIKE ? " +
                      "ORDER BY full_name";
        
        ResultSet rs = null;
        try {
            rs = dbManager.executeQuery(query, 
                "%" + searchTerm + "%",
                "%" + searchTerm + "%",
                "%" + searchTerm + "%",
                "%" + searchTerm + "%"
            );
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
            return users;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get user statistics
    public java.util.Map<String, Integer> getUserStatistics() throws SQLException {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        String query = "SELECT role, COUNT(*) as count FROM users WHERE status = 'ACTIVE' GROUP BY role";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query);
            
            while (rs.next()) {
                stats.put(rs.getString("role"), rs.getInt("count"));
            }
            
            // Add total active users
            query = "SELECT COUNT(*) as total FROM users WHERE status = 'ACTIVE'";
            rs = dbManager.executeQuery(query);
            if (rs.next()) {
                stats.put("total", rs.getInt("total"));
            }
            
            return stats;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Check if username exists
    public boolean usernameExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM users WHERE username = ?";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, username);
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            return false;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Check if email exists
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) as count FROM users WHERE email = ?";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, email);
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            return false;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Validate user credentials
    public User validateCredentials(String username, String passwordHash) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password_hash = ? AND status = 'ACTIVE'";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, username, passwordHash);
            
            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                // Update last login
                updateLastLogin(user.getUserId());
                return user;
            }
            return null;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Helper method to map ResultSet to User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(User.Role.valueOf(rs.getString("role").toUpperCase()));
        user.setDepartment(rs.getString("department"));
        user.setStatus(User.Status.valueOf(rs.getString("status").toUpperCase()));
        user.setHireDate(rs.getTimestamp("hire_date"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}

