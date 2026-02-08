package gui;

import model.*;
import service.FileService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginScreen - User authentication interface
 */
public class LoginScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginScreen() {
        setTitle("Academic Seminar Management System - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        // Main panel with simple layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title
        JLabel titleLabel = new JLabel("Seminar Management System - Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel);

        // Email
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(new JLabel("Email:"));
        emailField = new JTextField(20);
        emailPanel.add(emailField);
        mainPanel.add(emailPanel);

        // Password
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        // Role
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.add(new JLabel("Login as:"));
        String[] roles = {"Student", "Evaluator", "Coordinator"};
        roleComboBox = new JComboBox<>(roles);
        rolePanel.add(roleComboBox);
        mainPanel.add(rolePanel);

        // Login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton);

        // Status label
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        mainPanel.add(statusLabel);

        add(mainPanel);

        // Enter key listener
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String roleStr = (String) roleComboBox.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter email and password");
            return;
        }

        Role role;
        switch (roleStr) {
            case "Student":
                role = Role.STUDENT;
                break;
            case "Evaluator":
                role = Role.EVALUATOR;
                break;
            case "Coordinator":
                role = Role.STAFF;
                break;
            default:
                role = Role.STUDENT;
        }

        // Validate credentials
        if (FileService.validateCredentials(email, password, role)) {
            statusLabel.setText("Login successful!");
            statusLabel.setForeground(Color.GREEN);

            // Load user and open appropriate dashboard
            SwingUtilities.invokeLater(() -> {
                this.dispose();
                switch (role) {
                    case STUDENT:
                        Student student = FileService.loadStudentByEmail(email);
                        if (student != null) {
                            new StudentDashboard(student);
                        }
                        break;
                    case EVALUATOR:
                        Evaluator evaluator = FileService.loadEvaluatorByEmail(email);
                        if (evaluator != null) {
                            new EvaluatorDashboard(evaluator);
                        }
                        break;
                    case STAFF:
                        Coordinator coordinator = FileService.loadCoordinatorByEmail(email);
                        if (coordinator != null) {
                            new CoordinatorDashboard(coordinator);
                        }
                        break;
                }
            });
        } else {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Invalid credentials!");
        }
    }
}
