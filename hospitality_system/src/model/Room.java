// Room.java
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Room {
    public enum Status {
        AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE, CLEANING
    }
    
    private int roomId;
    private String roomNumber;
    private String roomType;
    private String floor;
    private String wingSection;
    private int capacity;
    private String bedConfiguration;
    private int roomSizeSqft;
    private String viewType;
    private boolean smokingAllowed;
    private BigDecimal basePrice;
    private BigDecimal weekendSurchargePercent;
    private String currency;
    private Status status;
    private String description;
    private List<String> amenities;
    private Timestamp lastCleaned;
    private Timestamp lastMaintenance;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public Room() {
        this.amenities = new ArrayList<>();
        this.status = Status.AVAILABLE;
        this.currency = "USD";
        this.weekendSurchargePercent = new BigDecimal("15.00");
        this.smokingAllowed = false;
        this.capacity = 2;
    }
    
    public Room(String roomNumber, String roomType, String floor, 
                BigDecimal basePrice, int capacity) {
        this();
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.floor = floor;
        this.basePrice = basePrice;
        this.capacity = capacity;
    }
    
    // Getters and Setters
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
    
    public String getWingSection() { return wingSection; }
    public void setWingSection(String wingSection) { this.wingSection = wingSection; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public String getBedConfiguration() { return bedConfiguration; }
    public void setBedConfiguration(String bedConfiguration) { this.bedConfiguration = bedConfiguration; }
    
    public int getRoomSizeSqft() { return roomSizeSqft; }
    public void setRoomSizeSqft(int roomSizeSqft) { this.roomSizeSqft = roomSizeSqft; }
    
    public String getViewType() { return viewType; }
    public void setViewType(String viewType) { this.viewType = viewType; }
    
    public boolean isSmokingAllowed() { return smokingAllowed; }
    public void setSmokingAllowed(boolean smokingAllowed) { this.smokingAllowed = smokingAllowed; }
    
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    
    public BigDecimal getWeekendSurchargePercent() { return weekendSurchargePercent; }
    public void setWeekendSurchargePercent(BigDecimal weekendSurchargePercent) { 
        this.weekendSurchargePercent = weekendSurchargePercent; 
    }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    
    public Timestamp getLastCleaned() { return lastCleaned; }
    public void setLastCleaned(Timestamp lastCleaned) { this.lastCleaned = lastCleaned; }
    
    public Timestamp getLastMaintenance() { return lastMaintenance; }
    public void setLastMaintenance(Timestamp lastMaintenance) { this.lastMaintenance = lastMaintenance; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    // Business Logic Methods
    public BigDecimal calculateTotalPrice(int nights, boolean includeWeekend) {
        BigDecimal total = basePrice.multiply(new BigDecimal(nights));
        
        if (includeWeekend && weekendSurchargePercent != null) {
            BigDecimal weekendSurcharge = total.multiply(weekendSurchargePercent)
                                             .divide(new BigDecimal("100"));
            total = total.add(weekendSurcharge);
        }
        
        return total;
    }
    
    public boolean isAvailable() {
        return status == Status.AVAILABLE;
    }
    
    public boolean canCheckIn() {
        return status == Status.AVAILABLE || status == Status.RESERVED;
    }
    
    public String getFormattedPrice() {
        return currency + " " + basePrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }
    
    public String getStatusColor() {
        switch (status) {
            case AVAILABLE: return "#27ae60"; // Green
            case OCCUPIED: return "#e74c3c";  // Red
            case RESERVED: return "#f39c12";  // Orange
            case MAINTENANCE: return "#34495e"; // Dark gray
            case CLEANING: return "#3498db";  // Blue
            default: return "#95a5a6";        // Gray
        }
    }
    
    @Override
    public String toString() {
        return "Room #" + roomNumber + " - " + roomType + " (" + status + ")";
    }
}