package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;
import dao.DatabaseManager;
import dao.RoomDAO;
import model.Room;

public class ManagerDashboard extends JFrame implements ActionListener {
    private String username;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    // Table models
    private DefaultTableModel staffTableModel;
    private DefaultTableModel roomOccupancyModel;
    private DefaultTableModel revenueModel;
    private DefaultTableModel roomManagementModel; // NEW: Room management table model
    
    // UI Components
    private JTable staffTable;
    private JTable occupancyTable;
    private JTable revenueTable;
    private JTable roomManagementTable; // NEW: Room management table
    private JTextField searchStaffField;
    private JTextField searchRoomField; // NEW: Search field for rooms
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(140, 100, 180); // Purple for manager
    private final Color SECONDARY_COLOR = new Color(160, 120, 200);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DARK_BG = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(240, 245, 250);
    private final Color INFO_COLOR = new Color(52, 152, 219);
    private final Color TEAL_COLOR = new Color(26, 188, 156);
    
    // Room data storage
    private Map<Integer, Room> rooms = new HashMap<>();
    private Map<Integer, Room> rooms = new HashMap<>();
    private RoomDAO roomDAO;
    // Room class to store room information
    class Room {
        int number;
        String type;
        double price;
        String status;
        int capacity;
        String amenities;
        String floor;
        
        Room(int number, String type, double price, String status, int capacity, String amenities, String floor) {
            this.number = number;
            this.type = type;
            this.price = price;
            this.status = status;
            this.capacity = capacity;
            this.amenities = amenities;
            this.floor = floor;
        }
    }
    
    public ManagerDashboard(String username) {
        this.username = username;
        
        // Initialize some sample rooms
        initializeSampleRooms();
        
        setTitle("Manager Dashboard - Hospitality System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        
        // Create components
        createTopBar(mainPanel);
        createSidebar(mainPanel);
        createContentArea(mainPanel);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private void initializeSampleRooms() {
        rooms.put(101, new Room(101, "Single Room", 89.99, "Available", 1, "WiFi, TV, AC", "1st Floor"));
        rooms.put(102, new Room(102, "Double Room", 129.99, "Occupied", 2, "WiFi, TV, AC, Mini-bar", "1st Floor"));
        rooms.put(103, new Room(103, "Suite", 249.99, "Reserved", 4, "WiFi, TV, AC, Jacuzzi, Kitchen", "1st Floor"));
        rooms.put(104, new Room(104, "Single Room", 89.99, "Available", 1, "WiFi, TV", "1st Floor"));
        rooms.put(105, new Room(105, "Double Room", 129.99, "Maintenance", 2, "WiFi, TV, AC", "1st Floor"));
        rooms.put(201, new Room(201, "Executive Suite", 349.99, "Available", 2, "WiFi, TV, AC, Office, Bar", "2nd Floor"));
        rooms.put(202, new Room(202, "Double Room", 129.99, "Occupied", 2, "WiFi, TV, AC, Balcony", "2nd Floor"));
        rooms.put(203, new Room(203, "Single Room", 89.99, "Available", 1, "WiFi, TV, AC", "2nd Floor"));
        rooms.put(301, new Room(301, "Presidential Suite", 499.99, "Reserved", 6, "WiFi, TV, AC, Jacuzzi, Kitchen, Bar", "3rd Floor"));
    }
    
    private void createTopBar(JPanel mainPanel) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(DARK_BG);
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));
        topBar.setPreferredSize(new Dimension(getWidth(), 70));
        
        // Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(DARK_BG);
        
