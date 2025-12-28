package view;

import javax.swing.*;
import java.awt.*;

public class ReceptionistDashboard extends BaseDashboard {
    
    public ReceptionistDashboard(String username) {
        super(username, "Receptionist");
        setVisible(true);
    }
    
    @Override
    protected String getDashboardTitle() {
        return "Receptionist Dashboard - Hospitality System";
    }
    
    @Override
    protected Color getHeaderColor() {
        return new Color(80, 120, 180); // Different blue for receptionist
    }
    
    @Override
    protected void createUI() {
        super.createUI();
        addReceptionistSpecificFeatures();
    }
    
    private void addReceptionistSpecificFeatures() {
        Container content = getContentPane();
        content.removeAll();
        
        // Header
        content.add(createHeader(), BorderLayout.NORTH);
        
        // Main content with tabs for receptionist
        JTabbedPane tabs = new JTabbedPane();
        
        tabs.addTab("✅ Check-in", createCheckInPanel());
        tabs.addTab("📤 Check-out", createCheckOutPanel());
        tabs.addTab("👥 All Guests", createAllGuestsPanel());
        tabs.addTab("🏨 Room Status", createRoomStatusPanel());
        
        content.add(tabs, BorderLayout.CENTER);
        content.add(createFooter(), BorderLayout.SOUTH);
        
        revalidate();
        repaint();
    }
    
    private JPanel createCheckInPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Guest Check-in");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Guest Name:"), gbc);
        
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Room Number:"), gbc);
        
        gbc.gridx = 1;
        JTextField roomField = new JTextField(10);
        panel.add(roomField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Booking ID:"), gbc);
        
        gbc.gridx = 1;
        JTextField bookingField = new JTextField(15);
        panel.add(bookingField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton checkinBtn = new JButton("✅ Process Check-in");
        checkinBtn.setBackground(new Color(50, 150, 100));
        checkinBtn.setForeground(Color.WHITE);
        checkinBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Checking in:\n" +
                "Guest: " + nameField.getText() + "\n" +
                "Room: " + roomField.getText() + "\n" +
                "Booking: " + bookingField.getText(),
                "Check-in Successful",
                JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(checkinBtn, gbc);
        
        return panel;
    }
    
    private JPanel createCheckOutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Select a guest to check-out:", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);
        
        // List of current guests
        String[] guests = {
            "John Doe - Room 101 - Check-in: 2024-12-20",
            "Jane Smith - Room 205 - Check-in: 2024-12-22",
            "Bob Wilson - Room 312 - Check-in: Today"
        };
        
        JList<String> guestList = new JList<>(guests);
        JScrollPane scrollPane = new JScrollPane(guestList);
        
        JButton checkoutBtn = new JButton("📤 Process Check-out");
        checkoutBtn.addActionListener(e -> {
            String selected = guestList.getSelectedValue();
            if (selected != null) {
                JOptionPane.showMessageDialog(this,
                    "Processing check-out for:\n" + selected + "\n" +
                    "Generating invoice...",
                    "Check-out",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(checkoutBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAllGuestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table showing all guests
        String[] columns = {"Name", "Room", "Check-in", "Check-out", "Status"};
        Object[][] data = {
            {"John Doe", "101", "2024-12-20", "2024-12-25", "Checked-in"},
            {"Jane Smith", "205", "2024-12-22", "2024-12-28", "Checked-in"},
            {"Bob Wilson", "312", "Today", "2024-12-30", "Checked-in"},
            {"Alice Brown", "108", "Tomorrow", "2025-01-05", "Reserved"}
        };
        
        JTable guestsTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(guestsTable);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRoomStatusPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 5, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create room status boxes
        for (int i = 101; i <= 120; i++) {
            JButton roomBtn = new JButton("Room " + i);
            roomBtn.setPreferredSize(new Dimension(80, 60));
            
            // Random status for demo
            int status = i % 4;
            switch (status) {
                case 0:
                    roomBtn.setBackground(Color.GREEN); // Available
                    roomBtn.setText("Room " + i + "\nAvailable");
                    break;
                case 1:
                    roomBtn.setBackground(Color.ORANGE); // Occupied
                    roomBtn.setText("Room " + i + "\nOccupied");
                    break;
                case 2:
                    roomBtn.setBackground(Color.RED); // Maintenance
                    roomBtn.setText("Room " + i + "\nMaintenance");
                    break;
                case 3:
                    roomBtn.setBackground(Color.YELLOW); // Cleaning
                    roomBtn.setText("Room " + i + "\nCleaning");
                    break;
            }
            
            panel.add(roomBtn);
        }
        
        return panel;
    }
}