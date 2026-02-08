package gui;

import model.*;
import service.FileService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * EvaluatorDashboard - Evaluator role interface
 */
public class EvaluatorDashboard extends JFrame {
    private Evaluator evaluator;
    private JTabbedPane tabbedPane;
    private DefaultTableModel submissionsTableModel;
    private ArrayList<Submission> assignedSubmissions;

    public EvaluatorDashboard(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.assignedSubmissions = FileService.loadSubmissionsByEvaluator(evaluator.getEvaluatorID());

        setTitle("Evaluator Dashboard - " + evaluator.getName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Simple header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Evaluator: " + evaluator.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Simple tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Assigned Submissions", createSubmissionsPanel());
        tabbedPane.addTab("Sessions", createSessionsPanel());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSubmissionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Assigned Submissions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        String[] columns = {"Submission ID", "Title", "Student ID", "Type", "Status"};
        submissionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable submissionsTable = new JTable(submissionsTableModel);
        submissionsTable.setRowHeight(25);

        refreshSubmissions();

        JScrollPane scrollPane = new JScrollPane(submissionsTable);

        // Simple buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton evaluateButton = new JButton("Evaluate Selected");
        evaluateButton.addActionListener(e -> {
            int selectedRow = submissionsTable.getSelectedRow();
            if (selectedRow != -1) {
                String submissionID = (String) submissionsTableModel.getValueAt(selectedRow, 0);
                showEvaluationDialog(submissionID);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a submission.");
            }
        });

        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> {
            int selectedRow = submissionsTable.getSelectedRow();
            if (selectedRow != -1) {
                String submissionID = (String) submissionsTableModel.getValueAt(selectedRow, 0);
                showSubmissionDetails(submissionID);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a submission.");
            }
        });

        buttonPanel.add(viewButton);
        buttonPanel.add(evaluateButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshSubmissions() {
        submissionsTableModel.setRowCount(0);
        assignedSubmissions = FileService.loadSubmissionsByEvaluator(evaluator.getEvaluatorID());
        for (Submission sub : assignedSubmissions) {
            submissionsTableModel.addRow(new Object[]{
                sub.getSubmissionID(),
                sub.getTitle(),
                sub.getStudentID(),
                sub.getPreferredType(),
                sub.getEvaluationStatus()
            });
        }
    }

    private void showSubmissionDetails(String submissionID) {
        Submission submission = null;
        for (Submission sub : assignedSubmissions) {
            if (sub.getSubmissionID().equals(submissionID)) {
                submission = sub;
                break;
            }
        }

        if (submission == null) return;

        JTextArea detailsArea = new JTextArea(20, 50);
        detailsArea.setEditable(false);

        String details = "SUBMISSION DETAILS\n\n" +
                        "ID: " + submission.getSubmissionID() + "\n" +
                        "Title: " + submission.getTitle() + "\n" +
                        "Student: " + submission.getStudentID() + "\n" +
                        "Type: " + submission.getPreferredType() + "\n" +
                        "Supervisor: " + submission.getSupervisorName() + "\n" +
                        "File: " + submission.getFilepath() + "\n\n" +
                        "ABSTRACT:\n" + submission.getAbstractText() + "\n\n" +
                        "EVALUATION:\n" + submission.getEvaluationDetails();

        detailsArea.setText(details);

        JOptionPane.showMessageDialog(this, new JScrollPane(detailsArea), 
            "Submission Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEvaluationDialog(String submissionID) {
        Submission submission = null;
        for (Submission sub : assignedSubmissions) {
            if (sub.getSubmissionID().equals(submissionID)) {
                submission = sub;
                break;
            }
        }

        if (submission == null) return;

        // Simple evaluation form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        formPanel.add(new JLabel("Problem Clarity (0-10):"));
        JSpinner claritySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        formPanel.add(claritySpinner);

        formPanel.add(new JLabel("Methodology (0-10):"));
        JSpinner methodologySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        formPanel.add(methodologySpinner);

        formPanel.add(new JLabel("Results (0-10):"));
        JSpinner resultsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        formPanel.add(resultsSpinner);

        formPanel.add(new JLabel("Presentation (0-10):"));
        JSpinner presentationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        formPanel.add(presentationSpinner);

        formPanel.add(new JLabel("Comments:"));
        JTextField commentsField = new JTextField();
        formPanel.add(commentsField);

        Submission finalSubmission = submission;
        int result = JOptionPane.showConfirmDialog(this, formPanel,
            "Evaluate: " + submission.getTitle(), JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int clarity = (Integer) claritySpinner.getValue();
            int methodology = (Integer) methodologySpinner.getValue();
            int results = (Integer) resultsSpinner.getValue();
            int presentation = (Integer) presentationSpinner.getValue();
            String comments = commentsField.getText().trim();

            if (comments.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please provide comments.");
                return;
            }

            boolean success = finalSubmission.provideEvaluation(
                evaluator.getEvaluatorID(), clarity, methodology, results, presentation, comments);

            if (success) {
                ArrayList<Submission> allSubmissions = FileService.loadAllSubmissions();
                for (int i = 0; i < allSubmissions.size(); i++) {
                    if (allSubmissions.get(i).getSubmissionID().equals(finalSubmission.getSubmissionID())) {
                        allSubmissions.set(i, finalSubmission);
                        break;
                    }
                }
                FileService.updateAllSubmissions(allSubmissions);
                JOptionPane.showMessageDialog(this, "Evaluation submitted!");
                refreshSubmissions();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit evaluation.");
            }
        }
    }

    private JPanel createSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Sessions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        ArrayList<Session> sessions = FileService.loadAllSessions();

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Session session : sessions) {
            listModel.addElement(session.toString());
        }

        JList<String> sessionList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(sessionList);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            evaluator.logout();
            dispose();
            new LoginScreen();
        }
    }
}
