package model;

/**
 * Submission class - Represents a presentation submission
 * Contains nested Evaluation class for scoring
 */
public class Submission {
    private String submissionID;
    private String sessionID;
    private String studentID;
    private String filepath;
    private String supervisorName;
    private String abstractText;
    private String title;
    private SessionType preferredType;
    private String boardID;
    private String assignedEvaluatorID;
    private Evaluation eval;
    private int peopleVote;

    /**
     * Constructor for new submissions
     */
    public Submission(String submissionID, String studentID, String title, String abstractText, 
                      String supervisorName, String filepath, SessionType preferredType) {
        this.submissionID = submissionID;
        this.studentID = studentID;
        this.title = title;
        this.abstractText = abstractText;
        this.supervisorName = supervisorName;
        this.filepath = filepath;
        this.preferredType = preferredType;
        this.eval = new Evaluation();
        this.peopleVote = 0;
    }

    /**
     * Constructor for loading from CSV
     */
    public Submission(String submissionID, String sessionID, String studentID, String title, 
                      String abstractText, String supervisorName, String filepath, 
                      SessionType preferredType, String evalID, String boardID,
                      int clarity, int method, int res, int pres, String comm, 
                      int vote, EvaluationStat stat) {
        this.submissionID = submissionID;
        this.sessionID = sessionID;
        this.studentID = studentID;
        this.title = title;
        this.abstractText = abstractText;
        this.supervisorName = supervisorName;
        this.filepath = filepath;
        this.preferredType = preferredType;
        this.assignedEvaluatorID = evalID;
        this.boardID = boardID;
        this.peopleVote = vote;
        this.eval = new Evaluation(clarity, method, res, pres, comm, vote, stat);
    }

    /**
     * Private inner class for Evaluation
     */
    private class Evaluation {
        private int scoreProblemClarity;
        private int scoreMethodology;
        private int scoreResults;
        private int scorePresentation;
        private String comments;
        private EvaluationStat evaluationStatus;

        // Fresh constructor
        public Evaluation() {
            this.evaluationStatus = EvaluationStat.PENDING;
            this.comments = "";
        }

        // Factory constructor
        public Evaluation(int clarity, int method, int res, int pres, 
                         String comm, int vote, EvaluationStat stat) {
            this.scoreProblemClarity = clarity;
            this.scoreMethodology = method;
            this.scoreResults = res;
            this.scorePresentation = pres;
            this.comments = comm;
            this.evaluationStatus = stat;
        }

        public void submitEvaluation(int clarity, int method, int res, int pres, String comm) {
            this.scoreProblemClarity = clarity;
            this.scoreMethodology = method;
            this.scoreResults = res;
            this.scorePresentation = pres;
            this.comments = comm;
            this.evaluationStatus = EvaluationStat.COMPLETED;
        }

        public int getTotalScore() {
            return scoreProblemClarity + scoreMethodology + scoreResults + scorePresentation;
        }
    }

    /**
     * Provide evaluation for this submission
     */
    public boolean provideEvaluation(String evaluatorID, int clarity, int methodology, 
                                     int result, int presentation, String comment) {
        if (this.assignedEvaluatorID != null && 
            this.assignedEvaluatorID.equals(evaluatorID) && 
            this.eval.evaluationStatus == EvaluationStat.PROCESSING) {
            this.eval.submitEvaluation(clarity, methodology, result, presentation, comment);
            return true;
        }
        return false;
    }

    /**
     * Assign evaluator to this submission
     */
    public boolean assignEvaluator(Evaluator evaluator) {
        if (this.assignedEvaluatorID == null || this.assignedEvaluatorID.isEmpty()) {
            this.assignedEvaluatorID = evaluator.getEvaluatorID();
            this.eval.evaluationStatus = EvaluationStat.PROCESSING;
            evaluator.addAssignedSubmission(this);
            return true;
        }
        return false;
    }

    /**
     * Assign session to this submission
     */
    public boolean assignSession(Session session) {
        if (this.sessionID == null || this.sessionID.isEmpty()) {
            this.sessionID = session.getSessionID();
            return true;
        }
        return false;
    }

    /**
     * Set board ID for poster presentations
     */
    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    /**
     * Increment people's choice vote
     */
    public void addVote() {
        this.peopleVote++;
    }

    /**
     * Get total evaluation score
     */
    public int getTotalScore() {
        return eval.getTotalScore();
    }

    /**
     * Get evaluation status
     */
    public EvaluationStat getEvaluationStatus() {
        return eval.evaluationStatus;
    }

    /**
     * Get evaluation details as string
     */
    public String getEvaluationDetails() {
        if (eval.evaluationStatus == EvaluationStat.PENDING) {
            return "Evaluation Pending";
        }
        return String.format("Clarity: %d, Methodology: %d, Results: %d, Presentation: %d\nTotal: %d/40\nComments: %s",
            eval.scoreProblemClarity, eval.scoreMethodology, eval.scoreResults, 
            eval.scorePresentation, getTotalScore(), eval.comments);
    }

    // Getters
    public String getSubmissionID() {
        return submissionID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public String getTitle() {
        return title;
    }

    public SessionType getPreferredType() {
        return preferredType;
    }

    public String getBoardID() {
        return boardID;
    }

    public String getAssignedEvaluatorID() {
        return assignedEvaluatorID;
    }

    public int getPeopleVote() {
        return peopleVote;
    }

    public int getScoreProblemClarity() {
        return eval.scoreProblemClarity;
    }

    public int getScoreMethodology() {
        return eval.scoreMethodology;
    }

    public int getScoreResults() {
        return eval.scoreResults;
    }

    public int getScorePresentation() {
        return eval.scorePresentation;
    }

    public String getComments() {
        return eval.comments;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", submissionID, title, preferredType);
    }
}
