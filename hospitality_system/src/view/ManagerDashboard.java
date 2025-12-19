package view;

import javax.swing.*;
import java.awt.*;

public class ManagerDashboard extends BaseDashboard {
    
    public ManagerDashboard(String username) {
        super(username, "Manager");
        setVisible(true);
    }
    
    @Override
    protected String getDashboardTitle() {
        return "Manager Dashboard - Operational Oversight";
    }
    
    @Override
    protected Color getHeaderColor() {
        return new Color(140, 100, 180); // Purple for manager
    }
}