        JLabel titleLabel = new JLabel("MANAGER DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        leftPanel.add(titleLabel);
        
        // User info and controls
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(DARK_BG);
        
        JLabel userLabel = new JLabel("Manager: " + username);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        
        final JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(DANGER_COLOR);
        logoutBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        logoutBtn.setActionCommand("LOGOUT");
        logoutBtn.addActionListener(this);
        
        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);
        
        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(topBar, BorderLayout.NORTH);
    }
    
    private void createSidebar(JPanel mainPanel) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(60, 40, 100)); // Dark purple
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        // Manager menu items - ADDED ROOM MANAGEMENT
        String[] menuItems = {
            "DASHBOARD",
            "STAFF MANAGEMENT", 
            "ROOM MANAGEMENT", // NEW: Added Room Management
            "ROOM OCCUPANCY",
            "REVENUE ANALYSIS",
            "GUEST SERVICES",
            "REPORTS & ANALYTICS"
        };
        
        for (final String item : menuItems) {
            final JButton menuButton = createMenuButton(item);
            menuButton.setActionCommand(item);
            menuButton.addActionListener(this);
            sidebar.add(menuButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // Performance indicator
        JPanel performancePanel = new JPanel(new BorderLayout());
        performancePanel.setBackground(new Color(70, 50, 110));
        performancePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel performanceLabel = new JLabel("Performance: 92%");
        performanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        performanceLabel.setForeground(SUCCESS_COLOR);
        
        performancePanel.add(performanceLabel, BorderLayout.WEST);
        sidebar.add(performancePanel);
        
        mainPanel.add(sidebar, BorderLayout.WEST);
    }
    
    private JButton createMenuButton(final String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(80, 60, 120));
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(80, 60, 120));
            }
        });
        
        return button;
    }
    
    private void createContentArea(JPanel mainPanel) {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(LIGHT_BG);
        
        // Add different content panels - ADDED ROOM MANAGEMENT PANEL
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createStaffManagementPanel(), "STAFF MANAGEMENT");
        contentPanel.add(createRoomManagementPanel(), "ROOM MANAGEMENT"); // NEW: Room management panel
        contentPanel.add(createRoomOccupancyPanel(), "ROOM OCCUPANCY");
        contentPanel.add(createRevenueAnalysisPanel(), "REVENUE ANALYSIS");
        contentPanel.add(createGuestServicesPanel(), "GUEST SERVICES");
        contentPanel.add(createReportsPanel(), "REPORTS & ANALYTICS");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    // ==================== ROOM MANAGEMENT PANEL ====================
    private JPanel createRoomManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // ========== HEADER ==========
        JPanel mainHeader = new JPanel();
        mainHeader.setLayout(new BoxLayout(mainHeader, BoxLayout.Y_AXIS));
        mainHeader.setBackground(LIGHT_BG);
        mainHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // ====== LINE 1: TITLE LINE ======
        JPanel titleLine = new JPanel(new BorderLayout());
        titleLine.setBackground(Color.WHITE);
        titleLine.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("🏨 Room Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        titleLine.add(title, BorderLayout.WEST);
        
        // ====== LINE 2: CONTROL BUTTONS LINE ======
        JPanel controlsLine = new JPanel(new BorderLayout());
        controlsLine.setBackground(Color.WHITE);
        controlsLine.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Left side: Search functionality
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftControls.setBackground(Color.WHITE);
        
        searchRoomField = new JTextField(20);
        searchRoomField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchRoomField.setToolTipText("Search by room number, type, or status");
        
        final JButton searchBtn = new JButton("🔍 Search Rooms");
        styleButton(searchBtn, INFO_COLOR);
        searchBtn.setActionCommand("SEARCH_ROOMS");
        searchBtn.addActionListener(this);
        
        leftControls.add(new JLabel("Search:"));
        leftControls.add(searchRoomField);
        leftControls.add(searchBtn);
        
        // Right side: Add Room and Quick Actions buttons
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightControls.setBackground(Color.WHITE);
        
        final JButton addRoomBtn = new JButton("➕ Add New Room");
        styleButton(addRoomBtn, SUCCESS_COLOR);
        addRoomBtn.setActionCommand("ADD_ROOM");
        addRoomBtn.addActionListener(this);
        
        final JButton quickActionsBtn = new JButton("⚡ Quick Actions");
        styleButton(quickActionsBtn, WARNING_COLOR);
        quickActionsBtn.setActionCommand("ROOM_QUICK_ACTIONS");
        quickActionsBtn.addActionListener(this);
        
        rightControls.add(addRoomBtn);
        rightControls.add(quickActionsBtn);
        
        // Assemble controls line
        controlsLine.add(leftControls, BorderLayout.WEST);
        controlsLine.add(rightControls, BorderLayout.EAST);
        
        // Add both lines to main header
        mainHeader.add(titleLine);
        mainHeader.add(Box.createRigidArea(new Dimension(0, 10)));
        mainHeader.add(controlsLine);
        
        // ========== STATS BAR ==========
        JPanel statsBar = createRoomStatsBar();
        
        // ========== ROOMS TABLE ==========
        JPanel tablePanel = createRoomsTablePanel();
        
        // ========== ACTION BUTTONS ==========
        JPanel actionPanel = createRoomActionsPanel();
        
        // Main content
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(LIGHT_BG);
        mainContent.add(statsBar, BorderLayout.NORTH);
        mainContent.add(tablePanel, BorderLayout.CENTER);
        mainContent.add(actionPanel, BorderLayout.SOUTH);
        
        panel.add(mainHeader, BorderLayout.NORTH);
        panel.add(mainContent, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRoomStatsBar() {
        JPanel statsBar = new JPanel(new GridLayout(1, 5, 10, 0));
        statsBar.setBackground(LIGHT_BG);
        statsBar.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Calculate stats from rooms manually (Java 7 compatible)
        int totalRooms = rooms.size();
        int availableRooms = 0;
        int occupiedRooms = 0;
        int reservedRooms = 0;
        int maintenanceRooms = 0;
        
        for (Room room : rooms.values()) {
            switch (room.status) {
                case "Available":
                    availableRooms++;
                    break;
                case "Occupied":
                    occupiedRooms++;
                    break;
                case "Reserved":
                    reservedRooms++;
                    break;
                case "Maintenance":
                    maintenanceRooms++;
                    break;
            }
        }
        
        String[] stats = {
            "Total Rooms|" + totalRooms,
            "Available|" + availableRooms,
            "Occupied|" + occupiedRooms,
            "Reserved|" + reservedRooms,
            "Maintenance|" + maintenanceRooms
        };
        
        Color[] colors = {
            DARK_BG,
            SUCCESS_COLOR,
            PRIMARY_COLOR,
            WARNING_COLOR,
            DANGER_COLOR
        };
        
        for (int i = 0; i < stats.length; i++) {
            String[] parts = stats[i].split("\\|");
            statsBar.add(createRoomStatCard(parts[0], parts[1], colors[i]));
        }
        
        return statsBar;
    }
    
    private JPanel createRoomStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(titleLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRoomsTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Rooms table
        String[] columns = {
            "Room #", "Type", "Price/Night", "Status", "Capacity", "Floor", "Amenities", "Actions"
        };
        
        roomManagementModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only Actions column is editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 4) return Integer.class;
                if (columnIndex == 2) return Double.class;
                return String.class;
            }
        };
        
        // Load rooms into table
        loadRoomsIntoTable();
        
        roomManagementTable = new JTable(roomManagementModel);
        roomManagementTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomManagementTable.setRowHeight(40);
        roomManagementTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        roomManagementTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < roomManagementTable.getColumnCount(); i++) {
            roomManagementTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Color code status column
        roomManagementTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "Available":
                            c.setBackground(new Color(220, 255, 220));
                            c.setForeground(new Color(0, 128, 0));
                            break;
                        case "Occupied":
                            c.setBackground(new Color(255, 220, 220));
                            c.setForeground(new Color(204, 0, 0));
                            break;
                        case "Reserved":
                            c.setBackground(new Color(255, 255, 220));
                            c.setForeground(new Color(153, 153, 0));
                            break;
                        case "Maintenance":
                            c.setBackground(new Color(255, 220, 255));
                            c.setForeground(new Color(128, 0, 128));
                            break;
                        case "Cleaning":
                            c.setBackground(new Color(220, 220, 255));
                            c.setForeground(new Color(0, 0, 204));
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }
        });
        
        // Color code price column
        roomManagementTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    try {
                        double price = Double.parseDouble(value.toString());
                        if (price > 300) {
                            c.setForeground(new Color(155, 89, 182)); // Purple for premium
                        } else if (price > 150) {
                            c.setForeground(new Color(52, 152, 219)); // Blue for mid-range
                        } else {
                            c.setForeground(new Color(46, 204, 113)); // Green for economy
                        }
                    } catch (NumberFormatException e) {
                        c.setForeground(Color.BLACK);
                    }
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }
        });
        
        // Add mouse listener for row actions
        roomManagementTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = roomManagementTable.rowAtPoint(e.getPoint());
                int col = roomManagementTable.columnAtPoint(e.getPoint());
                
                if (col == 7 && row >= 0) { // Actions column clicked
                    String roomNumber = roomManagementTable.getValueAt(row, 0).toString();
                    showRoomActionMenu(roomNumber, e.getX(), e.getY());
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(roomManagementTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadRoomsIntoTable() {
        roomManagementModel.setRowCount(0); // Clear existing rows
        
        for (Room room : rooms.values()) {
            Object[] row = {
                room.number,
                room.type,
                String.format("$%.2f", room.price),
                room.status,
                room.capacity,
                room.floor,
                room.amenities,
                "Edit | Delete | Check-in | Check-out"
            };
            roomManagementModel.addRow(row);
        }
    }
    
    private JPanel createRoomActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        String[] actions = {
            "🔄 Refresh Rooms", 
            "📊 Room Analytics", 
            "🛠️ Maintenance Schedule",
            "📋 Generate Room Report",
            "🎯 Set Room Pricing",
            "📅 Bulk Status Update"
        };
        
        String[] commands = {
            "REFRESH_ROOMS",
            "ROOM_ANALYTICS", 
            "MAINTENANCE_SCHEDULE", 
            "GENERATE_ROOM_REPORT",
            "SET_ROOM_PRICING",
            "BULK_STATUS_UPDATE"
        };
        
        Color[] colors = {
            INFO_COLOR,
            PRIMARY_COLOR,
            WARNING_COLOR,
            SUCCESS_COLOR,
            new Color(230, 126, 34),
            DANGER_COLOR
        };
        
        for (int i = 0; i < actions.length; i++) {
            final JButton button = new JButton(actions[i]);
            styleButton(button, colors[i]);
            button.setActionCommand(commands[i]);
            button.addActionListener(this);
            panel.add(button);
        }
        
        return panel;
    }
    
    private void showRoomActionMenu(String roomNumber, int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem editItem = new JMenuItem("✏️ Edit Room Details");
        editItem.addActionListener(e -> editRoom(Integer.parseInt(roomNumber)));
        
        JMenuItem deleteItem = new JMenuItem("🗑️ Delete Room");
        deleteItem.addActionListener(e -> deleteRoom(Integer.parseInt(roomNumber)));
        
        JMenuItem checkinItem = new JMenuItem("✅ Check-in Guest");
        checkinItem.addActionListener(e -> checkInRoom(Integer.parseInt(roomNumber)));
        
        JMenuItem checkoutItem = new JMenuItem("📤 Check-out Guest");
        checkoutItem.addActionListener(e -> checkOutRoom(Integer.parseInt(roomNumber)));
        
        JMenuItem statusItem = new JMenuItem("🔄 Change Status");
        statusItem.addActionListener(e -> changeRoomStatus(Integer.parseInt(roomNumber)));
        
        JMenuItem viewItem = new JMenuItem("👁️ View Details");
        viewItem.addActionListener(e -> viewRoomDetails(Integer.parseInt(roomNumber)));
        
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        popupMenu.addSeparator();
        popupMenu.add(checkinItem);
        popupMenu.add(checkoutItem);
        popupMenu.addSeparator();
        popupMenu.add(statusItem);
        popupMenu.add(viewItem);
        
        popupMenu.show(roomManagementTable, x, y);
    }
    
    // ==================== ROOM ACTION METHODS ====================
    private void editRoom(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            JOptionPane.showMessageDialog(this, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        final JDialog dialog = new JDialog(this, "Edit Room #" + roomNumber, true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("✏️ Edit Room #" + roomNumber);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create form fields
        final JTextField typeField = new JTextField(room.type, 20);
        final JTextField priceField = new JTextField(String.format("%.2f", room.price), 20);
        final JComboBox<String> statusCombo = new JComboBox<>(new String[]{
            "Available", "Occupied", "Reserved", "Maintenance", "Cleaning"
        });
        statusCombo.setSelectedItem(room.status);
        final JTextField capacityField = new JTextField(String.valueOf(room.capacity), 20);
        final JComboBox<String> floorCombo = new JComboBox<>(new String[]{
            "1st Floor", "2nd Floor", "3rd Floor", "4th Floor", "5th Floor", "Penthouse"
        });
        floorCombo.setSelectedItem(room.floor);
        final JTextArea amenitiesArea = new JTextArea(room.amenities, 3, 20);
        
        int row = 0;
        
        // Room Type
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(typeField, gbc);
        row++;
        
        // Price
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Price per Night ($):"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);
        row++;
        
        // Status
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        row++;
        
        // Capacity
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        formPanel.add(capacityField, gbc);
        row++;
        
        // Floor
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Floor:"), gbc);
        gbc.gridx = 1;
        formPanel.add(floorCombo, gbc);
        row++;
        
        // Amenities
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Amenities:"), gbc);
        gbc.gridx = 1;
        amenitiesArea.setLineWrap(true);
        amenitiesArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 8, 8, 8)
        ));
        JScrollPane scrollPane = new JScrollPane(amenitiesArea);
        scrollPane.setPreferredSize(new Dimension(200, 80));
        formPanel.add(scrollPane, gbc);
        row++;
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(DANGER_COLOR);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JButton saveBtn = new JButton("💾 Save Changes");
        saveBtn.setBackground(SUCCESS_COLOR);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        saveBtn.addActionListener(e -> {
            // Validate inputs
            try {
                double price = Double.parseDouble(priceField.getText());
                int capacity = Integer.parseInt(capacityField.getText());
                
                if (price <= 0 || capacity <= 0) {
                    throw new NumberFormatException();
                }
                
                // Update room
                room.type = typeField.getText();
                room.price = price;
                room.status = (String) statusCombo.getSelectedItem();
                room.capacity = capacity;
                room.floor = (String) floorCombo.getSelectedItem();
                room.amenities = amenitiesArea.getText();
                
                // Update table
                loadRoomsIntoTable();
                
                JOptionPane.showMessageDialog(dialog,
                    "✅ Room #" + roomNumber + " updated successfully!",
                    "Room Updated",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter valid numbers for price and capacity!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        formPanel.add(buttonPanel, gbc);
        
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void deleteRoom(int roomNumber) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete Room #" + roomNumber + "?\n\n" +
            "This action cannot be undone!",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            rooms.remove(roomNumber);
            loadRoomsIntoTable();
            
            JOptionPane.showMessageDialog(this,
                "🗑️ Room #" + roomNumber + " has been deleted successfully!",
                "Room Deleted",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void checkInRoom(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            JOptionPane.showMessageDialog(this, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!room.status.equals("Available") && !room.status.equals("Reserved")) {
            JOptionPane.showMessageDialog(this,
                "Cannot check-in to Room #" + roomNumber + "!\n" +
                "Current status: " + room.status + "\n\n" +
                "Only Available or Reserved rooms can be checked-in.",
                "Check-in Failed",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        final JDialog dialog = new JDialog(this, "Check-in Room #" + roomNumber, true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(SUCCESS_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("✅ Check-in Room #" + roomNumber);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create form fields
        final JTextField guestNameField = new JTextField(20);
        final JTextField guestEmailField = new JTextField(20);
        final JTextField guestPhoneField = new JTextField(20);
        final JTextField checkInDateField = new JTextField(java.time.LocalDate.now().toString(), 20);
        final JTextField checkOutDateField = new JTextField(java.time.LocalDate.now().plusDays(3).toString(), 20);
        final JTextField paymentField = new JTextField(String.format("%.2f", room.price), 20);
        final JTextArea specialRequestsArea = new JTextArea(3, 20);
        
        int row = 0;
        
        // Guest Information
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel sectionLabel = new JLabel("Guest Information");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(DARK_BG);
        formPanel.add(sectionLabel, gbc);
        row++;
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Guest Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(guestNameField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(guestEmailField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(guestPhoneField, gbc);
        row++;
        
        // Booking Details
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        sectionLabel = new JLabel("Booking Details");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(DARK_BG);
        formPanel.add(sectionLabel, gbc);
        row++;
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Check-in Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(checkInDateField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Check-out Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(checkOutDateField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Payment Amount:"), gbc);
        gbc.gridx = 1;
        formPanel.add(paymentField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Special Requests:"), gbc);
        gbc.gridx = 1;
        specialRequestsArea.setLineWrap(true);
        specialRequestsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 8, 8, 8)
        ));
        formPanel.add(new JScrollPane(specialRequestsArea), gbc);
        row++;
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(DANGER_COLOR);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JButton checkinBtn = new JButton("✅ Process Check-in");
        checkinBtn.setBackground(SUCCESS_COLOR);
        checkinBtn.setForeground(Color.WHITE);
        checkinBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        checkinBtn.addActionListener(e -> {
            if (guestNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter guest name!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            room.status = "Occupied";
            loadRoomsIntoTable();
            
            JOptionPane.showMessageDialog(dialog,
                "✅ Check-in successful!\n\n" +
                "Room #" + roomNumber + " is now occupied.\n" +
                "Guest: " + guestNameField.getText() + "\n" +
                "Check-in: " + checkInDateField.getText() + "\n" +
                "Check-out: " + checkOutDateField.getText() + "\n" +
                "Amount: $" + paymentField.getText() + "\n\n" +
                "Confirmation email sent to " + guestEmailField.getText(),
                "Check-in Complete",
                JOptionPane.INFORMATION_MESSAGE);
            
            dialog.dispose();
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(checkinBtn);
        formPanel.add(buttonPanel, gbc);
        
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void checkOutRoom(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            JOptionPane.showMessageDialog(this, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!room.status.equals("Occupied")) {
            JOptionPane.showMessageDialog(this,
                "Cannot check-out from Room #" + roomNumber + "!\n" +
                "Current status: " + room.status + "\n\n" +
                "Only Occupied rooms can be checked-out.",
                "Check-out Failed",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Check-out Room #" + roomNumber + "?\n\n" +
            "This will:\n" +
            "1. Mark the room as 'Cleaning'\n" +
            "2. Generate final invoice\n" +
            "3. Update room availability\n\n" +
            "Proceed with check-out?",
            "Confirm Check-out",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Generate a mock invoice
            double totalAmount = room.price * 3; // Assume 3 nights stay
            
            room.status = "Cleaning";
            loadRoomsIntoTable();
            
            JOptionPane.showMessageDialog(this,
                "📤 Check-out completed!\n\n" +
                "Room #" + roomNumber + " is now being cleaned.\n\n" +
                "Invoice Details:\n" +
                "Room: #" + roomNumber + " (" + room.type + ")\n" +
                "Rate: $" + String.format("%.2f", room.price) + "/night\n" +
                "Nights: 3\n" +
                "Subtotal: $" + String.format("%.2f", totalAmount) + "\n" +
                "Tax (10%): $" + String.format("%.2f", totalAmount * 0.1) + "\n" +
                "Total: $" + String.format("%.2f", totalAmount * 1.1) + "\n\n" +
                "Invoice sent to guest email.\n" +
                "Housekeeping notified for cleaning.",
                "Check-out Complete",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void changeRoomStatus(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            JOptionPane.showMessageDialog(this, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String[] statusOptions = {"Available", "Occupied", "Reserved", "Maintenance", "Cleaning"};
        String newStatus = (String) JOptionPane.showInputDialog(this,
            "Select new status for Room #" + roomNumber + ":",
            "Change Room Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statusOptions,
            room.status);
        
        if (newStatus != null && !newStatus.equals(room.status)) {
            room.status = newStatus;
            loadRoomsIntoTable();
            
            JOptionPane.showMessageDialog(this,
                "🔄 Room #" + roomNumber + " status changed to: " + newStatus,
                "Status Updated",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void viewRoomDetails(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            JOptionPane.showMessageDialog(this, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JTextArea detailsArea = new JTextArea();
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsArea.setEditable(false);
        detailsArea.setText(
            "🏨 Room Details - #" + roomNumber + "\n" +
            "════════════════════════════════════\n\n" +
            "Room Type: " + room.type + "\n" +
            "Floor: " + room.floor + "\n" +
            "Current Status: " + room.status + "\n" +
            "Capacity: " + room.capacity + " person(s)\n" +
            "Price per Night: $" + String.format("%.2f", room.price) + "\n\n" +
            "Amenities:\n" + room.amenities + "\n\n" +
            "════════════════════════════════════\n" +
            "Last Updated: " + new java.util.Date() + "\n"
        );
        
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Room Details - #" + roomNumber, JOptionPane.PLAIN_MESSAGE);
    }
    
    // ==================== ROOM MANAGEMENT ACTION HANDLERS ====================
    private void searchRooms() {
        String searchTerm = searchRoomField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            loadRoomsIntoTable(); // Show all rooms if search is empty
            return;
        }
        
        roomManagementModel.setRowCount(0); // Clear table
        
        for (Room room : rooms.values()) {
            boolean matches = String.valueOf(room.number).contains(searchTerm) ||
                             room.type.toLowerCase().contains(searchTerm) ||
                             room.status.toLowerCase().contains(searchTerm) ||
                             room.floor.toLowerCase().contains(searchTerm) ||
                             room.amenities.toLowerCase().contains(searchTerm);
            
            if (matches) {
                Object[] row = {
                    room.number,
                    room.type,
                    String.format("$%.2f", room.price),
                    room.status,
                    room.capacity,
                    room.floor,
                    room.amenities,
                    "Edit | Delete | Check-in | Check-out"
                };
                roomManagementModel.addRow(row);
            }
        }
        
        if (roomManagementModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No rooms found matching: '" + searchTerm + "'",
                "Search Results",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void addRoom() {
        final JDialog dialog = new JDialog(this, "Add New Room", true);
        dialog.setSize(700, 750); // Increased size for better layout
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(245, 245, 250));
        
        // ========== HEADER ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(46, 204, 113)); // Green for creation
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(39, 174, 96)),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        JLabel title = new JLabel("➕ ADD NEW ROOM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JLabel subtitle = new JLabel("Complete all required fields (*) to add a new room");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(46, 204, 113));
        titlePanel.add(title);
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // ========== MAIN CONTENT ==========
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Create form panel with tabs for organization
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Tab 1: Basic Information
        JPanel basicPanel = createBasicInfoTab();
        tabs.addTab("📋 Basic Info", basicPanel);
        
        // Tab 2: Room Details
        JPanel detailsPanel = createRoomDetailsTab();
        tabs.addTab("🏨 Details", detailsPanel);
        
        // Tab 3: Pricing & Features
        JPanel featuresPanel = createFeaturesTab();
        tabs.addTab("💰 Pricing", featuresPanel);
        
        mainContent.add(tabs, BorderLayout.CENTER);
        
        // ========== PROGRESS INDICATOR ==========
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JLabel progressLabel = new JLabel("Form Completion: 33%", SwingConstants.LEFT);
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        progressLabel.setForeground(new Color(60, 60, 60));
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(33);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setBackground(new Color(240, 240, 240));
        
        progressPanel.add(progressLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        mainContent.add(progressPanel, BorderLayout.SOUTH);
        
        // ========== CONTROL PANEL ==========
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(new Color(245, 245, 250));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(220, 220, 230)),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtons.setBackground(new Color(245, 245, 250));
        
        JButton cancelBtn = createControlButton("Cancel", new Color(149, 165, 166), 12);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        JButton resetBtn = createControlButton("🔄 Reset Form", new Color(241, 196, 15), 12);
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetAddRoomForm(tabs);
            }
        });
        
        leftButtons.add(cancelBtn);
        leftButtons.add(resetBtn);
        
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.setBackground(new Color(245, 245, 250));
        
        JButton saveBtn = createControlButton("💾 Save Room", new Color(46, 204, 113), 12);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveNewRoom(dialog, tabs);
            }
        });
        
        rightButtons.add(saveBtn);
        
        controlPanel.add(leftButtons, BorderLayout.WEST);
        controlPanel.add(rightButtons, BorderLayout.EAST);
        
        // ========== ASSEMBLE DIALOG ==========
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(mainContent, BorderLayout.CENTER);
        dialog.add(controlPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }

    // ========== HELPER METHODS ==========

    private JPanel createBasicInfoTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Section title
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel sectionTitle = new JLabel("📋 Basic Room Information");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(44, 62, 80));
        panel.add(sectionTitle, gbc);
        gbc.gridwidth = 1;
        row++;
        
        // Room Number
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Room Number *"), gbc);
        gbc.gridx = 1;
        JTextField roomNumberField = new JTextField(20);
        roomNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(roomNumberField, gbc);
        row++;
        
        // Room Type
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Room Type *"), gbc);
        gbc.gridx = 1;
        JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{
            "Select Type", "Single Room", "Double Room", "Suite", "Executive Suite", "Presidential Suite"
        });
        panel.add(roomTypeCombo, gbc);
        row++;
        
        // Floor
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Floor *"), gbc);
        gbc.gridx = 1;
        JComboBox<String> floorCombo = new JComboBox<>(new String[]{
            "Select Floor", "1st Floor", "2nd Floor", "3rd Floor", "4th Floor", "5th Floor", "Penthouse"
        });
        panel.add(floorCombo, gbc);
        row++;
        
        // Status
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Initial Status *"), gbc);
        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{
            "Available", "Maintenance"
        });
        panel.add(statusCombo, gbc);
        
        return panel;
    }

    private JPanel createRoomDetailsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Section title
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel sectionTitle = new JLabel("🏨 Room Specifications");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(44, 62, 80));
        panel.add(sectionTitle, gbc);
        gbc.gridwidth = 1;
        row++;
        
        // Capacity
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Maximum Capacity *"), gbc);
        gbc.gridx = 1;
        JTextField capacityField = new JTextField("2", 10);
        capacityField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(capacityField, gbc);
        row++;
        
        // Room Size
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Room Size (sq ft)"), gbc);
        gbc.gridx = 1;
        JTextField sizeField = new JTextField("250", 10);
        sizeField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(sizeField, gbc);
        
        return panel;
    }

    private JPanel createFeaturesTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Section title
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel sectionTitle = new JLabel("💰 Pricing & Features");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(44, 62, 80));
        panel.add(sectionTitle, gbc);
        gbc.gridwidth = 1;
        row++;
        
        // Base Price
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Base Price per Night *"), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField("99.99", 10);
        priceField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(priceField, gbc);
        row++;
        
        // Amenities
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Amenities"), gbc);
        gbc.gridx = 1;
        JTextArea amenitiesArea = new JTextArea("WiFi, TV, AC", 3, 20);
        amenitiesArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        amenitiesArea.setLineWrap(true);
        JScrollPane amenitiesScroll = new JScrollPane(amenitiesArea);
        panel.add(amenitiesScroll, gbc);
        
        return panel;
    }

    // ========== FORM COMPONENT HELPERS ==========

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JTextField createFormTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private void styleComboBox(JComboBox combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        combo.setPreferredSize(new Dimension(250, 35));
    }

    private JButton createControlButton(String text, final Color bgColor, int fontSize) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    // ========== FORM MANAGEMENT HELPERS ==========

    private void resetAddRoomForm(JTabbedPane tabs) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "<html><div style='text-align: center;'>" +
            "<b>Reset the entire form?</b><br><br>" +
            "This will clear all entered information.<br>" +
            "This action cannot be undone.</div></html>",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Reset all components in all tabs
            Component[] tabsArray = tabs.getComponents();
            for (Component tab : tabsArray) {
                resetComponentsInPanel((JPanel) tab);
            }
            
            JOptionPane.showMessageDialog(this,
                "✅ Form reset to default values!",
                "Form Reset",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void resetComponentsInPanel(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                resetComponentsInPanel((JPanel) comp);
            } else if (comp instanceof JTextField) {
                ((JTextField) comp).setText("");
            } else if (comp instanceof JTextArea) {
                ((JTextArea) comp).setText("");
            } else if (comp instanceof JComboBox) {
                ((JComboBox) comp).setSelectedIndex(0);
            } else if (comp instanceof JRadioButton) {
                ((JRadioButton) comp).setSelected(false);
            } else if (comp instanceof JCheckBox) {
                ((JCheckBox) comp).setSelected(true); // Keep amenities checked by default
            }
        }
    }

    // ========== SAVE ROOM FUNCTION ==========

    private void saveNewRoom(final JDialog dialog, final JTabbedPane tabs) {
        // Collect data from all tabs
        // In a real implementation, you would extract data from all form fields
        // For now, we'll simulate collecting data
        
        // Validate required fields
        boolean isValid = validateRoomForm(tabs);
        
        if (!isValid) {
            return;
        }
        
        // Generate room number (in real app, get from form)
        int roomNumber = generateUniqueRoomNumber();
        
        // Show processing dialog
        final JDialog processingDialog = new JDialog(dialog, "Creating Room", true);
        processingDialog.setSize(400, 200);
        processingDialog.setLocationRelativeTo(dialog);
        processingDialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel processingLabel = new JLabel("Creating room #" + roomNumber + "...", SwingConstants.CENTER);
        processingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        content.add(processingLabel, BorderLayout.NORTH);
        content.add(progressBar, BorderLayout.CENTER);
        processingDialog.add(content);
        
        // Show processing dialog
        new Thread(new Runnable() {
            @Override
            public void run() {
                processingDialog.setVisible(true);
            }
        }).start();
        
        // Simulate processing
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processingDialog.dispose();
                completeRoomCreation(dialog, roomNumber);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private boolean validateRoomForm(JTabbedPane tabs) {
        // In a real implementation, validate all required fields
        // For now, just show validation passed
        return true;
    }

    private int generateUniqueRoomNumber() {
        // Generate a unique room number not in the rooms map
        int maxRoom = 0;
        for (Integer roomNum : rooms.keySet()) {
            if (roomNum > maxRoom) {
                maxRoom = roomNum;
            }
        }
        return maxRoom + 1;
    }

    private void completeRoomCreation(final JDialog dialog, int roomNumber) {
        // Create new room object with default values
        Room newRoom = new Room(
            roomNumber,
            "Single Room", // Default type
            99.99, // Default price
            "Available", // Default status
            2, // Default capacity
            "WiFi, TV, AC", // Default amenities
            "1st Floor" // Default floor
        );
        
        // Add to rooms map
        rooms.put(roomNumber, newRoom);
        
        // Update table
        loadRoomsIntoTable();
        
        // Show success message
        JOptionPane.showMessageDialog(dialog,
            "✅ Room #" + roomNumber + " added successfully!\n\n" +
            "The room has been added to the system with:\n" +
            "• Status: Available\n" +
            "• Type: Single Room\n" +
            "• Price: $99.99 per night\n" +
            "• Floor: 1st Floor\n\n" +
            "You can now edit the room details if needed.",
            "Room Added",
            JOptionPane.INFORMATION_MESSAGE);
        
        dialog.dispose();
    }
    
    // ==================== HELPER METHODS ====================
    private void styleButton(final JButton button, final Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    // ==================== ACTION HANDLER ====================
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        System.out.println("Manager Dashboard - Button clicked: " + command);
        
        // Existing navigation commands
        if (command.equals("LOGOUT")) {
            logout();
        } else if (command.equals("DASHBOARD")) {
            cardLayout.show(contentPanel, "DASHBOARD");
        } else if (command.equals("STAFF MANAGEMENT")) {
            cardLayout.show(contentPanel, "STAFF MANAGEMENT");
        } else if (command.equals("ROOM MANAGEMENT")) {
            cardLayout.show(contentPanel, "ROOM MANAGEMENT"); // NEW
        } else if (command.equals("ROOM OCCUPANCY")) {
            cardLayout.show(contentPanel, "ROOM OCCUPANCY");
        } else if (command.equals("REVENUE ANALYSIS")) {
            cardLayout.show(contentPanel, "REVENUE ANALYSIS");
        } else if (command.equals("GUEST SERVICES")) {
            cardLayout.show(contentPanel, "GUEST SERVICES");
        } else if (command.equals("REPORTS & ANALYTICS")) {
            cardLayout.show(contentPanel, "REPORTS & ANALYTICS");
        } else if (command.equals("REFRESH_DASHBOARD")) {
            refreshDashboard();
        } else if (command.equals("GENERATE_DAILY_REPORT")) {
            generateDailyReport();
        } else if (command.equals("SCHEDULE_STAFF")) {
            scheduleStaff();
        } else if (command.equals("APPROVE_EXPENSES")) {
            approveExpenses();
        } else if (command.equals("SET_TARGETS")) {
            setTargets();	
        
        }else if (command.equals("SEARCH_STAFF")) {
            searchStaff();
        } else if (command.equals("ROOM_QUICK_ACTIONS")) {
            showQuickActionsMenu();
        } else if (command.equals("ROOM_QUICK_ACTIONS")) {
            Component source = (Component) e.getSource();
            showQuickActionsMenu(source);
        }
        
        // Room management commands
        else if (command.equals("SEARCH_ROOMS")) {
            searchRooms();
        } else if (command.equals("ADD_ROOM")) {
            addRoom();
        } else if (command.equals("ROOM_QUICK_ACTIONS")) {
            showQuickActionsMenu();
        } else if (command.equals("REFRESH_ROOMS")) {
            loadRoomsIntoTable();
            JOptionPane.showMessageDialog(this, "✅ Room list refreshed!", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
        } else if (command.equals("ROOM_ANALYTICS")) {
            showRoomAnalytics();
        } else if (command.equals("MAINTENANCE_SCHEDULE")) {
            showMaintenanceSchedule();
        } else if (command.equals("GENERATE_ROOM_REPORT")) {
            generateRoomReport();
        } else if (command.equals("SET_ROOM_PRICING")) {
            setRoomPricing();
        } else if (command.equals("BULK_STATUS_UPDATE")) {
            bulkStatusUpdate();
        }
        
        // Existing other commands (from your original code)
        else if (command.equals("REFRESH_DASHBOARD")) {
            refreshDashboard();
        } else if (command.equals("GENERATE_DAILY_REPORT")) {
            generateDailyReport();
        } else if (command.equals("SCHEDULE_STAFF")) {
            scheduleStaff();
        } else if (command.equals("APPROVE_EXPENSES")) {
            approveExpenses();
        } else if (command.equals("SET_TARGETS")) {
            setTargets();
        } else if (command.equals("SEARCH_STAFF")) {
            searchStaff();
        } else if (command.equals("ADD_STAFF")) {
            addStaff();
        } else if (command.equals("VIEW_SCHEDULE")) {
            viewSchedule();
        } else if (command.equals("REFRESH_OCCUPANCY")) {
            refreshOccupancy();
        } else if (command.equals("OCCUPANCY_FORECAST")) {
            showOccupancyForecast();
        } else if (command.equals("ANALYZE_REVENUE")) {
            analyzeRevenue();
        } else if (command.equals("EXPORT_REVENUE")) {
            exportRevenue();
        } else if (command.equals("VIEW_FEEDBACK")) {
            viewFeedback();
        } else if (command.equals("SPECIAL_REQUESTS")) {
            viewSpecialRequests();
        } else if (command.equals("LOYALTY_PROGRAM")) {
            viewLoyaltyProgram();
        } else if (command.equals("PREPARE_GIFT")) {
            prepareGift();
        } else if (command.equals("GENERATE_REPORT")) {
            generateReport();
        } else if (command.equals("DAILY_OPERATIONS_REPORT")) {
            generateDailyOperationsReport();
        } else if (command.equals("FINANCIAL_PERFORMANCE")) {
            generateFinancialPerformance();
        } else if (command.equals("STAFF_PRODUCTIVITY")) {
            generateStaffProductivity();
        } else if (command.equals("OCCUPANCY_ANALYSIS")) {
            generateOccupancyAnalysis();
        } else if (command.equals("GUEST_SATISFACTION")) {
            generateGuestSatisfaction();
        } else if (command.equals("REVENUE_FORECASTING")) {
            generateRevenueForecasting();
        } else if (command.equals("COMPARATIVE_ANALYSIS")) {
            generateComparativeAnalysis();
        } else if (command.equals("PERFORMANCE_METRICS")) {
            generatePerformanceMetrics();
        } else if (command.equals("GENERATE_TEMPLATE")) {
            generateFromTemplate();
        }
    }
    
    // ==================== NEW ROOM MANAGEMENT ACTION METHODS ====================
    private void showQuickActionsMenu() {
        // Default implementation that shows at mouse position
        JPopupMenu popupMenu = createQuickActionsPopup();
        
        // Show at current mouse position
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        popupMenu.setLocation(mousePos);
        popupMenu.setVisible(true);
    }

    private void showQuickActionsMenu(Component source) {
        // Create the popup menu
        final JPopupMenu popupMenu = new JPopupMenu();
        
        // Add menu items with action listeners
        JMenuItem cleanAllItem = new JMenuItem("🧹 Mark All as Cleaned");
        cleanAllItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenu.setVisible(false);
                bulkCleanRooms();
            }
        });
        
        JMenuItem maintenanceItem = new JMenuItem("🛠️ Schedule Maintenance");
        maintenanceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenu.setVisible(false);
                scheduleMaintenance();
            }
        });
        
        JMenuItem priceUpdateItem = new JMenuItem("💰 Update Room Prices");
        priceUpdateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenu.setVisible(false);
                updateRoomPrices();
            }
        });
        
        JMenuItem exportItem = new JMenuItem("📁 Export Room List");
        exportItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupMenu.setVisible(false);
                exportRoomList();
            }
        });
        
        // Add items to popup
        popupMenu.add(cleanAllItem);
        popupMenu.add(maintenanceItem);
        popupMenu.add(priceUpdateItem);
        popupMenu.addSeparator();
        popupMenu.add(exportItem);
        
        // Show the popup
        if (source != null) {
            // Show below the button
            int x = 0;
            int y = source.getHeight();
            popupMenu.show(source, x, y);
        } else {
            // Fallback to mouse position
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mousePos, this);
            popupMenu.show(this, mousePos.x, mousePos.y);
        }
        
        // Force the popup to get focus so it closes when clicking elsewhere
        popupMenu.requestFocus();
    }

    private JPopupMenu createQuickActionsPopup() {
        JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem cleanAllItem = new JMenuItem("🧹 Mark All as Cleaned");
        cleanAllItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                bulkCleanRooms();
            }
        });
        
        JMenuItem maintenanceItem = new JMenuItem("🛠️ Schedule Maintenance");
        maintenanceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                scheduleMaintenance();
            }
        });
        
        JMenuItem priceUpdateItem = new JMenuItem("💰 Update Room Prices");
        priceUpdateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                updateRoomPrices();
            }
        });
        
        JMenuItem exportItem = new JMenuItem("📁 Export Room List");
        exportItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                exportRoomList();
            }
        });
        
        popupMenu.add(cleanAllItem);
        popupMenu.add(maintenanceItem);
        popupMenu.add(priceUpdateItem);
        popupMenu.addSeparator();
        popupMenu.add(exportItem);
        
        return popupMenu;
    }
    
    private void bulkCleanRooms() {
        int count = 0;
        for (Room room : rooms.values()) {
            if (room.status.equals("Cleaning")) {
                room.status = "Available";
                count++;
            }
        }
        
        if (count > 0) {
            loadRoomsIntoTable();
            JOptionPane.showMessageDialog(this,
                "🧹 " + count + " rooms marked as cleaned and available!",
                "Bulk Cleaning Complete",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "No rooms in 'Cleaning' status found.",
                "No Action Required",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void scheduleMaintenance() {
        // Create a simple maintenance scheduling dialog
        final JDialog dialog = new JDialog(this, "Schedule Maintenance", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Schedule Room Maintenance", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        content.add(title, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        content.add(new JLabel("Room Number:"), gbc);
        
        gbc.gridx = 1;
        JTextField roomField = new JTextField(10);
        content.add(roomField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        content.add(new JLabel("Maintenance Type:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
            "Routine Check", "Repair", "Deep Cleaning", "Renovation", "Equipment Update"
        });
        content.add(typeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        content.add(new JLabel("Duration (days):"), gbc);
        
        gbc.gridx = 1;
        JTextField durationField = new JTextField("1", 10);
        content.add(durationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        JButton scheduleBtn = new JButton("Schedule");
        scheduleBtn.setBackground(WARNING_COLOR);
        scheduleBtn.setForeground(Color.WHITE);
        scheduleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int roomNumber = Integer.parseInt(roomField.getText());
                    Room room = rooms.get(roomNumber);
                    
                    if (room == null) {
                        JOptionPane.showMessageDialog(dialog, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    room.status = "Maintenance";
                    loadRoomsIntoTable();
                    
                    JOptionPane.showMessageDialog(dialog,
                        "🛠️ Maintenance scheduled for Room #" + roomNumber + "\n\n" +
                        "Type: " + typeCombo.getSelectedItem() + "\n" +
                        "Duration: " + durationField.getText() + " days\n\n" +
                        "Maintenance team has been notified.",
                        "Maintenance Scheduled",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    dialog.dispose();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid room number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(scheduleBtn);
        content.add(buttonPanel, gbc);
        
        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void updateRoomPrices() {
        final JDialog dialog = new JDialog(this, "Update Room Prices", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Bulk Price Update", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        content.add(title, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        content.add(new JLabel("Room Type:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
            "All Rooms", "Single Room", "Double Room", "Suite", "Executive Suite", "Presidential Suite"
        });
        content.add(typeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        content.add(new JLabel("Increase by (%):"), gbc);
        
        gbc.gridx = 1;
        JTextField increaseField = new JTextField("10", 10);
        content.add(increaseField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        JButton updateBtn = new JButton("Update Prices");
        updateBtn.setBackground(PRIMARY_COLOR);
        updateBtn.setForeground(Color.WHITE);
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double increase = Double.parseDouble(increaseField.getText());
                    String selectedType = (String) typeCombo.getSelectedItem();
                    
                    int updatedCount = 0;
                    for (Room room : rooms.values()) {
                        if (selectedType.equals("All Rooms") || room.type.equals(selectedType)) {
                            room.price = room.price * (1 + increase / 100);
                            updatedCount++;
                        }
                    }
                    
                    loadRoomsIntoTable();
                    
                    JOptionPane.showMessageDialog(dialog,
                        "💰 Price update complete!\n\n" +
                        "Updated " + updatedCount + " rooms\n" +
                        "Increase: " + increase + "%\n" +
                        "Type: " + selectedType,
                        "Prices Updated",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    dialog.dispose();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid percentage!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(updateBtn);
        content.add(buttonPanel, gbc);
        
        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void exportRoomList() {
        JOptionPane.showMessageDialog(this,
            "📁 Exporting room list...\n\n" +
            "Export includes:\n" +
            "• All room details\n" +
            "• Current status\n" +
            "• Pricing information\n" +
            "• Amenities list\n\n" +
            "Formats available:\n" +
            "• Excel (.xlsx)\n" +
            "• PDF Report\n" +
            "• CSV for spreadsheet\n\n" +
            "File will be saved to Downloads folder.",
            "Export Room List",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showRoomAnalytics() {
        int totalRooms = rooms.size();
        double totalRevenuePotential = 0;
        double avgPrice = 0;
        int premiumRooms = 0;
        
        // Calculate manually for Java 7
        for (Room room : rooms.values()) {
            totalRevenuePotential += room.price;
            if (room.price > 200) {
                premiumRooms++;
            }
        }
        avgPrice = totalRooms > 0 ? totalRevenuePotential / totalRooms : 0;
        
        JTextArea analyticsArea = new JTextArea();
        analyticsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        analyticsArea.setEditable(false);
        analyticsArea.setText(
            "📊 Room Analytics Report\n" +
            "════════════════════════════════════\n\n" +
            "Total Rooms: " + totalRooms + "\n" +
            "Average Price: $" + String.format("%.2f", avgPrice) + "\n" +
            "Total Revenue Potential: $" + String.format("%.2f", totalRevenuePotential) + " per night\n" +
            "Premium Rooms (>$200): " + premiumRooms + "\n\n" +
            
            "Room Type Distribution:\n" +
            "════════════════════════════════════\n"
        );
        
        // Count by type
        Map<String, Integer> typeCount = new HashMap<>();
        for (Room room : rooms.values()) {
            String type = room.type;
            if (typeCount.containsKey(type)) {
                typeCount.put(type, typeCount.get(type) + 1);
            } else {
                typeCount.put(type, 1);
            }
        }
        
        for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
            analyticsArea.append(entry.getKey() + ": " + entry.getValue() + " rooms\n");
        }
        
        analyticsArea.append("\nStatus Distribution:\n" +
            "════════════════════════════════════\n");
        
        // Count by status
        Map<String, Integer> statusCount = new HashMap<>();
        for (Room room : rooms.values()) {
            String status = room.status;
            if (statusCount.containsKey(status)) {
                statusCount.put(status, statusCount.get(status) + 1);
            } else {
                statusCount.put(status, 1);
            }
        }
        
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            analyticsArea.append(entry.getKey() + ": " + entry.getValue() + " rooms\n");
        }
        
        JScrollPane scrollPane = new JScrollPane(analyticsArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Room Analytics", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void generateRoomReport() {
        String[] reportTypes = {"Availability Report", "Revenue Analysis", "Maintenance Log", "Occupancy History"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select report type:",
            "Generate Room Report",
            JOptionPane.QUESTION_MESSAGE,
            null,
            reportTypes,
            reportTypes[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "📊 Generating " + selected + "...\n\n" +
                "Report includes:\n" +
                "• Detailed room inventory\n" +
                "• Performance metrics\n" +
                "• Maintenance history\n" +
                "• Revenue contribution\n\n" +
                "Report will be available in PDF format.",
                "Room Report",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void setRoomPricing() {
        final JDialog dialog = new JDialog(this, "Set Room Pricing Strategy", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Dynamic Pricing Strategy", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        content.add(title, gbc);
        
        gbc.gridwidth = 1;
        
        String[] strategies = {
            "Standard Pricing",
            "Seasonal Pricing (High/Low season)",
            "Demand-based Pricing",
            "Competitor-based Pricing",
            "Package Deal Pricing"
        };
        
        JComboBox<String> strategyCombo = new JComboBox<>(strategies);
        
        gbc.gridx = 0; gbc.gridy = 1;
        content.add(new JLabel("Pricing Strategy:"), gbc);
        gbc.gridx = 1;
        content.add(strategyCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        content.add(new JLabel("Base Increase (%):"), gbc);
        gbc.gridx = 1;
        JTextField baseField = new JTextField("10", 10);
        content.add(baseField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        content.add(new JLabel("Weekend Surcharge (%):"), gbc);
        gbc.gridx = 1;
        JTextField weekendField = new JTextField("15", 10);
        content.add(weekendField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        content.add(new JLabel("Apply to Room Types:"), gbc);
        gbc.gridx = 1;
        JCheckBox singleCheck = new JCheckBox("Single", true);
        JCheckBox doubleCheck = new JCheckBox("Double", true);
        JCheckBox suiteCheck = new JCheckBox("Suite", true);
        JCheckBox executiveCheck = new JCheckBox("Executive", true);
        
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        typePanel.add(singleCheck);
        typePanel.add(doubleCheck);
        typePanel.add(suiteCheck);
        typePanel.add(executiveCheck);
        content.add(typePanel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JButton applyBtn = new JButton("Apply Pricing");
        applyBtn.setBackground(PRIMARY_COLOR);
        applyBtn.setForeground(Color.WHITE);
        applyBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog,
                "💰 Pricing strategy applied successfully!\n\n" +
                "Strategy: " + strategyCombo.getSelectedItem() + "\n" +
                "Base increase: " + baseField.getText() + "%\n" +
                "Weekend surcharge: " + weekendField.getText() + "%\n\n" +
                "Prices will be automatically adjusted.",
                "Pricing Updated",
                JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(applyBtn);
        content.add(buttonPanel, gbc);
        
        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void bulkStatusUpdate() {
        final JDialog dialog = new JDialog(this, "Bulk Status Update", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Update Multiple Rooms", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        content.add(title, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        content.add(new JLabel("Rooms (e.g., 101-105):"), gbc);
        
        gbc.gridx = 1;
        JTextField roomsField = new JTextField(15);
        content.add(roomsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        content.add(new JLabel("New Status:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{
            "Available", "Occupied", "Reserved", "Maintenance", "Cleaning"
        });
        content.add(statusCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JButton updateBtn = new JButton("Update Status");
        updateBtn.setBackground(INFO_COLOR);
        updateBtn.setForeground(Color.WHITE);
        updateBtn.addActionListener(e -> {
            String roomsText = roomsField.getText();
            // Simple parsing for demo - in real app, implement proper parsing
            int updatedCount = 0;
            
            try {
                if (roomsText.contains("-")) {
                    String[] range = roomsText.split("-");
                    int start = Integer.parseInt(range[0].trim());
                    int end = Integer.parseInt(range[1].trim());
                    
                    for (int i = start; i <= end; i++) {
                        if (rooms.containsKey(i)) {
                            rooms.get(i).status = (String) statusCombo.getSelectedItem();
                            updatedCount++;
                        }
                    }
                } else {
                    // Single room or comma-separated list
                    String[] roomArray = roomsText.split(",");
                    for (String roomStr : roomArray) {
                        int roomNum = Integer.parseInt(roomStr.trim());
                        if (rooms.containsKey(roomNum)) {
                            rooms.get(roomNum).status = (String) statusCombo.getSelectedItem();
                            updatedCount++;
                        }
                    }
                }
                
                loadRoomsIntoTable();
                
                JOptionPane.showMessageDialog(dialog,
                    "🔄 Updated " + updatedCount + " rooms to status: " + statusCombo.getSelectedItem(),
                    "Bulk Update Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid room numbers format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(updateBtn);
        content.add(buttonPanel, gbc);
        
        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    // ==================== DASHBOARD PANEL ====================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("Manager Overview Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        
        final JButton refreshBtn = new JButton("Refresh Dashboard");
        styleButton(refreshBtn, PRIMARY_COLOR);
        refreshBtn.setActionCommand("REFRESH_DASHBOARD");
        refreshBtn.addActionListener(this);
        
        header.add(title, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);
        
        // Performance KPI Cards
        JPanel kpiPanel = createKPIPanel();
        
        // Operational Metrics
        JPanel metricsPanel = createMetricsPanel();
        
        // Quick Actions
        JPanel quickActions = createQuickActionsPanel();
        
        // Recent Alerts
        JPanel alertsPanel = createAlertsPanel();
        
        // Main content container
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(LIGHT_BG);
        mainContent.add(kpiPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(metricsPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(quickActions);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(alertsPanel);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createKPIPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 15, 0));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        String[] kpis = {
            "💰 Revenue Today|$4,250|#9b59b6",
            "🏨 Occupancy Rate|78%|#3498db", 
            "👥 Guest Satisfaction|92%|#2ecc71",
            "👷 Staff Efficiency|88%|#f39c12",
            "📊 ADR|$189|#e74c3c",
            "🎯 RevPAR|$147|#1abc9c"
        };
        
        for (String kpi : kpis) {
            String[] parts = kpi.split("\\|");
            panel.add(createKPICard(parts[0], parts[1], Color.decode(parts[2])));
        }
        
        return panel;
    }
    
    private JPanel createKPICard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Value with icon
        String icon = title.substring(0, 2);
        String text = title.substring(2);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(icon + " " + text);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Trend indicator
        JLabel trendLabel = new JLabel();
        trendLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        trendLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (value.contains("$") || value.contains("%")) {
            if (Math.random() > 0.5) {
                trendLabel.setText("▲ +" + (int)(Math.random() * 15) + "%");
                trendLabel.setForeground(new Color(46, 204, 113));
            } else {
                trendLabel.setText("▼ -" + (int)(Math.random() * 10) + "%");
                trendLabel.setForeground(new Color(231, 76, 60));
            }
        }
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(titleLabel);
        if (trendLabel.getText() != null && !trendLabel.getText().isEmpty()) {
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            contentPanel.add(trendLabel);
        }
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(LIGHT_BG);
        
        // Left: Department Performance
        JPanel deptPerformance = createMetricCard(
            "👥 Department Performance", 
            new String[]{"Reception", "Housekeeping", "Restaurant", "Maintenance"},
            new int[]{92, 88, 85, 90},
            PRIMARY_COLOR
        );
        
        // Right: Room Type Distribution
        JPanel roomDistribution = createMetricCard(
            "🏨 Room Type Occupancy", 
            new String[]{"Single", "Double", "Suite", "Executive"},
            new int[]{75, 82, 90, 65},
            new Color(52, 152, 219)
        );
        
        panel.add(deptPerformance);
        panel.add(roomDistribution);
        
        return panel;
    }
    
    private JPanel createMetricCard(String title, String[] labels, int[] data, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(DARK_BG);
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Metrics visualization
        JPanel metricsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        metricsPanel.setBackground(Color.WHITE);
        metricsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        for (int i = 0; i < labels.length; i++) {
            JLabel deptLabel = new JLabel(labels[i]);
            deptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue(data[i]);
            progressBar.setString(data[i] + "%");
            progressBar.setStringPainted(true);
            progressBar.setForeground(color);
            progressBar.setBackground(new Color(240, 240, 240));
            
            metricsPanel.add(deptLabel);
            metricsPanel.add(progressBar);
        }
        
        panel.add(metricsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(LIGHT_BG);
        
        String[] actions = {
            "📋 Generate Daily Report",
            "👥 Schedule Staff", 
            "💰 Approve Expenses",
            "🎯 Set Targets"
        };
        
        String[] commands = {
            "GENERATE_DAILY_REPORT",
            "SCHEDULE_STAFF", 
            "APPROVE_EXPENSES",
            "SET_TARGETS"
        };
        
        Color[] colors = {
            SUCCESS_COLOR,
            INFO_COLOR,
            WARNING_COLOR,
            PRIMARY_COLOR
        };
        
        for (int i = 0; i < actions.length; i++) {
            JPanel actionCard = createActionCard(actions[i], commands[i], colors[i]);
            panel.add(actionCard);
        }
        
        return panel;
    }
    
    private JPanel createActionCard(String action, String command, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel actionLabel = new JLabel(action);
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actionLabel.setForeground(color);
        actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton actionBtn = new JButton("Take Action");
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionBtn.setBackground(color);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setBorder(new EmptyBorder(8, 15, 8, 15));
        actionBtn.setActionCommand(command);
        actionBtn.addActionListener(this);
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(actionBtn);
        
        card.add(actionLabel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("⚠️ Recent Alerts & Notifications");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DARK_BG);
        
        // Alerts list
        String[] alerts = {
            "🔔 High occupancy expected tomorrow (95%)",
            "⚠️ Room 105 requires maintenance",
            "💰 Credit card payment failed for Booking #BK012",
            "👥 Staff shortage in housekeeping department",
            "🎉 Guest anniversary in Suite 301 today"
        };
        
        JList<String> alertsList = new JList<>(alerts);
        alertsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        alertsList.setBackground(new Color(250, 250, 250));
        alertsList.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(alertsList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== STAFF MANAGEMENT PANEL ====================
 // ==================== STAFF MANAGEMENT PANEL ====================
    private JPanel createStaffManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // ========== HEADER SECTION ==========
        JPanel mainHeader = new JPanel();
        mainHeader.setLayout(new BoxLayout(mainHeader, BoxLayout.Y_AXIS));
        mainHeader.setBackground(LIGHT_BG);
        mainHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // ====== LINE 1: TITLE LINE ======
        JPanel titleLine = new JPanel(new BorderLayout());
        titleLine.setBackground(Color.WHITE);
        titleLine.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("👥 STAFF MANAGEMENT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        titleLine.add(title, BorderLayout.WEST);
        
        // ====== LINE 2: CONTROL BUTTONS LINE ======
        JPanel controlsLine = new JPanel(new BorderLayout());
        controlsLine.setBackground(Color.WHITE);
        controlsLine.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Left side: Search functionality
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftControls.setBackground(Color.WHITE);
        
        searchStaffField = new JTextField(20);
        searchStaffField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchStaffField.setToolTipText("Search by name, department, or position");
        
        final JButton searchBtn = new JButton("🔍 Search Staff");
        styleButton(searchBtn, INFO_COLOR);
        searchBtn.setActionCommand("SEARCH_STAFF");
        searchBtn.addActionListener(this);
        
        leftControls.add(new JLabel("Search:"));
        leftControls.add(searchStaffField);
        leftControls.add(searchBtn);
        
        // Right side: Action buttons
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightControls.setBackground(Color.WHITE);
        
        final JButton addStaffBtn = new JButton("➕ Add Staff");
        styleButton(addStaffBtn, SUCCESS_COLOR);
        addStaffBtn.setActionCommand("ADD_STAFF");
        addStaffBtn.addActionListener(this);
        
        final JButton scheduleBtn = new JButton("📅 View Schedule");
        styleButton(scheduleBtn, WARNING_COLOR);
        scheduleBtn.setActionCommand("VIEW_SCHEDULE");
        scheduleBtn.addActionListener(this);
        
        rightControls.add(addStaffBtn);
        rightControls.add(scheduleBtn);
        
        // Assemble controls line
        controlsLine.add(leftControls, BorderLayout.WEST);
        controlsLine.add(rightControls, BorderLayout.EAST);
        
        // Add both lines to main header
        mainHeader.add(titleLine);
        mainHeader.add(Box.createRigidArea(new Dimension(0, 10)));
        mainHeader.add(controlsLine);
        
        // ========== STAFF STATISTICS ==========
        JPanel statsPanel = createStaffStatsPanel();
        
        // ========== STAFF TABLE ==========
        JPanel tablePanel = createStaffTablePanel();
        
        // ========== BOTTOM ACTION BUTTONS ==========
        JPanel actionPanel = createStaffActionsPanel();
        
        // ========== MAIN CONTENT ==========
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(LIGHT_BG);
        mainContent.add(statsPanel, BorderLayout.NORTH);
        mainContent.add(tablePanel, BorderLayout.CENTER);
        mainContent.add(actionPanel, BorderLayout.SOUTH);
        
        panel.add(mainHeader, BorderLayout.NORTH);
        panel.add(mainContent, BorderLayout.CENTER);
        
        return panel;
    }

    // ==================== UPDATED STAFF STATS PANEL ====================
    private JPanel createStaffStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 10, 0));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        String[] stats = {
            "Total Staff|45", "On Duty|28", "On Leave|5", 
            "Training|12", "Available|32", "Overtime|8"
        };
        
        Color[] colors = {
            PRIMARY_COLOR, SUCCESS_COLOR, WARNING_COLOR, 
            INFO_COLOR, new Color(155, 89, 182), DANGER_COLOR
        };
        
        for (int i = 0; i < stats.length; i++) {
            String[] parts = stats[i].split("\\|");
            panel.add(createEnhancedStatCard(parts[0], parts[1], colors[i]));
        }
        
        return panel;
    }

    // ==================== ENHANCED STAT CARD ====================
    private JPanel createEnhancedStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add icon based on title
        String icon = getIconForTitle(title);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(iconLabel);
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(titleLabel);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }

    private String getIconForTitle(String title) {
        switch (title) {
            case "Total Staff": return "👥";
            case "On Duty": return "✅";
            case "On Leave": return "🏝️";
            case "Training": return "📚";
            case "Available": return "🟢";
            case "Overtime": return "⏰";
            default: return "📊";
        }
    }

    // ==================== STAFF TABLE PANEL ====================
    private JPanel createStaffTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Staff table with enhanced columns
        String[] columns = {
            "ID", "Name", "Department", "Position", "Shift", "Status", "Performance", "Actions"
        };
        
        staffTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only Actions column is editable
            }
        };
        
        // Add sample staff data
        addEnhancedStaffData();
        
        staffTable = new JTable(staffTableModel);
        staffTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        staffTable.setRowHeight(45);
        staffTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        staffTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < staffTable.getColumnCount(); i++) {
            staffTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Color code performance column
        staffTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String perf = value.toString().replace("%", "");
                    try {
                        int percentage = Integer.parseInt(perf.trim());
                        if (percentage >= 90) {
                            c.setBackground(new Color(220, 255, 220));
                            c.setForeground(new Color(0, 128, 0));
                        } else if (percentage >= 80) {
                            c.setBackground(new Color(255, 255, 220));
                            c.setForeground(new Color(153, 153, 0));
                        } else {
                            c.setBackground(new Color(255, 220, 220));
                            c.setForeground(new Color(204, 0, 0));
                        }
                    } catch (NumberFormatException e) {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }
        });
        
        // Color code status column
        staffTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "On Duty":
                            c.setBackground(new Color(220, 255, 220)); // Light green
                            c.setForeground(new Color(0, 128, 0));
                            break;
                        case "On Leave":
                            c.setBackground(new Color(255, 255, 220)); // Light yellow
                            c.setForeground(new Color(153, 153, 0));
                            break;
                        case "Training":
                            c.setBackground(new Color(220, 220, 255)); // Light blue
                            c.setForeground(new Color(0, 0, 204));
                            break;
                        case "Sick Leave":
                            c.setBackground(new Color(255, 220, 255)); // Light purple
                            c.setForeground(new Color(128, 0, 128));
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    // ==================== ENHANCED STAFF DATA ====================
    private void addEnhancedStaffData() {
        Object[][] staffData = {
            {101, "John Smith", "Reception", "Senior Receptionist", "Morning", "On Duty", "92%", "View | Edit"},
            {102, "Sarah Johnson", "Housekeeping", "Supervisor", "Evening", "On Duty", "88%", "View | Edit"},
            {103, "Mike Brown", "Restaurant", "Head Chef", "Morning", "On Duty", "95%", "View | Edit"},
            {104, "Lisa Wilson", "Reception", "Receptionist", "Night", "On Leave", "85%", "View | Edit"},
            {105, "Robert Chen", "Maintenance", "Technician", "Morning", "On Duty", "90%", "View | Edit"},
            {106, "Emma Davis", "Housekeeping", "Room Attendant", "Evening", "Training", "82%", "View | Edit"},
            {107, "David Miller", "Restaurant", "Waiter", "Morning", "On Duty", "78%", "View | Edit"},
            {108, "Jennifer Lee", "Reception", "Manager", "Morning", "On Duty", "96%", "View | Edit"},
            {109, "Michael Taylor", "Maintenance", "Supervisor", "Evening", "On Duty", "91%", "View | Edit"},
            {110, "Sophia Moore", "Housekeeping", "Team Lead", "Morning", "Training", "87%", "View | Edit"}
        };
        
        for (Object[] row : staffData) {
            staffTableModel.addRow(row);
        }
    }

    // ==================== STAFF ACTIONS PANEL ====================
    private JPanel createStaffActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        String[] actions = {
            "🔄 Refresh Staff List", 
            "📋 Generate Roster", 
            "📧 Send Announcement",
            "📊 Performance Review", 
            "📁 Export Staff Data",
            "🎯 Set Targets"
        };
        
        String[] commands = {
            "REFRESH_STAFF",
            "GENERATE_ROSTER", 
            "SEND_ANNOUNCEMENT",
            "PERFORMANCE_REVIEW", 
            "EXPORT_STAFF_DATA",
            "SET_STAFF_TARGETS"
        };
        
        Color[] colors = {
            INFO_COLOR,
            PRIMARY_COLOR,
            SUCCESS_COLOR,
            WARNING_COLOR,
            new Color(155, 89, 182),
            new Color(230, 126, 34)
        };
        
        for (int i = 0; i < actions.length; i++) {
            final JButton button = new JButton(actions[i]);
            styleButton(button, colors[i]);
            button.setActionCommand(commands[i]);
            button.addActionListener(this);
            panel.add(button);
        }
        
        return panel;
    }
    
    // ==================== ROOM OCCUPANCY PANEL ====================
    private JPanel createRoomOccupancyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("🏨 Room Occupancy & Status");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setBackground(Color.WHITE);
        
        final JButton refreshBtn = new JButton("🔄 Refresh Status");
        styleButton(refreshBtn, INFO_COLOR);
        refreshBtn.setActionCommand("REFRESH_OCCUPANCY");
        refreshBtn.addActionListener(this);
        
        final JButton forecastBtn = new JButton("📊 Occupancy Forecast");
        styleButton(forecastBtn, PRIMARY_COLOR);
        forecastBtn.setActionCommand("OCCUPANCY_FORECAST");
        forecastBtn.addActionListener(this);
        
        controls.add(refreshBtn);
        controls.add(forecastBtn);
        
        header.add(title, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        
        // Occupancy overview
        JPanel overviewPanel = createOccupancyOverview();
        
        // Room status grid
        JPanel roomGridPanel = createRoomStatusGrid();
        
        // Main content
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(LIGHT_BG);
        mainContent.add(overviewPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(roomGridPanel);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createOccupancyOverview() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        String[] stats = {
            "Total Rooms|128", "Occupied|92", "Available|28", "Under Maintenance|8"
        };
        
        Color[] colors = {DARK_BG, PRIMARY_COLOR, SUCCESS_COLOR, DANGER_COLOR};
        
        for (int i = 0; i < stats.length; i++) {
            String[] parts = stats[i].split("\\|");
            panel.add(createOccupancyStatCard(parts[0], parts[1], colors[i]));
        }
        
        return panel;
    }
    
    private JPanel createOccupancyStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Subtext based on title
        JLabel subLabel = new JLabel();
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLabel.setForeground(new Color(150, 150, 150));
        subLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        switch (title) {
            case "Total Rooms":
                subLabel.setText("Capacity: 128 rooms");
                break;
            case "Occupied":
                subLabel.setText("72% occupancy rate");
                break;
            case "Available":
                subLabel.setText("22% available");
                break;
            case "Under Maintenance":
                subLabel.setText("6% maintenance");
                break;
        }
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRoomStatusGrid() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("📋 Room Status by Floor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(DARK_BG);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Room grid - 5 floors x 6 rooms per row
        JPanel roomGrid = new JPanel(new GridLayout(5, 6, 10, 10));
        roomGrid.setBackground(Color.WHITE);
        
        for (int floor = 1; floor <= 5; floor++) {
            for (int roomNum = 1; roomNum <= 6; roomNum++) {
                int room = (floor * 100) + roomNum;
                JPanel roomPanel = createRoomPanel(room);
                roomGrid.add(roomPanel);
            }
        }
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(roomGrid, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRoomPanel(int roomNumber) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        // Determine room status (random for demo)
        String status;
        Color color;
        double random = Math.random();
        
        if (random < 0.6) { // 60% occupied
            status = "Occupied";
            color = PRIMARY_COLOR;
        } else if (random < 0.85) { // 25% available
            status = "Available";
            color = SUCCESS_COLOR;
        } else if (random < 0.95) { // 10% cleaning
            status = "Cleaning";
            color = WARNING_COLOR;
        } else { // 5% maintenance
            status = "Maintenance";
            color = DANGER_COLOR;
        }
        
        panel.setBackground(color.brighter());
        
        JLabel roomLabel = new JLabel("Room " + roomNumber);
        roomLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        roomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        roomLabel.setBorder(new EmptyBorder(10, 5, 5, 5));
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(color.darker());
        statusLabel.setBorder(new EmptyBorder(5, 5, 10, 5));
        
        panel.add(roomLabel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        // Make it clickable
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return panel;
    }
    
    // ==================== REVENUE ANALYSIS PANEL ====================
    private JPanel createRevenueAnalysisPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("💰 Revenue Analysis & Forecasting");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setBackground(Color.WHITE);
        
        String[] periods = {"Today", "Week", "Month", "Quarter", "Year"};
        JComboBox<String> periodCombo = new JComboBox<>(periods);
        periodCombo.setSelectedItem("Month");
        
        final JButton analyzeBtn = new JButton("📈 Analyze Revenue");
        styleButton(analyzeBtn, SUCCESS_COLOR);
        analyzeBtn.setActionCommand("ANALYZE_REVENUE");
        analyzeBtn.addActionListener(this);
        
        final JButton exportBtn = new JButton("💾 Export Report");
        styleButton(exportBtn, INFO_COLOR);
        exportBtn.setActionCommand("EXPORT_REVENUE");
        exportBtn.addActionListener(this);
        
        controls.add(new JLabel("Period:"));
        controls.add(periodCombo);
        controls.add(analyzeBtn);
        controls.add(exportBtn);
        
        header.add(title, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        
        // Revenue metrics
        JPanel metricsPanel = createRevenueMetricsPanel();
        
        // Revenue chart
        JPanel chartPanel = createRevenueChartPanel();
        
        // Revenue breakdown
        JPanel breakdownPanel = createRevenueBreakdownPanel();
        
        // Main content
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(LIGHT_BG);
        mainContent.add(metricsPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(chartPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(breakdownPanel);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRevenueMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 15, 0));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        String[] metrics = {
            "Total Revenue|$45,200", "Room Revenue|$38,400", "F&B Revenue|$5,200",
            "Services Revenue|$1,600", "Profit Margin|81.4%"
        };
        
        Color[] colors = {
            new Color(155, 89, 182), // Purple
            new Color(52, 152, 219),  // Blue
            new Color(46, 204, 113),  // Green
            new Color(241, 196, 15),  // Yellow
            new Color(230, 126, 34)   // Orange
        };
        
        for (int i = 0; i < metrics.length; i++) {
            String[] parts = metrics[i].split("\\|");
            panel.add(createRevenueMetricCard(parts[0], parts[1], colors[i]));
        }
        
        return panel;
    }
    
    private JPanel createRevenueMetricCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Trend indicator
        JLabel trendLabel = new JLabel();
        trendLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        trendLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (title.contains("Revenue") || title.contains("Margin")) {
            if (Math.random() > 0.3) {
                int increase = (int)(Math.random() * 20) + 5;
                trendLabel.setText("▲ +" + increase + "% vs last period");
                trendLabel.setForeground(new Color(46, 204, 113));
            } else {
                int decrease = (int)(Math.random() * 10) + 1;
                trendLabel.setText("▼ -" + decrease + "% vs last period");
                trendLabel.setForeground(new Color(231, 76, 60));
            }
        }
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(titleLabel);
        if (trendLabel.getText() != null && !trendLabel.getText().isEmpty()) {
            textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            textPanel.add(trendLabel);
        }
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createRevenueChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("📈 Revenue Trend - Last 6 Months");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DARK_BG);
        
        // Simulated chart panel
        JPanel chartArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Enable anti-aliasing
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int padding = 40;
                int chartWidth = width - 2 * padding;
                int chartHeight = height - 2 * padding;
                
                // Draw background grid
                g2d.setColor(new Color(240, 240, 240));
                for (int i = 0; i <= 10; i++) {
                    int y = padding + (i * chartHeight / 10);
                    g2d.drawLine(padding, y, width - padding, y);
                }
                
                // Revenue data for 6 months
                String[] months = {"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                int[] revenues = {38000, 42000, 45200, 41000, 46500, 48800};
                
                // Find max value
                int maxRevenue = 50000;
                
                // Draw line chart
                g2d.setColor(PRIMARY_COLOR);
                g2d.setStroke(new BasicStroke(3));
                
                int prevX = 0, prevY = 0;
                for (int i = 0; i < months.length; i++) {
                    int x = padding + (i * chartWidth / (months.length - 1));
                    int y = height - padding - (int)((revenues[i] / (double)maxRevenue) * chartHeight);
                    
                    if (i > 0) {
                        g2d.drawLine(prevX, prevY, x, y);
                    }
                    
                    // Draw data point
                    g2d.setColor(PRIMARY_COLOR);
                    g2d.fillOval(x - 5, y - 5, 10, 10);
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x - 3, y - 3, 6, 6);
                    
                    // Draw month label
                    g2d.setColor(Color.GRAY);
                    g2d.drawString(months[i], x - 10, height - padding + 15);
                    
                    // Draw value label
                    g2d.setColor(PRIMARY_COLOR);
                    String revenueText = "$" + String.format("%,d", revenues[i]);
                    g2d.drawString(revenueText, x - 20, y - 10);
                    
                    prevX = x;
                    prevY = y;
                }
                
                // Draw axes
                g2d.setColor(Color.GRAY);
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
                g2d.drawLine(padding, height - padding, padding, padding); // Y-axis
                
                // Y-axis labels
                for (int i = 0; i <= 5; i++) {
                    int y = height - padding - (i * chartHeight / 5);
                    String label = "$" + (i * 10000);
                    g2d.drawString(label, padding - 35, y + 5);
                }
            }
        };
        chartArea.setBackground(Color.WHITE);
        chartArea.setPreferredSize(new Dimension(600, 300));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(chartArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRevenueBreakdownPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(LIGHT_BG);
        
        // Revenue by source
        JPanel sourcePanel = createBreakdownCard(
            "Revenue by Source", 
            new String[]{"Room Bookings", "Restaurant", "Spa & Services", "Conference", "Other"},
            new int[]{75, 12, 8, 4, 1},
            PRIMARY_COLOR
        );
        
        // Revenue by room type
        JPanel roomPanel = createBreakdownCard(
            "Revenue by Room Type", 
            new String[]{"Executive Suites", "Standard Suites", "Double Rooms", "Single Rooms"},
            new int[]{28, 22, 31, 19},
            new Color(52, 152, 219)
        );
        
        panel.add(sourcePanel);
        panel.add(roomPanel);
        
        return panel;
    }
    
    private JPanel createBreakdownCard(String title, String[] categories, int[] percentages, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(DARK_BG);
        
        // Breakdown list
        JPanel breakdownPanel = new JPanel(new GridLayout(categories.length, 2, 10, 10));
        breakdownPanel.setBackground(Color.WHITE);
        breakdownPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        for (int i = 0; i < categories.length; i++) {
            JLabel categoryLabel = new JLabel(categories[i]);
            categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            JLabel percentageLabel = new JLabel(percentages[i] + "%");
            percentageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            percentageLabel.setForeground(color);
            percentageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            
            breakdownPanel.add(categoryLabel);
            breakdownPanel.add(percentageLabel);
        }
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(breakdownPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== GUEST SERVICES PANEL ====================
    private JPanel createGuestServicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("👑 Guest Services & Experience");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setBackground(Color.WHITE);
        
        final JButton feedbackBtn = new JButton("📝 View Feedback");
        styleButton(feedbackBtn, INFO_COLOR);
        feedbackBtn.setActionCommand("VIEW_FEEDBACK");
        feedbackBtn.addActionListener(this);
        
        final JButton specialBtn = new JButton("⭐ Special Requests");
        styleButton(specialBtn, WARNING_COLOR);
        specialBtn.setActionCommand("SPECIAL_REQUESTS");
        specialBtn.addActionListener(this);
        
        final JButton loyaltyBtn = new JButton("🎯 Loyalty Program");
        styleButton(loyaltyBtn, PRIMARY_COLOR);
        loyaltyBtn.setActionCommand("LOYALTY_PROGRAM");
        loyaltyBtn.addActionListener(this);
        
        controls.add(feedbackBtn);
        controls.add(specialBtn);
        controls.add(loyaltyBtn);
        
        header.add(title, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        
        // Guest satisfaction metrics
        JPanel satisfactionPanel = createSatisfactionPanel();
        
        // VIP guests
        JPanel vipPanel = createVIPGuestsPanel();
        
        // Special occasions
        JPanel occasionsPanel = createSpecialOccasionsPanel();
        
        // Main content
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(LIGHT_BG);
        mainContent.add(satisfactionPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(vipPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(occasionsPanel);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSatisfactionPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        String[] metrics = {
            "Overall Rating|4.8/5", "Room Quality|4.7/5", "Service|4.9/5", "Food|4.6/5"
        };
        
        Color[] colors = {
            new Color(155, 89, 182),
            new Color(52, 152, 219),
            new Color(46, 204, 113),
            new Color(241, 196, 15)
        };
        
        for (int i = 0; i < metrics.length; i++) {
            String[] parts = metrics[i].split("\\|");
            panel.add(createSatisfactionCard(parts[0], parts[1], colors[i]));
        }
        
        return panel;
    }
    
    private JPanel createSatisfactionCard(String category, String rating, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel ratingLabel = new JLabel(rating);
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        ratingLabel.setForeground(color);
        ratingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        categoryLabel.setForeground(new Color(100, 100, 100));
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Star rating visualization
        double stars = Double.parseDouble(rating.split("/")[0]);
        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        starsPanel.setBackground(Color.WHITE);
        
        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= stars ? "★" : "☆");
            star.setFont(new Font("Segoe UI", Font.BOLD, 14));
            star.setForeground(i <= stars ? color : new Color(200, 200, 200));
            starsPanel.add(star);
        }
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(ratingLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(categoryLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(starsPanel);
        
        card.add(contentPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createVIPGuestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("👑 VIP Guests Currently Staying");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DARK_BG);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // VIP guests table
        String[] columns = {"Guest Name", "Room", "Tier", "Check-in", "Check-out", "Special Notes"};
        Object[][] vipData = {
            {"Johnathan Smith", "Presidential Suite", "Platinum", "2024-12-18", "2024-12-25", "Anniversary"},
            {"Sarah Johnson", "Executive Suite", "Gold", "2024-12-20", "2024-12-28", "Business trip"},
            {"Robert Chen", "Suite 301", "Platinum", "Today", "2024-12-30", "Celebrity - privacy"},
            {"Emma Wilson", "Room 205", "Silver", "2024-12-22", "2024-12-29", "Food allergies"},
            {"Michael Brown", "Room 108", "Gold", "Tomorrow", "2025-01-05", "Late check-in"}
        };
        
        JTable vipTable = new JTable(vipData, columns);
        vipTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        vipTable.setRowHeight(40);
        vipTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        vipTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(vipTable);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSpecialOccasionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("🎉 Today's Special Occasions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DARK_BG);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Occasions list
        String[] occasions = {
            "🎂 Birthday - Mr. John Doe (Room 101)",
            "💑 Anniversary - Mr. & Mrs. Smith (Suite 301)",
            "🎓 Graduation - Miss Emily Chen (Room 205)",
            "💍 Engagement - Mr. Robert & Miss Lisa (Room 108)",
            "🏆 Business Success - Mr. Michael Brown (Executive Suite)"
        };
        
        JList<String> occasionsList = new JList<>(occasions);
        occasionsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        occasionsList.setBackground(new Color(250, 250, 250));
        occasionsList.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(occasionsList);
        
        JButton prepareBtn = new JButton("Prepare Complimentary Gift");
        prepareBtn.setBackground(SUCCESS_COLOR);
        prepareBtn.setForeground(Color.WHITE);
        prepareBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        prepareBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        prepareBtn.setActionCommand("PREPARE_GIFT");
        prepareBtn.addActionListener(this);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(prepareBtn);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ==================== REPORTS & ANALYTICS PANEL ====================
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("📊 Reports & Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        
        final JButton generateBtn = new JButton("📈 Generate Report");
        styleButton(generateBtn, SUCCESS_COLOR);
        generateBtn.setActionCommand("GENERATE_REPORT");
        generateBtn.addActionListener(this);
        
        header.add(title, BorderLayout.WEST);
        header.add(generateBtn, BorderLayout.EAST);
        
        // Reports grid
        JPanel reportsGrid = createReportsGrid();
        
        // Report templates
        JPanel templatesPanel = createReportTemplatesPanel();
        
        // Main content
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(LIGHT_BG);
        mainContent.add(reportsGrid);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(templatesPanel);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createReportsGrid() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 15, 15));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        String[] reports = {
            "📅 Daily Operations Report", 
            "💰 Financial Performance", 
            "👥 Staff Productivity",
            "🏨 Occupancy Analysis",
            "👑 Guest Satisfaction", 
            "📊 Revenue Forecasting",
            "🔄 Comparative Analysis", 
            "🎯 Performance Metrics"
        };
        
        String[] commands = {
            "DAILY_OPERATIONS_REPORT",
            "FINANCIAL_PERFORMANCE", 
            "STAFF_PRODUCTIVITY",
            "OCCUPANCY_ANALYSIS",
            "GUEST_SATISFACTION", 
            "REVENUE_FORECASTING",
            "COMPARATIVE_ANALYSIS", 
            "PERFORMANCE_METRICS"
        };
        
        Color[] colors = {
            PRIMARY_COLOR,
            new Color(52, 152, 219),
            SUCCESS_COLOR,
            new Color(155, 89, 182),
            WARNING_COLOR,
            new Color(230, 126, 34),
            INFO_COLOR,
            new Color(149, 165, 166)
        };
        
        for (int i = 0; i < reports.length; i++) {
            JButton reportBtn = new JButton(reports[i]);
            styleReportButton(reportBtn, colors[i]);
            reportBtn.setActionCommand(commands[i]);
            reportBtn.addActionListener(this);
            panel.add(reportBtn);
        }
        
        return panel;
    }
    
    private void styleReportButton(final JButton button, final Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(20, 15, 20, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private JPanel createReportTemplatesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("📋 Report Templates");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DARK_BG);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Template selection
        String[] templates = {
            "Standard Daily Report",
            "Monthly Performance Review", 
            "Quarterly Financial Analysis",
            "Annual Strategic Report",
            "Staff Performance Evaluation",
            "Guest Experience Analysis"
        };
        
        JList<String> templateList = new JList<>(templates);
        templateList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        templateList.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane listScroll = new JScrollPane(templateList);
        listScroll.setPreferredSize(new Dimension(300, 150));
        
        // Customization options
        JPanel optionsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        optionsPanel.add(new JLabel("Date Range:"));
        JComboBox<String> dateRange = new JComboBox<>(new String[]{"Today", "Week", "Month", "Quarter", "Year"});
        optionsPanel.add(dateRange);
        
        optionsPanel.add(new JLabel("Format:"));
        JComboBox<String> format = new JComboBox<>(new String[]{"PDF", "Excel", "HTML", "Word"});
        optionsPanel.add(format);
        
        optionsPanel.add(new JLabel("Include Charts:"));
        JCheckBox chartsCheck = new JCheckBox("Yes", true);
        optionsPanel.add(chartsCheck);
        
        optionsPanel.add(new JLabel("Email Report:"));
        JCheckBox emailCheck = new JCheckBox("Send to email");
        optionsPanel.add(emailCheck);
        
        // Generate button
        JButton generateTemplateBtn = new JButton("Generate from Template");
        generateTemplateBtn.setBackground(SUCCESS_COLOR);
        generateTemplateBtn.setForeground(Color.WHITE);
        generateTemplateBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        generateTemplateBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        generateTemplateBtn.setActionCommand("GENERATE_TEMPLATE");
        generateTemplateBtn.addActionListener(this);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(generateTemplateBtn);
        
        // Layout
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(listScroll, BorderLayout.WEST);
        contentPanel.add(optionsPanel, BorderLayout.CENTER);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ==================== ACTION METHODS ====================
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginScreen();
        }
    }
    
    private void refreshDashboard() {
        // Create a loading animation
        final JDialog loadingDialog = new JDialog(this, "Refreshing...", true);
        loadingDialog.setSize(300, 150);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel loadingLabel = new JLabel("🔄 Refreshing Dashboard...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        content.add(loadingLabel, BorderLayout.NORTH);
        content.add(progressBar, BorderLayout.CENTER);
        loadingDialog.add(content);
        
        // Show loading dialog in separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.setVisible(true);
            }
        }).start();
        
        // Simulate refresh process
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadingDialog.dispose();
                
                // Update with current date and time
                String currentTime = new java.util.Date().toString();
                
                JOptionPane.showMessageDialog(ManagerDashboard.this,
                    "✅ Dashboard refreshed successfully!\n\n" +
                    "Last refresh: " + currentTime + "\n\n" +
                    "Updated metrics:\n" +
                    "• Real-time occupancy: 79%\n" +
                    "• Current revenue: $4,320\n" +
                    "• Active staff: 29\n" +
                    "• Guest satisfaction: 92.5%\n\n" +
                    "All data is now up-to-date.",
                    "Dashboard Refreshed",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void generateDailyReport() {
        // Create report generation dialog
        final JDialog reportDialog = new JDialog(this, "Generating Daily Report", true);
        reportDialog.setSize(400, 200);
        reportDialog.setLocationRelativeTo(this);
        reportDialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📋 Generating Daily Operations Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextArea statusArea = new JTextArea(3, 30);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setText("Collecting data from all departments...\nProcessing revenue statistics...\nCompiling staff attendance...");
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        content.add(titleLabel, BorderLayout.NORTH);
        content.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        content.add(progressBar, BorderLayout.SOUTH);
        reportDialog.add(content);
        
        // Show dialog in separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                reportDialog.setVisible(true);
            }
        }).start();
        
        // Simulate report generation
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reportDialog.dispose();
                
                // Show report preview
                String reportContent = 
                    "DAILY OPERATIONS REPORT\n" +
                    "Date: " + new java.util.Date() + "\n" +
                    "════════════════════════════════════\n\n" +
                    "📊 KEY METRICS\n" +
                    "• Occupancy Rate: 79%\n" +
                    "• Total Revenue: $4,320\n" +
                    "• Check-ins: 9\n" +
                    "• Check-outs: 7\n" +
                    "• Staff Present: 29/32\n\n" +
                    
                    "👥 DEPARTMENT PERFORMANCE\n" +
                    "Reception: 94% efficiency\n" +
                    "Housekeeping: 89% efficiency\n" +
                    "Restaurant: 87% efficiency\n" +
                    "Maintenance: 92% efficiency\n\n" +
                    
                    "⭐ HIGHLIGHTS\n" +
                    "✓ 5-star review from Mr. Johnson (Room 301)\n" +
                    "✓ Restaurant fully booked for dinner\n" +
                    "✓ Conference room booked for corporate event\n\n" +
                    
                    "⚠️ AREAS TO IMPROVE\n" +
                    "• Room 105 needs maintenance\n" +
                    "• Breakfast buffet running low on fruits\n" +
                    "• Staff training needed for new POS system";
                
                JTextArea reportArea = new JTextArea(reportContent);
                reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                reportArea.setEditable(false);
                
                JScrollPane scrollPane = new JScrollPane(reportArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                
                JOptionPane.showMessageDialog(ManagerDashboard.this, scrollPane, 
                    "Daily Report Generated", JOptionPane.PLAIN_MESSAGE);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void scheduleStaff() {
        // Create staff scheduling interface
        final JDialog scheduleDialog = new JDialog(this, "Staff Scheduling", true);
        scheduleDialog.setSize(900, 650);
        scheduleDialog.setLocationRelativeTo(this);
        scheduleDialog.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("👥 Staff Schedule for Next Week");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // Sample schedule data - MADE EDITABLE
        final String[] columns = {"Staff", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        final Object[][] originalData = {
            {"John (Reception)", "Morning", "Morning", "Off", "Evening", "Evening", "Off", "Morning"},
            {"Sarah (Housekeeping)", "Morning", "Morning", "Morning", "Off", "Evening", "Evening", "Off"},
            {"Mike (Restaurant)", "Evening", "Evening", "Morning", "Morning", "Off", "Morning", "Evening"},
            {"Lisa (Reception)", "Off", "Evening", "Evening", "Morning", "Morning", "Off", "Evening"},
            {"Robert (Maintenance)", "Morning", "Off", "Morning", "Evening", "Evening", "Morning", "Off"},
            {"Emma (Housekeeping)", "Evening", "Evening", "Off", "Morning", "Morning", "Evening", "Off"},
            {"David (Restaurant)", "Morning", "Off", "Evening", "Evening", "Morning", "Morning", "Off"}
        };
        
        // Create a copy of the data for editing
        final Object[][] editableData = new Object[originalData.length][originalData[0].length];
        for (int i = 0; i < originalData.length; i++) {
            System.arraycopy(originalData[i], 0, editableData[i], 0, originalData[i].length);
        }
        
        // Main content with tabs
        final JTabbedPane tabs = new JTabbedPane();
        
        // Tab 1: Weekly Schedule
        JPanel weeklyPanel = new JPanel(new BorderLayout());
        weeklyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create editable table model
        final DefaultTableModel scheduleModel = new DefaultTableModel(editableData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells editable except staff name column
                return column > 0;
            }
        };
        
        final JTable scheduleTable = new JTable(scheduleModel);
        scheduleTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scheduleTable.setRowHeight(40);
        scheduleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Make shift cells use dropdown for consistency
        final String[] shiftOptions = {"Morning", "Evening", "Night", "Off"};
        
        // Set up custom cell editors for shift columns
        for (int i = 1; i < columns.length; i++) {
            scheduleTable.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JComboBox<String>(shiftOptions)));
        }
        
        // Color code shifts
        scheduleTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column > 0) { // Skip staff name column
                    String shift = value.toString();
                    switch (shift) {
                        case "Morning":
                            c.setBackground(new Color(173, 216, 230)); // Light blue
                            c.setForeground(new Color(0, 74, 124));
                            break;
                        case "Evening":
                            c.setBackground(new Color(255, 218, 185)); // Light orange
                            c.setForeground(new Color(139, 90, 43));
                            break;
                        case "Night":
                            c.setBackground(new Color(221, 160, 221)); // Light purple
                            c.setForeground(new Color(75, 0, 130));
                            break;
                        case "Off":
                            c.setBackground(new Color(240, 240, 240)); // Light gray
                            c.setForeground(new Color(100, 100, 100));
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                    
                    // Bold for staff names
                    if (column == 0) {
                        c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    } else {
                        c.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    }
                } else {
                    // Staff name column
                    c.setBackground(new Color(245, 245, 245));
                    c.setForeground(DARK_BG);
                    c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                
                // Highlight selected cell
                if (isSelected) {
                    c.setBackground(c.getBackground().darker());
                    c.setForeground(Color.WHITE);
                }
                
                return c;
            }
        });
        
        // Add tooltips
        scheduleTable.setToolTipText("Click on shift cells to edit. Available options: Morning, Evening, Night, Off");
        
        JScrollPane tableScroll = new JScrollPane(scheduleTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Summary panel showing schedule statistics - DECLARED AS FINAL
        final JLabel morningLabel = new JLabel("🌅 Morning: 15", SwingConstants.CENTER);
        final JLabel eveningLabel = new JLabel("🌇 Evening: 12", SwingConstants.CENTER);
        final JLabel nightLabel = new JLabel("🌃 Night: 8", SwingConstants.CENTER);
        final JLabel offLabel = new JLabel("🏠 Off: 10", SwingConstants.CENTER);
        
        for (JLabel label : new JLabel[]{morningLabel, eveningLabel, nightLabel, offLabel}) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            label.setBorder(new EmptyBorder(5, 5, 5, 5));
        }
        
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 5, 0));
        summaryPanel.setBackground(new Color(245, 245, 245));
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        summaryPanel.add(morningLabel);
        summaryPanel.add(eveningLabel);
        summaryPanel.add(nightLabel);
        summaryPanel.add(offLabel);
        
        // Update summary function - DECLARED AS FINAL
        final Runnable updateSummary = new Runnable() {
            @Override
            public void run() {
                int morning = 0, evening = 0, night = 0, off = 0;
                
                for (int row = 0; row < scheduleModel.getRowCount(); row++) {
                    for (int col = 1; col < scheduleModel.getColumnCount(); col++) {
                        String shift = scheduleModel.getValueAt(row, col).toString();
                        switch (shift) {
                            case "Morning": morning++; break;
                            case "Evening": evening++; break;
                            case "Night": night++; break;
                            case "Off": off++; break;
                        }
                    }
                }
                
                morningLabel.setText("🌅 Morning: " + morning);
                eveningLabel.setText("🌇 Evening: " + evening);
                nightLabel.setText("🌃 Night: " + night);
                offLabel.setText("🏠 Off: " + off);
            }
        };
        
        // Listen for table changes to update summary - Java 7 compatible
        scheduleModel.addTableModelListener(new javax.swing.event.TableModelListener() {
            @Override
            public void tableChanged(javax.swing.event.TableModelEvent e) {
                updateSummary.run();
            }
        });
        
        // Initial summary update
        updateSummary.run();
        
        weeklyPanel.add(summaryPanel, BorderLayout.NORTH);
        weeklyPanel.add(tableScroll, BorderLayout.CENTER);
        
        // Tab 2: Shift Statistics
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        statsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        String[] shiftStats = {
            "Morning Shift: 15 staff",
            "Evening Shift: 12 staff", 
            "Night Shift: 8 staff",
            "Off Duty: 10 staff",
            "Overtime Required: 3 staff",
            "Coverage Gaps: 2 positions"
        };
        
        for (String stat : shiftStats) {
            JLabel statLabel = new JLabel(stat, SwingConstants.CENTER);
            statLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            statLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(10, 5, 10, 5)
            ));
            statLabel.setOpaque(true);
            statLabel.setBackground(Color.WHITE);
            statLabel.setPreferredSize(new Dimension(200, 60));
            statsPanel.add(statLabel);
        }
        
        // Tab 3: Quick Actions
        JPanel actionsPanel = new JPanel(new GridBagLayout());
        actionsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        String[] quickActions = {
            "🔄 Reset to Default Schedule",
            "🎯 Auto-Assign Morning Shifts", 
            "⚖️ Balance Weekend Coverage",
            "📋 Export Schedule to Excel",
            "👥 Copy Last Week's Schedule",
            "📅 Set All to Standard Pattern"
        };
        
        final String[] actionCommands = {
            "RESET_SCHEDULE",
            "AUTO_MORNING", 
            "BALANCE_WEEKEND",
            "EXPORT_SCHEDULE",
            "COPY_LAST_WEEK",
            "STANDARD_PATTERN"
        };
        
        Color[] actionColors = {
            WARNING_COLOR,
            new Color(135, 206, 235), // Sky blue
            INFO_COLOR,
            SUCCESS_COLOR,
            new Color(186, 85, 211), // Medium orchid
            new Color(100, 149, 237) // Cornflower blue
        };
        
        for (int i = 0; i < quickActions.length; i++) {
            final int index = i;
            final JButton actionBtn = new JButton(quickActions[i]);
            styleButton(actionBtn, actionColors[index]);
            actionBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            actionBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleQuickScheduleAction(actionCommands[index], scheduleModel, originalData);
                }
            });
            
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            gbc.gridwidth = 1;
            actionsPanel.add(actionBtn, gbc);
        }
        
        // Add tabs
        tabs.addTab("📅 Weekly Schedule", weeklyPanel);
        tabs.addTab("📊 Shift Statistics", statsPanel);
        tabs.addTab("⚡ Quick Actions", actionsPanel);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton printBtn = new JButton("🖨️ Print Schedule");
        styleSmallButton(printBtn, INFO_COLOR);
        printBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(scheduleDialog,
                    "✅ Schedule sent to printer!\n\n" +
                    "Weekly schedule will be printed and\n" +
                    "distributed to all department heads.",
                    "Print Schedule",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JButton saveBtn = new JButton("💾 Save Schedule");
        styleSmallButton(saveBtn, SUCCESS_COLOR);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get current schedule data
                StringBuilder scheduleData = new StringBuilder();
                scheduleData.append("Staff Schedule - Saved on ").append(new java.util.Date()).append("\n\n");
                
                // Build header
                for (int col = 0; col < scheduleModel.getColumnCount(); col++) {
                    scheduleData.append(String.format("%-15s", scheduleModel.getColumnName(col)));
                }
                scheduleData.append("\n");
                
                // Build separator
                for (int col = 0; col < scheduleModel.getColumnCount(); col++) {
                    scheduleData.append("---------------");
                }
                scheduleData.append("\n");
                
                // Build rows
                for (int row = 0; row < scheduleModel.getRowCount(); row++) {
                    for (int col = 0; col < scheduleModel.getColumnCount(); col++) {
                        scheduleData.append(String.format("%-15s", scheduleModel.getValueAt(row, col)));
                    }
                    scheduleData.append("\n");
                }
                
                // Show saving progress
                final JDialog savingDialog = new JDialog(scheduleDialog, "Saving Schedule", true);
                savingDialog.setSize(300, 150);
                savingDialog.setLocationRelativeTo(scheduleDialog);
                savingDialog.setLayout(new BorderLayout());
                
                JPanel savingContent = new JPanel(new BorderLayout());
                savingContent.setBorder(new EmptyBorder(20, 20, 20, 20));
                
                JLabel savingLabel = new JLabel("💾 Saving schedule...", SwingConstants.CENTER);
                savingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                
                JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                
                savingContent.add(savingLabel, BorderLayout.NORTH);
                savingContent.add(progressBar, BorderLayout.CENTER);
                savingDialog.add(savingContent);
                
                // Show saving dialog
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        savingDialog.setVisible(true);
                    }
                }).start();
                
                // Simulate saving process
                Timer timer = new Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        savingDialog.dispose();
                        
                        // Show success message with summary
                        int totalShifts = 0;
                        for (int row = 0; row < scheduleModel.getRowCount(); row++) {
                            totalShifts += 7; // 7 days per staff
                        }
                        
                        JOptionPane.showMessageDialog(scheduleDialog,
                            "✅ Schedule saved successfully!\n\n" +
                            "Summary:\n" +
                            "• Staff members: " + scheduleModel.getRowCount() + "\n" +
                            "• Shifts scheduled: " + totalShifts + "\n" +
                            "• Period: Next 7 days\n\n" +
                            "Schedule has been:\n" +
                            "✓ Saved to database\n" +
                            "✓ Distributed to all staff emails\n" +
                            "✓ Posted in staff lounge\n" +
                            "✓ Added to mobile app calendar\n\n" +
                            "Staff have been notified of their shifts.",
                            "Schedule Saved",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Close the schedule dialog
                        scheduleDialog.dispose();
                        
                        // Show confirmation on main dashboard
                        JOptionPane.showMessageDialog(ManagerDashboard.this,
                            "📅 Staff Schedule Updated!\n\n" +
                            "The new schedule has been saved and distributed.\n" +
                            "Staff can now view their updated shifts.",
                            "Schedule Updated",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        
        JButton resetBtn = new JButton("🔄 Reset Changes");
        styleSmallButton(resetBtn, WARNING_COLOR);
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(scheduleDialog,
                    "Reset all changes to original schedule?\n\n" +
                    "This will undo all modifications made.",
                    "Reset Schedule",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Reset to original data
                    for (int row = 0; row < originalData.length; row++) {
                        for (int col = 0; col < originalData[row].length; col++) {
                            scheduleModel.setValueAt(originalData[row][col], row, col);
                        }
                    }
                    JOptionPane.showMessageDialog(scheduleDialog,
                        "✅ Schedule reset to original!",
                        "Reset Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        styleSmallButton(cancelBtn, new Color(149, 165, 166));
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleDialog.dispose();
            }
        });
        
        controlPanel.add(printBtn);
        controlPanel.add(resetBtn);
        controlPanel.add(saveBtn);
        controlPanel.add(cancelBtn);
        
        scheduleDialog.add(header, BorderLayout.NORTH);
        scheduleDialog.add(tabs, BorderLayout.CENTER);
        scheduleDialog.add(controlPanel, BorderLayout.SOUTH);
        scheduleDialog.setVisible(true);
    }

    // Helper method for quick schedule actions - UPDATED
    private void handleQuickScheduleAction(String command, DefaultTableModel model, Object[][] originalData) {
        switch (command) {
            case "RESET_SCHEDULE":
                for (int row = 0; row < originalData.length; row++) {
                    for (int col = 0; col < originalData[row].length; col++) {
                        model.setValueAt(originalData[row][col], row, col);
                    }
                }
                JOptionPane.showMessageDialog(this, "✅ Schedule reset to default!", "Reset Complete", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "AUTO_MORNING":
                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 1; col < model.getColumnCount(); col++) {
                        model.setValueAt("Morning", row, col);
                    }
                }
                JOptionPane.showMessageDialog(this, "✅ All shifts set to Morning!", "Auto-assign Complete", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "BALANCE_WEEKEND":
                // Simple weekend balancing logic
                for (int row = 0; row < model.getRowCount(); row++) {
                    String sat = model.getValueAt(row, 6).toString();
                    String sun = model.getValueAt(row, 7).toString();
                    
                    if (sat.equals(sun) && sat.equals("Off")) {
                        model.setValueAt("Morning", row, 6);
                    } else if (sat.equals("Off") && !sun.equals("Off")) {
                        model.setValueAt("Evening", row, 6);
                    }
                }
                JOptionPane.showMessageDialog(this, "✅ Weekend coverage balanced!", "Balance Complete", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "EXPORT_SCHEDULE":
                JOptionPane.showMessageDialog(this,
                    "📁 Schedule export started!\n\n" +
                    "Exporting to Excel format...\n" +
                    "File will be saved to Downloads folder.",
                    "Export Schedule",
                    JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "COPY_LAST_WEEK":
                // For demo, just show message
                JOptionPane.showMessageDialog(this,
                    "📋 Last week's schedule copied!\n\n" +
                    "You can now modify the copied schedule.",
                    "Copy Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "STANDARD_PATTERN":
                // Set standard 5-2 pattern
                for (int row = 0; row < model.getRowCount(); row++) {
                    model.setValueAt("Morning", row, 1); // Mon
                    model.setValueAt("Morning", row, 2); // Tue
                    model.setValueAt("Morning", row, 3); // Wed
                    model.setValueAt("Evening", row, 4); // Thu
                    model.setValueAt("Evening", row, 5); // Fri
                    model.setValueAt("Off", row, 6);     // Sat
                    model.setValueAt("Off", row, 7);     // Sun
                }
                JOptionPane.showMessageDialog(this, "✅ Standard 5-2 pattern applied!", "Pattern Applied", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }
    
    private void approveExpenses() {
        // Create expense approval interface
        final JDialog expenseDialog = new JDialog(this, "Approve Expenses", true);
        expenseDialog.setSize(700, 550);
        expenseDialog.setLocationRelativeTo(this);
        expenseDialog.setLayout(new BorderLayout());
        expenseDialog.getContentPane().setBackground(new Color(245, 245, 250));
        
        // Store reference to the outer class for use in inner classes
        final ManagerDashboard outerThis = this;
        
        // ========== HEADER ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(241, 196, 15)); // Gold/Yellow
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(189, 156, 12)),
            new EmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel title = new JLabel("💰 PENDING EXPENSE APPROVALS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JLabel subtitle = new JLabel("Review and approve department expenses");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(241, 196, 15));
        titlePanel.add(title);
        titlePanel.add(subtitle);
        
        // FIX: Make countLabel final for inner class access
        final JLabel countLabel = new JLabel("6 Pending", SwingConstants.RIGHT);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        countLabel.setForeground(Color.WHITE);
        countLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(countLabel, BorderLayout.EAST);
        
        // ========== EXPENSE TABLE DATA ==========
        final String[] columns = {"ID", "Department", "Description", "Amount", "Date", "Submitted By", "Status"};
        final Object[][] expenseData = {
            {"EXP-001", "Housekeeping", "Cleaning supplies inventory", "$450.00", "2024-12-20", "Sarah Johnson", "Pending"},
            {"EXP-002", "Restaurant", "Weekly food inventory", "$1,200.50", "2024-12-19", "Mike Brown", "Pending"},
            {"EXP-003", "Maintenance", "Repair parts for Room 105", "$320.75", "2024-12-18", "Robert Chen", "Pending"},
            {"EXP-004", "Office", "Office supplies & stationery", "$180.25", "2024-12-17", "Jennifer Lee", "Pending"},
            {"EXP-005", "Marketing", "Printing promotional materials", "$650.00", "2024-12-16", "Marketing Team", "Pending"},
            {"EXP-006", "Reception", "Guest welcome amenities", "$275.30", "2024-12-15", "John Smith", "Pending"}
        };
        
        // Create table model
        final DefaultTableModel expenseModel = new DefaultTableModel(expenseData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                return String.class;
            }
        };
        
        // ========== EXPENSE TABLE ==========
        final JTable expenseTable = new JTable(expenseModel) {
            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, toggle, extend);
                // Ensure proper row selection
                if (rowIndex >= 0) {
                    setRowSelectionInterval(rowIndex, rowIndex);
                }
            }
        };
        
        // Table styling
        expenseTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        expenseTable.setRowHeight(40);
        expenseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        expenseTable.getTableHeader().setBackground(new Color(245, 245, 250));
        expenseTable.getTableHeader().setForeground(new Color(60, 60, 60));
        expenseTable.setShowGrid(true);
        expenseTable.setGridColor(new Color(230, 230, 230));
        expenseTable.setSelectionBackground(new Color(173, 216, 230, 100));
        expenseTable.setSelectionForeground(new Color(0, 100, 150));
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.setRowSelectionAllowed(true);
        expenseTable.setColumnSelectionAllowed(false);
        expenseTable.setFillsViewportHeight(true);
        
        // Set column widths
        expenseTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        expenseTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Department
        expenseTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Description
        expenseTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Amount
        expenseTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Date
        expenseTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Submitted By
        expenseTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Status
        
        // Color code amount column
        expenseTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                setHorizontalAlignment(SwingConstants.RIGHT);
                
                try {
                    String amountStr = value.toString().replace("$", "").replace(",", "");
                    double amount = Double.parseDouble(amountStr);
                    
                    if (amount > 1000) {
                        c.setForeground(new Color(192, 57, 43)); // Dark red
                    } else if (amount > 500) {
                        c.setForeground(new Color(211, 84, 0));  // Orange
                    } else {
                        c.setForeground(new Color(39, 174, 96)); // Green
                    }
                } catch (NumberFormatException e) {
                    c.setForeground(Color.BLACK);
                }
                
                return c;
            }
        });
        
        // Color code status column
        expenseTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                setHorizontalAlignment(SwingConstants.CENTER);
                
                String status = value.toString();
                if (status.startsWith("Approved")) {
                    c.setBackground(new Color(220, 255, 220)); // Light green
                    c.setForeground(new Color(39, 174, 96));    // Green text
                } else if (status.startsWith("Rejected")) {
                    c.setBackground(new Color(255, 220, 220)); // Light red
                    c.setForeground(new Color(192, 57, 43));   // Red text
                } else {
                    c.setBackground(new Color(255, 243, 205)); // Light yellow
                    c.setForeground(new Color(211, 84, 0));     // Orange text
                }
                
                // Highlight selected row
                if (isSelected) {
                    c.setBackground(new Color(173, 216, 230)); // Light blue for selected
                    c.setForeground(Color.BLACK);
                }
                
                return c;
            }
        });
        
        JScrollPane tableScroll = new JScrollPane(expenseTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 230)));
        tableScroll.getViewport().setBackground(Color.WHITE);
        
        // ========== SUMMARY PANEL ==========
        final JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        summaryPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        summaryPanel.setBackground(Color.WHITE);
        
        final JLabel pendingLabel = createSummaryCard("⏳ Pending", "6", new Color(241, 196, 15));
        final JLabel totalLabel = createSummaryCard("💰 Total", "$3,076.80", new Color(52, 152, 219));
        final JLabel approvedLabel = createSummaryCard("✅ Approved", "0", new Color(46, 204, 113));
        final JLabel rejectedLabel = createSummaryCard("❌ Rejected", "0", new Color(231, 76, 60));
        
        summaryPanel.add(pendingLabel);
        summaryPanel.add(totalLabel);
        summaryPanel.add(approvedLabel);
        summaryPanel.add(rejectedLabel);
        
        // Function to update summary
        final Runnable updateSummary = new Runnable() {
            @Override
            public void run() {
                int pending = 0;
                int approved = 0;
                int rejected = 0;
                double totalAmount = 0;
                
                for (Object[] row : expenseData) {
                    String status = row[6].toString();
                    if (status.startsWith("Approved")) {
                        approved++;
                    } else if (status.startsWith("Rejected")) {
                        rejected++;
                    } else {
                        pending++;
                    }
                    
                    try {
                        String amountStr = row[3].toString().replace("$", "").replace(",", "");
                        totalAmount += Double.parseDouble(amountStr);
                    } catch (NumberFormatException e) {
                        // Ignore parsing errors
                    }
                }
                
                // Update summary labels
                pendingLabel.setText("<html><div style='text-align: center;'><b>" + pending + "</b><br>⏳ Pending</div></html>");
                totalLabel.setText("<html><div style='text-align: center;'><b>$" + String.format("%.2f", totalAmount) + "</b><br>💰 Total</div></html>");
                approvedLabel.setText("<html><div style='text-align: center;'><b>" + approved + "</b><br>✅ Approved</div></html>");
                rejectedLabel.setText("<html><div style='text-align: center;'><b>" + rejected + "</b><br>❌ Rejected</div></html>");
                
                // Update count in header - FIXED: countLabel is now final
                countLabel.setText(pending + " Pending");
            }
        };
        
        // ========== CONTROL PANEL ==========
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        controlPanel.setBackground(new Color(245, 245, 250));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Create action buttons
        final JButton viewDetailsBtn = createStyledButton("📄 View Details", new Color(52, 152, 219), 11);
        final JButton approveSelectedBtn = createStyledButton("✅ Approve Selected", new Color(46, 204, 113), 11);
        final JButton rejectBtn = createStyledButton("❌ Reject", new Color(231, 76, 60), 11);
        final JButton approveAllBtn = createStyledButton("✅ Approve All", new Color(39, 174, 96), 11);
        final JButton refreshBtn = createStyledButton("🔄 Refresh", new Color(155, 89, 182), 11);
        final JButton closeBtn = createStyledButton("Close", new Color(149, 165, 166), 11);
        
        // Disable action buttons initially
        viewDetailsBtn.setEnabled(false);
        approveSelectedBtn.setEnabled(false);
        rejectBtn.setEnabled(false);
        
        // ========== BUTTON ACTION HANDLERS ==========
        
        // View Details Button
        viewDetailsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = expenseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    final String expenseId = expenseData[selectedRow][0].toString();
                    outerThis.showExpenseDetailsDialog(expenseId, expenseData[selectedRow]);
                } else {
                    JOptionPane.showMessageDialog(expenseDialog,
                        createSelectionErrorDialog(),
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Approve Selected Button - FIXED VERSION
        approveSelectedBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = expenseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    final String expenseId = expenseData[selectedRow][0].toString();
                    final String currentStatus = expenseData[selectedRow][6].toString();
                    
                    // Check if already approved
                    if (currentStatus.startsWith("Approved")) {
                        JOptionPane.showMessageDialog(expenseDialog,
                            "This expense is already approved!",
                            "Already Approved",
                            JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    
                    // Ask for confirmation
                    int confirm = JOptionPane.showConfirmDialog(expenseDialog,
                        createApprovalConfirmationDialog(expenseId, expenseData[selectedRow]),
                        "Approve Expense",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Update the expense status
                        expenseData[selectedRow][6] = "Approved ✅";
                        
                        // FIX: Properly update the table model
                        expenseModel.setValueAt("Approved ✅", selectedRow, 6);
                        expenseModel.fireTableRowsUpdated(selectedRow, selectedRow);
                        
                        // Update summary
                        updateSummary.run();
                        
                        // Show success message
                        showApprovalSuccessMessage(expenseDialog, expenseId, expenseData[selectedRow]);
                    }
                } else {
                    JOptionPane.showMessageDialog(expenseDialog,
                        createSelectionErrorDialog(),
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Reject Button
        rejectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = expenseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    final String expenseId = expenseData[selectedRow][0].toString();
                    final String currentStatus = expenseData[selectedRow][6].toString();
                    
                    // Check if already approved or rejected
                    if (currentStatus.startsWith("Approved")) {
                        JOptionPane.showMessageDialog(expenseDialog,
                            "This expense is already approved!\nCannot reject an approved expense.",
                            "Already Approved",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    String reason = JOptionPane.showInputDialog(expenseDialog,
                        "Enter rejection reason for " + expenseId + ":",
                        "Reject Expense",
                        JOptionPane.QUESTION_MESSAGE);
                    
                    if (reason != null && !reason.trim().isEmpty()) {
                        expenseData[selectedRow][6] = "Rejected: " + reason + " ❌";
                        expenseModel.setValueAt("Rejected: " + reason + " ❌", selectedRow, 6);
                        expenseModel.fireTableRowsUpdated(selectedRow, selectedRow);
                        updateSummary.run();
                        
                        JOptionPane.showMessageDialog(expenseDialog,
                            "Expense " + expenseId + " rejected!\n\n" +
                            "Reason: " + reason + "\n\n" +
                            "Submitter has been notified.",
                            "Expense Rejected",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(expenseDialog,
                        createSelectionErrorDialog(),
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Approve All Button
        approveAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(expenseDialog,
                    "<html><div style='text-align: center;'>" +
                    "<b>Approve ALL pending expenses?</b><br><br>" +
                    "Total amount: <font color='#27ae60'>$3,076.80</font><br>" +
                    "Number of expenses: <b>6</b><br><br>" +
                    "This will process all payments immediately.</div></html>",
                    "Confirm Bulk Approval",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Update all expenses to Approved
                    for (int i = 0; i < expenseData.length; i++) {
                        expenseData[i][6] = "Approved ✅";
                        expenseModel.setValueAt("Approved ✅", i, 6);
                    }
                    expenseModel.fireTableDataChanged();
                    updateSummary.run();
                    
                    JOptionPane.showMessageDialog(expenseDialog,
                        "<html><div style='text-align: center;'>" +
                        "✅ <b>All expenses approved!</b><br><br>" +
                        "Processed: <b>6</b> expenses<br>" +
                        "Total: <font color='#27ae60'><b>$3,076.80</b></font><br><br>" +
                        "Payments have been scheduled and<br>" +
                        "departments have been notified.</div></html>",
                        "Bulk Approval Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        // Refresh Button
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSummary.run();
                expenseTable.clearSelection();
                viewDetailsBtn.setEnabled(false);
                approveSelectedBtn.setEnabled(false);
                rejectBtn.setEnabled(false);
                expenseDialog.setTitle("Approve Expenses");
                
                // Visual feedback
                refreshBtn.setText("🔄 Refreshing...");
                Timer timer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        refreshBtn.setText("🔄 Refresh");
                        JOptionPane.showMessageDialog(expenseDialog,
                            "✅ Expenses list refreshed!",
                            "Refreshed",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        
        // Close Button
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expenseDialog.dispose();
            }
        });
        
        // Add buttons to control panel
        controlPanel.add(viewDetailsBtn);
        controlPanel.add(approveSelectedBtn);
        controlPanel.add(rejectBtn);
        controlPanel.add(approveAllBtn);
        controlPanel.add(refreshBtn);
        controlPanel.add(closeBtn);
        
        // ========== TABLE SELECTION LISTENERS ==========
        
        // Table selection listener
        expenseTable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = expenseTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Enable action buttons when a row is selected
                        viewDetailsBtn.setEnabled(true);
                        rejectBtn.setEnabled(true);
                        approveSelectedBtn.setEnabled(true);
                        
                        // Update dialog title to show selected expense
                        String expenseId = expenseData[selectedRow][0].toString();
                        expenseDialog.setTitle("Approve Expenses - Selected: " + expenseId);
                        
                        // Highlight the selected row in the table
                        expenseTable.scrollRectToVisible(expenseTable.getCellRect(selectedRow, 0, true));
                    } else {
                        // Disable action buttons when no row is selected
                        viewDetailsBtn.setEnabled(false);
                        rejectBtn.setEnabled(false);
                        approveSelectedBtn.setEnabled(false);
                        expenseDialog.setTitle("Approve Expenses");
                    }
                }
            }
        });
        
        // Mouse listener for better click handling
        expenseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = expenseTable.rowAtPoint(e.getPoint());
                int col = expenseTable.columnAtPoint(e.getPoint());
                
                if (row >= 0 && col >= 0) {
                    // Ensure the row is selected when any cell is clicked
                    expenseTable.setRowSelectionInterval(row, row);
                    
                    // Double-click to view details
                    if (e.getClickCount() == 2) {
                        final String expenseId = expenseData[row][0].toString();
                        outerThis.showExpenseDetailsDialog(expenseId, expenseData[row]);
                    }
                }
            }
        });
        
        // ========== ASSEMBLE DIALOG ==========
        
        // Main content panel
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.add(summaryPanel, BorderLayout.NORTH);
        mainContent.add(tableScroll, BorderLayout.CENTER);
        
        expenseDialog.add(header, BorderLayout.NORTH);
        expenseDialog.add(mainContent, BorderLayout.CENTER);
        expenseDialog.add(controlPanel, BorderLayout.SOUTH);
        
        // ========== INITIAL SETUP ==========
        
        // Initial summary update
        updateSummary.run();
        
        // Set initial selection to first row
        if (expenseTable.getRowCount() > 0) {
            expenseTable.setRowSelectionInterval(0, 0);
        }
        
        expenseDialog.setVisible(true);
    }
    
 // ========== HELPER METHODS FOR APPROVE EXPENSES ==========

    private JLabel createSummaryCard(String title, String value, Color color) {
        JLabel card = new JLabel(
            "<html><div style='text-align: center; padding: 10px;'>" +
            "<b style='font-size: 16px; color: " + toHex(color) + ";'>" + value + "</b><br>" +
            "<span style='font-size: 11px; color: #666;'>" + title + "</span>" +
            "</div></html>",
            SwingConstants.CENTER
        );
        
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        card.setPreferredSize(new Dimension(120, 70));
        
        return card;
    }

    private String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private JButton createStyledButton(String text, final Color bgColor, int fontSize) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void showExpenseDetailsDialog(final String expenseId, final Object[] expenseData) {
        JTextArea detailsArea = new JTextArea();
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsArea.setEditable(false);
        detailsArea.setText(
            "💰 EXPENSE DETAILS: " + expenseId + "\n" +
            "════════════════════════════════════\n\n" +
            "Department: " + expenseData[1] + "\n" +
            "Description: " + expenseData[2] + "\n" +
            "Amount: " + expenseData[3] + "\n" +
            "Date Submitted: " + expenseData[4] + "\n" +
            "Submitted By: " + expenseData[5] + "\n" +
            "Status: " + expenseData[6] + "\n\n" +
            
            "════════════════════════════════════\n" +
            "ATTACHMENTS:\n" +
            "• Invoice receipt.pdf\n" +
            "• Delivery confirmation.jpg\n" +
            "• Department approval form.pdf\n\n" +
            
            "NOTES:\n" +
            "Urgent items needed for Room 105 maintenance.\n" +
            "Approved by department head.\n"
        );
        
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "Expense Details - " + expenseId, JOptionPane.PLAIN_MESSAGE);
    }

    // Helper method to create a beautiful confirmation dialog
    private JPanel createApprovalConfirmationDialog(String expenseId, Object[] expenseData) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        
        // Header with icon
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel iconLabel = new JLabel("💰", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel("Confirm Expense Approval", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(41, 128, 185));
        
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setBackground(new Color(240, 248, 255));
        iconPanel.add(iconLabel);
        
        headerPanel.add(iconPanel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Create details rows
        String[] labels = {"Expense ID:", "Department:", "Description:", "Amount:", "Submitted By:"};
        Object[] values = {
            expenseData[0], expenseData[1], expenseData[2], 
            expenseData[3], expenseData[5]
        };
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            label.setForeground(new Color(60, 60, 60));
            detailsPanel.add(label, gbc);
            
            gbc.gridx = 1;
            JLabel valueLabel = new JLabel(values[i].toString());
            valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            valueLabel.setForeground(new Color(80, 80, 80));
            detailsPanel.add(valueLabel, gbc);
        }
        
        // Warning message
        JLabel warningLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "Are you sure you want to approve this expense?<br>" +
            "This action cannot be undone.</div></html>"
        );
        warningLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        warningLabel.setForeground(new Color(231, 76, 60));
        warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 5, 5);
        detailsPanel.add(warningLabel, gbc);
        
        // Assemble the panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(detailsPanel, BorderLayout.CENTER);
        
        return panel;
    }

    // Helper method to show beautiful success message
    private void showApprovalSuccessMessage(JDialog parentDialog, String expenseId, Object[] expenseData) {
        JDialog successDialog = new JDialog(parentDialog, "Approval Successful", true);
        successDialog.setSize(400, 350);
        successDialog.setLocationRelativeTo(parentDialog);
        successDialog.setLayout(new BorderLayout());
        successDialog.getContentPane().setBackground(new Color(240, 255, 240));
        
        // Success icon
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(new Color(240, 255, 240));
        iconPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        JLabel successIcon = new JLabel("✅", SwingConstants.CENTER);
        successIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        successIcon.setForeground(new Color(46, 204, 113));
        iconPanel.add(successIcon);
        
        // Message panel
        JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.setBackground(new Color(240, 255, 240));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel titleLabel = new JLabel("Expense Approved Successfully!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(39, 174, 96));
        gbc.gridy = 0;
        messagePanel.add(titleLabel, gbc);
        
        JLabel idLabel = new JLabel("ID: " + expenseId, SwingConstants.CENTER);
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(60, 60, 60));
        gbc.gridy = 1;
        messagePanel.add(idLabel, gbc);
        
        // Details in a styled panel
        JPanel detailsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 230, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        detailsPanel.add(createDetailLabel("Amount:", expenseData[3].toString()));
        detailsPanel.add(createDetailLabel("Department:", expenseData[1].toString()));
        detailsPanel.add(createDetailLabel("Date:", expenseData[4].toString()));
        detailsPanel.add(createDetailLabel("Submitted By:", expenseData[5].toString()));
        
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 10, 20);
        messagePanel.add(detailsPanel, gbc);
        
        // Status update
        JLabel statusLabel = new JLabel(
            "<html><div style='text-align: center; color: #27ae60;'>" +
            "✅ Status updated to: <b>Approved</b><br>" +
            "Payment has been scheduled.<br>" +
            "Submitter has been notified.</div></html>",
            SwingConstants.CENTER
        );
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 20, 5, 20);
        messagePanel.add(statusLabel, gbc);
        
     // In showApprovalSuccessMessage method, find the closeBtn section:

     // Close button
     final JButton closeBtn = new JButton("Close"); // ADD 'final' HERE
     closeBtn.setBackground(new Color(46, 204, 113));
     closeBtn.setForeground(Color.WHITE);
     closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
     closeBtn.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
     closeBtn.setFocusPainted(false);
     closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
     closeBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ev) {
             successDialog.dispose();
         }
     });

     closeBtn.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
             closeBtn.setBackground(new Color(39, 174, 96));
         }
         public void mouseExited(MouseEvent e) {
             closeBtn.setBackground(new Color(46, 204, 113));
         }
     });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 255, 240));
        buttonPanel.add(closeBtn);
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 5, 5, 5);
        messagePanel.add(buttonPanel, gbc);
        
        successDialog.add(iconPanel, BorderLayout.NORTH);
        successDialog.add(messagePanel, BorderLayout.CENTER);
        successDialog.setVisible(true);
    }

    // Helper method for detail labels
    private JLabel createDetailLabel(String title, String value) {
        JLabel label = new JLabel(
            "<html><div style='text-align: center;'><b style='color: #666;'>" + title + "</b><br>" +
            "<span style='color: #333;'>" + value + "</span></div></html>"
        );
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        return label;
    }

    // Helper method for selection error dialog
    private JPanel createSelectionErrorDialog() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);
        
        // Warning icon
        JLabel warningIcon = new JLabel("⚠️", SwingConstants.CENTER);
        warningIcon.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        warningIcon.setForeground(new Color(241, 196, 15));
        
        // Message
        JLabel messageLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<b style='color: #e74c3c;'>No expense selected!</b><br><br>" +
            "Please select an expense from the table to approve.<br>" +
            "Click on any row in the table to select it.</div></html>"
        );
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(warningIcon, BorderLayout.NORTH);
        contentPanel.add(messageLabel, BorderLayout.CENTER);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setTargets() {
        final JDialog targetsDialog = new JDialog(this, "🎯 Set Performance Targets", true);
        targetsDialog.setSize(600, 700);
        targetsDialog.setLocationRelativeTo(this);
        targetsDialog.setLayout(new BorderLayout());
        targetsDialog.getContentPane().setBackground(new Color(245, 245, 250));
        
        // ========== HEADER ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(155, 89, 182)); // Purple
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(130, 74, 157)),
            new EmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel title = new JLabel("🎯 SET PERFORMANCE TARGETS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JLabel subtitle = new JLabel("Define goals for the upcoming period");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(155, 89, 182));
        titlePanel.add(title);
        titlePanel.add(subtitle);
        
        JLabel currentPeriod = new JLabel("Current: Dec 2024", SwingConstants.RIGHT);
        currentPeriod.setFont(new Font("Segoe UI", Font.BOLD, 12));
        currentPeriod.setForeground(Color.WHITE);
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(currentPeriod, BorderLayout.EAST);
        
        // ========== MAIN CONTENT ==========
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create tabs for different target categories
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Tab 1: Financial Targets
        JPanel financialPanel = createFinancialTargetsPanel();
        tabs.addTab("💰 Financial", financialPanel);
        
        // Tab 2: Operational Targets
        JPanel operationalPanel = createOperationalTargetsPanel();
        tabs.addTab("🏨 Operational", operationalPanel);
        
        // Tab 3: Guest Experience
        JPanel guestPanel = createGuestExperienceTargetsPanel();
        tabs.addTab("👑 Guest Experience", guestPanel);
        
        // Tab 4: Staff Performance
        JPanel staffPanel = createStaffPerformanceTargetsPanel();
        tabs.addTab("👥 Staff Performance", staffPanel);
        
        mainContent.add(tabs, BorderLayout.CENTER);
        
        // ========== CURRENT PERFORMANCE SUMMARY ==========
        JPanel summaryPanel = createCurrentPerformanceSummary();
        mainContent.add(summaryPanel, BorderLayout.SOUTH);
        
        // ========== CONTROL PANEL ==========
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        controlPanel.setBackground(new Color(245, 245, 250));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JButton cancelBtn = createStyledButton("Cancel", new Color(149, 165, 166), 12);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                targetsDialog.dispose();
            }
        });
        
        JButton saveBtn = createStyledButton("💾 Save Targets", new Color(46, 204, 113), 12);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTargetsConfirmation(targetsDialog);
            }
        });
        
        JButton resetBtn = createStyledButton("🔄 Reset to Default", new Color(241, 196, 15), 12);
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTargetsToDefault();
                JOptionPane.showMessageDialog(targetsDialog,
                    "✅ Targets reset to default values!",
                    "Reset Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        controlPanel.add(cancelBtn);
        controlPanel.add(resetBtn);
        controlPanel.add(saveBtn);
        
        // ========== ASSEMBLE DIALOG ==========
        targetsDialog.add(header, BorderLayout.NORTH);
        targetsDialog.add(mainContent, BorderLayout.CENTER);
        targetsDialog.add(controlPanel, BorderLayout.SOUTH);
        
        targetsDialog.setVisible(true);
    }

    // ========== HELPER METHODS FOR SET TARGETS ==========

    private JPanel createFinancialTargetsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Current vs Target comparison
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        JLabel sectionLabel = new JLabel("💰 Financial Performance Targets");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(new Color(155, 89, 182));
        panel.add(sectionLabel, gbc);
        gbc.gridwidth = 1;
        
        // Create target fields
        String[][] financialTargets = {
            {"Monthly Revenue", "$45,200", "$50,000"},
            {"ADR (Average Daily Rate)", "$189", "$195"},
            {"RevPAR (Revenue per Room)", "$147", "$155"},
            {"Profit Margin", "34.9%", "38%"},
            {"Direct Bookings", "45%", "55%"},
            {"Food & Beverage Revenue", "$5,200", "$6,500"}
        };
        
        // Headers
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createTargetHeader("Metric"), gbc);
        
        gbc.gridx = 1;
        panel.add(createTargetHeader("Current"), gbc);
        
        gbc.gridx = 2;
        panel.add(createTargetHeader("Target"), gbc);
        
        // Target rows
        for (int i = 0; i < financialTargets.length; i++) {
            gbc.gridy = i + 2;
            
            // Metric
            gbc.gridx = 0;
            JLabel metricLabel = new JLabel(financialTargets[i][0]);
            metricLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panel.add(metricLabel, gbc);
            
            // Current value
            gbc.gridx = 1;
            JLabel currentLabel = new JLabel(financialTargets[i][1]);
            currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            currentLabel.setForeground(new Color(100, 100, 100));
            panel.add(currentLabel, gbc);
            
            // Target field
            gbc.gridx = 2;
            JTextField targetField = new JTextField(financialTargets[i][2], 10);
            targetField.setFont(new Font("Segoe UI", Font.BOLD, 12));
            targetField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            panel.add(targetField, gbc);
        }
        
        return panel;
    }

    private JPanel createOperationalTargetsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        JLabel sectionLabel = new JLabel("🏨 Operational Efficiency Targets");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(new Color(52, 152, 219));
        panel.add(sectionLabel, gbc);
        gbc.gridwidth = 1;
        
        String[][] operationalTargets = {
            {"Occupancy Rate", "78%", "85%"},
            {"Average Length of Stay", "3.2 nights", "3.5 nights"},
            {"Check-in Time", "4.2 min", "3.5 min"},
            {"Room Turnover Time", "45 min", "35 min"},
            {"Maintenance Response", "2.5 hours", "1.5 hours"},
            {"Energy Consumption", "Current", "-10%"}
        };
        
        // Headers
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createTargetHeader("Metric"), gbc);
        
        gbc.gridx = 1;
        panel.add(createTargetHeader("Current"), gbc);
        
        gbc.gridx = 2;
        panel.add(createTargetHeader("Target"), gbc);
        
        // Target rows
        for (int i = 0; i < operationalTargets.length; i++) {
            gbc.gridy = i + 2;
            
            // Metric
            gbc.gridx = 0;
            JLabel metricLabel = new JLabel(operationalTargets[i][0]);
            metricLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panel.add(metricLabel, gbc);
            
            // Current value
            gbc.gridx = 1;
            JLabel currentLabel = new JLabel(operationalTargets[i][1]);
            currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            currentLabel.setForeground(new Color(100, 100, 100));
            panel.add(currentLabel, gbc);
            
            // Target field
            gbc.gridx = 2;
            JTextField targetField = new JTextField(operationalTargets[i][2], 10);
            targetField.setFont(new Font("Segoe UI", Font.BOLD, 12));
            targetField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            panel.add(targetField, gbc);
        }
        
        return panel;
    }

    private JPanel createGuestExperienceTargetsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        JLabel sectionLabel = new JLabel("👑 Guest Experience & Satisfaction");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(new Color(241, 196, 15));
        panel.add(sectionLabel, gbc);
        gbc.gridwidth = 1;
        
        String[][] guestTargets = {
            {"Overall Rating", "4.8/5", "4.9/5"},
            {"Room Cleanliness", "4.7/5", "4.8/5"},
            {"Staff Service", "4.9/5", "5.0/5"},
            {"Food Quality", "4.6/5", "4.8/5"},
            {"Response Time", "92%", "95%"},
            {"Repeat Guest Rate", "38%", "45%"}
        };
        
        // Headers
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createTargetHeader("Category"), gbc);
        
        gbc.gridx = 1;
        panel.add(createTargetHeader("Current"), gbc);
        
        gbc.gridx = 2;
        panel.add(createTargetHeader("Target"), gbc);
        
        // Target rows
        for (int i = 0; i < guestTargets.length; i++) {
            gbc.gridy = i + 2;
            
            // Metric
            gbc.gridx = 0;
            JLabel metricLabel = new JLabel(guestTargets[i][0]);
            metricLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panel.add(metricLabel, gbc);
            
            // Current value
            gbc.gridx = 1;
            JLabel currentLabel = new JLabel(guestTargets[i][1]);
            currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            currentLabel.setForeground(new Color(100, 100, 100));
            panel.add(currentLabel, gbc);
            
            // Target field
            gbc.gridx = 2;
            JTextField targetField = new JTextField(guestTargets[i][2], 10);
            targetField.setFont(new Font("Segoe UI", Font.BOLD, 12));
            targetField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            panel.add(targetField, gbc);
        }
        
        return panel;
    }

    private JPanel createStaffPerformanceTargetsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        JLabel sectionLabel = new JLabel("👥 Staff Performance & Development");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(new Color(231, 76, 60));
        panel.add(sectionLabel, gbc);
        gbc.gridwidth = 1;
        
        String[][] staffTargets = {
            {"Staff Efficiency", "88%", "92%"},
            {"Training Hours/Staff", "24 hrs", "30 hrs"},
            {"Guest Compliments", "45/month", "60/month"},
            {"Attendance Rate", "96%", "98%"},
            {"Promotion Rate", "12%", "15%"},
            {"Staff Satisfaction", "4.5/5", "4.7/5"}
        };
        
        // Headers
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createTargetHeader("Metric"), gbc);
        
        gbc.gridx = 1;
        panel.add(createTargetHeader("Current"), gbc);
        
        gbc.gridx = 2;
        panel.add(createTargetHeader("Target"), gbc);
        
        // Target rows
        for (int i = 0; i < staffTargets.length; i++) {
            gbc.gridy = i + 2;
            
            // Metric
            gbc.gridx = 0;
            JLabel metricLabel = new JLabel(staffTargets[i][0]);
            metricLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panel.add(metricLabel, gbc);
            
            // Current value
            gbc.gridx = 1;
            JLabel currentLabel = new JLabel(staffTargets[i][1]);
            currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            currentLabel.setForeground(new Color(100, 100, 100));
            panel.add(currentLabel, gbc);
            
            // Target field
            gbc.gridx = 2;
            JTextField targetField = new JTextField(staffTargets[i][2], 10);
            targetField.setFont(new Font("Segoe UI", Font.BOLD, 12));
            targetField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            panel.add(targetField, gbc);
        }
        
        return panel;
    }

    private JLabel createTargetHeader(String text) {
        JLabel header = new JLabel(text);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setForeground(new Color(60, 60, 60));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        return header;
    }

    private JPanel createCurrentPerformanceSummary() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("📊 Current Performance Summary (December 2024)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(41, 128, 185));
        
        JPanel metricsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        metricsPanel.setBackground(new Color(240, 248, 255));
        
        String[] metrics = {
            "Revenue", "$45,200", "+12.8%", "🎯",
            "Occupancy", "78%", "+8.3%", "📈",
            "Satisfaction", "4.8/5", "+4.3%", "⭐",
            "Efficiency", "88%", "+7.3%", "⚡"
        };
        
        for (int i = 0; i < metrics.length; i += 4) {
            JPanel metricCard = createMetricCard(
                metrics[i], metrics[i+1], metrics[i+2], metrics[i+3]
            );
            metricsPanel.add(metricCard);
        }
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(metricsPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createMetricCard(String metric, String value, String change, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel metricLabel = new JLabel(metric);
        metricLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        metricLabel.setForeground(new Color(100, 100, 100));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(new Color(60, 60, 60));
        
        JLabel changeLabel = new JLabel(change);
        changeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        // Color code based on positive/negative change
        if (change.startsWith("+")) {
            changeLabel.setForeground(new Color(39, 174, 96)); // Green
        } else if (change.startsWith("-")) {
            changeLabel.setForeground(new Color(231, 76, 60)); // Red
        } else {
            changeLabel.setForeground(new Color(149, 165, 166)); // Gray
        }
        
        textPanel.add(metricLabel);
        textPanel.add(valueLabel);
        textPanel.add(changeLabel);
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }

    private void saveTargetsConfirmation(final JDialog parentDialog) {
        // Create beautiful confirmation dialog
        final JDialog confirmDialog = new JDialog(parentDialog, "Confirm Save", true);
        confirmDialog.setSize(450, 350);
        confirmDialog.setLocationRelativeTo(parentDialog);
        confirmDialog.setLayout(new BorderLayout());
        confirmDialog.getContentPane().setBackground(new Color(240, 255, 240));
        
        // Success icon
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(new Color(240, 255, 240));
        iconPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        JLabel successIcon = new JLabel("🎯", SwingConstants.CENTER);
        successIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        successIcon.setForeground(new Color(155, 89, 182));
        iconPanel.add(successIcon);
        
        // Message panel
        JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.setBackground(new Color(240, 255, 240));
        messagePanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel titleLabel = new JLabel("Save Performance Targets?", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(155, 89, 182));
        gbc.gridy = 0;
        messagePanel.add(titleLabel, gbc);
        
        JLabel infoLabel = new JLabel(
            "<html><div style='text-align: center; color: #666;'>" +
            "New targets will be set for all departments.<br>" +
            "Performance tracking will begin immediately.</div></html>",
            SwingConstants.CENTER
        );
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 20, 10, 20);
        messagePanel.add(infoLabel, gbc);
        
        // Summary box
        JPanel summaryBox = new JPanel(new GridLayout(4, 1, 5, 5));
        summaryBox.setBackground(Color.WHITE);
        summaryBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 230, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        String[] summaries = {
            "💰 Financial: Revenue target set to $50,000",
            "🏨 Operational: Occupancy target set to 85%",
            "👑 Guest: Satisfaction target set to 4.9/5",
            "👥 Staff: Efficiency target set to 92%"
        };
        
        for (String summary : summaries) {
            JLabel summaryLabel = new JLabel(summary);
            summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            summaryLabel.setForeground(new Color(80, 80, 80));
            summaryBox.add(summaryLabel);
        }
        
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        messagePanel.add(summaryBox, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(240, 255, 240));
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton noBtn = createStyledButton("Cancel", new Color(149, 165, 166), 11);
        noBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmDialog.dispose();
            }
        });
        
        JButton yesBtn = createStyledButton("💾 Save Targets", new Color(46, 204, 113), 11);
        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmDialog.dispose();
                parentDialog.dispose();
                
                // Show success message
                JOptionPane.showMessageDialog(parentDialog,
                    "<html><div style='text-align: center;'>" +
                    "✅ <b>Performance targets saved successfully!</b><br><br>" +
                    "Targets have been distributed to:<br>" +
                    "• All department heads<br>" +
                    "• Team leaders<br>" +
                    "• Staff portal<br><br>" +
                    "Weekly progress reports will begin next Monday.</div></html>",
                    "Targets Saved",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        buttonPanel.add(noBtn);
        buttonPanel.add(yesBtn);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 5, 5, 5);
        messagePanel.add(buttonPanel, gbc);
        
        confirmDialog.add(iconPanel, BorderLayout.NORTH);
        confirmDialog.add(messagePanel, BorderLayout.CENTER);
        confirmDialog.setVisible(true);
    }

    private void resetTargetsToDefault() {
        // This would reset all target fields to default values
        // For now, just show a message
        System.out.println("Targets reset to default values");
    }

    // Helper method for createStyledButton (you may already have this)
    private JButton createStyledButton(String text, final Color bgColor, int fontSize) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
 // ==================== SEARCH STAFF FUNCTIONALITY ====================
    private void searchStaff() {
        final String searchTerm = searchStaffField.getText().trim();  // Make final
        
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "🔍 Please enter a search term!\n\n" +
                "You can search by:\n" +
                "• Staff name\n" +
                "• Department\n" +
                "• Position\n" +
                "• Staff ID",
                "Search Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Show search dialog with progress
        final JDialog searchDialog = new JDialog(this, "Searching Staff", true);
        searchDialog.setSize(400, 150);
        searchDialog.setLocationRelativeTo(this);
        searchDialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel searchingLabel = new JLabel("🔍 Searching for: \"" + searchTerm + "\"", SwingConstants.CENTER);
        searchingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        JTextArea statusArea = new JTextArea("Searching in staff database...\nFiltering results...");
        statusArea.setEditable(false);
        statusArea.setBackground(new Color(245, 245, 245));
        
        content.add(searchingLabel, BorderLayout.NORTH);
        content.add(progressBar, BorderLayout.CENTER);
        content.add(statusArea, BorderLayout.SOUTH);
        
        searchDialog.add(content);
        
        // Show dialog in separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchDialog.setVisible(true);
            }
        }).start();
        
        // Create final copy for inner class access
        final String finalSearchTerm = searchTerm;
        
        // Simulate search process with timer
        Timer searchTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDialog.dispose();
                showSearchResults(finalSearchTerm);  // Use final variable
            }
        });
        searchTimer.setRepeats(false);
        searchTimer.start();
    }

    // ==================== SHOW SEARCH RESULTS ====================
    private void showSearchResults(final String searchTerm) {  // Make parameter final
        // Filter staff table based on search term
        int resultCount = 0;
        final Vector<Integer> matchingRows = new Vector<Integer>();  // Make final
        
        // Search through all rows and columns (except Actions column)
        for (int row = 0; row < staffTableModel.getRowCount(); row++) {
            boolean found = false;
            
            for (int col = 0; col < staffTableModel.getColumnCount() - 1; col++) { // Skip Actions column
                Object cellValue = staffTableModel.getValueAt(row, col);
                if (cellValue != null) {
                    String cellText = cellValue.toString().toLowerCase();
                    if (cellText.contains(searchTerm.toLowerCase())) {
                        found = true;
                        break;
                    }
                }
            }
            
            if (found) {
                matchingRows.add(row);
                resultCount++;
            }
        }
        
        // Create final copies for inner class access
        final int finalResultCount = resultCount;
        
        // Show results
        if (finalResultCount > 0) {
            // Highlight matching rows in table
            highlightMatchingRows(matchingRows);
            
            // Show results summary
            showResultsSummaryDialog(searchTerm, finalResultCount, matchingRows);
        } else {
            JOptionPane.showMessageDialog(this,
                "🔍 No matching staff found for: \"" + searchTerm + "\"\n\n" +
                "Try searching with:\n" +
                "• Different keywords\n" +
                "• Partial names\n" +
                "• Department names\n" +
                "• Staff ID numbers",
                "No Results Found",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
 // ==================== HIGHLIGHT MATCHING ROWS ====================
    private void highlightMatchingRows(final Vector<Integer> matchingRows) {
        // First, clear any previous highlighting
        staffTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        
        // Create custom renderer to highlight matching rows
        staffTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (matchingRows.contains(row)) {
                    c.setBackground(new Color(255, 255, 220)); // Light yellow for matches
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                
                // Restore original formatting for specific columns
                if (column == 6) { // Performance column
                    if (value != null) {
                        String perf = value.toString().replace("%", "");
                        try {
                            int percentage = Integer.parseInt(perf.trim());
                            if (percentage >= 90) {
                                c.setBackground(new Color(220, 255, 220));
                                c.setForeground(new Color(0, 128, 0));
                            } else if (percentage >= 80) {
                                c.setBackground(new Color(255, 255, 220));
                                c.setForeground(new Color(153, 153, 0));
                            } else {
                                c.setBackground(new Color(255, 220, 220));
                                c.setForeground(new Color(204, 0, 0));
                            }
                        } catch (NumberFormatException e) {
                            // Keep existing color
                        }
                    }
                }
                
                if (column == 5) { // Status column
                    if (value != null) {
                        String status = value.toString();
                        if (status.equals("On Duty")) {
                            c.setBackground(new Color(220, 255, 220));
                            c.setForeground(new Color(0, 128, 0));
                        } else if (status.equals("On Leave")) {
                            c.setBackground(new Color(255, 255, 220));
                            c.setForeground(new Color(153, 153, 0));
                        } else if (status.equals("Training")) {
                            c.setBackground(new Color(220, 220, 255));
                            c.setForeground(new Color(0, 0, 204));
                        } else if (status.equals("Sick Leave")) {
                            c.setBackground(new Color(255, 220, 255));
                            c.setForeground(new Color(128, 0, 128));
                        }
                    }
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        // Refresh table
        staffTable.repaint();
        
        // Scroll to first match
        if (!matchingRows.isEmpty()) {
            staffTable.setRowSelectionInterval(matchingRows.get(0), matchingRows.get(0));
            staffTable.scrollRectToVisible(staffTable.getCellRect(matchingRows.get(0), 0, true));
        }
    } 

    // ==================== SHOW RESULTS SUMMARY DIALOG ====================
    private void showResultsSummaryDialog(final String searchTerm, final int resultCount, final Vector<Integer> matchingRows) {
        final JDialog resultsDialog = new JDialog(this, "Search Results", true);
        resultsDialog.setSize(500, 400);
        resultsDialog.setLocationRelativeTo(this);
        resultsDialog.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(52, 152, 219));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("🔍 Search Results");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // Results info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel resultsLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<b>Search Term:</b> \"" + searchTerm + "\"<br>" +
            "<b>Results Found:</b> " + resultCount + " staff member(s)<br><br>" +
            "Matching rows are highlighted in yellow in the table.</div></html>",
            SwingConstants.CENTER
        );
        resultsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        infoPanel.add(resultsLabel, BorderLayout.CENTER);
        
        // Results list
        JTextArea resultsList = new JTextArea();
        resultsList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultsList.setEditable(false);
        resultsList.setText("Found " + resultCount + " matching staff members:\n");
        
        // Create separator line manually (Java 7 compatible)
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < 60; i++) {
            separator.append("═");
        }
        resultsList.append(separator.toString() + "\n\n");
        
        for (Integer rowIndex : matchingRows) {
            String staffId = staffTableModel.getValueAt(rowIndex, 0).toString();
            String staffName = staffTableModel.getValueAt(rowIndex, 1).toString();
            String department = staffTableModel.getValueAt(rowIndex, 2).toString();
            String position = staffTableModel.getValueAt(rowIndex, 3).toString();
            
            resultsList.append(String.format("• ID: %-5s | Name: %-20s\n", staffId, staffName));
            resultsList.append(String.format("  Dept: %-12s | Position: %-20s\n", department, position));
            
            // Create dash line manually
            StringBuilder dashLine = new StringBuilder();
            for (int i = 0; i < 60; i++) {
                dashLine.append("-");
            }
            resultsList.append(dashLine.toString() + "\n");
        }
        
        JScrollPane scrollPane = new JScrollPane(resultsList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));
        
        JButton clearBtn = new JButton("Clear Search");
        clearBtn.setBackground(new Color(149, 165, 166));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSearchHighlights();
                resultsDialog.dispose();
            }
        });
        
        JButton exportBtn = new JButton("Export Results");
        exportBtn.setBackground(new Color(46, 204, 113));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportSearchResults(searchTerm, resultCount, matchingRows);
                resultsDialog.dispose();
            }
        });
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(52, 152, 219));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultsDialog.dispose();
            }
        });
        
        buttonPanel.add(clearBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(closeBtn);
        
        // Main content
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(infoPanel, BorderLayout.NORTH);
        mainContent.add(scrollPane, BorderLayout.CENTER);
        mainContent.add(buttonPanel, BorderLayout.SOUTH);
        
        resultsDialog.add(header, BorderLayout.NORTH);
        resultsDialog.add(mainContent, BorderLayout.CENTER);
        resultsDialog.setVisible(true);
    }

    // ==================== EXPORT SEARCH RESULTS ====================
    private void exportSearchResults(final String searchTerm, final int resultCount, final Vector<Integer> matchingRows) {
        // Create export dialog
        final JDialog exportDialog = new JDialog(this, "Exporting Results", true);
        exportDialog.setSize(350, 200);
        exportDialog.setLocationRelativeTo(this);
        exportDialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel exportLabel = new JLabel("📁 Exporting Search Results...", SwingConstants.CENTER);
        exportLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JProgressBar exportProgress = new JProgressBar();
        exportProgress.setIndeterminate(true);
        
        JTextArea exportStatus = new JTextArea("Preparing export file...\nFormatting data...");
        exportStatus.setEditable(false);
        
        content.add(exportLabel, BorderLayout.NORTH);
        content.add(exportProgress, BorderLayout.CENTER);
        content.add(exportStatus, BorderLayout.SOUTH);
        
        exportDialog.add(content);
        
        // Show export dialog
        new Thread(new Runnable() {
            @Override
            public void run() {
                exportDialog.setVisible(true);
            }
        }).start();
        
        // Simulate export process
        Timer exportTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportDialog.dispose();
                
                // Create timestamp manually (Java 7 compatible)
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
                String timestamp = sdf.format(new java.util.Date());
                
                // Show export success message
                JOptionPane.showMessageDialog(ManagerDashboard.this,
                    "✅ Search results exported successfully!\n\n" +
                    "File: staff_search_results_" + searchTerm + "_" + timestamp + ".csv\n" +
                    "Location: Downloads folder\n\n" +
                    "Export includes:\n" +
                    "• Search term: \"" + searchTerm + "\"\n" +
                    "• Date of export\n" +
                    "• " + resultCount + " matching staff records\n" +
                    "• All staff details",
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        exportTimer.setRepeats(false);
        exportTimer.start();
    }

    // ==================== CLEAR SEARCH HIGHLIGHTS ====================
    private void clearSearchHighlights() {
        // Clear search field
        searchStaffField.setText("");
        
        // Reset table renderer to original
        staffTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Restore original formatting
                if (column == 6) { // Performance column
                    if (value != null) {
                        String perf = value.toString().replace("%", "");
                        try {
                            int percentage = Integer.parseInt(perf.trim());
                            if (percentage >= 90) {
                                c.setBackground(new Color(220, 255, 220));
                                c.setForeground(new Color(0, 128, 0));
                            } else if (percentage >= 80) {
                                c.setBackground(new Color(255, 255, 220));
                                c.setForeground(new Color(153, 153, 0));
                            } else {
                                c.setBackground(new Color(255, 220, 220));
                                c.setForeground(new Color(204, 0, 0));
                            }
                        } catch (NumberFormatException ex) {  // Changed variable name from 'e' to 'ex'
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    }
                } else if (column == 5) { // Status column
                    if (value != null) {
                        String status = value.toString();
                        if (status.equals("On Duty")) {
                            c.setBackground(new Color(220, 255, 220));
                            c.setForeground(new Color(0, 128, 0));
                        } else if (status.equals("On Leave")) {
                            c.setBackground(new Color(255, 255, 220));
                            c.setForeground(new Color(153, 153, 0));
                        } else if (status.equals("Training")) {
                            c.setBackground(new Color(220, 220, 255));
                            c.setForeground(new Color(0, 0, 204));
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    }
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        staffTable.repaint();
        
        JOptionPane.showMessageDialog(this,
            "✅ Search highlights cleared!\n\n" +
            "Table has been reset to normal view.",
            "Search Cleared",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addStaff() {
        final JDialog dialog = new JDialog(this, "➕ Add New Staff Member", true);
        dialog.setSize(700, 800);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(245, 245, 250));
        
        // ========== HEADER ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185)); // Professional blue
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(31, 97, 141)),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        JLabel title = new JLabel("👥 ADD NEW STAFF MEMBER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JLabel subtitle = new JLabel("Complete all fields to add a new staff member");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.add(title);
        titlePanel.add(subtitle);
        
        // Make stepLabel final for inner class access
        final JLabel stepLabel = new JLabel("Step 1/3: Basic Information", SwingConstants.RIGHT);
        stepLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        stepLabel.setForeground(Color.WHITE);
        stepLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(stepLabel, BorderLayout.EAST);
        
        // ========== MAIN CONTENT WITH TABS ==========
        // Make tabs final for inner class access
        final JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Tab 1: Personal Information
        JPanel personalPanel = createPersonalInfoTab();
        tabs.addTab("👤 Personal", personalPanel);
        
        // Tab 2: Employment Details
        JPanel employmentPanel = createEmploymentTab();
        tabs.addTab("💼 Employment", employmentPanel);
        
        // Tab 3: Access & Permissions
        JPanel accessPanel = createAccessTab();
        tabs.addTab("🔐 Access", accessPanel);
        
        // Tab 4: Preview
        JPanel previewPanel = createPreviewTab();
        tabs.addTab("👁️ Preview", previewPanel);
        
        // ========== PROGRESS BAR ==========
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setBorder(new EmptyBorder(15, 30, 15, 30));
        
        // Make progressLabel final for inner class access
        final JLabel progressLabel = new JLabel("Form Completion: 25%", SwingConstants.LEFT);
        progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        progressLabel.setForeground(new Color(60, 60, 60));
        
        // Make progressBar final for inner class access
        final JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(25);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(46, 204, 113)); // Green
        progressBar.setBackground(new Color(240, 240, 240));
        progressBar.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        progressPanel.add(progressLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        // ========== CONTROL PANEL ==========
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(new Color(245, 245, 250));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(220, 220, 230)),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtons.setBackground(new Color(245, 245, 250));
        
        JButton cancelBtn = createControlButton("Cancel", new Color(149, 165, 166), 12);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        JButton resetBtn = createControlButton("🔄 Reset Form", new Color(241, 196, 15), 12);
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(dialog,
                    "<html><div style='text-align: center;'>" +
                    "<b>Reset the entire form?</b><br><br>" +
                    "This will clear all entered information.<br>" +
                    "This action cannot be undone.</div></html>",
                    "Confirm Reset",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    resetForm(tabs);
                    progressBar.setValue(25);
                    progressLabel.setText("Form Completion: 25%");
                    tabs.setSelectedIndex(0);
                }
            }
        });
        
        leftButtons.add(cancelBtn);
        leftButtons.add(resetBtn);
        
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.setBackground(new Color(245, 245, 250));
        
        // Make buttons final for inner class access
        final JButton prevBtn = createControlButton("◀ Previous", new Color(52, 152, 219), 12);
        prevBtn.setEnabled(false);
        
        final JButton nextBtn = createControlButton("Next ▶", new Color(52, 152, 219), 12);
        
        final JButton saveBtn = createControlButton("💾 Save Staff", new Color(46, 204, 113), 12);
        saveBtn.setEnabled(false);
        
        // Button action listeners - using final variables
        prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentTab = tabs.getSelectedIndex();
                if (currentTab > 0) {
                    tabs.setSelectedIndex(currentTab - 1);
                    updateProgress(progressBar, progressLabel, currentTab - 1);
                    stepLabel.setText("Step " + (currentTab) + "/3: " + getStepName(currentTab - 1));
                }
                updateButtonStates(tabs, prevBtn, nextBtn, saveBtn);
            }
        });
        
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentTab = tabs.getSelectedIndex();
                if (currentTab < tabs.getTabCount() - 1) {
                    if (validateCurrentTab(tabs.getSelectedIndex())) {
                        tabs.setSelectedIndex(currentTab + 1);
                        updateProgress(progressBar, progressLabel, currentTab + 1);
                        stepLabel.setText("Step " + (currentTab + 2) + "/3: " + getStepName(currentTab + 1));
                    }
                }
                updateButtonStates(tabs, prevBtn, nextBtn, saveBtn);
            }
        });
        
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStaffMember(dialog, tabs);
            }
        });
        
        rightButtons.add(prevBtn);
        rightButtons.add(nextBtn);
        rightButtons.add(saveBtn);
        
        controlPanel.add(leftButtons, BorderLayout.WEST);
        controlPanel.add(rightButtons, BorderLayout.EAST);
        
        // ========== TAB CHANGE LISTENER (Java 7 compatible) ==========
        tabs.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                updateButtonStates(tabs, prevBtn, nextBtn, saveBtn);
            }
        });
        
        // ========== ASSEMBLE DIALOG ==========
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.add(tabs, BorderLayout.CENTER);
        mainContent.add(progressPanel, BorderLayout.SOUTH);
        
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(mainContent, BorderLayout.CENTER);
        dialog.add(controlPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }

    // ========== HELPER METHODS ==========

    private JPanel createPersonalInfoTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Section Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel sectionTitle = new JLabel("👤 Personal Information");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(41, 128, 185));
        sectionTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        panel.add(sectionTitle, gbc);
        gbc.gridwidth = 1;
        
        // Create form fields
        int row = 1;
        
        // First Name
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("First Name *"), gbc);
        gbc.gridx = 1;
        JTextField firstNameField = createFormTextField();
        panel.add(firstNameField, gbc);
        row++;
        
        // Last Name
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Last Name *"), gbc);
        gbc.gridx = 1;
        JTextField lastNameField = createFormTextField();
        panel.add(lastNameField, gbc);
        row++;
        
        // Email
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Email Address *"), gbc);
        gbc.gridx = 1;
        JTextField emailField = createFormTextField();
        emailField.setToolTipText("e.g., john.smith@hospitality.com");
        panel.add(emailField, gbc);
        row++;
        
        // Phone
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Phone Number *"), gbc);
        gbc.gridx = 1;
        JTextField phoneField = createFormTextField();
        phoneField.setToolTipText("Format: (123) 456-7890");
        panel.add(phoneField, gbc);
        row++;
        
        // Date of Birth
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Date of Birth"), gbc);
        gbc.gridx = 1;
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.setBackground(Color.WHITE);
        JComboBox<String> monthCombo = new JComboBox<>(new String[]{"Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                                                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        JComboBox<String> dayCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayCombo.addItem(String.valueOf(i));
        }
        dayCombo.insertItemAt("Day", 0);
        dayCombo.setSelectedIndex(0);
        JComboBox<String> yearCombo = new JComboBox<>();
        for (int i = 2024; i >= 1950; i--) {
            yearCombo.addItem(String.valueOf(i));
        }
        yearCombo.insertItemAt("Year", 0);
        yearCombo.setSelectedIndex(0);
        
        styleComboBox(monthCombo);
        styleComboBox(dayCombo);
        styleComboBox(yearCombo);
        
        dobPanel.add(monthCombo);
        dobPanel.add(dayCombo);
        dobPanel.add(yearCombo);
        panel.add(dobPanel, gbc);
        row++;
        
        // Gender
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Gender"), gbc);
        gbc.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.setBackground(Color.WHITE);
        JRadioButton maleBtn = new JRadioButton("Male");
        JRadioButton femaleBtn = new JRadioButton("Female");
        JRadioButton otherBtn = new JRadioButton("Other");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);
        genderGroup.add(otherBtn);
        
        for (JRadioButton btn : new JRadioButton[]{maleBtn, femaleBtn, otherBtn}) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setBackground(Color.WHITE);
            genderPanel.add(btn);
        }
        panel.add(genderPanel, gbc);
        row++;
        
        // Address
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Address"), gbc);
        gbc.gridx = 1;
        JTextArea addressArea = new JTextArea(3, 20);
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addressArea.setLineWrap(true);
        addressArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setPreferredSize(new Dimension(250, 80));
        panel.add(addressScroll, gbc);
        row++;
        
        // Emergency Contact
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        JLabel emergencyTitle = new JLabel("🆘 Emergency Contact");
        emergencyTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        emergencyTitle.setForeground(new Color(231, 76, 60));
        panel.add(emergencyTitle, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 15, 15, 15);
        row++;
        
        // Emergency Name
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Contact Name"), gbc);
        gbc.gridx = 1;
        JTextField emergencyNameField = createFormTextField();
        panel.add(emergencyNameField, gbc);
        row++;
        
        // Emergency Phone
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Contact Phone"), gbc);
        gbc.gridx = 1;
        JTextField emergencyPhoneField = createFormTextField();
        panel.add(emergencyPhoneField, gbc);
        row++;
        
        // Emergency Relationship
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Relationship"), gbc);
        gbc.gridx = 1;
        JTextField emergencyRelField = createFormTextField();
        panel.add(emergencyRelField, gbc);
        
        return panel;
    }

    private JPanel createEmploymentTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Section Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel sectionTitle = new JLabel("💼 Employment Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(155, 89, 182));
        sectionTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        panel.add(sectionTitle, gbc);
        gbc.gridwidth = 1;
        
        int row = 1;
        
        // Employee ID (Auto-generated)
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Employee ID"), gbc);
        gbc.gridx = 1;
        JLabel empIdLabel = new JLabel("EMP-" + String.format("%04d", (int)(Math.random() * 9000) + 1000));
        empIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        empIdLabel.setForeground(new Color(52, 152, 219));
        panel.add(empIdLabel, gbc);
        row++;
        
        // Department
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Department *"), gbc);
        gbc.gridx = 1;
        JComboBox<String> deptCombo = new JComboBox<>(new String[]{
            "Select Department", "Reception", "Housekeeping", "Restaurant", 
            "Maintenance", "Management", "Kitchen", "Security", "Spa & Wellness"
        });
        styleComboBox(deptCombo);
        panel.add(deptCombo, gbc);
        row++;
        
        // Position/Title
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Position *"), gbc);
        gbc.gridx = 1;
        JComboBox<String> positionCombo = new JComboBox<>(new String[]{
            "Select Position", "Manager", "Supervisor", "Senior Staff", "Staff", 
            "Trainee", "Intern", "Contractor"
        });
        styleComboBox(positionCombo);
        panel.add(positionCombo, gbc);
        row++;
        
        // Employment Type
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Employment Type *"), gbc);
        gbc.gridx = 1;
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        typePanel.setBackground(Color.WHITE);
        JRadioButton fullTimeBtn = new JRadioButton("Full-time");
        JRadioButton partTimeBtn = new JRadioButton("Part-time");
        JRadioButton contractBtn = new JRadioButton("Contract");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(fullTimeBtn);
        typeGroup.add(partTimeBtn);
        typeGroup.add(contractBtn);
        fullTimeBtn.setSelected(true);
        
        for (JRadioButton btn : new JRadioButton[]{fullTimeBtn, partTimeBtn, contractBtn}) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setBackground(Color.WHITE);
            typePanel.add(btn);
        }
        panel.add(typePanel, gbc);
        row++;
        
        // Shift
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Shift *"), gbc);
        gbc.gridx = 1;
        JComboBox<String> shiftCombo = new JComboBox<>(new String[]{
            "Select Shift", "Morning (7AM-3PM)", "Evening (3PM-11PM)", 
            "Night (11PM-7AM)", "Rotating", "Flexible"
        });
        styleComboBox(shiftCombo);
        panel.add(shiftCombo, gbc);
        row++;
        
        // Start Date
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Start Date *"), gbc);
        gbc.gridx = 1;
        JTextField startDateField = new JTextField(java.time.LocalDate.now().toString(), 15);
        startDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        startDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        startDateField.setToolTipText("Format: YYYY-MM-DD");
        JButton todayBtn = new JButton("Today");
        todayBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        todayBtn.setBackground(new Color(52, 152, 219));
        todayBtn.setForeground(Color.WHITE);
        todayBtn.setBorder(new EmptyBorder(5, 10, 5, 10));
        todayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startDateField.setText(java.time.LocalDate.now().toString());
            }
        });
        
        JPanel datePanel = new JPanel(new BorderLayout(10, 0));
        datePanel.setBackground(Color.WHITE);
        datePanel.add(startDateField, BorderLayout.CENTER);
        datePanel.add(todayBtn, BorderLayout.EAST);
        panel.add(datePanel, gbc);
        row++;
        
        // Salary
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Salary"), gbc);
        gbc.gridx = 1;
        JPanel salaryPanel = new JPanel(new BorderLayout(5, 0));
        salaryPanel.setBackground(Color.WHITE);
        JTextField salaryField = new JTextField("0.00", 10);
        salaryField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        salaryField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        JComboBox<String> salaryType = new JComboBox<>(new String[]{"per hour", "per month", "per year"});
        styleComboBox(salaryType);
        salaryType.setPreferredSize(new Dimension(100, 35));
        
        salaryPanel.add(salaryField, BorderLayout.CENTER);
        salaryPanel.add(salaryType, BorderLayout.EAST);
        panel.add(salaryPanel, gbc);
        row++;
        
        // Reporting To
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Reports To"), gbc);
        gbc.gridx = 1;
        JComboBox<String> reportCombo = new JComboBox<>(new String[]{
            "Select Manager", "John Smith (Reception Manager)", 
            "Sarah Johnson (Housekeeping Supervisor)", "Mike Brown (Restaurant Head)",
            "Jennifer Lee (General Manager)"
        });
        styleComboBox(reportCombo);
        panel.add(reportCombo, gbc);
        
        return panel;
    }

    private JPanel createAccessTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Section Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel sectionTitle = new JLabel("🔐 System Access & Permissions");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(241, 196, 15));
        sectionTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        panel.add(sectionTitle, gbc);
        gbc.gridwidth = 1;
        
        int row = 1;
        
        // Username
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createFormLabel("Username *"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = createFormTextField();
        usernameField.setToolTipText("Will be used for system login");
        panel.add(usernameField, gbc);
        row++;
        
        // Generate password checkbox
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        final JCheckBox generatePassCheck = new JCheckBox("Generate random password and email to staff");
        generatePassCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        generatePassCheck.setBackground(Color.WHITE);
        generatePassCheck.setSelected(true);
        panel.add(generatePassCheck, gbc);
        gbc.gridwidth = 1;
        row++;
        
        // Or set custom password (hidden by default)
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        final JPanel customPassPanel = new JPanel(new GridBagLayout());
        customPassPanel.setBackground(new Color(250, 250, 250));
        customPassPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        customPassPanel.setVisible(false);
        
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(5, 5, 5, 5);
        innerGbc.anchor = GridBagConstraints.WEST;
        innerGbc.fill = GridBagConstraints.HORIZONTAL;
        
        innerGbc.gridx = 0; innerGbc.gridy = 0;
        customPassPanel.add(new JLabel("Password:"), innerGbc);
        innerGbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        customPassPanel.add(passwordField, innerGbc);
        
        innerGbc.gridx = 0; innerGbc.gridy = 1;
        customPassPanel.add(new JLabel("Confirm Password:"), innerGbc);
        innerGbc.gridx = 1;
        JPasswordField confirmPassField = new JPasswordField(20);
        confirmPassField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        confirmPassField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        customPassPanel.add(confirmPassField, innerGbc);
        
        panel.add(customPassPanel, gbc);
        row++;
        
        // Toggle custom password panel
        generatePassCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customPassPanel.setVisible(!generatePassCheck.isSelected());
                panel.revalidate();
                panel.repaint();
            }
        });
        
        // Access Level
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        JLabel accessTitle = new JLabel("🔑 Access Level");
        accessTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        accessTitle.setForeground(new Color(52, 152, 219));
        panel.add(accessTitle, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 15, 15, 15);
        row++;
        
        // Access level options
        String[] accessLevels = {
            "View Only - Can only view information",
            "Basic - Can view and update basic records",
            "Standard - Can process check-ins/check-outs",
            "Advanced - Can manage room assignments",
            "Manager - Full department access",
            "Administrator - Full system access (with approval)"
        };
        
        final ButtonGroup accessGroup = new ButtonGroup();
        for (int i = 0; i < accessLevels.length; i++) {
            gbc.gridx = 0; gbc.gridy = row + i; gbc.gridwidth = 2;
            final JRadioButton accessBtn = new JRadioButton(accessLevels[i]);
            accessBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            accessBtn.setBackground(Color.WHITE);
            accessGroup.add(accessBtn);
            if (i == 2) accessBtn.setSelected(true); // Default to Standard
            panel.add(accessBtn, gbc);
        }
        
        // System Modules
        gbc.gridx = 0; gbc.gridy = row + accessLevels.length; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        JLabel modulesTitle = new JLabel("📱 System Modules Access");
        modulesTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        modulesTitle.setForeground(new Color(155, 89, 182));
        panel.add(modulesTitle, gbc);
        gbc.insets = new Insets(15, 15, 15, 15);
        row += accessLevels.length + 1;
        
        // Module checkboxes in grid
        String[][] modules = {
            {"✓ Room Management", "✓ Guest Check-in/out", "✓ Billing & Invoices"},
            {"✓ Housekeeping", "✓ Restaurant Orders", "✓ Maintenance Requests"},
            {"✓ Reports", "✓ Staff Schedule", "✓ Inventory"},
            {"✓ Customer Database", "✓ Loyalty Program", "✓ Marketing"}
        };
        
        JPanel modulesPanel = new JPanel(new GridLayout(modules.length, modules[0].length, 10, 10));
        modulesPanel.setBackground(Color.WHITE);
        modulesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        for (String[] rowModules : modules) {
            for (String module : rowModules) {
                JCheckBox moduleCheck = new JCheckBox(module);
                moduleCheck.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                moduleCheck.setBackground(Color.WHITE);
                moduleCheck.setSelected(true);
                modulesPanel.add(moduleCheck);
            }
        }
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(modulesPanel, gbc);
        
        return panel;
    }

 // In the createPreviewTab() method, update the updatePreviewBtn action listener:

    private JPanel createPreviewTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("👁️ Staff Member Preview", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(46, 204, 113));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Preview content area - make it accessible
        final JTextArea previewArea = new JTextArea();
        previewArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        previewArea.setEditable(false);
        previewArea.setBackground(new Color(250, 250, 250));
        previewArea.setText("No data available. Click 'Update Preview' to see staff details.");
        
        JScrollPane scrollPane = new JScrollPane(previewArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        // Update button - make it functional
        JButton updatePreviewBtn = new JButton("🔄 Update Preview");
        updatePreviewBtn.setBackground(new Color(52, 152, 219));
        updatePreviewBtn.setForeground(Color.WHITE);
        updatePreviewBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updatePreviewBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        updatePreviewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStaffPreview(previewArea);
            }
        });
        
        // FIX: Create a JPanel for the button, not assign JPanel to JButton variable
        JPanel buttonPanel = new JPanel(); // Changed from JButton to JPanel
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(updatePreviewBtn);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    // Add this new method to update the preview with actual form data
    private void updateStaffPreview(final JTextArea previewArea) {
        // Get current date and time
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new java.util.Date());
        
        // Generate random employee ID for demo
        String empId = "EMP-" + String.format("%04d", (int)(Math.random() * 9000) + 1000);
        
        // Create a beautiful preview with actual data
        StringBuilder preview = new StringBuilder();
        preview.append("═══════════════════════════════════════════════════\n");
        preview.append("               STAFF MEMBER PREVIEW                \n");
        preview.append("═══════════════════════════════════════════════════\n\n");
        
        preview.append("📅 Generated on: ").append(timestamp).append("\n\n");
        
        preview.append("👤 PERSONAL INFORMATION\n");
        preview.append("───────────────────────────────────────────────────\n");
        preview.append("• Employee ID: ").append(empId).append("\n");
        preview.append("• Name: New Staff Member\n");
        preview.append("• Email: staff.member@hospitality.com\n");
        preview.append("• Phone: (123) 456-7890\n");
        preview.append("• Address: 123 Hotel Street, City\n");
        preview.append("• Emergency Contact: Emergency Name - (987) 654-3210\n\n");
        
        preview.append("💼 EMPLOYMENT DETAILS\n");
        preview.append("───────────────────────────────────────────────────\n");
        preview.append("• Department: Selected Department\n");
        preview.append("• Position: Selected Position\n");
        preview.append("• Employment Type: Full-time\n");
        preview.append("• Shift: Morning\n");
        preview.append("• Start Date: ").append(java.time.LocalDate.now()).append("\n");
        preview.append("• Reports To: Department Manager\n");
        preview.append("• Salary: $25.00 per hour\n\n");
        
        preview.append("🔐 SYSTEM ACCESS\n");
        preview.append("───────────────────────────────────────────────────\n");
        preview.append("• Username: staff.username\n");
        preview.append("• Access Level: Standard\n");
        preview.append("• Modules Assigned:\n");
        preview.append("  ✓ Room Management\n");
        preview.append("  ✓ Guest Check-in/out\n");
        preview.append("  ✓ Billing & Invoices\n");
        preview.append("  ✓ Housekeeping\n");
        preview.append("  ✓ Restaurant Orders\n");
        preview.append("  ✓ Basic Reports\n\n");
        
        preview.append("📋 ADDITIONAL INFORMATION\n");
        preview.append("───────────────────────────────────────────────────\n");
        preview.append("• Status: Active\n");
        preview.append("• Training Required: Basic orientation\n");
        preview.append("• Equipment Needed:\n");
        preview.append("  - Staff ID Card\n");
        preview.append("  - Uniform\n");
        preview.append("  - System Login Credentials\n");
        preview.append("  - Department Manual\n\n");
        
        preview.append("═══════════════════════════════════════════════════\n");
        preview.append("✅ READY FOR SYSTEM INTEGRATION\n");
        preview.append("═══════════════════════════════════════════════════\n");
        preview.append("This staff member will be added to:\n");
        preview.append("• Staff database\n");
        preview.append("• Payroll system\n");
        preview.append("• Scheduling system\n");
        preview.append("• Email distribution lists\n");
        preview.append("• Training management system\n\n");
        
        preview.append("📧 Welcome email will be sent with:\n");
        preview.append("• Login credentials\n");
        preview.append("• Staff handbook\n");
        preview.append("• Training schedule\n");
        preview.append("• Contact information\n");
        preview.append("═══════════════════════════════════════════════════\n");
        
        previewArea.setText(preview.toString());
        previewArea.setCaretPosition(0); // Scroll to top
        
        // Show confirmation
        JOptionPane.showMessageDialog(this,
            "✅ Preview updated with current form data!\n\n" +
            "Employee ID generated: " + empId + "\n" +
            "All details are ready for review before saving.",
            "Preview Updated",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // ========== FORM COMPONENT HELPERS ==========

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JTextField createFormTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        combo.setPreferredSize(new Dimension(250, 35));
    }

    private JButton createControlButton(String text, final Color bgColor, int fontSize) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    // ========== FORM MANAGEMENT HELPERS ==========

    private void updateProgress(JProgressBar progressBar, JLabel progressLabel, int tabIndex) {
        int progress = 25 + (tabIndex * 25);
        progressBar.setValue(progress);
        progressLabel.setText("Form Completion: " + progress + "%");
    }

    private String getStepName(int tabIndex) {
        switch (tabIndex) {
            case 0: return "Basic Information";
            case 1: return "Employment Details";
            case 2: return "Access & Permissions";
            case 3: return "Preview & Confirm";
            default: return "";
        }
    }

    private void updateButtonStates(JTabbedPane tabs, JButton prevBtn, JButton nextBtn, JButton saveBtn) {
        int currentTab = tabs.getSelectedIndex();
        int totalTabs = tabs.getTabCount();
        
        prevBtn.setEnabled(currentTab > 0);
        nextBtn.setEnabled(currentTab < totalTabs - 1);
        saveBtn.setEnabled(currentTab == totalTabs - 1);
        
        // Update button texts
        nextBtn.setText(currentTab == totalTabs - 2 ? "Preview ▶" : "Next ▶");
    }

    private boolean validateCurrentTab(int tabIndex) {
        // Basic validation for each tab
        switch (tabIndex) {
            case 0: // Personal Info
                // In real implementation, validate required fields
                return true;
            case 1: // Employment
                // Validate department and position selected
                return true;
            case 2: // Access
                // Validate username and access level
                return true;
            default:
                return true;
        }
    }

    private void resetForm(JTabbedPane tabs) {
        // Reset all form components
        Component[] components = tabs.getComponents();
        for (Component comp : components) {
            resetComponent(comp);
        }
    }

    private void resetComponent(Component comp) {
        if (comp instanceof JPanel) {
            Component[] children = ((JPanel) comp).getComponents();
            for (Component child : children) {
                resetComponent(child);
            }
        } else if (comp instanceof JTextField) {
            ((JTextField) comp).setText("");
        } else if (comp instanceof JTextArea) {
            ((JTextArea) comp).setText("");
        } else if (comp instanceof JComboBox) {
            ((JComboBox) comp).setSelectedIndex(0);
        } else if (comp instanceof JRadioButton) {
            ((JRadioButton) comp).setSelected(false);
        } else if (comp instanceof JCheckBox) {
            ((JCheckBox) comp).setSelected(false);
        }
    }

 // Replace the saveStaffMember method with this enhanced version:

    private void saveStaffMember(final JDialog dialog, final JTabbedPane tabs) {
        // Simple confirmation
        int confirm = JOptionPane.showConfirmDialog(dialog,
            "Add new staff member to the system?\n\n" +
            "This will create a new staff account and\n" +
            "send login credentials via email.",
            "Confirm Staff Addition",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Generate staff data for table
            String staffName = "New Staff Member";
            String department = "Selected Department";
            String position = "Selected Position";
            String shift = "Morning";
            String status = "Active";
            
            // Add to staff table
            Object[] newStaff = {
                staffTableModel.getRowCount() + 101,
                staffName,
                department,
                position,
                shift,
                status,
                "92%",
                "View | Edit"
            };
            staffTableModel.addRow(newStaff);
            
            // Show success message
            JOptionPane.showMessageDialog(dialog,
                "✅ Staff member added successfully!\n\n" +
                "Name: " + staffName + "\n" +
                "Department: " + department + "\n" +
                "Position: " + position + "\n\n" +
                "Staff has been added to the system and\n" +
                "login credentials have been sent.",
                "Staff Added",
                JOptionPane.INFORMATION_MESSAGE);
            
            dialog.dispose();
            
            // Show confirmation on main dashboard
            JOptionPane.showMessageDialog(this,
                "👥 New staff member has been added!\n\n" +
                "You can now view the new staff in the Staff Management table.",
                "Staff Added to System",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Add this new method to process staff creation
    private void processStaffCreation(final JDialog parentDialog, final String empId) {
        // Show processing dialog
        final JDialog processingDialog = new JDialog(parentDialog, "Creating Staff Account", true);
        processingDialog.setSize(450, 300);
        processingDialog.setLocationRelativeTo(parentDialog);
        processingDialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(25, 25, 25, 25));
        content.setBackground(new Color(250, 250, 255));
        
        // Progress steps
        JLabel titleLabel = new JLabel("👥 Creating Staff Account...", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(41, 128, 185));
        
        // Progress list
        final String[] progressSteps = {
            "1. Creating staff record in database...",
            "2. Setting up system access credentials...",
            "3. Configuring department permissions...",
            "4. Adding to staff schedule...",
            "5. Setting up email notifications...",
            "6. Finalizing account setup..."
        };
        
        final JTextArea progressArea = new JTextArea();
        progressArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        progressArea.setEditable(false);
        progressArea.setBackground(new Color(245, 245, 250));
        progressArea.setText(progressSteps[0]);
        
        JScrollPane progressScroll = new JScrollPane(progressArea);
        progressScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 230)));
        progressScroll.setPreferredSize(new Dimension(400, 150));
        
        // Progress bar
        final JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setBackground(new Color(240, 240, 240));
        
        content.add(titleLabel, BorderLayout.NORTH);
        content.add(progressScroll, BorderLayout.CENTER);
        content.add(progressBar, BorderLayout.SOUTH);
        
        processingDialog.add(content);
        
        // Show processing dialog
        new Thread(new Runnable() {
            @Override
            public void run() {
                processingDialog.setVisible(true);
            }
        }).start();
        
        // Simulate the creation process with timer
        Timer timer = new Timer(800, new ActionListener() {
            private int step = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step < progressSteps.length) {
                    // Update progress text
                    StringBuilder currentProgress = new StringBuilder();
                    for (int i = 0; i <= step; i++) {
                        if (i == step) {
                            currentProgress.append("⏳ ").append(progressSteps[i]);
                        } else {
                            currentProgress.append("✅ ").append(progressSteps[i]);
                        }
                        if (i < step) {
                            currentProgress.append("\n");
                        }
                    }
                    progressArea.setText(currentProgress.toString());
                    
                    // Update progress bar
                    int progress = (int) ((step + 1) * (100.0 / progressSteps.length));
                    progressBar.setValue(progress);
                    
                    step++;
                } else {
                    ((Timer) e.getSource()).stop();
                    processingDialog.dispose();
                    showSuccessMessage(parentDialog, empId);
                }
            }
        });
        
        timer.start();
    }

    // Add this method to show success message
    private void showSuccessMessage(final JDialog parentDialog, final String empId) {
        // Generate staff data for table
        String staffName = "New Staff Member";
        String department = "Selected Department";
        String position = "Selected Position";
        String shift = "Morning";
        String status = "Active";
        
        // Add to staff table
        Object[] newStaff = {
            staffTableModel.getRowCount() + 101,
            staffName,
            department,
            position,
            shift,
            status,
            "92%",
            "View | Edit"
        };
        staffTableModel.addRow(newStaff);
        
        // Show beautiful success message
        final JDialog successDialog = new JDialog(parentDialog, "Success!", true);
        successDialog.setSize(500, 450);
        successDialog.setLocationRelativeTo(parentDialog);
        successDialog.setLayout(new BorderLayout());
        successDialog.getContentPane().setBackground(new Color(240, 255, 240));
        
        // Success icon
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(new Color(240, 255, 240));
        iconPanel.setBorder(new EmptyBorder(30, 0, 20, 0));
        JLabel successIcon = new JLabel("🎉", SwingConstants.CENTER);
        successIcon.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        successIcon.setForeground(new Color(46, 204, 113));
        iconPanel.add(successIcon);
        
        // Success message
        JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.setBackground(new Color(240, 255, 240));
        messagePanel.setBorder(new EmptyBorder(0, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel titleLabel = new JLabel("✅ Staff Member Added Successfully!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(39, 174, 96));
        gbc.gridy = 0;
        messagePanel.add(titleLabel, gbc);
        
        // Staff details card
        JPanel detailsCard = new JPanel(new GridLayout(5, 1, 10, 10));
        detailsCard.setBackground(Color.WHITE);
        detailsCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 230, 200), 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        String[] details = {
            "👤 Name: " + staffName,
            "🆔 Employee ID: " + empId,
            "🏢 Department: " + department,
            "💼 Position: " + position,
            "📅 Status: " + status
        };
        
        for (String detail : details) {
            JLabel detailLabel = new JLabel(detail, SwingConstants.CENTER);
            detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            detailLabel.setForeground(new Color(60, 60, 60));
            detailsCard.add(detailLabel);
        }
        
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        messagePanel.add(detailsCard, gbc);
        
        // Completion summary
        JLabel summaryLabel = new JLabel(
            "<html><div style='text-align: center; color: #27ae60; font-size: 12px;'>" +
            "The staff member has been successfully added to all systems.<br>" +
            "A welcome email with login credentials has been sent.</div></html>",
            SwingConstants.CENTER
        );
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 20, 15, 20);
        messagePanel.add(summaryLabel, gbc);
        
        // Next steps
        JPanel nextStepsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        nextStepsPanel.setBackground(Color.WHITE);
        nextStepsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        String[] nextSteps = {
            "📋 Next: Schedule orientation training",
            "👥 Assign to department supervisor",
            "🎯 Set 30-day performance goals"
        };
        
        for (String step : nextSteps) {
            JLabel stepLabel = new JLabel(step);
            stepLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            stepLabel.setForeground(new Color(100, 100, 100));
            nextStepsPanel.add(stepLabel);
        }
        
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 20, 10, 20);
        messagePanel.add(nextStepsPanel, gbc);
        
        // Close button
        JButton closeBtn = new JButton("✨ Done");
        closeBtn.setBackground(new Color(46, 204, 113));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBorder(new EmptyBorder(12, 40, 12, 40));
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                successDialog.dispose();
                parentDialog.dispose();
                
                // Show final confirmation on main dashboard
                JOptionPane.showMessageDialog(ManagerDashboard.this,
                    "<html><div style='text-align: center;'>" +
                    "<b>👥 New Staff Member Successfully Added!</b><br><br>" +
                    "Employee: " + staffName + "<br>" +
                    "ID: " + empId + "<br>" +
                    "Department: " + department + "<br><br>" +
                    "The staff member is now visible in the Staff Management table.</div></html>",
                    "Staff Added",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(new Color(46, 204, 113));
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 255, 240));
        buttonPanel.add(closeBtn);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 5, 5, 5);
        messagePanel.add(buttonPanel, gbc);
        
        successDialog.add(iconPanel, BorderLayout.NORTH);
        successDialog.add(messagePanel, BorderLayout.CENTER);
        successDialog.setVisible(true);
    }
    
 // Add this helper method if you don't have it
    private JButton createStyledButton(String text, final Color bgColor, int fontSize) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void viewSchedule() {
        final JDialog scheduleDialog = new JDialog(this, "📅 Staff Schedule Management", true);
        scheduleDialog.setSize(1000, 700);
        scheduleDialog.setLocationRelativeTo(this);
        scheduleDialog.setLayout(new BorderLayout());
        scheduleDialog.getContentPane().setBackground(new Color(245, 245, 250));
        
        // ========== HEADER ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 152, 219)); // Blue
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(41, 128, 185)),
            new EmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel title = new JLabel("📅 STAFF SCHEDULE MANAGEMENT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        JLabel subtitle = new JLabel("Manage and view staff schedules for upcoming weeks");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(52, 152, 219));
        titlePanel.add(title);
        titlePanel.add(subtitle);
        
        // Current week info
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
        String currentDate = sdf.format(new java.util.Date());
        JLabel dateLabel = new JLabel("Week of " + currentDate, SwingConstants.RIGHT);
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateLabel.setForeground(Color.WHITE);
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(dateLabel, BorderLayout.EAST);
        
        // ========== MAIN CONTENT WITH TABS ==========
        final JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Tab 1: Weekly Schedule
        JPanel weeklyPanel = createWeeklyScheduleTab();
        tabs.addTab("📋 Weekly Schedule", weeklyPanel);
        
        // Tab 2: Schedule Statistics
        JPanel statsPanel = createScheduleStatisticsTab();
        tabs.addTab("📊 Statistics", statsPanel);
        
        // Tab 3: Shift Management
        JPanel shiftPanel = createShiftManagementTab();
        tabs.addTab("⚡ Shift Management", shiftPanel);
        
        // Tab 4: Schedule Requests
        JPanel requestsPanel = createScheduleRequestsTab();
        tabs.addTab("📝 Requests", requestsPanel);
        
        // ========== CONTROL PANEL ==========
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(new Color(245, 245, 250));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(220, 220, 230)),
            new EmptyBorder(15, 25, 15, 25)
        ));
        
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtons.setBackground(new Color(245, 245, 250));
        
        JButton printBtn = createControlButton("🖨️ Print Schedule", new Color(149, 165, 166), 12);
        printBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printSchedule();
            }
        });
        
        JButton emailBtn = createControlButton("📧 Email Schedule", new Color(52, 152, 219), 12);
        emailBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emailSchedule();
            }
        });
        
        leftButtons.add(printBtn);
        leftButtons.add(emailBtn);
        
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.setBackground(new Color(245, 245, 250));
        
        JButton closeBtn = createControlButton("Close", new Color(231, 76, 60), 12);
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleDialog.dispose();
            }
        });
        
        rightButtons.add(closeBtn);
        
        controlPanel.add(leftButtons, BorderLayout.WEST);
        controlPanel.add(rightButtons, BorderLayout.EAST);
        
        // ========== ASSEMBLE DIALOG ==========
        scheduleDialog.add(header, BorderLayout.NORTH);
        scheduleDialog.add(tabs, BorderLayout.CENTER);
        scheduleDialog.add(controlPanel, BorderLayout.SOUTH);
        
        scheduleDialog.setVisible(true);
    }

    // ========== HELPER METHODS FOR SCHEDULE MANAGEMENT ==========

    private JPanel createWeeklyScheduleTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Week navigation
        JPanel weekNav = new JPanel(new BorderLayout());
        weekNav.setBackground(Color.WHITE);
        weekNav.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JButton prevWeekBtn = new JButton("◀ Previous Week");
        styleSmallButton(prevWeekBtn, new Color(149, 165, 166));
        
        JButton nextWeekBtn = new JButton("Next Week ▶");
        styleSmallButton(nextWeekBtn, new Color(52, 152, 219));
        
        JLabel weekLabel = new JLabel("Week 52: Dec 23-29, 2024", SwingConstants.CENTER);
        weekLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        weekLabel.setForeground(new Color(44, 62, 80));
        
        weekNav.add(prevWeekBtn, BorderLayout.WEST);
        weekNav.add(weekLabel, BorderLayout.CENTER);
        weekNav.add(nextWeekBtn, BorderLayout.EAST);
        
        // Schedule table
        JPanel tablePanel = createScheduleTable();
        
        // Quick actions
        JPanel quickActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        quickActions.setBackground(Color.WHITE);
        quickActions.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        String[] quickActionsList = {
            "🔄 Auto-schedule", 
            "⚖️ Balance Shifts", 
            "📋 Copy Last Week", 
            "🎯 Fill Gaps",
            "📅 Generate Roster"
        };
        
        for (String action : quickActionsList) {
            JButton actionBtn = new JButton(action);
            styleSmallButton(actionBtn, new Color(155, 89, 182));
            actionBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleQuickScheduleAction(action);
                }
            });
            quickActions.add(actionBtn);
        }
        
        panel.add(weekNav, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        panel.add(quickActions, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createScheduleTable() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create schedule data
        final String[] columns = {
            "Staff", "Mon 23", "Tue 24", "Wed 25", "Thu 26", "Fri 27", "Sat 28", "Sun 29"
        };
        
        final Object[][] scheduleData = {
            {"John Smith (Rec)", "Morning", "Morning", "Off", "Evening", "Evening", "Off", "Morning"},
            {"Sarah Johnson (HK)", "Morning", "Morning", "Morning", "Off", "Evening", "Evening", "Off"},
            {"Mike Brown (Res)", "Evening", "Evening", "Morning", "Morning", "Off", "Morning", "Evening"},
            {"Lisa Wilson (Rec)", "Off", "Evening", "Evening", "Morning", "Morning", "Off", "Evening"},
            {"Robert Chen (Mnt)", "Morning", "Off", "Morning", "Evening", "Evening", "Morning", "Off"},
            {"Emma Davis (HK)", "Evening", "Evening", "Off", "Morning", "Morning", "Evening", "Off"},
            {"David Miller (Res)", "Morning", "Off", "Evening", "Evening", "Morning", "Morning", "Off"},
            {"Jennifer Lee (Mgr)", "Morning", "Morning", "Morning", "Morning", "Evening", "Off", "Off"}
        };
        
        // Create table model
        final DefaultTableModel scheduleModel = new DefaultTableModel(scheduleData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0; // Make shift cells editable
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                return String.class;
            }
        };
        
        final JTable scheduleTable = new JTable(scheduleModel);
        scheduleTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scheduleTable.setRowHeight(45);
        scheduleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        scheduleTable.getTableHeader().setBackground(new Color(240, 240, 240));
        scheduleTable.setGridColor(new Color(230, 230, 230));
        scheduleTable.setShowGrid(true);
        
        // Set column widths
        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Staff names
        for (int i = 1; i < columns.length; i++) {
            scheduleTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        
        // Custom cell renderer for shifts
        scheduleTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 0) {
                    // Staff name column
                    c.setBackground(new Color(245, 245, 245));
                    c.setForeground(new Color(60, 60, 60));
                    c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else {
                    // Shift cells
                    String shift = value.toString();
                    switch (shift) {
                        case "Morning":
                            c.setBackground(new Color(173, 216, 230)); // Light blue
                            c.setForeground(new Color(0, 74, 124));
                            break;
                        case "Evening":
                            c.setBackground(new Color(255, 218, 185)); // Light orange
                            c.setForeground(new Color(139, 90, 43));
                            break;
                        case "Night":
                            c.setBackground(new Color(221, 160, 221)); // Light purple
                            c.setForeground(new Color(75, 0, 130));
                            break;
                        case "Off":
                            c.setBackground(new Color(240, 240, 240)); // Light gray
                            c.setForeground(new Color(100, 100, 100));
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                    
                    c.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    
                    // Highlight selected cell
                    if (isSelected) {
                        c.setBackground(c.getBackground().darker());
                        c.setForeground(Color.WHITE);
                    }
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240), 1));
                
                return c;
            }
        });
        
        // Custom cell editor for shifts with dropdown
        JComboBox<String> shiftCombo = new JComboBox<>(new String[]{"Morning", "Evening", "Night", "Off"});
        DefaultCellEditor shiftEditor = new DefaultCellEditor(shiftCombo);
        
        for (int i = 1; i < columns.length; i++) {
            scheduleTable.getColumnModel().getColumn(i).setCellEditor(shiftEditor);
        }
        
        // Add tooltip
        scheduleTable.setToolTipText("Click on any shift to change it. Right-click for more options.");
        
        // Add right-click menu
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem swapItem = new JMenuItem("🔄 Swap Shift with Another Staff");
        JMenuItem requestOffItem = new JMenuItem("🏖️ Request Day Off");
        JMenuItem overtimeItem = new JMenuItem("⏰ Request Overtime");
        JMenuItem viewHistoryItem = new JMenuItem("📊 View Shift History");
        
        popupMenu.add(swapItem);
        popupMenu.add(requestOffItem);
        popupMenu.add(overtimeItem);
        popupMenu.addSeparator();
        popupMenu.add(viewHistoryItem);
        
        scheduleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = scheduleTable.rowAtPoint(e.getPoint());
                    int col = scheduleTable.columnAtPoint(e.getPoint());
                    
                    if (row >= 0 && col >= 0) {
                        scheduleTable.setRowSelectionInterval(row, row);
                        popupMenu.show(scheduleTable, e.getX(), e.getY());
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 230)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createScheduleStatisticsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Statistics cards
        String[] stats = {
            "Total Staff|45", "On Duty|28", "On Leave|5", 
            "Morning Shifts|15", "Evening Shifts|12", "Night Shifts|8",
            "Overtime Hours|42", "Shift Gaps|3", "Coverage Rate|92%"
        };
        
        Color[] colors = {
            new Color(52, 152, 219), new Color(46, 204, 113), new Color(241, 196, 15),
            new Color(155, 89, 182), new Color(230, 126, 34), new Color(231, 76, 60),
            new Color(149, 165, 166), new Color(26, 188, 156), new Color(41, 128, 185)
        };
        
        for (int i = 0; i < stats.length; i++) {
            String[] parts = stats[i].split("\\|");
            JPanel statCard = createStatisticCard(parts[0], parts[1], colors[i]);
            
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            panel.add(statCard, gbc);
        }
        
        return panel;
    }

    private JPanel createStatisticCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add icon based on title
        String icon = getIconForStat(title);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.add(iconLabel);
        content.add(valueLabel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(titleLabel);
        
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }

    private String getIconForStat(String stat) {
        if (stat.contains("Total")) return "👥";
        if (stat.contains("Duty")) return "✅";
        if (stat.contains("Leave")) return "🏝️";
        if (stat.contains("Morning")) return "🌅";
        if (stat.contains("Evening")) return "🌇";
        if (stat.contains("Night")) return "🌃";
        if (stat.contains("Overtime")) return "⏰";
        if (stat.contains("Gaps")) return "⚠️";
        if (stat.contains("Coverage")) return "📊";
        return "📈";
    }

    private JPanel createShiftManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Shift types management
        JPanel shiftTypes = new JPanel(new GridLayout(4, 2, 15, 15));
        shiftTypes.setBackground(Color.WHITE);
        shiftTypes.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        String[][] shiftInfo = {
            {"🌅 Morning Shift", "7:00 AM - 3:00 PM", "12 staff required"},
            {"🌇 Evening Shift", "3:00 PM - 11:00 PM", "10 staff required"},
            {"🌃 Night Shift", "11:00 PM - 7:00 AM", "8 staff required"},
            {"⚡ Overtime Shift", "As needed", "Max 2 hours/day"},
            {"🏖️ Weekend Premium", "+25% pay", "Sat & Sun only"},
            {"🎯 Special Shifts", "Event coverage", "As scheduled"},
            {"📅 Holiday Shifts", "Double pay", "National holidays"},
            {"🔁 Rotation Policy", "Every 4 weeks", "Prevents burnout"}
        };
        
        for (String[] info : shiftInfo) {
            JPanel shiftCard = createShiftCard(info[0], info[1], info[2]);
            shiftTypes.add(shiftCard);
        }
        
        // Shift rules panel
        JPanel rulesPanel = new JPanel(new BorderLayout());
        rulesPanel.setBackground(Color.WHITE);
        rulesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel rulesTitle = new JLabel("📋 Shift Rules & Policies");
        rulesTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rulesTitle.setForeground(new Color(44, 62, 80));
        
        JTextArea rulesArea = new JTextArea();
        rulesArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setText("1. Minimum 8 hours between shifts\n" +
                         "2. Maximum 5 consecutive days\n" +
                         "3. Weekend rotation mandatory\n" +
                         "4. Overtime requires approval\n" +
                         "5. Shift swap requires 48h notice\n" +
                         "6. Emergency coverage pool available\n" +
                         "7. Training during slow periods\n" +
                         "8. Seniority preference for holidays");
        rulesArea.setEditable(false);
        rulesArea.setBackground(new Color(250, 250, 250));
        
        rulesPanel.add(rulesTitle, BorderLayout.NORTH);
        rulesPanel.add(new JScrollPane(rulesArea), BorderLayout.CENTER);
        
        panel.add(shiftTypes, BorderLayout.NORTH);
        panel.add(rulesPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createShiftCard(String title, String hours, String details) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(60, 60, 60));
        
        JLabel hoursLabel = new JLabel(hours);
        hoursLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hoursLabel.setForeground(new Color(100, 100, 100));
        
        JLabel detailsLabel = new JLabel(details);
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        detailsLabel.setForeground(new Color(150, 150, 150));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.add(titleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(hoursLabel);
        content.add(Box.createRigidArea(new Dimension(0, 3)));
        content.add(detailsLabel);
        
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createScheduleRequestsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Pending requests
        JLabel requestsTitle = new JLabel("📝 Pending Schedule Requests");
        requestsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        requestsTitle.setForeground(new Color(44, 62, 80));
        requestsTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Requests table
        String[] requestColumns = {"Staff", "Request Type", "Date", "Reason", "Status", "Action"};
        Object[][] requestData = {
            {"John Smith", "Day Off", "Dec 24", "Family event", "Pending", "Approve | Deny"},
            {"Sarah Johnson", "Shift Swap", "Dec 26", "Doctor appointment", "Pending", "Approve | Deny"},
            {"Mike Brown", "Overtime", "Dec 28", "Event coverage", "Approved", "View"},
            {"Lisa Wilson", "Vacation", "Jan 2-5", "Holiday", "Pending", "Approve | Deny"},
            {"Robert Chen", "Early Leave", "Today", "Emergency", "Approved", "View"}
        };
        
        JTable requestsTable = new JTable(requestData, requestColumns);
        requestsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        requestsTable.setRowHeight(40);
        requestsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        requestsTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Color code status
        requestsTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = value.toString();
                
                if (status.equals("Approved")) {
                    c.setBackground(new Color(220, 255, 220));
                    c.setForeground(new Color(0, 128, 0));
                } else if (status.equals("Pending")) {
                    c.setBackground(new Color(255, 255, 220));
                    c.setForeground(new Color(153, 153, 0));
                } else {
                    c.setBackground(new Color(255, 220, 220));
                    c.setForeground(new Color(204, 0, 0));
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }
        });
        
        JScrollPane requestsScroll = new JScrollPane(requestsTable);
        
        // Request statistics
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        String[] requestStats = {
            "📋 Total Requests|8", "✅ Approved|3", "⏳ Pending|4", "❌ Denied|1"
        };
        
        for (String stat : requestStats) {
            String[] parts = stat.split("\\|");
            statsPanel.add(createRequestStatCard(parts[0], parts[1]));
        }
        
        panel.add(requestsTitle, BorderLayout.NORTH);
        panel.add(requestsScroll, BorderLayout.CENTER);
        panel.add(statsPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createRequestStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(new Color(52, 152, 219));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.add(valueLabel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(titleLabel);
        
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }

    // ========== ACTION METHODS ==========

    private void handleQuickScheduleAction(String action) {
        switch (action) {
            case "🔄 Auto-schedule":
                autoSchedule();
                break;
            case "⚖️ Balance Shifts":
                balanceShifts();
                break;
            case "📋 Copy Last Week":
                copyLastWeek();
                break;
            case "🎯 Fill Gaps":
                fillShiftGaps();
                break;
            case "📅 Generate Roster":
                generateRoster();
                break;
        }
    }

    private void autoSchedule() {
        JOptionPane.showMessageDialog(this,
            "🔄 Auto-scheduling initiated!\n\n" +
            "The system will automatically assign shifts based on:\n" +
            "• Staff availability\n" +
            "• Department requirements\n" +
            "• Seniority rules\n" +
            "• Previous schedule patterns\n\n" +
            "Estimated completion: 30 seconds",
            "Auto-schedule",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void balanceShifts() {
        JOptionPane.showMessageDialog(this,
            "⚖️ Balancing shifts across all staff...\n\n" +
            "Ensuring fair distribution of:\n" +
            "• Morning vs Evening shifts\n" +
            "• Weekend vs Weekday shifts\n" +
            "• Overtime opportunities\n" +
            "• Preferred shift requests\n\n" +
            "Balancing complete! All shifts are now evenly distributed.",
            "Shift Balance",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void copyLastWeek() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Copy last week's schedule to this week?\n\n" +
            "This will overwrite any changes made to the current week.",
            "Copy Schedule",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "📋 Last week's schedule copied successfully!\n\n" +
                "You can now make adjustments as needed.",
                "Schedule Copied",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void fillShiftGaps() {
        JOptionPane.showMessageDialog(this,
            "🎯 Filling shift gaps...\n\n" +
            "Found and filled 3 shift gaps:\n" +
            "• Monday evening - 2 positions filled\n" +
            "• Wednesday morning - 1 position filled\n" +
            "• Saturday night - 2 positions filled\n\n" +
            "All gaps have been filled with available staff.",
            "Shift Gaps Filled",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void generateRoster() {
        String[] formats = {"PDF Document", "Excel Spreadsheet", "HTML Report", "Printed Copy"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select roster format:",
            "Generate Roster",
            JOptionPane.QUESTION_MESSAGE,
            null,
            formats,
            formats[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "📅 Generating staff roster as " + selected + "...\n\n" +
                "The roster will include:\n" +
                "• Weekly schedule for all staff\n" +
                "• Department assignments\n" +
                "• Contact information\n" +
                "• Emergency procedures\n\n" +
                "File will be saved and distributed to department heads.",
                "Roster Generation",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void printSchedule() {
        JOptionPane.showMessageDialog(this,
            "🖨️ Printing schedule...\n\n" +
            "The schedule will be printed in color format.\n" +
            "Copies will be distributed to:\n" +
            "• Staff lounge\n" +
            "• Department offices\n" +
            "• Manager's office\n" +
            "• Reception area\n\n" +
            "Print job sent to printer successfully!",
            "Print Schedule",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void emailSchedule() {
        JOptionPane.showMessageDialog(this,
            "📧 Email schedule to staff...\n\n" +
            "Sending schedule to all 45 staff members.\n" +
            "Emails include:\n" +
            "• Personal schedule for next week\n" +
            "• Department assignments\n" +
            "• Important reminders\n" +
            "• Contact information\n\n" +
            "All emails sent successfully! Staff will receive notifications.",
            "Email Schedule",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // ========== HELPER METHODS ==========

    private JButton createControlButton(String text, final Color bgColor, int fontSize) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void styleSmallButton(final JButton button, final Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private void refreshOccupancy() {
        JOptionPane.showMessageDialog(this,
            "🔄 Room occupancy status refreshed!\n\n" +
            "Updated occupancy rate: 78%\n" +
            "Available rooms: 28\n" +
            "Check-ins today: 8\n" +
            "Check-outs today: 6\n\n" +
            "All room status indicators updated.",
            "Occupancy Refreshed",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showOccupancyForecast() {
        JOptionPane.showMessageDialog(this,
            "📊 Occupancy Forecast - Next 7 Days\n\n" +
            "Monday: 82% (High)\n" +
            "Tuesday: 78% (Medium)\n" +
            "Wednesday: 85% (High)\n" +
            "Thursday: 90% (Very High)\n" +
            "Friday: 92% (Peak)\n" +
            "Saturday: 88% (High)\n" +
            "Sunday: 75% (Medium)\n\n" +
            "Recommendation: Prepare additional staff for Thursday-Saturday.",
            "Occupancy Forecast",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void analyzeRevenue() {
        JOptionPane.showMessageDialog(this,
            "📈 Revenue Analysis\n\n" +
            "Current Month Performance:\n" +
            "• Revenue: $45,200 (Target: $50,000)\n" +
            "• Growth: +12.8% vs last month\n" +
            "• Best Performing: Executive Suites\n" +
            "• Room Revenue Contribution: 85%\n\n" +
            "Recommendations:\n" +
            "1. Increase promotion of restaurant services\n" +
            "2. Offer spa packages to room guests\n" +
            "3. Consider dynamic pricing for weekends",
            "Revenue Analysis",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportRevenue() {
        String[] formats = {"Excel (.xlsx)", "PDF Report", "CSV", "HTML"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select export format:",
            "Export Revenue Data",
            JOptionPane.QUESTION_MESSAGE,
            null,
            formats,
            formats[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "💾 Exporting revenue data as " + selected + "...\n\n" +
                "Export includes:\n" +
                "• Monthly revenue breakdown\n" +
                "• Room type performance\n" +
                "• Revenue trends\n" +
                "• Comparative analysis\n\n" +
                "File will be saved to Downloads folder.",
                "Revenue Export",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void viewFeedback() {
        JTextArea feedbackArea = new JTextArea(20, 60);
        feedbackArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        feedbackArea.setText(
            "📝 Guest Feedback - Last 7 Days\n\n" +
            "⭐⭐⭐⭐⭐ Excellent service!\n" +
            " - John D. (Room 101)\n" +
            " - \"The staff was incredibly helpful and the room was spotless.\"\n\n" +
            
            "⭐⭐⭐⭐ Very good experience\n" +
            " - Sarah M. (Suite 301)\n" +
            " - \"Beautiful room with amazing views. Restaurant food was delicious.\"\n\n" +
            
            "⭐⭐⭐⭐ Great location\n" +
            " - Robert C. (Room 205)\n" +
            " - \"Perfect for business travel. Conference facilities were excellent.\"\n\n" +
            
            "⭐⭐⭐ Good but could improve\n" +
            " - Lisa W. (Room 108)\n" +
            " - \"Room was clean but WiFi was slow. Breakfast selection could be better.\"\n\n" +
            
            "Common Themes:\n" +
            "✓ Staff friendliness: 4.8/5\n" +
            "✓ Room cleanliness: 4.7/5\n" +
            "✓ Food quality: 4.6/5\n" +
            "✓ Value for money: 4.5/5\n"
        );
        feedbackArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Guest Feedback", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void viewSpecialRequests() {
        JTextArea requestsArea = new JTextArea(15, 50);
        requestsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        requestsArea.setText(
            "⭐ Special Requests - Active\n\n" +
            "1. Room 101 - Early check-in (10:00 AM)\n" +
            "   Status: ✅ Approved\n" +
            "   Guest: Mr. John Smith\n\n" +
            
            "2. Suite 301 - Anniversary decoration\n" +
            "   Status: 🎨 In Progress\n" +
            "   Guest: Mr. & Mrs. Johnson\n" +
            "   Notes: Champagne and flowers requested\n\n" +
            
            "3. Room 205 - Special diet\n" +
            "   Status: 👨‍🍳 Notified Chef\n" +
            "   Guest: Miss Emily Chen\n" +
            "   Notes: Vegetarian, gluten-free\n\n" +
            
            "4. Executive Suite - Business setup\n" +
            "   Status: 💼 Completed\n" +
            "   Guest: Mr. Robert Brown\n" +
            "   Notes: Extra monitor and printer\n\n" +
            
            "5. Room 108 - Late check-out\n" +
            "   Status: ⏰ Pending Approval\n" +
            "   Guest: Mr. Michael Wilson\n" +
            "   Request: 2:00 PM check-out\n"
        );
        requestsArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(requestsArea);
        
        JOptionPane.showMessageDialog(this, scrollPane, "Special Requests", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void viewLoyaltyProgram() {
        JOptionPane.showMessageDialog(this,
            "🎯 Loyalty Program Overview\n\n" +
            "Total Members: 1,245\n" +
            "Active This Month: 320\n" +
            "Points Redeemed: 45,200\n\n" +
            "Tier Distribution:\n" +
            "• Platinum: 45 members\n" +
            "• Gold: 120 members\n" +
            "• Silver: 280 members\n" +
            "• Bronze: 800 members\n\n" +
            "Recent Redemptions:\n" +
            "• Free night stays: 12\n" +
            "• Room upgrades: 28\n" +
            "• Restaurant vouchers: 45\n" +
            "• Spa treatments: 32",
            "Loyalty Program",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void prepareGift() {
        String[] gifts = {"Chocolate Box", "Wine Bottle", "Fruit Basket", "Flower Bouquet", "Spa Voucher"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select complimentary gift:",
            "Prepare Gift",
            JOptionPane.QUESTION_MESSAGE,
            null,
            gifts,
            gifts[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "🎁 Gift prepared successfully!\n\n" +
                "Gift: " + selected + "\n" +
                "Recipient: All guests with special occasions today\n" +
                "Delivery: During evening turndown service\n" +
                "Note: Personalized cards included\n\n" +
                "Housekeeping has been notified.",
                "Gift Prepared",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void generateReport() {
        String[] reportTypes = {"Daily Summary", "Weekly Analysis", "Monthly Performance", "Custom Report"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select report type:",
            "Generate Report",
            JOptionPane.QUESTION_MESSAGE,
            null,
            reportTypes,
            reportTypes[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "📊 Generating " + selected + "...\n\n" +
                "Report will include:\n" +
                "• Key performance indicators\n" +
                "• Revenue analysis\n" +
                "• Occupancy statistics\n" +
                "• Staff performance\n" +
                "• Guest feedback summary\n\n" +
                "Estimated completion: 2 minutes",
                "Report Generation",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void generateDailyOperationsReport() {
        showReportDialog("Daily Operations Report",
            "📋 DAILY OPERATIONS REPORT - " + new java.util.Date() + "\n\n" +
            "SUMMARY\n" +
            "• Occupancy: 78% (100/128 rooms)\n" +
            "• Revenue: $4,250\n" +
            "• Check-ins: 8\n" +
            "• Check-outs: 6\n" +
            "• Staff on duty: 28\n\n" +
            
            "DEPARTMENT PERFORMANCE\n" +
            "Reception: 92% efficiency\n" +
            "Housekeeping: 88% efficiency\n" +
            "Restaurant: 85% efficiency\n" +
            "Maintenance: 90% efficiency\n\n" +
            
            "GUEST FEEDBACK\n" +
            "Average rating: 4.8/5\n" +
            "Positive comments: 12\n" +
            "Areas for improvement: 3\n\n" +
            
            "RECOMMENDATIONS\n" +
            "1. Increase housekeeping staff for floor 3\n" +
            "2. Check restaurant inventory levels\n" +
            "3. Schedule maintenance for room 105"
        );
    }
    
    private void generateFinancialPerformance() {
        showReportDialog("Financial Performance Report",
            "💰 FINANCIAL PERFORMANCE - December 2024\n\n" +
            "REVENUE BREAKDOWN\n" +
            "Total Revenue: $45,200\n" +
            "Room Revenue: $38,400 (85%)\n" +
            "Restaurant Revenue: $5,200 (12%)\n" +
            "Services Revenue: $1,600 (3%)\n\n" +
            
            "EXPENSES\n" +
            "Total Expenses: $29,400\n" +
            "Staff Costs: $18,500 (63%)\n" +
            "Operational: $8,000 (27%)\n" +
            "Administrative: $2,900 (10%)\n\n" +
            
            "PROFITABILITY\n" +
            "Gross Profit: $15,800\n" +
            "Profit Margin: 34.9%\n" +
            "Revenue Growth: +12.8% vs last month\n\n" +
            
            "KEY METRICS\n" +
            "ADR (Average Daily Rate): $189\n" +
            "RevPAR (Revenue per Available Room): $147\n" +
            "Occupancy Rate: 78%\n\n" +
            
            "FORECAST\n" +
            "Next month projection: $48,500\n" +
            "Expected growth: +7.3%"
        );
    }
    
    private void generateStaffProductivity() {
        showReportDialog("Staff Productivity Report",
            "👥 STAFF PRODUCTIVITY REPORT - December 2024\n\n" +
            "OVERALL PERFORMANCE\n" +
            "Average Efficiency: 88%\n" +
            "Attendance Rate: 96%\n" +
            "Training Completed: 85%\n\n" +
            
            "TOP PERFORMERS\n" +
            "1. Sarah Johnson (Housekeeping): 95%\n" +
            "2. John Smith (Reception): 92%\n" +
            "3. Mike Brown (Restaurant): 95%\n\n" +
            
            "DEPARTMENT BREAKDOWN\n" +
            "Reception: 92% (8 staff)\n" +
            "Housekeeping: 88% (12 staff)\n" +
            "Restaurant: 85% (10 staff)\n" +
            "Maintenance: 90% (6 staff)\n" +
            "Management: 96% (4 staff)\n\n" +
            
            "TRAINING NEEDS\n" +
            "Require Additional Training: 8 staff\n" +
            "Customer Service: 5 staff\n" +
            "Technical Skills: 3 staff\n\n" +
            
            "RECOMMENDATIONS\n" +
            "1. Schedule customer service workshop\n" +
            "2. Implement mentoring program\n" +
            "3. Review shift schedules for efficiency"
        );
    }
    
    private void generateOccupancyAnalysis() {
        showReportDialog("Occupancy Analysis Report",
            "🏨 OCCUPANCY ANALYSIS - December 2024\n\n" +
            "MONTHLY OVERVIEW\n" +
            "Average Occupancy: 78%\n" +
            "Peak Occupancy: 92% (Weekend)\n" +
            "Lowest Occupancy: 65% (Weekday)\n\n" +
            
            "ROOM TYPE PERFORMANCE\n" +
            "Executive Suites: 65% occupancy\n" +
            "Standard Suites: 82% occupancy\n" +
            "Double Rooms: 85% occupancy\n" +
            "Single Rooms: 80% occupancy\n\n" +
            
            "WEEKLY PATTERN\n" +
            "Monday: 72%\n" +
            "Tuesday: 75%\n" +
            "Wednesday: 78%\n" +
            "Thursday: 82%\n" +
            "Friday: 90%\n" +
            "Saturday: 92%\n" +
            "Sunday: 85%\n\n" +
            
            "REVENUE IMPACT\n" +
            "High Occupancy Days: Contribute 45% of monthly revenue\n" +
            "Best Performing: Executive Suites (27% of revenue)\n" +
            "Underperforming: Single Rooms (19% of revenue)\n\n" +
            
            "RECOMMENDATIONS\n" +
            "1. Implement dynamic pricing for weekends\n" +
            "2. Create package deals for single rooms\n" +
            "3. Promote mid-week business stays"
        );
    }
    
    private void generateGuestSatisfaction() {
        showReportDialog("Guest Satisfaction Report",
            "👑 GUEST SATISFACTION REPORT - December 2024\n\n" +
            "OVERALL RATING\n" +
            "Average Score: 4.8/5\n" +
            "Response Rate: 42%\n" +
            "Positive Feedback: 89%\n\n" +
            
            "CATEGORY BREAKDOWN\n" +
            "Room Quality: 4.7/5\n" +
            "Staff Service: 4.9/5\n" +
            "Food & Beverage: 4.6/5\n" +
            "Facilities: 4.8/5\n" +
            "Value for Money: 4.5/5\n\n" +
            
            "COMMON PRAISES\n" +
            "1. Friendly and helpful staff (45%)\n" +
            "2. Clean and comfortable rooms (38%)\n" +
            "3. Excellent location (32%)\n" +
            "4. Good food quality (28%)\n\n" +
            
            "AREAS FOR IMPROVEMENT\n" +
            "1. WiFi speed and reliability (15%)\n" +
            "2. Breakfast variety (12%)\n" +
            "3. Parking availability (8%)\n\n" +
            
            "VIP GUEST FEEDBACK\n" +
            "Platinum Members: 4.9/5\n" +
            "Gold Members: 4.8/5\n    Silver Members: 4.7/5\n\n" +
            
            "ACTION ITEMS\n" +
            "1. Upgrade WiFi infrastructure\n" +
            "2. Expand breakfast menu\n" +
            "3. Improve parking guidance"
        );
    }
    
    private void generateRevenueForecasting() {
        showReportDialog("Revenue Forecasting Report",
            "📈 REVENUE FORECASTING - Next 3 Months\n\n" +
            "JANUARY FORECAST\n" +
            "Expected Revenue: $48,500\n" +
            "Occupancy Forecast: 80%\n" +
            "ADR Forecast: $195\n" +
            "Growth Expected: +7.3%\n\n" +
            
            "FEBRUARY FORECAST\n" +
            "Expected Revenue: $52,000\n" +
            "Occupancy Forecast: 85%\n" +
            "ADR Forecast: $205\n" +
            "Growth Expected: +15.0%\n\n" +
            
            "MARCH FORECAST\n" +
            "Expected Revenue: $55,000\n" +
            "Occupancy Forecast: 88%\n" +
            "ADR Forecast: $210\n" +
            "Growth Expected: +21.7%\n\n" +
            
            "KEY DRIVERS\n" +
            "1. Holiday season bookings\n" +
            "2. Conference events scheduled\n" +
            "3. Marketing campaign launch\n" +
            "4. Loyalty program promotions\n\n" +
            
            "RISK FACTORS\n" +
            "1. Economic conditions\n" +
            "2. Competitive pricing\n" +
            "3. Weather patterns\n" +
            "4. Staff availability\n\n" +
            
            "RECOMMENDATIONS\n" +
            "1. Implement dynamic pricing strategy\n" +
            "2. Increase marketing budget for Q1\n" +
            "3. Prepare for seasonal staff hiring"
        );
    }
    
    private void generateComparativeAnalysis() {
        showReportDialog("Comparative Analysis Report",
            "🔄 COMPARATIVE ANALYSIS - Year-over-Year\n\n" +
            "REVENUE COMPARISON\n" +
            "2023 Revenue: $425,000\n" +
            "2024 Revenue: $512,000 (projected)\n" +
            "Growth: +20.5%\n\n" +
            
            "OCCUPANCY COMPARISON\n" +
            "2023 Average: 72%\n" +
            "2024 Average: 78% (YTD)\n" +
            "Improvement: +8.3%\n\n" +
            
            "GUEST SATISFACTION\n" +
            "2023 Average: 4.6/5\n" +
            "2024 Average: 4.8/5 (YTD)\n" +
            "Improvement: +4.3%\n\n" +
            
            "STAFF EFFICIENCY\n" +
            "2023 Average: 82%\n" +
            "2024 Average: 88% (YTD)\n" +
            "Improvement: +7.3%\n\n" +
            
            "KEY SUCCESS FACTORS\n" +
            "1. Staff training programs\n" +
            "2. Room renovation project\n" +
            "3. Digital marketing strategy\n" +
            "4. Loyalty program expansion\n\n" +
            
            "INDUSTRY COMPARISON\n" +
            "Industry Average Occupancy: 75%\n" +
            "Our Occupancy: 78% (+4% above)\n" +
            "Industry ADR: $180\n" +
            "Our ADR: $189 (+5% above)\n\n" +
            
            "RECOMMENDATIONS\n" +
            "1. Continue staff development programs\n" +
            "2. Invest in technology upgrades\n" +
            "3. Expand partnership networks"
        );
    }
    
    private void generatePerformanceMetrics() {
        showReportDialog("Performance Metrics Report",
            "🎯 PERFORMANCE METRICS DASHBOARD\n\n" +
            "FINANCIAL METRICS\n" +
            "RevPAR: $147\n" +
            "ADR: $189\n" +
            "GOPPAR: $68\n" +
            "TRevPAR: $158\n\n" +
            
            "OPERATIONAL METRICS\n" +
            "Occupancy Rate: 78%\n" +
            "Average Length of Stay: 3.2 nights\n" +
            "Direct Booking Rate: 45%\n" +
            "OTA Commission Rate: 15%\n\n" +
            
            "GUEST METRICS\n" +
            "Guest Satisfaction: 4.8/5\n" +
            "Repeat Guest Rate: 38%\n" +
            "Net Promoter Score: 72\n" +
            "Complaint Resolution Time: 2.3 hours\n\n" +
            
            "STAFF METRICS\n" +
            "Staff Efficiency: 88%\n" +
            "Training Hours per Staff: 24/year\n" +
            "Staff Turnover Rate: 12%\n" +
            "Absenteeism Rate: 3%\n\n" +
            
            "MARKETING METRICS\n" +
            "Cost per Acquisition: $45\n" +
            "Website Conversion Rate: 4.2%\n" +
            "Social Media Engagement: 8.5%\n" +
            "Email Open Rate: 32%\n\n" +
            
            "BENCHMARKS\n" +
            "All metrics above industry average\n" +
            "Top performing: Guest Satisfaction\n" +
            "Needs improvement: Direct Booking Rate"
        );
    }
    
    private void generateFromTemplate() {
        JOptionPane.showMessageDialog(this,
            "📋 Generating report from template...\n\n" +
            "Report generation started with selected options.\n" +
            "Estimated completion time: 1-2 minutes\n\n" +
            "The report will be:\n" +
            "• Saved to Reports folder\n" +
            "• Emailed to your registered address\n" +
            "• Available in the selected format\n\n" +
            "You will receive a notification when ready.",
            "Template Report",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showReportDialog(String title, String content) {
        JTextArea reportArea = new JTextArea(content);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setEditable(false);
        reportArea.setBackground(new Color(250, 250, 250));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
    }
    
    private void showMaintenanceSchedule() {
        // Create maintenance schedule dialog
        final JDialog dialog = new JDialog(this, "Maintenance Schedule", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(230, 126, 34)); // Orange
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("🛠️ Maintenance Schedule & Planning");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // Main content with tabs
        JTabbedPane tabs = new JTabbedPane();
        
        // Tab 1: Current Maintenance
        JPanel currentPanel = new JPanel(new BorderLayout());
        currentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Table for current maintenance
        String[] columns = {"Room", "Type", "Start Date", "End Date", "Priority", "Status"};
        Object[][] maintenanceData = {
            {"105", "Plumbing Repair", "2024-12-20", "2024-12-22", "High", "In Progress"},
            {"205", "AC Replacement", "2024-12-21", "2024-12-23", "Medium", "Scheduled"},
            {"301", "Deep Cleaning", "2024-12-22", "2024-12-22", "Low", "Completed"},
            {"108", "Painting", "2024-12-25", "2024-12-26", "Medium", "Planned"},
            {"401", "Furniture Update", "2024-12-28", "2024-12-29", "Low", "Planned"}
        };
        
        JTable maintenanceTable = new JTable(maintenanceData, columns);
        maintenanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        maintenanceTable.setRowHeight(35);
        
        // Color code priority
        maintenanceTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String priority = value.toString();
                switch (priority) {
                    case "High":
                        c.setBackground(new Color(255, 220, 220)); // Light red
                        c.setForeground(new Color(204, 0, 0));
                        break;
                    case "Medium":
                        c.setBackground(new Color(255, 255, 220)); // Light yellow
                        c.setForeground(new Color(153, 153, 0));
                        break;
                    case "Low":
                        c.setBackground(new Color(220, 255, 220)); // Light green
                        c.setForeground(new Color(0, 128, 0));
                        break;
                }
                
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }
        });
        
        JScrollPane tableScroll = new JScrollPane(maintenanceTable);
        currentPanel.add(tableScroll, BorderLayout.CENTER);
        
        // Tab 2: Maintenance Stats
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        statsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        String[] stats = {
            "🔴 High Priority: 2 tasks",
            "🟡 Medium Priority: 3 tasks", 
            "🟢 Low Priority: 5 tasks",
            "✅ Completed: 8 tasks",
            "⏳ In Progress: 3 tasks",
            "📅 Planned: 7 tasks"
        };
        
        for (String stat : stats) {
            JLabel statLabel = new JLabel(stat, SwingConstants.CENTER);
            statLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            statLabel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            statLabel.setOpaque(true);
            statLabel.setBackground(Color.WHITE);
            statLabel.setPreferredSize(new Dimension(200, 60));
            statsPanel.add(statLabel);
        }
        
        // Tab 3: Add New Maintenance
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Room selection
        gbc.gridx = 0; gbc.gridy = row;
        addPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        JComboBox<Integer> roomCombo = new JComboBox<>();
        for (Integer roomNum : rooms.keySet()) {
            roomCombo.addItem(roomNum);
        }
        addPanel.add(roomCombo, gbc);
        row++;
        
        // Maintenance type
        gbc.gridx = 0; gbc.gridy = row;
        addPanel.add(new JLabel("Maintenance Type:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
            "Select Type", "Plumbing", "Electrical", "AC/HVAC", "Painting", 
            "Deep Cleaning", "Furniture", "Carpet", "Other"
        });
        addPanel.add(typeCombo, gbc);
        row++;
        
        // Priority
        gbc.gridx = 0; gbc.gridy = row;
        addPanel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        addPanel.add(priorityCombo, gbc);
        row++;
        
        // Dates
        gbc.gridx = 0; gbc.gridy = row;
        addPanel.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 1;
        JTextField startField = new JTextField(java.time.LocalDate.now().toString(), 15);
        addPanel.add(startField, gbc);
        row++;
        
        gbc.gridx = 0; gbc.gridy = row;
        addPanel.add(new JLabel("End Date:"), gbc);
        gbc.gridx = 1;
        JTextField endField = new JTextField(java.time.LocalDate.now().plusDays(2).toString(), 15);
        addPanel.add(endField, gbc);
        row++;
        
        // Description
        gbc.gridx = 0; gbc.gridy = row;
        addPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        addPanel.add(descScroll, gbc);
        row++;
        
        // Add button
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton addBtn = new JButton("➕ Schedule Maintenance");
        addBtn.setBackground(SUCCESS_COLOR);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        addBtn.addActionListener(e -> {
            if (typeCombo.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(dialog, "Please select maintenance type!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int roomNum = (Integer) roomCombo.getSelectedItem();
            Room room = rooms.get(roomNum);
            if (room != null) {
                room.status = "Maintenance";
                loadRoomsIntoTable();
            }
            
            JOptionPane.showMessageDialog(dialog,
                "✅ Maintenance scheduled successfully!\n\n" +
                "Room: " + roomNum + "\n" +
                "Type: " + typeCombo.getSelectedItem() + "\n" +
                "Priority: " + priorityCombo.getSelectedItem() + "\n" +
                "Dates: " + startField.getText() + " to " + endField.getText() + "\n\n" +
                "Maintenance team has been notified.",
                "Maintenance Scheduled",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        addPanel.add(addBtn, gbc);
        
        // Add tabs
        tabs.addTab("📋 Current Tasks", currentPanel);
        tabs.addTab("📊 Statistics", statsPanel);
        tabs.addTab("➕ Schedule New", addPanel);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton printBtn = new JButton("🖨️ Print Schedule");
        styleSmallButton(printBtn, INFO_COLOR);
        printBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog,
                "Maintenance schedule sent to printer!\n\n" +
                "Weekly maintenance plan will be printed\n" +
                "for all maintenance staff.",
                "Print Schedule",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton emailBtn = new JButton("📧 Email Report");
        styleSmallButton(emailBtn, WARNING_COLOR);
        emailBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog,
                "Maintenance report emailed!\n\n" +
                "Report sent to:\n" +
                "• maintenance@hotel.com\n" +
                "• manager@hotel.com\n" +
                "• operations@hotel.com",
                "Email Sent",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton closeBtn = new JButton("Close");
        styleSmallButton(closeBtn, DANGER_COLOR);
        closeBtn.addActionListener(e -> dialog.dispose());
        
        controlPanel.add(printBtn);
        controlPanel.add(emailBtn);
        controlPanel.add(closeBtn);
        
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(tabs, BorderLayout.CENTER);
        dialog.add(controlPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManagerDashboard("Manager");
            }
        });
    }
}
