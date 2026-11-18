package models;

public class User {

    private int id;
    private String fullName;
    private String username;
    private String password;
    private String role;

    public User(int id, String fullName, String username, String password, String role) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
