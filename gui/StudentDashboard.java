package gui;

import model.*;
import service.FileService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * StudentDashboard - Student role interface
 */
public class StudentDashboard extends JFrame {
    private Student student;
    private JTabbedPane tabbedPane;
    private DefaultTableModel sessionsTableModel;
    private DefaultTableModel mySubmissionsTableModel;
    private ArrayList<Session> allSessions;
    private ArrayList<Submission> mySubmissions;

    public StudentDashboard(Student student) {
        this.student = student;
        this.allSessions = FileService.loadAllSessions();
        this.mySubmissions = FileService.loadSubmissionsByStudent(student.getStudentID());

        setTitle("Student Dashboard - " + student.getName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header panel - simpler
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Student: " + student.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Tabbed pane - simpler
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Available Sessions", createAvailableSessionsPanel());
        tabbedPane.addTab("My Submissions", createMySubmissionsPanel());
        tabbedPane.addTab("Vote & Awards", createAwardsPanel());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createAvailableSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Available Sessions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Sessions table
        String[] columns = {"Session ID", "Title", "Type", "Date/Time", "Venue"};
        sessionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable sessionsTable = new JTable(sessionsTableModel);
        sessionsTable.setRowHeight(25);

        loadSessions();

        JScrollPane scrollPane = new JScrollPane(sessionsTable);

        // Register button - simpler
        JButton registerButton = new JButton("Register for Selected Session");
        registerButton.addActionListener(e -> showRegistrationDialog(sessionsTable));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(registerButton, BorderLayout.SOUTH);

        return panel;
    }

    private void loadSessions() {
        sessionsTableModel.setRowCount(0);
        for (Session session : allSessions) {
            sessionsTableModel.addRow(new Object[]{
                session.getSessionID(),
                session.getTitle(),
                session.getSessionType(),
                session.getDateTime(),
                session.getVenue()
            });
        }
    }

    private void showRegistrationDialog(JTable sessionsTable) {
        int selectedRow = sessionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a session first.");
            return;
        }

        String sessionID = (String) sessionsTableModel.getValueAt(selectedRow, 0);
        Session selectedSession = null;
        for (Session s : allSessions) {
            if (s.getSessionID().equals(sessionID)) {
                selectedSession = s;
                break;
            }
        }

        if (selectedSession == null) return;

        // Simple registration form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        formPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Abstract:"));
        JTextField abstractField = new JTextField();
        formPanel.add(abstractField);

        formPanel.add(new JLabel("Supervisor:"));
        JTextField supervisorField = new JTextField();
        formPanel.add(supervisorField);

        formPanel.add(new JLabel("File Path:"));
        JTextField fileField = new JTextField();
        formPanel.add(fileField);

        formPanel.add(new JLabel("Type:"));
        JComboBox<SessionType> typeCombo = new JComboBox<>(SessionType.values());
        formPanel.add(typeCombo);

        Session finalSession = selectedSession;
        int result = JOptionPane.showConfirmDialog(this, formPanel, 
            "Register for " + selectedSession.getTitle(), 
            JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String abstractText = abstractField.getText().trim();
            String supervisor = supervisorField.getText().trim();
            String filepath = fileField.getText().trim();
            SessionType type = (SessionType) typeCombo.getSelectedItem();

            if (title.isEmpty() || abstractText.isEmpty() || supervisor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                return;
            }

            String submissionID = FileService.generateNextID("SUB", "SubmissionData.csv");
            Submission submission = new Submission(submissionID, student.getStudentID(),
                title, abstractText, supervisor, filepath, type);
            submission.assignSession(finalSession);

            if (FileService.saveSubmission(submission)) {
                mySubmissions.add(submission);
                JOptionPane.showMessageDialog(this, "Registration successful!");
                refreshMySubmissions();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed!");
            }
        }
    }

    private JPanel createMySubmissionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("My Submissions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Submissions table
        String[] columns = {"Submission ID", "Title", "Type", "Status", "Score"};
        mySubmissionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable submissionsTable = new JTable(mySubmissionsTableModel);
        submissionsTable.setRowHeight(25);

        refreshMySubmissions();

        JScrollPane scrollPane = new JScrollPane(submissionsTable);

        // Buttons - simpler
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton viewButton = new JButton("View Evaluation");
        viewButton.addActionListener(e -> {
            int selectedRow = submissionsTable.getSelectedRow();
            if (selectedRow != -1) {
                String submissionID = (String) mySubmissionsTableModel.getValueAt(selectedRow, 0);
                showEvaluationDetails(submissionID);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a submission.");
            }
        });

        JButton voteButton = new JButton("Vote (People's Choice)");
        voteButton.addActionListener(e -> showVotingDialog());

        buttonPanel.add(viewButton);
        buttonPanel.add(voteButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshMySubmissions() {
        mySubmissionsTableModel.setRowCount(0);
        mySubmissions = FileService.loadSubmissionsByStudent(student.getStudentID());
        for (Submission sub : mySubmissions) {
            String score = sub.getEvaluationStatus() == EvaluationStat.COMPLETED ?
                String.valueOf(sub.getTotalScore()) + "/40" : "Not Evaluated";
            mySubmissionsTableModel.addRow(new Object[]{
                sub.getSubmissionID(),
                sub.getTitle(),
                sub.getPreferredType(),
                sub.getEvaluationStatus(),
                score
            });
        }
    }

    private void showEvaluationDetails(String submissionID) {
        Submission submission = null;
        for (Submission sub : mySubmissions) {
            if (sub.getSubmissionID().equals(submissionID)) {
                submission = sub;
                break;
            }
        }

        if (submission == null) return;

        JTextArea detailsArea = new JTextArea(15, 40);
        detailsArea.setEditable(false);
        detailsArea.setText(String.format(
            "Submission: %s\nTitle: %s\nType: %s\n\n%s",
            submission.getSubmissionID(),
            submission.getTitle(),
            submission.getPreferredType(),
            submission.getEvaluationDetails()
        ));

        JOptionPane.showMessageDialog(this, new JScrollPane(detailsArea), 
            "Evaluation Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showVotingDialog() {
        ArrayList<Submission> allSubmissions = FileService.loadAllSubmissions();

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Submission sub : allSubmissions) {
            if (!sub.getStudentID().equals(student.getStudentID())) {
                listModel.addElement(sub.getSubmissionID() + " - " + sub.getTitle());
            }
        }

        JList<String> submissionList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(submissionList);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        int result = JOptionPane.showConfirmDialog(this, scrollPane, 
            "Vote for People's Choice Award", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selected = submissionList.getSelectedValue();
            if (selected != null) {
                String submissionID = selected.split(" - ")[0];
                for (Submission sub : allSubmissions) {
                    if (sub.getSubmissionID().equals(submissionID)) {
                        sub.addVote();
                        break;
                    }
                }
                FileService.updateAllSubmissions(allSubmissions);
                JOptionPane.showMessageDialog(this, "Vote recorded!");
            }
        }
    }

    private JPanel createAwardsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Awards & Recognition");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextArea awardsArea = new JTextArea();
        awardsArea.setEditable(false);
        awardsArea.setText("Award winners will be displayed here.\n\nCategories:\n- Best Overall\n- Best Poster\n- Best Oral\n- People's Choice");

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(awardsArea), BorderLayout.CENTER);

        return panel;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            student.logout();
            dispose();
            new LoginScreen();
        }
    }
}
