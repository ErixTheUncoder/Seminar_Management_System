package model;

import service.FileService;
import java.util.ArrayList;

/**
 * Evaluator class - Staff user role for evaluating sessions
 * Inherits from User class
 */
public class Evaluator extends User {
    private String evaluatorID;
    private String faculty;
    private String expertise;
    private ArrayList<Submission> assignedSubmissions;

    /**
     * Constructor to initialize Evaluator
     */
    public Evaluator(String name, String email, String password, String evaluatorID, 
                     String faculty, String expertise) {
        super(name, email, password, Role.EVALUATOR);
        this.evaluatorID = evaluatorID;
        this.faculty = faculty;
        this.expertise = expertise;
        this.assignedSubmissions = new ArrayList<>();
    }

    /**
     * Override login method
     */
    @Override
    public boolean login(String email, String password) {
        return FileService.validateCredentials(email, password, Role.EVALUATOR);
    }

    /**
     * Override logout method
     */
    @Override
    public void logout() {
        System.out.println("Evaluator " + getName() + " logged out successfully.");
    }

    /**
     * Get assigned submissions
     */
    public ArrayList<Submission> getAssignedSubmissions() {
        return new ArrayList<>(assignedSubmissions);
    }

    /**
     * Add submission to assigned list
     */
    public void addAssignedSubmission(Submission submission) {
        if (!assignedSubmissions.contains(submission)) {
            assignedSubmissions.add(submission);
        }
    }

    // Getter methods
    public String getEvaluatorID() {
        return evaluatorID;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getExpertise() {
        return expertise;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s, Expertise: %s", 
            getName(), evaluatorID, faculty, expertise);
    }
}
