package model;

import service.FileService;

/**
 * Coordinator class - Staff user role for managing seminars
 * Inherits from User class
 */
public class Coordinator extends User {
    private String staffID;
    private String faculty;
    private String officeContact;

    /**
     * Constructor to initialize Coordinator
     */
    public Coordinator(String name, String email, String password, String staffID, 
                       String faculty, String officeContact) {
        super(name, email, password, Role.STAFF);
        this.staffID = staffID;
        this.faculty = faculty;
        this.officeContact = officeContact;
    }

    /**
     * Override login method
     */
    @Override
    public boolean login(String email, String password) {
        return FileService.validateCredentials(email, password, Role.STAFF);
    }

    /**
     * Override logout method
     */
    @Override
    public void logout() {
        System.out.println("Coordinator " + getName() + " logged out successfully.");
    }

    /**
     * Schedule a submission - assign evaluator, session, and board
     */
    public boolean scheduleSubmission(Submission sub, Evaluator eval, Session session, Board board) {
        // Perform all memory updates
        sub.assignEvaluator(eval);
        sub.assignSession(session);
        
        if (board != null && sub.getPreferredType() == SessionType.POSTER) {
            session.assignBoard(board.getBoardID(), sub);
        }

        // Update session
        session.addSubmission(sub);
        
        return true;
    }

    // Getter methods
    public String getStaffID() {
        return staffID;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getOfficeContact() {
        return officeContact;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s, Office: %s", 
            getName(), staffID, faculty, officeContact);
    }
}
