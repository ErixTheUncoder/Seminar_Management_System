package service;

import model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * FileService - Handles all file I/O operations for the system
 * Manages CSV files for data persistence
 */
public class FileService {
    private static final String DATA_DIR = "data/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Initialize data directory and CSV files if they don't exist
     */
    public static void initializeDataFiles() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Create CSV files with headers if they don't exist
        createFileIfNotExists(DATA_DIR + "StudentData.csv", 
            "studentID,name,email,password,course");
        createFileIfNotExists(DATA_DIR + "EvaluatorData.csv", 
            "evaluatorID,name,email,password,faculty,expertise");
        createFileIfNotExists(DATA_DIR + "CoordinatorData.csv", 
            "staffID,name,email,password,faculty,officeContact");
        createFileIfNotExists(DATA_DIR + "SessionData.csv", 
            "sessionID,title,sessionType,dateTime,venue");
        createFileIfNotExists(DATA_DIR + "SubmissionData.csv", 
            "submissionID,sessionID,studentID,title,abstract,supervisor,filepath,preferredType,evaluatorID,boardID,clarityScore,methodologyScore,resultsScore,presentationScore,comments,peopleVote,evaluationStatus");
        createFileIfNotExists(DATA_DIR + "BoardData.csv", 
            "boardID,orientation,paperSize,dimensions,material,isAvailable");
    }

    /**
     * Create a file with header if it doesn't exist
     */
    private static void createFileIfNotExists(String filepath, String header) {
        File file = new File(filepath);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(header);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Error creating file: " + filepath);
                e.printStackTrace();
            }
        }
    }

    /**
     * Validate user credentials
     */
    public static boolean validateCredentials(String email, String password, Role userType) {
        String filename = "";
        int emailIndex = 2;
        int passwordIndex = 3;

        switch (userType) {
            case STUDENT:
                filename = DATA_DIR + "StudentData.csv";
                break;
            case EVALUATOR:
                filename = DATA_DIR + "EvaluatorData.csv";
                break;
            case STAFF:
                filename = DATA_DIR + "CoordinatorData.csv";
                break;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > passwordIndex) {
                    if (parts[emailIndex].equals(email) && parts[passwordIndex].equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error validating credentials: " + e.getMessage());
        }
        return false;
    }

    /**
     * Load Student by email
     */
    public static Student loadStudentByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "StudentData.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[2].equals(email)) {
                    return new Student(parts[1], parts[2], parts[3], parts[0], parts[4]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading student: " + e.getMessage());
        }
        return null;
    }

    /**
     * Load Evaluator by email
     */
    public static Evaluator loadEvaluatorByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "EvaluatorData.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[2].equals(email)) {
                    return new Evaluator(parts[1], parts[2], parts[3], parts[0], parts[4], parts[5]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading evaluator: " + e.getMessage());
        }
        return null;
    }

    /**
     * Load Coordinator by email
     */
    public static Coordinator loadCoordinatorByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "CoordinatorData.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[2].equals(email)) {
                    return new Coordinator(parts[1], parts[2], parts[3], parts[0], parts[4], parts[5]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading coordinator: " + e.getMessage());
        }
        return null;
    }

    /**
     * Load all students
     */
    public static ArrayList<Student> loadAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "StudentData.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    students.add(new Student(parts[1], parts[2], parts[3], parts[0], parts[4]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Load all evaluators
     */
    public static ArrayList<Evaluator> loadAllEvaluators() {
        ArrayList<Evaluator> evaluators = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "EvaluatorData.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    evaluators.add(new Evaluator(parts[1], parts[2], parts[3], parts[0], parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading evaluators: " + e.getMessage());
        }
        return evaluators;
    }

    /**
     * Load all sessions
     */
    public static ArrayList<Session> loadAllSessions() {
        ArrayList<Session> sessions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "SessionData.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    SessionType type = SessionType.valueOf(parts[2]);
                    LocalDateTime dateTime = LocalDateTime.parse(parts[3], DATE_FORMATTER);
                    sessions.add(new Session(parts[0], parts[1], type, dateTime, parts[4]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading sessions: " + e.getMessage());
        }
        return sessions;
    }

    /**
     * Load all boards
     */
    public static ArrayList<Board> loadAllBoards() {
        ArrayList<Board> boards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "BoardData.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    Board board = new Board(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    board.setAvailable(Boolean.parseBoolean(parts[5]));
                    boards.add(board);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading boards: " + e.getMessage());
        }
        return boards;
    }

    /**
     * Load all submissions
     */
    public static ArrayList<Submission> loadAllSubmissions() {
        ArrayList<Submission> submissions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + "SubmissionData.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // -1 to keep empty strings
                if (parts.length >= 17) {
                    SessionType type = SessionType.valueOf(parts[7]);
                    int clarity = parts[10].isEmpty() ? 0 : Integer.parseInt(parts[10]);
                    int methodology = parts[11].isEmpty() ? 0 : Integer.parseInt(parts[11]);
                    int results = parts[12].isEmpty() ? 0 : Integer.parseInt(parts[12]);
                    int presentation = parts[13].isEmpty() ? 0 : Integer.parseInt(parts[13]);
                    String comments = parts[14];
                    int peopleVote = parts[15].isEmpty() ? 0 : Integer.parseInt(parts[15]);
                    EvaluationStat status = EvaluationStat.valueOf(parts[16]);

                    submissions.add(new Submission(
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6],
                        type, parts[8], parts[9], clarity, methodology, results, presentation,
                        comments, peopleVote, status
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading submissions: " + e.getMessage());
        }
        return submissions;
    }

    /**
     * Load submissions for a specific student
     */
    public static ArrayList<Submission> loadSubmissionsByStudent(String studentID) {
        ArrayList<Submission> submissions = new ArrayList<>();
        ArrayList<Submission> allSubmissions = loadAllSubmissions();
        for (Submission sub : allSubmissions) {
            if (sub.getStudentID().equals(studentID)) {
                submissions.add(sub);
            }
        }
        return submissions;
    }

    /**
     * Load submissions for a specific evaluator
     */
    public static ArrayList<Submission> loadSubmissionsByEvaluator(String evaluatorID) {
        ArrayList<Submission> submissions = new ArrayList<>();
        ArrayList<Submission> allSubmissions = loadAllSubmissions();
        for (Submission sub : allSubmissions) {
            if (evaluatorID.equals(sub.getAssignedEvaluatorID())) {
                submissions.add(sub);
            }
        }
        return submissions;
    }

    /**
     * Save a new student
     */
    public static boolean saveStudent(Student student) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + "StudentData.csv", true))) {
            writer.write(String.format("%s,%s,%s,%s,%s",
                student.getStudentID(), student.getName(), student.getEmail(),
                student.getPassword(), student.getCourse()));
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Save a new session
     */
    public static boolean saveSession(Session session) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + "SessionData.csv", true))) {
            writer.write(String.format("%s,%s,%s,%s,%s",
                session.getSessionID(), session.getTitle(), session.getSessionType(),
                session.getDateTime().format(DATE_FORMATTER), session.getVenue()));
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving session: " + e.getMessage());
            return false;
        }
    }

    /**
     * Save a new submission
     */
    public static boolean saveSubmission(Submission submission) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + "SubmissionData.csv", true))) {
            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%d,%d,%d,%s,%d,%s",
                submission.getSubmissionID(),
                submission.getSessionID() != null ? submission.getSessionID() : "",
                submission.getStudentID(),
                submission.getTitle(),
                submission.getAbstractText(),
                submission.getSupervisorName(),
                submission.getFilepath(),
                submission.getPreferredType(),
                submission.getAssignedEvaluatorID() != null ? submission.getAssignedEvaluatorID() : "",
                submission.getBoardID() != null ? submission.getBoardID() : "",
                submission.getScoreProblemClarity(),
                submission.getScoreMethodology(),
                submission.getScoreResults(),
                submission.getScorePresentation(),
                submission.getComments(),
                submission.getPeopleVote(),
                submission.getEvaluationStatus()));
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving submission: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update all submissions (rewrite entire file)
     */
    public static boolean updateAllSubmissions(ArrayList<Submission> submissions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + "SubmissionData.csv"))) {
            // Write header
            writer.write("submissionID,sessionID,studentID,title,abstract,supervisor,filepath,preferredType,evaluatorID,boardID,clarityScore,methodologyScore,resultsScore,presentationScore,comments,peopleVote,evaluationStatus");
            writer.newLine();

            // Write all submissions
            for (Submission submission : submissions) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%d,%d,%d,%s,%d,%s",
                    submission.getSubmissionID(),
                    submission.getSessionID() != null ? submission.getSessionID() : "",
                    submission.getStudentID(),
                    submission.getTitle(),
                    submission.getAbstractText(),
                    submission.getSupervisorName(),
                    submission.getFilepath(),
                    submission.getPreferredType(),
                    submission.getAssignedEvaluatorID() != null ? submission.getAssignedEvaluatorID() : "",
                    submission.getBoardID() != null ? submission.getBoardID() : "",
                    submission.getScoreProblemClarity(),
                    submission.getScoreMethodology(),
                    submission.getScoreResults(),
                    submission.getScorePresentation(),
                    submission.getComments(),
                    submission.getPeopleVote(),
                    submission.getEvaluationStatus()));
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error updating submissions: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update all boards (rewrite entire file)
     */
    public static boolean updateAllBoards(ArrayList<Board> boards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_DIR + "BoardData.csv"))) {
            // Write header
            writer.write("boardID,orientation,paperSize,dimensions,material,isAvailable");
            writer.newLine();

            // Write all boards
            for (Board board : boards) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s",
                    board.getBoardID(), board.getOrientation(), board.getPaperSize(),
                    board.getDimensions(), board.getMaterial(), board.isAvailable()));
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error updating boards: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generate next ID for entity type
     */
    public static String generateNextID(String prefix, String csvFile) {
        int maxID = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + csvFile))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].startsWith(prefix)) {
                    try {
                        int id = Integer.parseInt(parts[0].substring(prefix.length()));
                        if (id > maxID) {
                            maxID = id;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error generating ID: " + e.getMessage());
        }
        return String.format("%s%03d", prefix, maxID + 1);
    }
}
