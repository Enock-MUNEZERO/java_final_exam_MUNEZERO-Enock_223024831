// RoomDAO.java
package dao;

import model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RoomDAO {
    private DatabaseManager dbManager;
    
    public RoomDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    // Create a new room
    public int createRoom(Room room) throws SQLException {
        String query = "INSERT INTO rooms (room_number, room_type, floor, wing_section, capacity, " +
                      "bed_configuration, room_size_sqft, view_type, smoking_allowed, base_price, " +
                      "weekend_surcharge_percent, currency, status, description) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        return dbManager.executeUpdate(query,
            room.getRoomNumber(),
            room.getRoomType(),
            room.getFloor(),
            room.getWingSection(),
            room.getCapacity(),
            room.getBedConfiguration(),
            room.getRoomSizeSqft(),
            room.getViewType(),
            room.isSmokingAllowed(),
            room.getBasePrice(),
            room.getWeekendSurchargePercent(),
            room.getCurrency(),
            room.getStatus().toString(),
            room.getDescription()
        );
    }
    
    // Get room by ID
    public Room getRoomById(int roomId) throws SQLException {
        String query = "SELECT * FROM rooms WHERE room_id = ?";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, roomId);
            
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
            return null;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get room by room number
    public Room getRoomByNumber(String roomNumber) throws SQLException {
        String query = "SELECT * FROM rooms WHERE room_number = ?";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, roomNumber);
            
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
            return null;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get all rooms
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms ORDER BY room_number";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query);
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
            return rooms;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get rooms by status
    public List<Room> getRoomsByStatus(String status) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE status = ? ORDER BY room_number";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, status);
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
            return rooms;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Get rooms by type
    public List<Room> getRoomsByType(String roomType) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE room_type = ? ORDER BY room_number";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, roomType);
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
            return rooms;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Update room
    public boolean updateRoom(Room room) throws SQLException {
        String query = "UPDATE rooms SET room_number = ?, room_type = ?, floor = ?, wing_section = ?, " +
                      "capacity = ?, bed_configuration = ?, room_size_sqft = ?, view_type = ?, " +
                      "smoking_allowed = ?, base_price = ?, weekend_surcharge_percent = ?, " +
                      "currency = ?, status = ?, description = ?, updated_at = CURRENT_TIMESTAMP " +
                      "WHERE room_id = ?";
        
        int affectedRows = dbManager.executeUpdate(query,
            room.getRoomNumber(),
            room.getRoomType(),
            room.getFloor(),
            room.getWingSection(),
            room.getCapacity(),
            room.getBedConfiguration(),
            room.getRoomSizeSqft(),
            room.getViewType(),
            room.isSmokingAllowed(),
            room.getBasePrice(),
            room.getWeekendSurchargePercent(),
            room.getCurrency(),
            room.getStatus().toString(),
            room.getDescription(),
            room.getRoomId()
        );
        
        return affectedRows > 0;
    }
    
    // Delete room
    public boolean deleteRoom(int roomId) throws SQLException {
        String query = "DELETE FROM rooms WHERE room_id = ?";
        int affectedRows = dbManager.executeUpdate(query, roomId);
        return affectedRows > 0;
    }
    
    // Update room status
    public boolean updateRoomStatus(int roomId, String status) throws SQLException {
        String query = "UPDATE rooms SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE room_id = ?";
        int affectedRows = dbManager.executeUpdate(query, status, roomId);
        return affectedRows > 0;
    }
    
    // Get room statistics
    public Map<String, Integer> getRoomStatistics() throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        String query = "SELECT status, COUNT(*) as count FROM rooms GROUP BY status";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query);
            
            while (rs.next()) {
                stats.put(rs.getString("status"), rs.getInt("count"));
            }
            
            // Add total rooms
            query = "SELECT COUNT(*) as total FROM rooms";
            rs = dbManager.executeQuery(query);
            if (rs.next()) {
                stats.put("total", rs.getInt("total"));
            }
            
            return stats;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Search rooms with filters
    public List<Room> searchRooms(String roomNumber, String roomType, String status, 
                                 String floor, Double maxPrice) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM rooms WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (roomNumber != null && !roomNumber.isEmpty()) {
            queryBuilder.append(" AND room_number LIKE ?");
            params.add("%" + roomNumber + "%");
        }
        
        if (roomType != null && !roomType.isEmpty()) {
            queryBuilder.append(" AND room_type = ?");
            params.add(roomType);
        }
        
        if (status != null && !status.isEmpty()) {
            queryBuilder.append(" AND status = ?");
            params.add(status);
        }
        
        if (floor != null && !floor.isEmpty()) {
            queryBuilder.append(" AND floor = ?");
            params.add(floor);
        }
        
        if (maxPrice != null) {
            queryBuilder.append(" AND base_price <= ?");
            params.add(maxPrice);
        }
        
        queryBuilder.append(" ORDER BY room_number");
        
        ResultSet rs = null;
        try {
            rs = dbManager.executeQuery(queryBuilder.toString(), params.toArray());
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
            return rooms;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Helper method to map ResultSet to Room object
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(rs.getString("room_type"));
        room.setFloor(rs.getString("floor"));
        room.setWingSection(rs.getString("wing_section"));
        room.setCapacity(rs.getInt("capacity"));
        room.setBedConfiguration(rs.getString("bed_configuration"));
        room.setRoomSizeSqft(rs.getInt("room_size_sqft"));
        room.setViewType(rs.getString("view_type"));
        room.setSmokingAllowed(rs.getBoolean("smoking_allowed"));
        room.setBasePrice(rs.getBigDecimal("base_price"));
        room.setWeekendSurchargePercent(rs.getBigDecimal("weekend_surcharge_percent"));
        room.setCurrency(rs.getString("currency"));
        room.setStatus(Room.Status.valueOf(rs.getString("status").toUpperCase()));
        room.setDescription(rs.getString("description"));
        room.setLastCleaned(rs.getTimestamp("last_cleaned"));
        room.setLastMaintenance(rs.getTimestamp("last_maintenance"));
        room.setCreatedAt(rs.getTimestamp("created_at"));
        room.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Load amenities
        room.setAmenities(getRoomAmenities(room.getRoomId()));
        
        return room;
    }
    
    // Get room amenities
    private List<String> getRoomAmenities(int roomId) throws SQLException {
        List<String> amenities = new ArrayList<>();
        String query = "SELECT amenity FROM room_amenities WHERE room_id = ?";
        ResultSet rs = null;
        
        try {
            rs = dbManager.executeQuery(query, roomId);
            
            while (rs.next()) {
                amenities.add(rs.getString("amenity"));
            }
            
            return amenities;
            
        } finally {
            if (rs != null) rs.close();
        }
    }
    
    // Add amenity to room
    public boolean addAmenityToRoom(int roomId, String amenity) throws SQLException {
        String query = "INSERT INTO room_amenities (room_id, amenity) VALUES (?, ?) " +
                      "ON DUPLICATE KEY UPDATE room_id = room_id";
        int affectedRows = dbManager.executeUpdate(query, roomId, amenity);
        return affectedRows > 0;
    }
    
    // Remove amenity from room
    public boolean removeAmenityFromRoom(int roomId, String amenity) throws SQLException {
        String query = "DELETE FROM room_amenities WHERE room_id = ? AND amenity = ?";
        int affectedRows = dbManager.executeUpdate(query, roomId, amenity);
        return affectedRows > 0;
    }
}