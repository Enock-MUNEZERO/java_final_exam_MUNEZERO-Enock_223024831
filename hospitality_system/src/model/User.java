// User.java
package model;

import java.sql.Timestamp;

public class User {
    public enum Role {
        MANAGER, RECEPTIONIST, HOUSEKEEPING, MAINTENANCE, RESTAURANT, ADMIN
    }
    
    public enum Status {
        ACTIVE, ON_LEAVE, TERMINATED, INACTIVE
    }
    
    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private String department;
    private Status status;
    private Timestamp hireDate;
    private Timestamp lastLogin;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public User() {
        this.status = Status.ACTIVE;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
    
    public User(String username, String passwordHash, String fullName, 
                String email, Role role, String department) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.department = department;
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public Timestamp getHireDate() { return hireDate; }
    public void setHireDate(Timestamp hireDate) { this.hireDate = hireDate; }
    
    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    // Business Logic Methods
    public boolean isManager() {
        return role == Role.MANAGER || role == Role.ADMIN;
    }
    
    public boolean isActive() {
        return status == Status.ACTIVE;
    }
    
    public boolean canManageUsers() {
        return role == Role.MANAGER || role == Role.ADMIN;
    }
    
    public boolean canManageRooms() {
        return role == Role.MANAGER || role == Role.RECEPTIONIST || role == Role.ADMIN;
    }
    
    public String getRoleDisplayName() {
        switch (role) {
            case MANAGER: return "Manager";
            case RECEPTIONIST: return "Receptionist";
            case HOUSEKEEPING: return "Housekeeping";
            case MAINTENANCE: return "Maintenance";
            case RESTAURANT: return "Restaurant Staff";
            case ADMIN: return "Administrator";
            default: return "Staff";
        }
    }
    
    public String getStatusColor() {
        switch (status) {
            case ACTIVE: return "#27ae60"; // Green
            case ON_LEAVE: return "#f39c12"; // Orange
            case TERMINATED: return "#e74c3c"; // Red
            case INACTIVE: return "#95a5a6"; // Gray
            default: return "#95a5a6";
        }
    }
    
    @Override
    public String toString() {
        return fullName + " (" + username + ") - " + getRoleDisplayName();
    }
}