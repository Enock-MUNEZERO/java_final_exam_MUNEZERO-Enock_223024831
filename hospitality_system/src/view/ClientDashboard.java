package view;

import javax.swing.*;
import java.awt.*;

public class ClientDashboard extends BaseDashboard {
    
    public ClientDashboard(String username) {
        super(username, "Client");
        setVisible(true);
    }
    
    @Override
    protected String getDashboardTitle() {
        return "Client Dashboard - Hospitality System";
    }
    
    @Override
    protected Color getHeaderColor() {
        return new Color(60, 140, 200); // Blue color for client
    }
    
    // Override createUI to add client-specific features
    @Override
    protected void createUI() {
        super.createUI(); // Call parent's createUI first
        
        // Add client-specific components
        addClientSpecificFeatures();
    }
    
    private void addClientSpecificFeatures() {
        // Get the content panel
        Container content = getContentPane();
        
        // Replace the generic content with client-specific content
        content.removeAll();
        
        // Header
        content.add(createHeader(), BorderLayout.NORTH);
        
        // Main content with tabs
        JTabbedPane tabs = new JTabbedPane();
        
        // Tab 1: Book a Room
        tabs.addTab("🔍 Book Room", createBookRoomPanel());
        
        // Tab 2: My Bookings
        tabs.addTab("📅 My Bookings", createMyBookingsPanel());
        
        // Tab 3: Profile
        tabs.addTab("👤 My Profile", createProfilePanel());
        
        content.add(tabs, BorderLayout.CENTER);
        
        // Footer
        content.add(createFooter(), BorderLayout.SOUTH);
        
        revalidate();
        repaint();
    }
    
    private JPanel createBookRoomPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel title = new JLabel("Book a Room");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, gbc);
        
        // Search criteria
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Check-in Date:"), gbc);
        
        gbc.gridx = 1;
        JTextField checkinField = new JTextField(15);
        panel.add(checkinField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Check-out Date:"), gbc);
        
        gbc.gridx = 1;
        JTextField checkoutField = new JTextField(15);
        panel.add(checkoutField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Guests:"), gbc);
        
        gbc.gridx = 1;
        String[] guests = {"1", "2", "3", "4+"};
        JComboBox<String> guestsCombo = new JComboBox<>(guests);
        panel.add(guestsCombo, gbc);
        
        // Search button
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton searchBtn = new JButton("🔍 Search Available Rooms");
        searchBtn.setBackground(new Color(50, 150, 100));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Searching rooms for:\n" +
                "Check-in: " + checkinField.getText() + "\n" +
                "Check-out: " + checkoutField.getText() + "\n" +
                "Guests: " + guestsCombo.getSelectedItem(),
                "Search Rooms",
                JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(searchBtn, gbc);
        
        return panel;
    }
    
    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table of bookings
        String[] columns = {"Booking ID", "Room", "Dates", "Status", "Amount"};
        Object[][] data = {
            {"B001", "Deluxe Suite", "2024-12-20 to 2024-12-25", "Confirmed", "$1,250"},
            {"B002", "Standard Room", "2024-12-28 to 2025-01-02", "Pending", "$450"}
        };
        
        JTable bookingsTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel("John Doe"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel("john.doe@email.com"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel("+1234567890"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Loyalty Points:"), gbc);
        
        gbc.gridx = 1;
        JLabel pointsLabel = new JLabel("2,450 points");
        pointsLabel.setForeground(Color.ORANGE);
        panel.add(pointsLabel, gbc);
        
        return panel;
    }
}