package model;

import service.FileService;

/**
 * Student class - Student user role
 * Inherits from User class
 */
public class Student extends User {
    private String studentID;
    private String course;
    private boolean registrationStatus;

    /**
     * Constructor to initialize Student
     */
    public Student(String name, String email, String password, String studentID, String course) {
        super(name, email, password, Role.STUDENT);
        this.studentID = studentID;
        this.course = course;
        this.registrationStatus = false;
    }

    /**
     * Override login method
     */
    @Override
    public boolean login(String email, String password) {
        return FileService.validateCredentials(email, password, Role.STUDENT);
    }

    /**
     * Override logout method
     */
    @Override
    public void logout() {
        System.out.println("Student " + getName() + " logged out successfully.");
    }

    /**
     * Register for a seminar by creating a submission
     */
    public boolean registerSeminar(String title, String abstractText, String supervisor, 
                                   String filepath, SessionType preferredType) {
        // This will create a submission object
        this.registrationStatus = true;
        return true;
    }

    // Getter methods
    public String getStudentID() {
        return studentID;
    }

    public String getCourse() {
        return course;
    }

    public boolean getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(boolean status) {
        this.registrationStatus = status;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", getName(), studentID, course);
    }
}
