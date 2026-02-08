package model;

/**
 * Abstract User class - Base class for all user types in the system
 * Handles authentication and common user properties
 */
public abstract class User {
    private final String name;
    private final String email;
    private final String password;
    protected Role userRole;

    /**
     * Constructor to initialize User
     * @param name User's full name
     * @param email User's email address
     * @param password User's password
     * @param userRole User's role (STUDENT, EVALUATOR, STAFF)
     */
    public User(String name, String email, String password, Role userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    /**
     * Abstract login method - Must be implemented by subclasses
     * @param email Email for login
     * @param password Password for login
     * @return true if login successful
     */
    public abstract boolean login(String email, String password);

    /**
     * Abstract logout method - Must be implemented by subclasses
     */
    public abstract void logout();

    // Getter methods
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return userRole;
    }
}
