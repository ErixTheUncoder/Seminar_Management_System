package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Session class - Represents a seminar session
 */
public class Session {
    private String sessionID;
    private String title;
    private SessionType sessionType;
    private LocalDateTime dateTime;
    private String venue;
    private ArrayList<Evaluator> assignedEvaluators;
    private ArrayList<Submission> uploadedSubmissions;
    private ArrayList<Board> availableBoards;

    /**
     * Constructor for Session
     */
    public Session(String sessionID, String title, SessionType sessionType, 
                   LocalDateTime dateTime, String venue) {
        this.sessionID = sessionID;
        this.title = title;
        this.sessionType = sessionType;
        this.dateTime = dateTime;
        this.venue = venue;
        this.assignedEvaluators = new ArrayList<>();
        this.uploadedSubmissions = new ArrayList<>();
        this.availableBoards = new ArrayList<>();
    }

    /**
     * Assign a board to a submission
     */
    public void assignBoard(String boardID, Submission submission) {
        if (submission.getPreferredType() == SessionType.POSTER) {
            submission.setBoardID(boardID);
            // Mark board as unavailable
            for (Board board : availableBoards) {
                if (board.getBoardID().equals(boardID)) {
                    board.setAvailable(false);
                    break;
                }
            }
        }
    }

    /**
     * Add evaluator to session
     */
    public void addEvaluator(Evaluator evaluator) {
        if (!assignedEvaluators.contains(evaluator)) {
            assignedEvaluators.add(evaluator);
        }
    }

    /**
     * Add submission to session
     */
    public void addSubmission(Submission submission) {
        if (!uploadedSubmissions.contains(submission)) {
            uploadedSubmissions.add(submission);
        }
    }

    /**
     * Add board to session
     */
    public void addBoard(Board board) {
        if (!availableBoards.contains(board)) {
            availableBoards.add(board);
        }
    }

    /**
     * Get session details as formatted string
     */
    public String getSessionDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("Session: %s\nTitle: %s\nType: %s\nDate/Time: %s\nVenue: %s\nEvaluators: %d\nSubmissions: %d",
            sessionID, title, sessionType, dateTime.format(formatter), venue, 
            assignedEvaluators.size(), uploadedSubmissions.size());
    }

    // Getters
    public String getSessionID() {
        return sessionID;
    }

    public String getTitle() {
        return title;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getVenue() {
        return venue;
    }

    public ArrayList<Evaluator> getAssignedEvaluators() {
        return new ArrayList<>(assignedEvaluators);
    }

    public ArrayList<Submission> getUploadedSubmissions() {
        return new ArrayList<>(uploadedSubmissions);
    }

    public ArrayList<Board> getAvailableBoards() {
        return new ArrayList<>(availableBoards);
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("%s - %s (%s) @ %s", sessionID, title, sessionType, 
                           dateTime.format(formatter));
    }
}
