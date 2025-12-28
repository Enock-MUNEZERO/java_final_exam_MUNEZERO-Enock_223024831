package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class AdminDashboard extends JFrame implements ActionListener {
    private String username;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    // Table models for data
    private DefaultTableModel userTableModel;
    private DefaultTableModel roomTableModel;
    private DefaultTableModel bookingTableModel;
    private Vector<Vector<Object>> originalBookingData;
    
    // UI Components we need to access
    private JTable userTable;
    private JTable roomTable;
    private JTable bookingTable;
    private JTextField searchUserField;
    private JTextField searchRoomField;
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DARK_BG = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(240, 245, 250);
    private final Color PURPLE_COLOR = new Color(155, 89, 182);
    private final Color TEAL_COLOR = new Color(26, 188, 156);
    
    public AdminDashboard(String username) {
        this.username = username;
        
        setTitle("Admin Dashboard - Hospitality System");
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
    
    private void createTopBar(JPanel mainPanel) {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(DARK_BG);
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));
        topBar.setPreferredSize(new Dimension(getWidth(), 70));
        
        // Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(DARK_BG);
        
        JLabel titleLabel = new JLabel("ADMIN DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        leftPanel.add(titleLabel);
        
        // User info and controls
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(DARK_BG);
        
        JLabel userLabel = new JLabel("Admin: " + username);
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
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        // Admin menu items
        String[] menuItems = {
            "DASHBOARD",
            "USER MANAGEMENT", 
            "ROOM MANAGEMENT",
            "BOOKING MANAGEMENT",
            "FINANCIAL REPORTS",
            "SYSTEM SETTINGS"
        };
        
        for (final String item : menuItems) {
            final JButton menuButton = createMenuButton(item);
            menuButton.setActionCommand(item);
            menuButton.addActionListener(this);
            sidebar.add(menuButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // System status
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(45, 62, 80));
        statusPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel statusLabel = new JLabel("System: ONLINE");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(SUCCESS_COLOR);
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        sidebar.add(statusPanel);
        
        mainPanel.add(sidebar, BorderLayout.WEST);
    }
    
    private JButton createMenuButton(final String text) {
        final JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(65, 85, 110));
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(65, 85, 110));
            }
        });
        
        return button;
    }
    
    private void createContentArea(JPanel mainPanel) {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(LIGHT_BG);
        
        // Add different content panels
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createUserManagementPanel(), "USER MANAGEMENT");
        contentPanel.add(createRoomManagementPanel(), "ROOM MANAGEMENT");
        contentPanel.add(createBookingManagementPanel(), "BOOKING MANAGEMENT");
        contentPanel.add(createFinancialPanel(), "FINANCIAL REPORTS");
        contentPanel.add(createSystemSettingsPanel(), "SYSTEM SETTINGS");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
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
        
        JLabel title = new JLabel("System Overview Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        
        final JButton refreshBtn = new JButton("Refresh Dashboard");
        styleButton(refreshBtn, SECONDARY_COLOR);
        refreshBtn.setActionCommand("REFRESH_DASHBOARD");
        refreshBtn.addActionListener(this);
        
        header.add(title, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);
        
        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        statsPanel.setBackground(LIGHT_BG);
        statsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Create stat cards
        String[] statData = {
            "Total Users|45", "Active Rooms|128", "Today's Revenue|$4,250", "Occupancy Rate|78%",
            "Pending Tasks|12", "System Uptime|99.8%", "Monthly Revenue|$45.2K", "Staff Online|8"
        };
        
        for (String data : statData) {
            String[] parts = data.split("\\|");
            statsPanel.add(createStatCard(parts[0], parts[1]));
        }
        
        // Quick actions
        JPanel quickActions = new JPanel(new GridLayout(1, 4, 15, 15));
        quickActions.setBackground(LIGHT_BG);
        quickActions.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        String[] actions = {"Add New User", "Generate Report", "System Check", "Send Announcement"};
        String[] commands = {"ADD_USER", "GENERATE_REPORT", "SYSTEM_CHECK", "SEND_ANNOUNCEMENT"};
        Color[] colors = {SUCCESS_COLOR, PRIMARY_COLOR, WARNING_COLOR, PURPLE_COLOR};
        
        for (int i = 0; i < actions.length; i++) {
            final JButton actionBtn = new JButton(actions[i]);
            styleButton(actionBtn, colors[i]);
            actionBtn.setActionCommand(commands[i]);
            actionBtn.addActionListener(this);
            quickActions.add(actionBtn);
        }
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(quickActions, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ==================== USER MANAGEMENT PANEL ====================
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header with search and add button
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel title = new JLabel("User Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setBackground(Color.WHITE);
        
        searchUserField = new JTextField(20);
        searchUserField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        final JButton searchBtn = new JButton("Search Users");
        styleButton(searchBtn, SECONDARY_COLOR);
        searchBtn.setActionCommand("SEARCH_USERS");
        searchBtn.addActionListener(this);
        
        final JButton addUserBtn = new JButton("Add New User");
        styleButton(addUserBtn, SUCCESS_COLOR);
        addUserBtn.setActionCommand("ADD_USER");
        addUserBtn.addActionListener(this);
        
        controls.add(new JLabel("Search:"));
        controls.add(searchUserField);
        controls.add(searchBtn);
        controls.add(addUserBtn);
        
        header.add(title, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        
        // User table
        String[] columns = {"ID", "Username", "Full Name", "Role", "Email", "Status", "Actions"};
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only Actions column is editable
            }
        };
        
        // Sample data
        addSampleUsers();
        
        userTable = new JTable(userTableModel);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userTable.setRowHeight(40);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        userTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== ROOM MANAGEMENT PANEL ====================
    private JPanel createRoomManagementPanel() {
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
        
        JLabel title = new JLabel("Room Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setBackground(Color.WHITE);
        
        searchRoomField = new JTextField(20);
        searchRoomField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        final JButton searchBtn = new JButton("Search Rooms");
        styleButton(searchBtn, SECONDARY_COLOR);
        searchBtn.setActionCommand("SEARCH_ROOMS");
        searchBtn.addActionListener(this);
        
        final JButton addRoomBtn = new JButton("Add New Room");
        styleButton(addRoomBtn, SUCCESS_COLOR);
        addRoomBtn.setActionCommand("ADD_ROOM");
        addRoomBtn.addActionListener(this);
        
        controls.add(new JLabel("Search:"));
        controls.add(searchRoomField);
        controls.add(searchBtn);
        controls.add(addRoomBtn);
        
        header.add(title, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        
        // Room table
        String[] columns = {"Room #", "Type", "Price/Night", "Status", "Amenities", "Actions"};
        roomTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only Actions column is editable
            }
        };
        
        // Sample data
        addSampleRooms();
        
        roomTable = new JTable(roomTableModel);
        roomTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomTable.setRowHeight(40);
        roomTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        roomTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== BOOKING MANAGEMENT PANEL ====================
    private JPanel createBookingManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Main header container with two lines
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
        
        JLabel title = new JLabel("📅 Booking Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        titleLine.add(title, BorderLayout.WEST);
        
        // Add status indicator
        JLabel statusLabel = new JLabel("🟢 Active: 12 | 📅 Today: 8 | 💰 Revenue: $4,250");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        titleLine.add(statusLabel, BorderLayout.EAST);
        
        // ====== LINE 2: CONTROLS LINE ======
        JPanel controlsLine = new JPanel(new BorderLayout());
        controlsLine.setBackground(Color.WHITE);
        controlsLine.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Left side: Filters and Search
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftControls.setBackground(Color.WHITE);
        
        // Filter control
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.setBackground(Color.WHITE);
        JLabel filterLabel = new JLabel("🔍 Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterPanel.add(filterLabel);
        
        final JComboBox<String> filterCombo = new JComboBox<>(new String[]{
            "All Bookings", "Today's Check-ins", "Today's Check-outs", 
            "Upcoming", "Active", "Cancelled", "Completed"
        });
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterCombo.setPreferredSize(new Dimension(140, 30));
        
        // Add action listener to filter combo
        filterCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyBookingFilter(filterCombo.getSelectedItem().toString());
            }
        });
        
        filterPanel.add(filterCombo);
        
        // Clear filter button
        final JButton clearFilterBtn = new JButton("Clear");
        styleSmallButton(clearFilterBtn, new Color(149, 165, 166));
        clearFilterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterCombo.setSelectedIndex(0);
                applyBookingFilter("All Bookings");
            }
        });
        filterPanel.add(clearFilterBtn);
        
        // Search control
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setBackground(Color.WHITE);
        JLabel searchLabel = new JLabel("🔎 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchPanel.add(searchLabel);
        
        final JTextField searchBookingField = new JTextField(15);
        searchBookingField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchBookingField.setToolTipText("Search by guest name, room, or booking ID");
        searchBookingField.setPreferredSize(new Dimension(150, 30));
        searchPanel.add(searchBookingField);
        
        final JButton searchBtn = new JButton("Search");
        styleSmallButton(searchBtn, SECONDARY_COLOR);
        searchBtn.setActionCommand("SEARCH_BOOKINGS");
        searchBtn.addActionListener(this);
        searchPanel.add(searchBtn);
        
        // Add to left controls
        leftControls.add(filterPanel);
        leftControls.add(Box.createRigidArea(new Dimension(20, 0)));
        leftControls.add(searchPanel);
        
        // Right side: Action buttons
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightControls.setBackground(Color.WHITE);
        
        final JButton newBookingBtn = new JButton("➕ New Booking");
        styleSmallButton(newBookingBtn, SUCCESS_COLOR);
        newBookingBtn.setActionCommand("NEW_BOOKING");
        newBookingBtn.addActionListener(this);
        
        final JButton reportBtn = new JButton("📊 Booking Report");
        styleSmallButton(reportBtn, PURPLE_COLOR);
        reportBtn.setActionCommand("BOOKING_REPORT");
        reportBtn.addActionListener(this);
        
        rightControls.add(newBookingBtn);
        rightControls.add(reportBtn);
        
        // Assemble controls line
        controlsLine.add(leftControls, BorderLayout.WEST);
        controlsLine.add(rightControls, BorderLayout.EAST);
        
        // Add both lines to main header
        mainHeader.add(titleLine);
        mainHeader.add(Box.createRigidArea(new Dimension(0, 10)));
        mainHeader.add(controlsLine);
        
        // Stats bar
        JPanel statsBar = createBookingStatsBar();
        
        // Bookings table
        JPanel tablePanel = createBookingsTablePanel();
        
        // Bottom action buttons
        JPanel actionPanel = createBookingActionsPanel();
        
        // Main content panel
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(LIGHT_BG);
        mainContent.add(statsBar, BorderLayout.NORTH);
        mainContent.add(tablePanel, BorderLayout.CENTER);
        mainContent.add(actionPanel, BorderLayout.SOUTH);
        
        panel.add(mainHeader, BorderLayout.NORTH);
        panel.add(mainContent, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== BOOKING FILTER METHOD ====================
    private void applyBookingFilter(String filterType) {
        // Store all rows for filtering on first use
        if (originalBookingData == null) {
            originalBookingData = new Vector<Vector<Object>>();
            for (int i = 0; i < bookingTableModel.getRowCount(); i++) {
                Vector<Object> row = new Vector<Object>();
                for (int j = 0; j < bookingTableModel.getColumnCount(); j++) {
                    row.add(bookingTableModel.getValueAt(i, j));
                }
                originalBookingData.add(row);
            }
        }
        
        // Clear current table
        bookingTableModel.setRowCount(0);
        
        // Apply filter
        for (Vector<Object> row : originalBookingData) {
            String status = row.get(5).toString(); // Status is at index 5
            
            boolean showRow = false;
            
            switch (filterType) {
                case "All Bookings":
                    showRow = true;
                    break;
                    
                case "Today's Check-ins":
                    showRow = status.equals("Check-in Today") || status.equals("Checked-in");
                    break;
                    
                case "Today's Check-outs":
                    showRow = status.equals("Check-out Today");
                    break;
                    
                case "Upcoming":
                    showRow = status.equals("Upcoming") || status.equals("Confirmed");
                    break;
                    
                case "Active":
                    showRow = status.equals("Checked-in") || status.equals("Check-in Today");
                    break;
                    
                case "Cancelled":
                    showRow = status.equals("Cancelled");
                    break;
                    
                case "Completed":
                    showRow = status.equals("Completed");
                    break;
            }
            
            if (showRow) {
                bookingTableModel.addRow(row.toArray());
            }
        }
        
        // Update console with filter info
        System.out.println("Filter applied: " + filterType + " | Showing " + bookingTableModel.getRowCount() + " bookings");
    }
    
    private JPanel createBookingStatsBar() {
        JPanel statsBar = new JPanel(new GridLayout(1, 6, 10, 0));
        statsBar.setBackground(LIGHT_BG);
        statsBar.setBorder(new EmptyBorder(15, 0, 25, 0));
        
        String[] stats = {
            "Total Bookings|45", "Active Today|12", "Check-ins Today|8", 
            "Check-outs Today|6", "Revenue Today|$4,250", "Occupancy|78%"
        };
        
        Color[] colors = {
            PRIMARY_COLOR, SUCCESS_COLOR, DANGER_COLOR, 
            PURPLE_COLOR, WARNING_COLOR, TEAL_COLOR
        };
        
        for (int i = 0; i < stats.length; i++) {
            String[] parts = stats[i].split("\\|");
            statsBar.add(createBookingStatCard(parts[0], parts[1], colors[i]));
        }
        
        return statsBar;
    }
    
    private JPanel createBookingStatCard(String title, String value, Color color) {
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
    
    private JPanel createBookingsTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Bookings table with enhanced columns
        String[] columns = {
            "Booking ID", "Guest Name", "Room", "Check-in", "Check-out", 
            "Status", "Amount", "Actions"
        };
        
        bookingTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only Actions column is editable
            }
        };
        
        // Add sample booking data
        addSampleBookings();
        
        bookingTable = new JTable(bookingTableModel);
        bookingTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bookingTable.setRowHeight(40);
        bookingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        bookingTable.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < bookingTable.getColumnCount(); i++) {
            bookingTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void addSampleBookings() {
        Object[][] bookingData = {
            {"BK001", "John Smith", "101, 102", "2024-12-20", "2024-12-25", "Confirmed", "$1,250.00", "View | Edit | Cancel"},
            {"BK002", "Emma Johnson", "205", "2024-12-22", "2024-12-28", "Checked-in", "$899.99", "View | Edit | Receipt"},
            {"BK003", "Robert Brown", "301", "Today", "2024-12-30", "Check-in Today", "$1,599.00", "View | Check-in"},
            {"BK004", "Sarah Wilson", "108", "2024-12-25", "2024-12-30", "Upcoming", "$750.00", "View | Edit | Cancel"},
            {"BK005", "Michael Davis", "312", "2024-12-19", "Today", "Check-out Today", "$650.00", "View | Check-out"},
            {"BK006", "Lisa Anderson", "401, 402", "2024-12-15", "2024-12-20", "Completed", "$2,100.00", "View | Invoice"},
            {"BK007", "David Miller", "214", "2024-12-23", "2024-12-29", "Cancelled", "$0.00", "View | Archive"},
            {"BK008", "Jennifer Lee", "500", "2024-12-24", "2024-12-31", "Confirmed", "$2,450.00", "View | Edit | Cancel"}
        };
        
        for (Object[] row : bookingData) {
            bookingTableModel.addRow(row);
        }
    }
    
    private JPanel createBookingActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        String[] actions = {
            "Print Today's Schedule", 
            "Send Reminders", 
            "Batch Update", 
            "Export Data",
            "Generate Vouchers",
            "Set Alerts"
        };
        
        String[] commands = {
            "PRINT_SCHEDULE",
            "SEND_REMINDERS", 
            "BATCH_UPDATE", 
            "EXPORT_BOOKINGS",
            "GENERATE_VOUCHERS",
            "SET_ALERTS"
        };
        
        Color[] colors = {
            SECONDARY_COLOR,
            PURPLE_COLOR,
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
    
    // ==================== FINANCIAL REPORTS PANEL ====================
    private JPanel createFinancialPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Main header container with two lines
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
        
        JLabel title = new JLabel("💰 Financial Reports & Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        titleLine.add(title, BorderLayout.WEST);
        
        // Add time period indicator
        JLabel periodLabel = new JLabel("📅 Period: Dec 1-31, 2024 | 💰 Target: $50,000");
        periodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        periodLabel.setForeground(new Color(100, 100, 100));
        titleLine.add(periodLabel, BorderLayout.EAST);
        
        // ====== LINE 2: CONTROLS LINE ======
        JPanel controlsLine = new JPanel(new BorderLayout());
        controlsLine.setBackground(Color.WHITE);
        controlsLine.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Left side: Time Period Selector
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftControls.setBackground(Color.WHITE);
        
        JPanel periodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        periodPanel.setBackground(Color.WHITE);
        JLabel periodLabel2 = new JLabel("📊 Period:");
        periodLabel2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        periodPanel.add(periodLabel2);
        
        JComboBox<String> periodCombo = new JComboBox<>(new String[]{
            "Today", "Yesterday", "This Week", "Last Week", 
            "This Month", "Last Month", "This Quarter", "Last Quarter",
            "This Year", "Last Year", "Custom Range"
        });
        periodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        periodCombo.setPreferredSize(new Dimension(140, 30));
        periodPanel.add(periodCombo);
        
        // Date range picker
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.setBackground(Color.WHITE);
        datePanel.add(new JLabel("From:"));
        
        JTextField fromDateField = new JTextField("2024-12-01", 10);
        fromDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fromDateField.setPreferredSize(new Dimension(100, 30));
        datePanel.add(fromDateField);
        
        datePanel.add(new JLabel("To:"));
        
        JTextField toDateField = new JTextField("2024-12-31", 10);
        toDateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        toDateField.setPreferredSize(new Dimension(100, 30));
        datePanel.add(toDateField);
        
        final JButton applyBtn = new JButton("Apply");
        styleSmallButton(applyBtn, SECONDARY_COLOR);
        applyBtn.setActionCommand("APPLY_DATE_RANGE");
        applyBtn.addActionListener(this);
        datePanel.add(applyBtn);
        
        // Add to left controls
        leftControls.add(periodPanel);
        leftControls.add(Box.createRigidArea(new Dimension(20, 0)));
        leftControls.add(datePanel);
        
        // Right side: Action buttons
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightControls.setBackground(Color.WHITE);
        
        final JButton generateBtn = new JButton("📈 Generate Report");
        styleSmallButton(generateBtn, SUCCESS_COLOR);
        generateBtn.setActionCommand("GENERATE_FINANCIAL_REPORT");
        generateBtn.addActionListener(this);
        
        final JButton exportBtn = new JButton("💾 Export Data");
        styleSmallButton(exportBtn, PURPLE_COLOR);
        exportBtn.setActionCommand("EXPORT_FINANCIAL_DATA");
        exportBtn.addActionListener(this);
        
        rightControls.add(generateBtn);
        rightControls.add(exportBtn);
        
        // Assemble controls line
        controlsLine.add(leftControls, BorderLayout.WEST);
        controlsLine.add(rightControls, BorderLayout.EAST);
        
        // Add both lines to main header
        mainHeader.add(titleLine);
        mainHeader.add(Box.createRigidArea(new Dimension(0, 10)));
        mainHeader.add(controlsLine);
        
        // Financial KPI Cards
        JPanel kpiPanel = createFinancialKPIPanel();
        
        // Charts Section
        JPanel chartsPanel = createFinancialChartsPanel();
        
        // Detailed Reports Section
        JPanel reportsPanel = createDetailedReportsPanel();
        
        // Main content panel
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(LIGHT_BG);
        mainContent.add(kpiPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(chartsPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContent.add(reportsPanel);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(mainHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFinancialKPIPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 15, 0));
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        String[] kpis = {
            "💰 Total Revenue|$45,200|#2ecc71",
            "📈 Monthly Growth|+12.8%|#3498db",
            "📊 Avg Daily Revenue|$1,480|#9b59b6",
            "💸 Total Expenses|$8,400|#e74c3c",
            "📉 Profit Margin|81.4%|#f39c12",
            "🎯 Target Achievement|90.4%|#1abc9c"
        };
        
        for (String kpi : kpis) {
            String[] parts = kpi.split("\\|");
            panel.add(createFinancialKPICard(parts[0], parts[1], Color.decode(parts[2])));
        }
        
        return panel;
    }
    
    private JPanel createFinancialKPICard(String title, String value, Color color) {
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
        
        // Optional trend indicator
        JLabel trendLabel = new JLabel();
        trendLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        trendLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (value.contains("+")) {
            trendLabel.setText("▲ Trending Up");
            trendLabel.setForeground(new Color(46, 204, 113));
        } else if (value.contains("-")) {
            trendLabel.setText("▼ Trending Down");
            trendLabel.setForeground(new Color(231, 76, 60));
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
    
    private JPanel createFinancialChartsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(LIGHT_BG);
        
        // Revenue Trend Chart
        JPanel revenueChart = createChartPanel(
            "📈 Revenue Trend - Last 6 Months", 
            new String[]{"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"},
            new int[]{38000, 42000, 45200, 41000, 46500, 48800},
            SECONDARY_COLOR,
            "Monthly Revenue ($)"
        );
        
        // Expense Breakdown Chart
        JPanel expenseChart = createChartPanel(
            "💸 Expense Breakdown", 
            new String[]{"Staff", "Utilities", "Maintenance", "Marketing", "Supplies", "Other"},
            new int[]{3500, 1200, 800, 1500, 900, 500},
            new Color(231, 76, 60),
            "Expense Amount ($)"
        );
        
        panel.add(revenueChart);
        panel.add(expenseChart);
        
        return panel;
    }
    
    private JPanel createChartPanel(final String title, final String[] labels, final int[] data, final Color color, final String yAxisLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel chartTitle = new JLabel(title);
        chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chartTitle.setForeground(DARK_BG);
        panel.add(chartTitle, BorderLayout.NORTH);
        
        // Chart area (simulated with custom painting)
        JPanel chartArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // For Java 7 compatibility
                Object antialiasing = RenderingHints.VALUE_ANTIALIAS_ON;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasing);
                
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
                
                // Find max value for scaling
                int maxValue = 0;
                for (int value : data) {
                    if (value > maxValue) maxValue = value;
                }
                maxValue = (int) (maxValue * 1.1); // Add 10% padding
                
                // Draw bars
                int barCount = data.length;
                int barWidth = Math.min(40, chartWidth / (barCount * 2));
                
                for (int i = 0; i < barCount; i++) {
                    int barHeight = (int) ((data[i] / (double) maxValue) * chartHeight);
                    int x = padding + (i * 2 * barWidth) + barWidth / 2;
                    int y = height - padding - barHeight;
                    
                    // Draw bar with solid color
                    g2d.setColor(color);
                    g2d.fillRect(x, y, barWidth, barHeight);
                    
                    // Draw border
                    g2d.setColor(color.darker());
                    g2d.drawRect(x, y, barWidth, barHeight);
                    
                    // Draw value on top
                    g2d.setColor(Color.BLACK);
                    String valueText = String.format("$%,d", data[i]);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(valueText);
                    g2d.drawString(valueText, x + (barWidth - textWidth) / 2, y - 5);
                    
                    // Draw label
                    g2d.drawString(labels[i], x + (barWidth - fm.stringWidth(labels[i])) / 2, height - padding + 15);
                }
                
                // Draw axes
                g2d.setColor(Color.GRAY);
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
                g2d.drawLine(padding, height - padding, padding, padding); // Y-axis
                
                // Simple Y-axis label at the top
                g2d.setColor(Color.BLACK);
                g2d.drawString(yAxisLabel, padding + 10, padding - 10);
            }
        };
        chartArea.setBackground(Color.WHITE);
        chartArea.setPreferredSize(new Dimension(400, 250));
        
        panel.add(chartArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDetailedReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        
        JLabel sectionTitle = new JLabel("📋 Detailed Financial Reports");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(DARK_BG);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Reports grid
        JPanel reportsGrid = new JPanel(new GridLayout(3, 3, 15, 15));
        reportsGrid.setBackground(LIGHT_BG);
        
        String[] reports = {
            "📊 Daily Sales Report", 
            "📅 Monthly Income Statement", 
            "💳 Payment Method Analysis",
            "🏨 Room Revenue Breakdown", 
            "🍽️ Restaurant & Services", 
            "📉 Expense Analysis Report",
            "👥 Staff Performance Report", 
            "🎯 Budget vs Actual", 
            "📈 Year-over-Year Comparison"
        };
        
        String[] commands = {
            "DAILY_SALES_REPORT",
            "MONTHLY_INCOME_STATEMENT", 
            "PAYMENT_METHOD_ANALYSIS",
            "ROOM_REVENUE_BREAKDOWN", 
            "RESTAURANT_SERVICES_REPORT", 
            "EXPENSE_ANALYSIS_REPORT",
            "STAFF_PERFORMANCE_REPORT", 
            "BUDGET_VS_ACTUAL", 
            "YEAR_OVER_YEAR_COMPARISON"
        };
        
        for (int i = 0; i < reports.length; i++) {
            final JButton reportBtn = new JButton(reports[i]);
            styleReportCard(reportBtn);
            reportBtn.setActionCommand(commands[i]);
            reportBtn.addActionListener(this);
            reportsGrid.add(reportBtn);
        }
        
        panel.add(sectionTitle, BorderLayout.NORTH);
        panel.add(reportsGrid, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void styleReportCard(final JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(DARK_BG);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 245, 250));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
        });
    }
    
    // ==================== SYSTEM SETTINGS PANEL ====================
    private JPanel createSystemSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Main container with scroll
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(LIGHT_BG);
        
        // ========== HEADER SECTION ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel title = new JLabel("⚙️ System Configuration & Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BG);
        
        // System status indicator
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        statusPanel.setBackground(Color.WHITE);
        
        JLabel statusLabel = new JLabel("🟢 System: Online | Last Backup: Today 02:00");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusPanel.add(statusLabel);
        
        header.add(title, BorderLayout.WEST);
        header.add(statusPanel, BorderLayout.EAST);
        
        mainContent.add(header);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // ========== SETTINGS SECTIONS ==========
        mainContent.add(createSystemSection("General Settings", createGeneralSettings()));
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        mainContent.add(createSystemSection("Hotel Configuration", createHotelConfig()));
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        mainContent.add(createSystemSection("Booking & Reservation", createBookingSettings()));
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        mainContent.add(createSystemSection("Email & Notifications", createEmailSettings()));
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        mainContent.add(createSystemSection("Security & Access", createSecuritySettings()));
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        mainContent.add(createSystemSection("Backup & Maintenance", createBackupSettings()));
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // ========== QUICK ACTIONS PANEL ==========
        JPanel quickActions = createQuickActionsPanel();
        mainContent.add(quickActions);
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSystemSection(String title, JPanel content) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(0, 0, 0, 0)
        ));
        
        // Section header
        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setBackground(new Color(245, 245, 245));
        sectionHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(DARK_BG);
        sectionHeader.add(titleLabel, BorderLayout.WEST);
        
        section.add(sectionHeader, BorderLayout.NORTH);
        section.add(content, BorderLayout.CENTER);
        
        return section;
    }
    
    private JPanel createGeneralSettings() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Hotel Name
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Hotel Name:"), gbc);
        gbc.gridx = 1;
        JTextField hotelNameField = new JTextField("Grand Hospitality Hotel", 25);
        styleTextField(hotelNameField);
        panel.add(hotelNameField, gbc);
        row++;
        
        // Currency
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Currency:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> currencyCombo = new JComboBox<>(new String[]{
            "USD ($)", "EUR (€)", "GBP (£)", "JPY (¥)", "CNY (¥)", "INR (₹)", "AUD (A$)"
        });
        styleComboBox(currencyCombo);
        panel.add(currencyCombo, gbc);
        row++;
        
        // Timezone
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Timezone:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> timezoneCombo = new JComboBox<>(new String[]{
            "UTC", "EST", "PST", "CET", "GMT", "IST", "CST"
        });
        styleComboBox(timezoneCombo);
        panel.add(timezoneCombo, gbc);
        row++;
        
        // Date Format
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Date Format:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> dateFormatCombo = new JComboBox<>(new String[]{
            "YYYY-MM-DD", "MM/DD/YYYY", "DD/MM/YYYY", "DD Month YYYY"
        });
        styleComboBox(dateFormatCombo);
        panel.add(dateFormatCombo, gbc);
        row++;
        
        // Language
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Language:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> languageCombo = new JComboBox<>(new String[]{
            "English", "Spanish", "French", "German", "Chinese", "Japanese", "Arabic"
        });
        styleComboBox(languageCombo);
        panel.add(languageCombo, gbc);
        row++;
        
        // Save button for this section
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton saveBtn = new JButton("💾 Save General Settings");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveBtn.setBackground(SECONDARY_COLOR);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        saveBtn.setActionCommand("SAVE_GENERAL_SETTINGS");
        saveBtn.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createHotelConfig() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Total Rooms
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Total Rooms:"), gbc);
        gbc.gridx = 1;
        JTextField roomsField = new JTextField("128", 10);
        styleTextField(roomsField);
        panel.add(roomsField, gbc);
        row++;
        
        // Floor Count
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Floors:"), gbc);
        gbc.gridx = 1;
        JTextField floorsField = new JTextField("5", 10);
        styleTextField(floorsField);
        panel.add(floorsField, gbc);
        row++;
        
        // Check-in Time
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Check-in Time:"), gbc);
        gbc.gridx = 1;
        JTextField checkinTimeField = new JTextField("14:00", 10);
        styleTextField(checkinTimeField);
        panel.add(checkinTimeField, gbc);
        row++;
        
        // Check-out Time
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Check-out Time:"), gbc);
        gbc.gridx = 1;
        JTextField checkoutTimeField = new JTextField("11:00", 10);
        styleTextField(checkoutTimeField);
        panel.add(checkoutTimeField, gbc);
        row++;
        
        // Tax Rate
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Tax Rate (%):"), gbc);
        gbc.gridx = 1;
        JTextField taxField = new JTextField("10.0", 10);
        styleTextField(taxField);
        panel.add(taxField, gbc);
        row++;
        
        // Service Charge
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Service Charge (%):"), gbc);
        gbc.gridx = 1;
        JTextField serviceField = new JTextField("5.0", 10);
        styleTextField(serviceField);
        panel.add(serviceField, gbc);
        row++;
        
        // Save button
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton saveBtn = new JButton("🏨 Save Hotel Config");
        styleButton(saveBtn, PURPLE_COLOR);
        saveBtn.setActionCommand("SAVE_HOTEL_CONFIG");
        saveBtn.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createBookingSettings() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Booking window
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Max Advance Booking (days):"), gbc);
        gbc.gridx = 1;
        JTextField advanceField = new JTextField("365", 10);
        styleTextField(advanceField);
        panel.add(advanceField, gbc);
        row++;
        
        // Minimum stay
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Minimum Stay (nights):"), gbc);
        gbc.gridx = 1;
        JTextField minStayField = new JTextField("1", 10);
        styleTextField(minStayField);
        panel.add(minStayField, gbc);
        row++;
        
        // Cancellation policy
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Cancellation Window (hours):"), gbc);
        gbc.gridx = 1;
        JTextField cancelField = new JTextField("48", 10);
        styleTextField(cancelField);
        panel.add(cancelField, gbc);
        row++;
        
        // Auto-confirmation
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Auto-confirm Bookings:"), gbc);
        gbc.gridx = 1;
        JPanel autoConfirmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        autoConfirmPanel.setBackground(Color.WHITE);
        
        final JRadioButton autoYes = new JRadioButton("Yes");
        final JRadioButton autoNo = new JRadioButton("No");
        ButtonGroup autoGroup = new ButtonGroup();
        autoGroup.add(autoYes);
        autoGroup.add(autoNo);
        autoYes.setSelected(true);
        
        autoConfirmPanel.add(autoYes);
        autoConfirmPanel.add(autoNo);
        panel.add(autoConfirmPanel, gbc);
        row++;
        
        // Require deposit
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Require Deposit:"), gbc);
        gbc.gridx = 1;
        JPanel depositPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        depositPanel.setBackground(Color.WHITE);
        
        final JRadioButton depositYes = new JRadioButton("Yes");
        final JRadioButton depositNo = new JRadioButton("No");
        ButtonGroup depositGroup = new ButtonGroup();
        depositGroup.add(depositYes);
        depositGroup.add(depositNo);
        depositNo.setSelected(true);
        
        depositPanel.add(depositYes);
        depositPanel.add(depositNo);
        panel.add(depositPanel, gbc);
        row++;
        
        // Save button
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton saveBtn = new JButton("📅 Save Booking Settings");
        styleButton(saveBtn, TEAL_COLOR);
        saveBtn.setActionCommand("SAVE_BOOKING_SETTINGS");
        saveBtn.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createEmailSettings() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // SMTP Server
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("SMTP Server:"), gbc);
        gbc.gridx = 1;
        JTextField smtpField = new JTextField("smtp.gmail.com", 25);
        styleTextField(smtpField);
        panel.add(smtpField, gbc);
        row++;
        
        // Port
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Port:"), gbc);
        gbc.gridx = 1;
        JTextField portField = new JTextField("587", 10);
        styleTextField(portField);
        panel.add(portField, gbc);
        row++;
        
        // Email
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Sender Email:"), gbc);
        gbc.gridx = 1;
        JTextField emailField = new JTextField("noreply@hospitality.com", 25);
        styleTextField(emailField);
        panel.add(emailField, gbc);
        row++;
        
        // Password (masked)
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField(20);
        passField.setEchoChar('•');
        styleTextField(passField);
        panel.add(passField, gbc);
        row++;
        
        // Send Test Email
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Test Email:"), gbc);
        gbc.gridx = 1;
        
        JPanel testPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        testPanel.setBackground(Color.WHITE);
        
        JTextField testEmailField = new JTextField("test@email.com", 20);
        styleTextField(testEmailField);
        
        JButton testBtn = new JButton("Send Test");
        testBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        testBtn.setBackground(SECONDARY_COLOR);
        testBtn.setForeground(Color.WHITE);
        testBtn.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        testPanel.add(testEmailField);
        testPanel.add(testBtn);
        panel.add(testPanel, gbc);
        row++;
        
        // Email Types
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Automated Emails:"), gbc);
        gbc.gridx = 1;
        
        JPanel emailTypesPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        emailTypesPanel.setBackground(Color.WHITE);
        
        String[] emailTypes = {
            "✓ Booking Confirmations",
            "✓ Check-in Reminders",
            "✓ Check-out Reminders",
            "✓ Payment Receipts",
            "✓ Cancellation Notices",
            "✓ Promotional Offers"
        };
        
        for (String type : emailTypes) {
            JCheckBox checkBox = new JCheckBox(type);
            checkBox.setSelected(true);
            emailTypesPanel.add(checkBox);
        }
        
        panel.add(emailTypesPanel, gbc);
        row++;
        
        // Save button
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton saveBtn = new JButton("📧 Save Email Settings");
        styleButton(saveBtn, WARNING_COLOR);
        saveBtn.setActionCommand("SAVE_EMAIL_SETTINGS");
        saveBtn.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createSecuritySettings() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Session timeout
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Session Timeout (minutes):"), gbc);
        gbc.gridx = 1;
        JTextField timeoutField = new JTextField("30", 10);
        styleTextField(timeoutField);
        panel.add(timeoutField, gbc);
        row++;
        
        // Max login attempts
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Max Login Attempts:"), gbc);
        gbc.gridx = 1;
        JTextField attemptsField = new JTextField("5", 10);
        styleTextField(attemptsField);
        panel.add(attemptsField, gbc);
        row++;
        
        // Password policy
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Password Policy:"), gbc);
        gbc.gridx = 1;
        
        JPanel policyPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        policyPanel.setBackground(Color.WHITE);
        
        String[] policies = {
            "✓ Minimum 8 characters",
            "✓ At least 1 uppercase letter",
            "✓ At least 1 number",
            "✓ At least 1 special character",
            "✓ Expires every 90 days"
        };
        
        for (String policy : policies) {
            JCheckBox checkBox = new JCheckBox(policy);
            checkBox.setSelected(true);
            policyPanel.add(checkBox);
        }
        
        panel.add(policyPanel, gbc);
        row++;
        
        // Two-factor authentication
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Two-Factor Auth:"), gbc);
        gbc.gridx = 1;
        
        JPanel twoFactorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        twoFactorPanel.setBackground(Color.WHITE);
        
        final JRadioButton twoFactorYes = new JRadioButton("Enabled");
        final JRadioButton twoFactorNo = new JRadioButton("Disabled");
        ButtonGroup twoFactorGroup = new ButtonGroup();
        twoFactorGroup.add(twoFactorYes);
        twoFactorGroup.add(twoFactorNo);
        twoFactorNo.setSelected(true);
        
        twoFactorPanel.add(twoFactorYes);
        twoFactorPanel.add(twoFactorNo);
        panel.add(twoFactorPanel, gbc);
        row++;
        
        // IP Whitelisting
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("IP Whitelist:"), gbc);
        gbc.gridx = 1;
        
        JPanel ipPanel = new JPanel(new BorderLayout());
        ipPanel.setBackground(Color.WHITE);
        
        JTextArea ipArea = new JTextArea("192.168.1.*\n10.0.0.*\n127.0.0.1", 4, 20);
        ipArea.setLineWrap(true);
        JScrollPane ipScroll = new JScrollPane(ipArea);
        ipScroll.setPreferredSize(new Dimension(250, 80));
        
        ipPanel.add(ipScroll, BorderLayout.CENTER);
        panel.add(ipPanel, gbc);
        row++;
        
        // Save button
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton saveBtn = new JButton("🔒 Save Security Settings");
        styleButton(saveBtn, DANGER_COLOR);
        saveBtn.setActionCommand("SAVE_SECURITY_SETTINGS");
        saveBtn.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createBackupSettings() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Auto backup frequency
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Auto Backup:"), gbc);
        gbc.gridx = 1;
        
        JComboBox<String> backupCombo = new JComboBox<>(new String[]{
            "Disabled", "Daily", "Weekly", "Monthly"
        });
        styleComboBox(backupCombo);
        backupCombo.setSelectedItem("Daily");
        panel.add(backupCombo, gbc);
        row++;
        
        // Backup location
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Backup Location:"), gbc);
        gbc.gridx = 1;
        
        JPanel locationPanel = new JPanel(new BorderLayout(10, 0));
        locationPanel.setBackground(Color.WHITE);
        
        JTextField locationField = new JTextField("/backups/hospitality/", 20);
        styleTextField(locationField);
        
        JButton browseBtn = new JButton("Browse...");
        browseBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        browseBtn.setBackground(SECONDARY_COLOR);
        browseBtn.setForeground(Color.WHITE);
        browseBtn.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        locationPanel.add(locationField, BorderLayout.CENTER);
        locationPanel.add(browseBtn, BorderLayout.EAST);
        panel.add(locationPanel, gbc);
        row++;
        
        // Retention period
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createSettingLabel("Retention (days):"), gbc);
        gbc.gridx = 1;
        JTextField retentionField = new JTextField("30", 10);
        styleTextField(retentionField);
        panel.add(retentionField, gbc);
        row++;
        
        // Backup now button
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton backupNowBtn = new JButton("💾 Run Backup Now");
        styleButton(backupNowBtn, SUCCESS_COLOR);
        backupNowBtn.setActionCommand("RUN_BACKUP");
        backupNowBtn.addActionListener(this);
        
        JPanel backupBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        backupBtnPanel.setBackground(Color.WHITE);
        backupBtnPanel.add(backupNowBtn);
        
        panel.add(backupBtnPanel, gbc);
        row++;
        
        // Last backup info
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel lastBackupLabel = new JLabel("Last Backup: Today 02:00 | Size: 45.2 MB | Status: ✅ Complete");
        lastBackupLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lastBackupLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(lastBackupLabel);
        
        panel.add(infoPanel, gbc);
        row++;
        
        // Save button
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton saveBtn = new JButton("🔄 Save Backup Settings");
        styleButton(saveBtn, new Color(149, 165, 166));
        saveBtn.setActionCommand("SAVE_BACKUP_SETTINGS");
        saveBtn.addActionListener(this);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JPanel actionsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        actionsPanel.setBackground(LIGHT_BG);
        
        String[] actions = {
            "🔧 System Diagnostics",
            "🗑️ Clear Cache", 
            "📊 Performance Logs",
            "🔍 Audit Trail",
            "🛠️ System Update", 
            "📋 Log Files",
            "⚡ Restart Services",
            "📈 Usage Statistics"
        };
        
        String[] commands = {
            "SYSTEM_DIAGNOSTICS",
            "CLEAR_CACHE", 
            "VIEW_LOGS",
            "VIEW_AUDIT",
            "SYSTEM_UPDATE", 
            "VIEW_LOG_FILES",
            "RESTART_SERVICES",
            "VIEW_STATISTICS"
        };
        
        Color[] colors = {
            WARNING_COLOR,
            DANGER_COLOR,
            SECONDARY_COLOR,
            PURPLE_COLOR,
            SUCCESS_COLOR,
            TEAL_COLOR,
            new Color(230, 126, 34),
            DARK_BG
        };
        
        for (int i = 0; i < actions.length; i++) {
            JButton actionBtn = new JButton(actions[i]);
            styleQuickActionButton(actionBtn, colors[i]);
            actionBtn.setActionCommand(commands[i]);
            actionBtn.addActionListener(this);
            actionsPanel.add(actionBtn);
        }
        
        panel.add(actionsPanel, BorderLayout.CENTER);
        
        return panel;
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
    
    private void styleSmallButton(final JButton button, final Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
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
    
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(DARK_BG);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(titleLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void addSampleUsers() {
        Object[][] userData = {
            {1, "admin", "System Administrator", "Administrator", "admin@hotel.com", "Active", "Edit | Delete"},
            {2, "manager1", "John Manager", "Manager", "john@hotel.com", "Active", "Edit | Delete"},
            {3, "reception1", "Sarah Reception", "Receptionist", "sarah@hotel.com", "Active", "Edit | Delete"},
            {4, "client1", "Robert Guest", "Client", "robert@email.com", "Inactive", "Edit | Delete"},
            {5, "reception2", "Mike Staff", "Receptionist", "mike@hotel.com", "Suspended", "Edit | Delete"}
        };
        
        for (Object[] row : userData) {
            userTableModel.addRow(row);
        }
    }
    
    private void addSampleRooms() {
        Object[][] roomData = {
            {101, "Single Room", "$89.99", "Available", "WiFi, TV, AC", "Edit | Delete"},
            {102, "Double Room", "$129.99", "Occupied", "WiFi, TV, AC, Mini-bar", "Edit | Delete"},
            {103, "Suite", "$249.99", "Reserved", "WiFi, TV, AC, Jacuzzi, Kitchen", "Edit | Delete"},
            {104, "Single Room", "$89.99", "Available", "WiFi, TV", "Edit | Delete"},
            {105, "Double Room", "$129.99", "Maintenance", "WiFi, TV, AC", "Edit | Delete"},
            {201, "Executive Suite", "$349.99", "Available", "WiFi, TV, AC, Office, Bar", "Edit | Delete"}
        };
        
        for (Object[] row : roomData) {
            roomTableModel.addRow(row);
        }
    }
    
    // ==================== HELPER METHODS FOR SETTINGS ====================
    private JLabel createSettingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(DARK_BG);
        return label;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new Dimension(200, 35));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        combo.setPreferredSize(new Dimension(200, 35));
    }
    
    private void styleQuickActionButton(final JButton button, final Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(12, 15, 12, 15));
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
        
        System.out.println("Button clicked: " + command);
        
        if (command.equals("LOGOUT")) {
            logout();
        } else if (command.equals("DASHBOARD")) {
            cardLayout.show(contentPanel, "DASHBOARD");
        } else if (command.equals("USER MANAGEMENT")) {
            cardLayout.show(contentPanel, "USER MANAGEMENT");
        } else if (command.equals("ROOM MANAGEMENT")) {
            cardLayout.show(contentPanel, "ROOM MANAGEMENT");
        } else if (command.equals("BOOKING MANAGEMENT")) {
            cardLayout.show(contentPanel, "BOOKING MANAGEMENT");
        } else if (command.equals("FINANCIAL REPORTS")) {
            cardLayout.show(contentPanel, "FINANCIAL REPORTS");
        } else if (command.equals("SYSTEM SETTINGS")) {
            cardLayout.show(contentPanel, "SYSTEM SETTINGS");
        } else if (command.equals("REFRESH_DASHBOARD")) {
            refreshDashboard();
        } else if (command.equals("ADD_USER")) {
            showAddUserDialog();
        } else if (command.equals("SEARCH_USERS")) {
            searchUsers();
        } else if (command.equals("ADD_ROOM")) {
            showAddRoomDialog();
        } else if (command.equals("SEARCH_ROOMS")) {
            searchRooms();
        } else if (command.equals("GENERATE_REPORT")) {
            generateReport();
        } else if (command.equals("SYSTEM_CHECK")) {
            systemCheck();
        } else if (command.equals("SEND_ANNOUNCEMENT")) {
            sendAnnouncement();
        } else if (command.equals("SEARCH_BOOKINGS")) {
            searchBookings();
        } else if (command.equals("NEW_BOOKING")) {
            showNewBookingDialog();
        } else if (command.equals("BOOKING_REPORT")) {
            generateBookingReport();
        } else if (command.equals("PRINT_SCHEDULE")) {
            printTodaySchedule();
        } else if (command.equals("SEND_REMINDERS")) {
            sendBookingReminders();
        } else if (command.equals("BATCH_UPDATE")) {
            batchUpdateBookings();
        } else if (command.equals("EXPORT_BOOKINGS")) {
            exportBookingsData();
        } else if (command.equals("GENERATE_VOUCHERS")) {
            generateBookingVouchers();
        } else if (command.equals("SET_ALERTS")) {
            setBookingAlerts();
        } else if (command.equals("APPLY_DATE_RANGE")) {
            applyDateRange();
        } else if (command.equals("GENERATE_FINANCIAL_REPORT")) {
            generateFinancialReport();
        } else if (command.equals("EXPORT_FINANCIAL_DATA")) {
            exportFinancialData();
        } else if (command.equals("DAILY_SALES_REPORT")) {
            showDailySalesReport();
        } else if (command.equals("MONTHLY_INCOME_STATEMENT")) {
            showMonthlyIncomeStatement();
        } else if (command.equals("PAYMENT_METHOD_ANALYSIS")) {
            showPaymentMethodAnalysis();
        } else if (command.equals("ROOM_REVENUE_BREAKDOWN")) {
            showRoomRevenueBreakdown();
        } else if (command.equals("RESTAURANT_SERVICES_REPORT")) {
            showRestaurantServicesReport();
        } else if (command.equals("EXPENSE_ANALYSIS_REPORT")) {
            showExpenseAnalysisReport();
        } else if (command.equals("STAFF_PERFORMANCE_REPORT")) {
            showStaffPerformanceReport();
        } else if (command.equals("BUDGET_VS_ACTUAL")) {
            showBudgetVsActual();
        } else if (command.equals("YEAR_OVER_YEAR_COMPARISON")) {
            showYearOverYearComparison();
        } else if (command.equals("SAVE_GENERAL_SETTINGS")) {
            saveGeneralSettings();
        } else if (command.equals("SAVE_HOTEL_CONFIG")) {
            saveHotelConfig();
        } else if (command.equals("SAVE_BOOKING_SETTINGS")) {
            saveBookingSettings();
        } else if (command.equals("SAVE_EMAIL_SETTINGS")) {
            saveEmailSettings();
        } else if (command.equals("SAVE_SECURITY_SETTINGS")) {
            saveSecuritySettings();
        } else if (command.equals("SAVE_BACKUP_SETTINGS")) {
            saveBackupSettings();
        } else if (command.equals("RUN_BACKUP")) {
            runBackup();
        } else if (command.equals("SYSTEM_DIAGNOSTICS")) {
            runSystemDiagnostics();
        } else if (command.equals("CLEAR_CACHE")) {
            clearCache();
        } else if (command.equals("VIEW_LOGS")) {
            viewPerformanceLogs();
        } else if (command.equals("VIEW_AUDIT")) {
            viewAuditTrail();
        } else if (command.equals("SYSTEM_UPDATE")) {
            checkForUpdates();
        } else if (command.equals("VIEW_LOG_FILES")) {
            viewLogFiles();
        } else if (command.equals("RESTART_SERVICES")) {
            restartServices();
        } else if (command.equals("VIEW_STATISTICS")) {
            viewUsageStatistics();
        }
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
        JOptionPane.showMessageDialog(this,
            "✅ Dashboard refreshed!\n\n" +
            "All statistics have been updated.\n" +
            "Last refresh: " + new java.util.Date(),
            "Dashboard Refreshed",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAddUserDialog() {
        final JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create form fields as final
        final JTextField usernameField = new JTextField(20);
        final JTextField fullNameField = new JTextField(20);
        final JTextField emailField = new JTextField(20);
        final JPasswordField passwordField = new JPasswordField(20);
        final JPasswordField confirmPasswordField = new JPasswordField(20);
        final JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Administrator", "Manager", "Receptionist", "Client"});
        
        String[] labels = {"Username:", "Full Name:", "Email:", "Password:", "Confirm Password:", "Role:"};
        JComponent[] fields = {usernameField, fullNameField, emailField, passwordField, confirmPasswordField, roleCombo};
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }
        
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        final JButton saveBtn = new JButton("Create User");
        styleButton(saveBtn, SUCCESS_COLOR);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate form
                if (usernameField.getText().trim().isEmpty() || 
                    fullNameField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty()) {
                    
                    JOptionPane.showMessageDialog(dialog,
                        "Please fill in all fields!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                
                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Please enter password and confirmation!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(dialog,
                        "Passwords do not match!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Add user to table
                Object[] newUser = {
                    userTableModel.getRowCount() + 1,
                    usernameField.getText(),
                    fullNameField.getText(),
                    roleCombo.getSelectedItem(),
                    emailField.getText(),
                    "Active",
                    "Edit | Delete"
                };
                userTableModel.addRow(newUser);
                
                JOptionPane.showMessageDialog(dialog, 
                    "✅ User created successfully!\n\n" +
                    "Username: " + usernameField.getText() + "\n" +
                    "Role: " + roleCombo.getSelectedItem() + "\n" +
                    "Status: Active",
                    "User Created",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
            }
        });
        
        panel.add(saveBtn, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void searchUsers() {
        String searchTerm = searchUserField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a search term!",
                "Search Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this,
            "🔍 Searching for users with: '" + searchTerm + "'\n\n" +
            "Found 5 matching users.\n" +
            "(In real system, this would filter the user table)",
            "Search Results",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAddRoomDialog() {
        final JDialog dialog = new JDialog(this, "Add New Room", true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // ========== HEADER ==========
        JPanel header = new JPanel();
        header.setBackground(SECONDARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("➕ Add New Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        
        // ========== FORM ==========
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create all form fields as FINAL
        final JTextField roomNumberField = new JTextField(15);
        final JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{
            "Select Room Type", "Single Room", "Double Room", "Suite", "Executive Suite"
        });
        final JTextField priceField = new JTextField(15);
        final JComboBox<String> statusCombo = new JComboBox<>(new String[]{
            "Available", "Occupied", "Under Maintenance", "Reserved"
        });
        final JTextArea amenitiesArea = new JTextArea(3, 15);
        
        // Add fields to form
        int row = 0;
        
        // Room Number
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(roomNumberField, gbc);
        row++;
        
        // Room Type
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(roomTypeCombo, gbc);
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
        
        // ========== BUTTONS ==========
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(149, 165, 166));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        JButton saveBtn = new JButton("Save Room");
        saveBtn.setBackground(SUCCESS_COLOR);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from form fields
                String roomNumber = roomNumberField.getText().trim();
                String roomType = roomTypeCombo.getSelectedItem().toString();
                String price = priceField.getText().trim();
                String status = statusCombo.getSelectedItem().toString();
                String amenities = amenitiesArea.getText().trim();
                
                // Validate
                if (roomNumber.isEmpty() || roomType.equals("Select Room Type") || price.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Please fill in all required fields!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    int roomNum = Integer.parseInt(roomNumber);
                    double priceVal = Double.parseDouble(price);
                    
                    if (roomNum <= 0) {
                        throw new NumberFormatException("Room number must be positive");
                    }
                    if (priceVal <= 0) {
                        throw new NumberFormatException("Price must be positive");
                    }
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Please enter valid numbers:\n" +
                        "• Room Number (positive integer)\n" +
                        "• Price (positive number)\n\n" +
                        "Error: " + ex.getMessage(),
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Add to table
                Object[] newRoom = {
                    roomNumber,
                    roomType,
                    "$" + price,
                    status,
                    amenities.isEmpty() ? "Standard amenities" : amenities,
                    "Edit | Delete"
                };
                roomTableModel.addRow(newRoom);
                
                JOptionPane.showMessageDialog(dialog,
                    "✅ Room " + roomNumber + " added successfully!\n\n" +
                    "Type: " + roomType + "\n" +
                    "Price: $" + price + " per night\n" +
                    "Status: " + status,
                    "Room Created",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        formPanel.add(buttonPanel, gbc);
        
        // ========== ASSEMBLE DIALOG ==========
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void searchRooms() {
        String searchTerm = searchRoomField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a search term!",
                "Search Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this,
            "🔍 Searching for rooms with: '" + searchTerm + "'\n\n" +
            "Found 3 matching rooms.\n" +
            "(In real system, this would filter the room table)",
            "Search Results",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void generateReport() {
        String[] reportTypes = {"Daily Report", "Weekly Report", "Monthly Report", "Annual Report"};
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
                "• Revenue summary\n" +
                "• Occupancy rates\n" +
                "• Guest statistics\n" +
                "• Staff performance\n\n" +
                "Report saved to: /reports/" + selected.toLowerCase().replace(" ", "_") + ".pdf",
                "Report Generation",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void systemCheck() {
        JOptionPane.showMessageDialog(this,
            "⚙️ Running system diagnostics...\n\n" +
            "✅ Database Connection: OK\n" +
            "✅ File System: OK\n" +
            "✅ Network: OK\n" +
            "✅ Security: OK\n" +
            "✅ Backup System: OK\n" +
            "✅ User Authentication: OK\n" +
            "✅ Payment Gateway: OK\n\n" +
            "🎉 All systems are functioning normally.",
            "System Diagnostics",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void sendAnnouncement() {
        String announcement = JOptionPane.showInputDialog(this,
            "Enter announcement message:",
            "Send Announcement",
            JOptionPane.QUESTION_MESSAGE);
        
        if (announcement != null && !announcement.trim().isEmpty()) {
            String[] recipients = {"All Users", "Staff Only", "Clients Only", "Managers Only"};
            String recipient = (String) JOptionPane.showInputDialog(this,
                "Select recipients:",
                "Select Recipients",
                JOptionPane.QUESTION_MESSAGE,
                null,
                recipients,
                recipients[0]);
            
            if (recipient != null) {
                JOptionPane.showMessageDialog(this,
                    "📢 Announcement sent!\n\n" +
                    "To: " + recipient + "\n" +
                    "Message: " + announcement + "\n\n" +
                    "Recipients notified via:\n" +
                    "• Email\n" +
                    "• Dashboard notification\n" +
                    "• Mobile app (if enabled)",
                    "Announcement Sent",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    // ==================== BOOKING ACTION METHODS ====================
    private void searchBookings() {
        JOptionPane.showMessageDialog(this,
            "🔍 Search functionality would filter the bookings table\n" +
            "Based on: Guest name, Room number, Booking ID, or Dates\n\n" +
            "Real implementation would query database and update table.",
            "Search Bookings",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showNewBookingDialog() {
        final JDialog dialog = new JDialog(this, "Create New Booking", true);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(SECONDARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("Create New Booking");
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
        
        // Create form fields as FINAL so they can be accessed in inner class
        final JTextField guestNameField = new JTextField(20);
        final JTextField emailField = new JTextField(20);
        final JTextField phoneField = new JTextField(20);
        final JTextField idField = new JTextField(20);
        final JTextField checkinField = new JTextField("2024-12-20", 20);
        final JTextField checkoutField = new JTextField("2024-12-25", 20);
        final JTextField guestsField = new JTextField("2", 20);
        final JComboBox<String> roomTypeCombo = new JComboBox<>(new String[]{
            "Select Room Type", "Single Room", "Double Room", "Suite", "Executive Suite"
        });
        final JTextArea requestsArea = new JTextArea(3, 20);
        
        // Guest Information Section
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel sectionTitle = new JLabel("Guest Information");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(DARK_BG);
        formPanel.add(sectionTitle, gbc);
        row++;
        
        gbc.gridwidth = 1;
        String[] guestLabels = {"Guest Name:", "Email:", "Phone:", "ID/Passport:"};
        JTextField[] guestFields = {guestNameField, emailField, phoneField, idField};
        
        for (int i = 0; i < guestLabels.length; i++) {
            gbc.gridx = 0; gbc.gridy = row;
            formPanel.add(new JLabel(guestLabels[i]), gbc);
            gbc.gridx = 1;
            formPanel.add(guestFields[i], gbc);
            row++;
        }
        
        // Booking Details Section
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        sectionTitle = new JLabel("Booking Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(DARK_BG);
        formPanel.add(sectionTitle, gbc);
        row++;
        
        gbc.gridwidth = 1;
        String[] bookingLabels = {"Check-in Date:", "Check-out Date:", "Number of Guests:", "Room Type:"};
        JComponent[] bookingFields = {checkinField, checkoutField, guestsField, roomTypeCombo};
        
        for (int i = 0; i < bookingLabels.length; i++) {
            gbc.gridx = 0; gbc.gridy = row;
            formPanel.add(new JLabel(bookingLabels[i]), gbc);
            gbc.gridx = 1;
            formPanel.add(bookingFields[i], gbc);
            row++;
        }
        
        // Special Requests
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Special Requests:"), gbc);
        gbc.gridx = 1;
        requestsArea.setLineWrap(true);
        requestsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(new JScrollPane(requestsArea), gbc);
        row++;
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        final JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, new Color(149, 165, 166));
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        final JButton saveBtn = new JButton("Create Booking");
        styleButton(saveBtn, SUCCESS_COLOR);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate required fields
                if (guestNameField.getText().trim().isEmpty() ||
                    checkinField.getText().trim().isEmpty() ||
                    checkoutField.getText().trim().isEmpty() ||
                    roomTypeCombo.getSelectedIndex() == 0) {
                    
                    JOptionPane.showMessageDialog(dialog,
                        "Please fill in all required fields:\n" +
                        "• Guest Name\n" +
                        "• Check-in Date\n" +
                        "• Check-out Date\n" +
                        "• Room Type",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Generate a new booking ID
                String bookingId = "BK" + String.format("%03d", (bookingTableModel.getRowCount() + 1));
                
                // Determine room number based on room type
                String roomNumber = "";
                String roomType = roomTypeCombo.getSelectedItem().toString();
                switch (roomType) {
                    case "Single Room": roomNumber = "1" + String.format("%02d", (int)(Math.random() * 20) + 1); break;
                    case "Double Room": roomNumber = "2" + String.format("%02d", (int)(Math.random() * 20) + 1); break;
                    case "Suite": roomNumber = "3" + String.format("%02d", (int)(Math.random() * 10) + 1); break;
                    case "Executive Suite": roomNumber = "5" + String.format("%02d", (int)(Math.random() * 5) + 1); break;
                    default: roomNumber = "TBD";
                }
                
                // Calculate price based on room type and duration
                double pricePerNight = 0;
                switch (roomType) {
                    case "Single Room": pricePerNight = 89.99; break;
                    case "Double Room": pricePerNight = 129.99; break;
                    case "Suite": pricePerNight = 249.99; break;
                    case "Executive Suite": pricePerNight = 349.99; break;
                }
                
                // Simple duration calculation (for demo)
                int nights = 5; // In real app, calculate from dates
                double totalAmount = pricePerNight * nights;
                
                // Create the new booking row
                Object[] newBooking = {
                    bookingId,
                    guestNameField.getText().trim(),
                    "Room " + roomNumber,
                    checkinField.getText().trim(),
                    checkoutField.getText().trim(),
                    "Confirmed",
                    "$" + String.format("%.2f", totalAmount),
                    "View | Edit | Cancel"
                };
                
                // Add the new booking to the table
                bookingTableModel.addRow(newBooking);
                
                // Show success message with booking details
                JOptionPane.showMessageDialog(dialog,
                    "✅ Booking created successfully!\n\n" +
                    "Booking ID: " + bookingId + "\n" +
                    "Guest: " + guestNameField.getText().trim() + "\n" +
                    "Room: " + roomNumber + " (" + roomType + ")\n" +
                    "Check-in: " + checkinField.getText().trim() + "\n" +
                    "Check-out: " + checkoutField.getText().trim() + "\n" +
                    "Amount: $" + String.format("%.2f", totalAmount) + "\n" +
                    "Status: Confirmed\n\n" +
                    "Confirmation sent to guest email.",
                    "Booking Created",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        formPanel.add(buttonPanel, gbc);
        
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void generateBookingReport() {
        String[] reportTypes = {"Daily Bookings", "Monthly Occupancy", "Revenue Analysis", "Guest Statistics"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select report type:",
            "Generate Booking Report",
            JOptionPane.QUESTION_MESSAGE,
            null,
            reportTypes,
            reportTypes[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "📊 Generating " + selected + " Report...\n\n" +
                "Report includes:\n" +
                "• Booking trends and patterns\n" +
                "• Revenue breakdown\n" +
                "• Occupancy rates\n" +
                "• Guest demographics\n" +
                "• Peak period analysis\n\n" +
                "Report will be saved as PDF and Excel formats.",
                "Booking Report",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void printTodaySchedule() {
        JOptionPane.showMessageDialog(this,
            "📋 Today's Schedule (Dec 20, 2024):\n\n" +
            "Check-ins (8):\n" +
            "  10:00 AM - Robert Brown (Suite 301)\n" +
            "  02:00 PM - Michael Chen (Room 214)\n" +
            "  04:00 PM - Sarah Johnson (Room 108)\n\n" +
            "Check-outs (6):\n" +
            "  11:00 AM - Michael Davis (Room 312)\n" +
            "  12:00 PM - Lisa Wilson (Room 205)\n\n" +
            "Special Events: Christmas Party in Ballroom (7 PM)",
            "Today's Schedule",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void sendBookingReminders() {
        String[] reminderTypes = {"Check-in Reminders", "Check-out Reminders", "Payment Reminders", "All"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select reminder type:",
            "Send Reminders",
            JOptionPane.QUESTION_MESSAGE,
            null,
            reminderTypes,
            reminderTypes[0]);
        
        if (selected != null) {
            int count = 12; // Example count
            
            JOptionPane.showMessageDialog(this,
                "📧 " + selected + " Sent!\n\n" +
                "✅ " + count + " reminders sent successfully\n" +
                "Delivery methods:\n" +
                "• Email notifications\n" +
                "• SMS alerts (if enabled)\n" +
                "• Mobile app push notifications\n\n" +
                "Guests will receive reminders 24 hours in advance.",
                "Reminders Sent",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void batchUpdateBookings() {
        String[] updateTypes = {"Extend Stay", "Change Room", "Update Status", "Apply Discount"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select batch operation:",
            "Batch Update",
            JOptionPane.QUESTION_MESSAGE,
            null,
            updateTypes,
            updateTypes[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "🔄 Batch Update: " + selected + "\n\n" +
                "Operation would apply to selected bookings.\n" +
                "Real implementation would show a selection dialog\n" +
                "and process updates in bulk.",
                "Batch Update",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void exportBookingsData() {
        String[] formats = {"Excel (.xlsx)", "CSV (.csv)", "PDF Report", "JSON"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select export format:",
            "Export Bookings",
            JOptionPane.QUESTION_MESSAGE,
            null,
            formats,
            formats[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "📁 Exporting booking data as " + selected + "...\n\n" +
                "Export includes:\n" +
                "• All booking details\n" +
                "• Guest information\n" +
                "• Financial data\n" +
                "• Date range: Last 12 months\n\n" +
                "File will be saved to Downloads folder.",
                "Data Export",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void generateBookingVouchers() {
        JOptionPane.showMessageDialog(this,
            "🎫 Generating Booking Vouchers...\n\n" +
            "Vouchers include:\n" +
            "• Booking confirmation\n" +
            "• Room details\n" +
            "• Check-in instructions\n" +
            "• Hotel amenities guide\n" +
            "• Map and directions\n\n" +
            "Vouchers will be:\n" +
            "• Emailed to guests\n" +
            "• Available for printing\n" +
            "• Stored in booking record",
            "Generate Vouchers",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setBookingAlerts() {
        JPanel alertPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        alertPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        alertPanel.add(new JLabel("Alert Type:"));
        JComboBox<String> alertType = new JComboBox<>(new String[]{
            "Check-in Alert", "Check-out Alert", "Payment Due", "Special Request", "Maintenance"
        });
        alertPanel.add(alertType);
        
        alertPanel.add(new JLabel("When to Alert:"));
        JComboBox<String> alertTime = new JComboBox<>(new String[]{
            "1 hour before", "3 hours before", "1 day before", "3 days before", "1 week before"
        });
        alertPanel.add(alertTime);
        
        alertPanel.add(new JLabel("Recipients:"));
        JTextField recipients = new JTextField("reception@hotel.com, manager@hotel.com");
        alertPanel.add(recipients);
        
        alertPanel.add(new JLabel("Message:"));
        JTextArea message = new JTextArea("Automated alert: Booking event upcoming");
        message.setLineWrap(true);
        alertPanel.add(new JScrollPane(message));
        
        int result = JOptionPane.showConfirmDialog(this, alertPanel, 
            "Set Booking Alert", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this,
                "🔔 Alert Created Successfully!\n\n" +
                "Type: " + alertType.getSelectedItem() + "\n" +
                "Timing: " + alertTime.getSelectedItem() + "\n" +
                "Recipients: " + recipients.getText() + "\n\n" +
                "Alerts will be sent automatically for all matching bookings.",
                "Alert Configured",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ==================== FINANCIAL ACTION METHODS ====================
    private void applyDateRange() {
        JOptionPane.showMessageDialog(this,
            "📅 Date range applied successfully!\n\n" +
            "All financial data has been updated for the selected period.\n" +
            "KPI cards and charts refreshed.",
            "Date Range Applied",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void generateFinancialReport() {
        String[] reportTypes = {"Executive Summary", "Detailed Analysis", "Comparative Report", "Forecast Report"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select report type:",
            "Generate Financial Report",
            JOptionPane.QUESTION_MESSAGE,
            null,
            reportTypes,
            reportTypes[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "📈 Generating " + selected + "...\n\n" +
                "Report includes:\n" +
                "• Executive summary\n" +
                "• Financial performance metrics\n" +
                "• Revenue and expense breakdown\n" +
                "• Profitability analysis\n" +
                "• Key trends and insights\n" +
                "• Recommendations\n\n" +
                "Report will be saved as PDF and Excel formats.",
                "Financial Report",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void exportFinancialData() {
        String[] formats = {"Excel (.xlsx)", "CSV (.csv)", "PDF Report", "JSON Data", "XML"};
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select export format:",
            "Export Financial Data",
            JOptionPane.QUESTION_MESSAGE,
            null,
            formats,
            formats[0]);
        
        if (selected != null) {
            JOptionPane.showMessageDialog(this,
                "💾 Exporting financial data as " + selected + "...\n\n" +
                "Export includes:\n" +
                "• All financial transactions\n" +
                "• Revenue breakdown by category\n" +
                "• Expense details\n" +
                "• Profit margins\n" +
                "• Comparative data\n\n" +
                "File will be saved to: /exports/financial_report" + 
                (selected.contains("Excel") ? ".xlsx" : 
                 selected.contains("CSV") ? ".csv" :
                 selected.contains("PDF") ? ".pdf" :
                 selected.contains("JSON") ? ".json" : ".xml"),
                "Data Export",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showDailySalesReport() {
        showFinancialDialog("Daily Sales Report", 
            "📊 Daily Sales Analysis for Dec 20, 2024\n\n" +
            "Total Revenue: $4,250\n" +
            "Room Revenue: $3,200\n" +
            "Restaurant Revenue: $850\n" +
            "Services Revenue: $200\n\n" +
            "Top Performing Rooms:\n" +
            "• Suite 301: $1,599\n" +
            "• Rooms 101-102: $1,250\n" +
            "• Room 205: $899\n\n" +
            "Trend: ▲ 12.5% vs Yesterday");
    }
    
    private void showMonthlyIncomeStatement() {
        showFinancialDialog("Monthly Income Statement", 
            "💰 Income Statement - December 2024\n\n" +
            "REVENUE\n" +
            "Room Revenue: $38,400\n" +
            "Restaurant Revenue: $5,200\n" +
            "Services Revenue: $1,600\n" +
            "Total Revenue: $45,200\n\n" +
            "EXPENSES\n" +
            "Staff Costs: $18,500\n" +
            "Utilities: $4,200\n" +
            "Supplies: $3,800\n" +
            "Marketing: $2,900\n" +
            "Total Expenses: $29,400\n\n" +
            "NET PROFIT: $15,800 (34.9% Margin)");
    }
    
    private void showPaymentMethodAnalysis() {
        showFinancialDialog("Payment Method Analysis", 
            "💳 Payment Method Breakdown - Last 30 Days\n\n" +
            "Credit Card: 65% ($29,380)\n" +
            "Debit Card: 20% ($9,040)\n" +
            "Cash: 8% ($3,616)\n" +
            "Digital Wallet: 5% ($2,260)\n" +
            "Bank Transfer: 2% ($904)\n\n" +
            "Trends:\n" +
            "• Digital payments up 15% vs last month\n" +
            "• Cash usage down 8%\n" +
            "• Average transaction: $245");
    }
    
    private void showRoomRevenueBreakdown() {
        showFinancialDialog("Room Revenue Breakdown", 
            "🏨 Room Category Revenue - December 2024\n\n" +
            "Executive Suites: $12,500 (27.7%)\n" +
            "Standard Suites: $9,800 (21.7%)\n" +
            "Double Rooms: $14,200 (31.4%)\n" +
            "Single Rooms: $8,700 (19.2%)\n\n" +
            "Performance Metrics:\n" +
            "Occupancy Rate: 78%\n" +
            "Average Daily Rate: $189\n" +
            "Revenue per Available Room: $147\n" +
            "RevPAR Growth: ▲ 8.2%");
    }
    
    private void showRestaurantServicesReport() {
        showFinancialDialog("Restaurant & Services Report", 
            "🍽️ F&B & Services Revenue - December 2024\n\n" +
            "RESTAURANT\n" +
            "Breakfast: $1,800\n" +
            "Lunch: $1,500\n" +
            "Dinner: $1,900\n" +
            "Total Restaurant: $5,200\n\n" +
            "SERVICES\n" +
            "Spa & Wellness: $800\n" +
            "Conference Rooms: $400\n" +
            "Laundry: $300\n" +
            "Transport: $100\n" +
            "Total Services: $1,600\n\n" +
            "Average Spend per Guest: $42");
    }
    
    private void showExpenseAnalysisReport() {
        showFinancialDialog("Expense Analysis Report", 
            "📉 Expense Analysis - December 2024\n\n" +
            "OPERATING EXPENSES\n" +
            "Staff Salaries: $15,200\n" +
            "Staff Benefits: $3,300\n" +
            "Total Staff Costs: $18,500\n\n" +
            "OPERATIONAL COSTS\n" +
            "Utilities: $4,200\n" +
            "Cleaning Supplies: $1,500\n" +
            "Food & Beverage: $2,300\n" +
            "Total Operational: $8,000\n\n" +
            "ADMINISTRATIVE\n" +
            "Marketing: $2,900\n" +
            "Insurance: $1,200\n" +
            "Software Licenses: $800\n" +
            "Total Admin: $4,900\n\n" +
            "TOTAL EXPENSES: $31,400");
    }
    
    private void showStaffPerformanceReport() {
        showFinancialDialog("Staff Performance Report", 
            "👥 Staff Performance Metrics - December 2024\n\n" +
            "TOP PERFORMERS\n" +
            "1. Sarah (Reception): Revenue Generated: $8,200\n" +
            "2. John (Manager): Upsell Success: 42%\n" +
            "3. Mike (Reception): Guest Satisfaction: 98%\n\n" +
            "DEPARTMENT METRICS\n" +
            "Reception: Average Check-in Time: 4.2 min\n" +
            "Housekeeping: Rooms per Staff: 12/day\n" +
            "Restaurant: Table Turnover: 1.8 hrs\n\n" +
            "Overall Staff Productivity: 92%");
    }
    
    private void showBudgetVsActual() {
        showFinancialDialog("Budget vs Actual", 
            "🎯 Budget vs Actual Performance - December 2024\n\n" +
            "REVENUE\n" +
            "Budget: $48,000 | Actual: $45,200 | Variance: -$2,800 (-5.8%)\n\n" +
            "EXPENSES\n" +
            "Budget: $32,000 | Actual: $31,400 | Variance: +$600 (+1.9%)\n\n" +
            "PROFIT\n" +
            "Budget: $16,000 | Actual: $15,800 | Variance: -$200 (-1.3%)\n\n" +
            "KEY INSIGHTS\n" +
            "• Room revenue below target by 6.2%\n" +
            "• Expense control better than expected\n" +
            "• Profit margin within 1% of target");
    }
    
    private void showYearOverYearComparison() {
        showFinancialDialog("Year-over-Year Comparison", 
            "📈 Year-over-Year Comparison - December\n\n" +
            "2023 vs 2024 PERFORMANCE\n" +
            "Revenue: $40,500 → $45,200 (+11.6%)\n" +
            "Expenses: $28,900 → $31,400 (+8.7%)\n" +
            "Profit: $11,600 → $15,800 (+36.2%)\n\n" +
            "KEY METRICS IMPROVEMENT\n" +
            "Occupancy Rate: 72% → 78% (+8.3%)\n" +
            "Average Daily Rate: $175 → $189 (+8.0%)\n" +
            "Guest Satisfaction: 92% → 95% (+3.3%)\n\n" +
            "TREND ANALYSIS\n" +
            "• Strong revenue growth\n" +
            "• Improved profitability\n" +
            "• Enhanced guest experience");
    }
    
    private void showFinancialDialog(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(new Color(250, 250, 250));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    // ==================== SETTINGS ACTION METHODS ====================
    private void saveGeneralSettings() {
        JOptionPane.showMessageDialog(this,
            "✅ General settings saved successfully!\n\n" +
            "Changes will take effect immediately.\n" +
            "Some settings may require restarting the application.",
            "Settings Saved",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void saveHotelConfig() {
        JOptionPane.showMessageDialog(this,
            "🏨 Hotel configuration updated!\n\n" +
            "All room pricing and tax calculations will use the new values.\n" +
            "Staff have been notified of check-in/check-out time changes.",
            "Hotel Config Saved",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void saveBookingSettings() {
        JOptionPane.showMessageDialog(this,
            "📅 Booking settings updated!\n\n" +
            "New policies will apply to all future bookings.\n" +
            "Existing bookings will maintain their original terms.",
            "Booking Settings Saved",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void saveEmailSettings() {
        JOptionPane.showMessageDialog(this,
            "📧 Email settings saved successfully!\n\n" +
            "Email server configuration updated.\n" +
            "A test email has been sent to verify the settings.\n" +
            "All automated emails will use the new configuration.",
            "Email Settings Saved",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void saveSecuritySettings() {
        JOptionPane.showMessageDialog(this,
            "🔒 Security settings updated!\n\n" +
            "Enhanced security measures have been applied.\n" +
            "All users will be logged out and required to login again.\n" +
            "New password policies effective immediately.",
            "Security Settings Saved",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void saveBackupSettings() {
        JOptionPane.showMessageDialog(this,
            "🔄 Backup settings saved!\n\n" +
            "New backup schedule has been configured.\n" +
            "Next automated backup: Tomorrow at 02:00 AM\n" +
            "Retention policy applied to existing backups.",
            "Backup Settings Saved",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void runBackup() {
        // Show progress dialog
        final JDialog dialog = new JDialog(this, "Running Backup", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("💾 Creating System Backup...", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        JTextArea statusArea = new JTextArea(3, 30);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setText("Initializing backup process...\nPreparing database export...\nCompressing files...");
        
        content.add(title, BorderLayout.NORTH);
        content.add(progressBar, BorderLayout.CENTER);
        content.add(new JScrollPane(statusArea), BorderLayout.SOUTH);
        
        dialog.add(content);
        dialog.setVisible(true);
        
        // Simulate backup process with timer
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                JOptionPane.showMessageDialog(AdminDashboard.this,
                    "✅ Backup completed successfully!\n\n" +
                    "Backup Summary:\n" +
                    "• Database: 15.2 MB\n" +
                    "• Files: 30.0 MB\n" +
                    "• Total: 45.2 MB\n" +
                    "• Location: /backups/hospitality/backup_20241220_1500.zip\n" +
                    "• Duration: 2.3 seconds\n\n" +
                    "Backup verified and ready for use.",
                    "Backup Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void runSystemDiagnostics() {
        JOptionPane.showMessageDialog(this,
            "🔧 Running System Diagnostics...\n\n" +
            "✅ Database Connection: Optimal\n" +
            "✅ Disk Space: 245 GB free\n" +
            "✅ Memory Usage: 65% (Normal)\n" +
            "✅ CPU Load: 12% (Low)\n" +
            "✅ Network: Connected (45 Mbps)\n" +
            "✅ Services: All running\n" +
            "✅ Security: No threats detected\n" +
            "✅ Backup System: Ready\n\n" +
            "🎉 All systems are healthy and performing optimally.",
            "System Diagnostics",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearCache() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear system cache?\n\n" +
            "This will remove:\n" +
            "• Temporary files\n" +
            "• Image thumbnails\n" +
            "• Session data\n" +
            "• Logged-out user sessions\n\n" +
            "System performance may be temporarily affected.",
            "Clear System Cache",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "🗑️ Cache cleared successfully!\n\n" +
                "• Removed: 245 MB of temporary files\n" +
                "• Database cache: Optimized\n" +
                "• User sessions: Cleaned\n" +
                "• System performance: Improved\n\n" +
                "The application may run faster now.",
                "Cache Cleared",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void viewPerformanceLogs() {
        JTextArea logsArea = new JTextArea(20, 60);
        logsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logsArea.setText(
            "=== Performance Logs - Last 24 Hours ===\n" +
            "2024-12-20 14:30: Database query: 0.045s\n" +
            "2024-12-20 14:15: User login: 0.012s\n" +
            "2024-12-20 13:45: Report generation: 1.234s\n" +
            "2024-12-20 12:30: Backup completed: 2.345s\n" +
            "2024-12-20 11:15: Email batch: 0.567s\n" +
            "2024-12-20 10:00: System check: 0.123s\n\n" +
            "=== System Metrics ===\n" +
            "Average Response Time: 0.456s\n" +
            "Peak Memory Usage: 512 MB\n" +
            "Database Connections: 12 active\n" +
            "Active Users: 8 concurrent\n\n" +
            "=== Recommendations ===\n" +
            "• System performance: Excellent\n" +
            "• No action required\n"
        );
        logsArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(logsArea);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "📊 Performance Logs", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void viewAuditTrail() {
        JTextArea auditArea = new JTextArea(20, 60);
        auditArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        auditArea.setText(
            "=== System Audit Trail - Today ===\n" +
            "2024-12-20 15:30: admin - Modified user permissions\n" +
            "2024-12-20 15:15: admin - Generated financial report\n" +
            "2024-12-20 14:45: manager1 - Updated room rates\n" +
            "2024-12-20 14:30: admin - Changed system settings\n" +
            "2024-12-20 14:15: reception1 - Checked-in guest\n" +
            "2024-12-20 13:45: admin - Exported booking data\n" +
            "2024-12-20 13:30: manager1 - Viewed revenue report\n" +
            "2024-12-20 12:45: admin - Created new user\n" +
            "2024-12-20 12:30: admin - Modified booking\n" +
            "2024-12-20 11:45: reception1 - Processed payment\n\n" +
            "=== Security Events ===\n" +
            "No unauthorized access attempts\n" +
            "All logins successful\n" +
            "No policy violations\n"
        );
        auditArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(auditArea);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "🔍 System Audit Trail", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void checkForUpdates() {
        JOptionPane.showMessageDialog(this,
            "🛠️ Checking for updates...\n\n" +
            "Current Version: v1.0.0\n" +
            "Latest Version: v1.0.0\n\n" +
            "✅ Your system is up to date!\n\n" +
            "Last checked: Today 15:30\n" +
            "Next check: Tomorrow 03:00\n\n" +
            "No updates available at this time.",
            "System Update Check",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void viewLogFiles() {
        String[] logFiles = {
            "system.log (45.2 MB)",
            "error.log (2.1 MB)", 
            "access.log (15.8 MB)",
            "database.log (8.4 MB)",
            "email.log (1.2 MB)",
            "security.log (3.5 MB)"
        };
        
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select log file to view:",
            "View Log Files",
            JOptionPane.QUESTION_MESSAGE,
            null,
            logFiles,
            logFiles[0]);
        
        if (selected != null) {
            JTextArea logContent = new JTextArea(25, 70);
            logContent.setFont(new Font("Monospaced", Font.PLAIN, 10));
            logContent.setText(
                "=== " + selected + " - Last 100 lines ===\n\n" +
                "2024-12-20 15:30: INFO - System backup completed\n" +
                "2024-12-20 15:15: INFO - Email sent to guest@email.com\n" +
                "2024-12-20 15:00: INFO - User 'admin' logged in\n" +
                "2024-12-20 14:45: INFO - Database connection pool refreshed\n" +
                "2024-12-20 14:30: INFO - Cache cleared by system\n" +
                "2024-12-20 14:15: INFO - Report generated successfully\n" +
                "2024-12-20 14:00: INFO - System maintenance completed\n" +
                "2024-12-20 13:45: INFO - New booking created: BK009\n" +
                "2024-12-20 13:30: INFO - Payment processed: $450.00\n" +
                "2024-12-20 13:15: INFO - User session started\n" +
                "2024-12-20 13:00: INFO - System check passed\n"
            );
            logContent.setEditable(false);
            logContent.setCaretPosition(0);
            
            JScrollPane scrollPane = new JScrollPane(logContent);
            
            JOptionPane.showMessageDialog(this, scrollPane, "📋 Log File: " + selected, JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    private void restartServices() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "⚠️ Restart System Services?\n\n" +
            "This will temporarily affect:\n" +
            "• Email notifications (2-3 minutes)\n" +
            "• Real-time updates (1-2 minutes)\n" +
            "• External API connections (1 minute)\n\n" +
            "Database and core functions will remain available.\n" +
            "Proceed with service restart?",
            "Restart Services",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Show progress
            final JDialog dialog = new JDialog(this, "Restarting Services", true);
            dialog.setSize(400, 150);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            
            JPanel content = new JPanel(new BorderLayout());
            content.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel title = new JLabel("🔄 Restarting system services...", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            
            content.add(title, BorderLayout.NORTH);
            content.add(progressBar, BorderLayout.CENTER);
            
            dialog.add(content);
            dialog.setVisible(true);
            
            // Simulate restart
            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "✅ Services restarted successfully!\n\n" +
                        "All system services are now running:\n" +
                        "• Email Service: Running\n" +
                        "• Notification Service: Running\n" +
                        "• API Service: Running\n" +
                        "• Database Service: Running\n" +
                        "• Backup Service: Running\n\n" +
                        "Downtime: 1.2 seconds",
                        "Services Restarted",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    private void viewUsageStatistics() {
        JTextArea statsArea = new JTextArea(20, 60);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statsArea.setText(
            "=== System Usage Statistics - Last 7 Days ===\n\n" +
            "👥 USER ACTIVITY\n" +
            "Total Logins: 245\n" +
            "Active Users: 42\n" +
            "Peak Concurrent: 18\n" +
            "Avg Session Time: 28.5 minutes\n\n" +
            "📅 BOOKING ACTIVITY\n" +
            "New Bookings: 56\n" +
            "Check-ins: 48\n" +
            "Check-outs: 45\n" +
            "Cancellations: 8\n" +
            "Revenue Generated: $45,200\n\n" +
            "📊 SYSTEM USAGE\n" +
            "Database Queries: 12,450\n" +
            "Emails Sent: 245\n" +
            "Reports Generated: 34\n" +
            "Backups Created: 7\n" +
            "API Calls: 3,450\n\n" +
            "📈 TRENDS\n" +
            "User growth: +12% week-over-week\n" +
            "Revenue growth: +8% week-over-week\n" +
            "System uptime: 99.8%\n" +
            "Avg response time: 0.45s\n"
        );
        statsArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(statsArea);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "📈 Usage Statistics", JOptionPane.PLAIN_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminDashboard("admin");
            }
        });
    }
}