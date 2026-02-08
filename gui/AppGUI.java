package gui;

import service.FileService;
import javax.swing.*;

/**
 * AppGUI - Main application entry point
 */
public class AppGUI {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize data files
        FileService.initializeDataFiles();

        // Launch login screen
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}
