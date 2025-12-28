package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton loginButton;
    
    public LoginScreen() {
        setTitle("Hospitality System Login");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 245, 250));
        
        // Add components
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel("🏨");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Hospitality System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subLabel = new JLabel("Login Portal");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(new Color(220, 240, 255));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(iconLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(subLabel);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        // Username field
        addCompactFormField(panel, "Username:", usernameField = createCompactTextField());
        
        // Password field
        addCompactFormField(panel, "Password:", passwordField = createCompactPasswordField());
        
        // Role selection
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] roles = {"Select Role", "Client", "Receptionist", "Manager", "Administrator"};
        roleCombo = new JComboBox<String>(roles);
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleCombo.setMaximumSize(new Dimension(280, 30));
        roleCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(roleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(roleCombo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(200, 40));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        
        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Forgot password
        JButton forgotButton = new JButton("Forgot Password?");
        forgotButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        forgotButton.setForeground(new Color(52, 152, 219));
        forgotButton.setBorderPainted(false);
        forgotButton.setContentAreaFilled(false);
        forgotButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(forgotButton);
        
        return panel;
    }
    
    private void addCompactFormField(JPanel panel, String label, JComponent field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 0));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setMaximumSize(new Dimension(350, 45));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jLabel.setPreferredSize(new Dimension(80, 25));
        
        fieldPanel.add(jLabel, BorderLayout.WEST);
        fieldPanel.add(field, BorderLayout.CENTER);
        
        panel.add(fieldPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private JTextField createCompactTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        field.setMaximumSize(new Dimension(250, 30));
        return field;
    }
    
    private JPasswordField createCompactPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        field.setMaximumSize(new Dimension(250, 30));
        return field;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel leftLabel = new JLabel("© 2024 Hospitality System");
        leftLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        leftLabel.setForeground(new Color(120, 120, 120));
        
        JLabel rightLabel = new JLabel("v1.0");
        rightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        rightLabel.setForeground(new Color(120, 120, 120));
        
        panel.add(leftLabel, BorderLayout.WEST);
        panel.add(rightLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();
            
            // Validation
            if (username.isEmpty() || password.isEmpty() || role.equals("Select Role")) {
                JOptionPane.showMessageDialog(this,
                    "Please fill in all fields!",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Check credentials
            boolean authenticated = false;
            
            if (username.equals("admin") && password.equals("admin") && role.equals("Administrator")) {
                authenticated = true;
            } else if (username.equals("manager") && password.equals("123") && role.equals("Manager")) {
                authenticated = true;
            } else if (username.equals("reception") && password.equals("123") && role.equals("Receptionist")) {
                authenticated = true;
            } else if (username.equals("client") && password.equals("123") && role.equals("Client")) {
                authenticated = true;
            }
            
            if (authenticated) {
                JOptionPane.showMessageDialog(this,
                    "Login successful!\nWelcome " + username,
                    "Access Granted",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
                openDashboard(username, role);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username, password, or role!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openDashboard(String username, String role) {
        try {
            if (role.equals("Administrator")) {
                new AdminDashboard(username);
            } else if (role.equals("Manager")) {
                // Now opens the actual ManagerDashboard instead of showing message
                new ManagerDashboard(username);
            } else if (role.equals("Receptionist")) {
                new ReceptionistDashboard(username);
            } else if (role.equals("Client")) {
                new ClientDashboard(username);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error opening dashboard: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen();
            }
        });
    }
}