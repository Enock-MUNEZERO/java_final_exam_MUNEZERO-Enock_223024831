package main;

import view.LoginScreen;
import javax.swing.*;

public class MainApplication {
    public static void main(String[] args) {
        // Simple launcher for Java 7
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen();
            }
        });
    }
}