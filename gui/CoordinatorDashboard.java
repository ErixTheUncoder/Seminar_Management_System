package gui;

import model.*;
import service.FileService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * CoordinatorDashboard - Coordinator role interface
 */
public class CoordinatorDashboard extends JFrame {
    private Coordinator coordinator;
    private JTabbedPane tabbedPane;
    private DefaultTableModel sessionsTableModel;
    private DefaultTableModel evaluatorsTableModel;
    private DefaultTableModel submissionsTableModel;
    private ArrayList<Session> allSessions;
    private ArrayList<Evaluator> allEvaluators;
    private ArrayList<Submission> allSubmissions;
    private ArrayList<Board> allBoards;

    public CoordinatorDashboard(Coordinator coordinator) {
        this.coordinator = coordinator;
        loadAllData();

        setTitle("Coordinator Dashboard - " + coordinator.getName());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setVisible(true);
    }

    private void loadAllData() {
        allSessions = FileService.loadAllSessions();
        allEvaluators = FileService.loadAllEvaluators();
        allSubmissions = FileService.loadAllSubmissions();
        allBoards = FileService.loadAllBoards();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Simple header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Coordinator: " + coordinator.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Simpler tabbed pane
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Sessions", createSessionsPanel());
        tabbedPane.addTab("Evaluators", createEvaluatorsPanel());
        tabbedPane.addTab("Submissions", createSubmissionsPanel());
        tabbedPane.addTab("Reports & Awards", createReportsPanel());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Session Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        String[] columns = {"Session ID", "Title", "Type", "Date/Time", "Venue"};
        sessionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable sessionsTable = new JTable(sessionsTableModel);
        sessionsTable.setRowHeight(25);

        refreshSessions();

        JScrollPane scrollPane = new JScrollPane(sessionsTable);

        // Simple buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton createButton = new JButton("Create Session");
        createButton.addActionListener(e -> showCreateSessionDialog());

        JButton assignButton = new JButton("Assign Evaluators");
        assignButton.addActionListener(e -> {
            int selectedRow = sessionsTable.getSelectedRow();
            if (selectedRow != -1) {
                String sessionID = (String) sessionsTableModel.getValueAt(selectedRow, 0);
                showAssignEvaluatorsDialog(sessionID);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a session.");
            }
        });

        buttonPanel.add(createButton);
        buttonPanel.add(assignButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshSessions() {
        sessionsTableModel.setRowCount(0);
        for (Session session : allSessions) {
            sessionsTableModel.addRow(new Object[]{
                session.getSessionID(),
                session.getTitle(),
                session.getSessionType(),
                session.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                session.getVenue()
            });
        }
    }

    private void showCreateSessionDialog() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        formPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Type:"));
        JComboBox<SessionType> typeCombo = new JComboBox<>(SessionType.values());
        formPanel.add(typeCombo);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        JTextField dateField = new JTextField("2026-03-15");
        formPanel.add(dateField);

        formPanel.add(new JLabel("Time (HH:MM):"));
        JTextField timeField = new JTextField("14:00");
        formPanel.add(timeField);

        formPanel.add(new JLabel("Venue:"));
        JTextField venueField = new JTextField();
        formPanel.add(venueField);

        int result = JOptionPane.showConfirmDialog(this, formPanel,
            "Create New Session", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText().trim();
                SessionType type = (SessionType) typeCombo.getSelectedItem();
                String dateStr = dateField.getText().trim() + " " + timeField.getText().trim();
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String venue = venueField.getText().trim();

                if (title.isEmpty() || venue.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    return;
                }

                String sessionID = FileService.generateNextID("SES", "SessionData.csv");
                Session session = new Session(sessionID, title, type, dateTime, venue);

                if (FileService.saveSession(session)) {
                    allSessions.add(session);
                    refreshSessions();
                    JOptionPane.showMessageDialog(this, "Session created!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create session.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date/time format!");
            }
        }
    }

    private void showEditSessionDialog(String sessionID) {
        JOptionPane.showMessageDialog(this, "Edit feature coming soon!");
    }

    private void showAssignEvaluatorsDialog(String sessionID) {
        Session session = null;
        for (Session s : allSessions) {
            if (s.getSessionID().equals(sessionID)) {
                session = s;
                break;
            }
        }
        if (session == null) return;

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Evaluator eval : allEvaluators) {
            listModel.addElement(eval.getEvaluatorID() + " - " + eval.getName());
        }

        JList<String> evaluatorList = new JList<>(listModel);
        evaluatorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(evaluatorList);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        Session finalSession = session;
        int result = JOptionPane.showConfirmDialog(this, scrollPane,
            "Assign Evaluators to " + session.getTitle(), JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            java.util.List<String> selectedValues = evaluatorList.getSelectedValuesList();
            if (selectedValues.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select evaluators.");
                return;
            }

            for (String selected : selectedValues) {
                String evalID = selected.split(" - ")[0];
                for (Evaluator eval : allEvaluators) {
                    if (eval.getEvaluatorID().equals(evalID)) {
                        finalSession.addEvaluator(eval);
                        break;
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Evaluators assigned!");
            refreshSessions();
        }
    }

    private JPanel createEvaluatorsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Evaluators");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        String[] columns = {"Evaluator ID", "Name", "Faculty", "Expertise"};
        evaluatorsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable evaluatorsTable = new JTable(evaluatorsTableModel);
        evaluatorsTable.setRowHeight(25);

        refreshEvaluators();

        JScrollPane scrollPane = new JScrollPane(evaluatorsTable);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshEvaluators() {
        evaluatorsTableModel.setRowCount(0);
        for (Evaluator eval : allEvaluators) {
            evaluatorsTableModel.addRow(new Object[]{
                eval.getEvaluatorID(),
                eval.getName(),
                eval.getFaculty(),
                eval.getExpertise()
            });
        }
    }

    private JPanel createSubmissionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Submissions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        String[] columns = {"Submission ID", "Title", "Student", "Type", "Status"};
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

        JButton assignButton = new JButton("Assign Evaluator");
        assignButton.addActionListener(e -> {
            int selectedRow = submissionsTable.getSelectedRow();
            if (selectedRow != -1) {
                String submissionID = (String) submissionsTableModel.getValueAt(selectedRow, 0);
                showAssignSubmissionDialog(submissionID);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a submission.");
            }
        });

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(assignButton, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshSubmissions() {
        submissionsTableModel.setRowCount(0);
        for (Submission sub : allSubmissions) {
            submissionsTableModel.addRow(new Object[]{
                sub.getSubmissionID(),
                sub.getTitle(),
                sub.getStudentID(),
                sub.getPreferredType(),
                sub.getEvaluationStatus()
            });
        }
    }

    private void showAssignSubmissionDialog(String submissionID) {
        Submission submission = null;
        for (Submission s : allSubmissions) {
            if (s.getSubmissionID().equals(submissionID)) {
                submission = s;
                break;
            }
        }
        if (submission == null) return;

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.add(new JLabel("Session:"));
        JComboBox<String> sessionCombo = new JComboBox<>();
        for (Session session : allSessions) {
            sessionCombo.addItem(session.getSessionID() + " - " + session.getTitle());
        }
        panel.add(sessionCombo);

        panel.add(new JLabel("Evaluator:"));
        JComboBox<String> evaluatorCombo = new JComboBox<>();
        for (Evaluator eval : allEvaluators) {
            evaluatorCombo.addItem(eval.getEvaluatorID() + " - " + eval.getName());
        }
        panel.add(evaluatorCombo);

        panel.add(new JLabel("Board (Poster):"));
        JComboBox<String> boardCombo = new JComboBox<>();
        boardCombo.addItem("None");
        for (Board board : allBoards) {
            if (board.isAvailable()) {
                boardCombo.addItem(board.getBoardID());
            }
        }
        panel.add(boardCombo);

        Submission finalSubmission = submission;
        int result = JOptionPane.showConfirmDialog(this, panel,
            "Assign " + submission.getTitle(), JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String sessionStr = (String) sessionCombo.getSelectedItem();
            String evaluatorStr = (String) evaluatorCombo.getSelectedItem();
            String boardStr = (String) boardCombo.getSelectedItem();

            String sessionID = sessionStr.split(" - ")[0];
            String evaluatorID = evaluatorStr.split(" - ")[0];

            Session selectedSession = null;
            Evaluator selectedEvaluator = null;
            Board selectedBoard = null;

            for (Session s : allSessions) {
                if (s.getSessionID().equals(sessionID)) {
                    selectedSession = s;
                    break;
                }
            }

            for (Evaluator ev : allEvaluators) {
                if (ev.getEvaluatorID().equals(evaluatorID)) {
                    selectedEvaluator = ev;
                    break;
                }
            }

            if (boardStr != null && !boardStr.equals("None")) {
                for (Board b : allBoards) {
                    if (b.getBoardID().equals(boardStr)) {
                        selectedBoard = b;
                        break;
                    }
                }
            }

            if (coordinator.scheduleSubmission(finalSubmission, selectedEvaluator, 
                                              selectedSession, selectedBoard)) {
                FileService.updateAllSubmissions(allSubmissions);
                if (selectedBoard != null) {
                    FileService.updateAllBoards(allBoards);
                }
                JOptionPane.showMessageDialog(this, "Assignment successful!");
                refreshSubmissions();
            }
        }
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Reports & Awards");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton reportButton = new JButton("Generate Report");
        reportButton.addActionListener(e -> {
            String report = "ANALYTICS REPORT\n\n" +
                          "Total Sessions: " + allSessions.size() + "\n" +
                          "Total Submissions: " + allSubmissions.size() + "\n" +
                          "Total Evaluators: " + allEvaluators.size() + "\n\n" +
                          "Evaluation Status:\n";
            
            int pending = 0, processing = 0, completed = 0;
            for (Submission sub : allSubmissions) {
                switch (sub.getEvaluationStatus()) {
                    case PENDING: pending++; break;
                    case PROCESSING: processing++; break;
                    case COMPLETED: completed++; break;
                }
            }
            report += "  Pending: " + pending + "\n";
            report += "  Processing: " + processing + "\n";
            report += "  Completed: " + completed + "\n";

            reportArea.setText(report);
        });

        JButton awardsButton = new JButton("Generate Awards");
        awardsButton.addActionListener(e -> {
            StringBuilder awards = new StringBuilder("AWARDS\n\n");

            ArrayList<Submission> completedSubs = new ArrayList<>();
            for (Submission sub : allSubmissions) {
                if (sub.getEvaluationStatus() == EvaluationStat.COMPLETED) {
                    completedSubs.add(sub);
                }
            }

            if (completedSubs.isEmpty()) {
                awards.append("No completed evaluations yet.\n");
            } else {
                completedSubs.sort(Comparator.comparingInt(Submission::getTotalScore).reversed());
                
                awards.append("Best Overall:\n");
                Submission best = completedSubs.get(0);
                awards.append("  ").append(best.getTitle()).append(" (").append(best.getTotalScore()).append("/40)\n\n");

                // People's Choice
                ArrayList<Submission> allSubs = new ArrayList<>(allSubmissions);
                allSubs.sort(Comparator.comparingInt(Submission::getPeopleVote).reversed());
                awards.append("People's Choice:\n");
                Submission pc = allSubs.get(0);
                awards.append("  ").append(pc.getTitle()).append(" (").append(pc.getPeopleVote()).append(" votes)\n");
            }

            reportArea.setText(awards.toString());
        });

        buttonPanel.add(reportButton);
        buttonPanel.add(awardsButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            coordinator.logout();
            dispose();
            new LoginScreen();
        }
    }
}
