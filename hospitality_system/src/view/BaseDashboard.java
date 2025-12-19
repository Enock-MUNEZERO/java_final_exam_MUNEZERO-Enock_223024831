package view;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDashboard extends JFrame {
    protected String username;
    protected String userRole;
    
    public BaseDashboard(String username, String userRole) {
        this.username = username;
        this.userRole = userRole;
        
        setTitle(getDashboardTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Basic setup
        createUI();
    }
    
    protected abstract String getDashboardTitle();
    protected abstract Color getHeaderColor();
    
    protected void createUI() {
        setLayout(new BorderLayout());
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Content
        add(createContent(), BorderLayout.CENTER);
        
        // Footer
        add(createFooter(), BorderLayout.SOUTH);
        
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(getHeaderColor());
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Title
        JLabel title = new JLabel(getDashboardTitle());
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        // User info
        JLabel userInfo = new JLabel("Welcome, " + username + " (" + userRole + ")");
        userInfo.setForeground(Color.WHITE);
        
        header.add(title, BorderLayout.WEST);
        header.add(userInfo, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        
        // Sidebar on left
        content.add(createSidebar(), BorderLayout.WEST);
        
        // Main content area
        JLabel mainContent = new JLabel(
            "<html><div style='padding: 50px; text-align: center;'>" +
            "<h1>" + getDashboardTitle() + "</h1>" +
            "<p>Select an option from the sidebar</p>" +
            "</div></html>",
            SwingConstants.CENTER
        );
        content.add(mainContent, BorderLayout.CENTER);
        
        return content;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 240, 240));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Common buttons for all dashboards
        String[] commonButtons = {"Dashboard", "Profile", "Settings", "Help", "Logout"};
        
        for (String btnText : commonButtons) {
            JButton button = new JButton(btnText);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(180, 40));
            button.setPreferredSize(new Dimension(180, 40));
            
            // Make Logout button red
            if (btnText.equals("Logout")) {
                button.setBackground(new Color(220, 80, 60));
                button.setForeground(Color.WHITE);
                button.addActionListener(e -> logout());
            }
            
            sidebar.add(button);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        return sidebar;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(240, 240, 240));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        
        JLabel footerText = new JLabel("© 2024 Hospitality System - " + userRole + " Portal");
        footer.add(footerText);
        
        return footer;
    }
    
    protected void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Close current window
            new LoginScreen(); // Open login screen
        }
    }
}