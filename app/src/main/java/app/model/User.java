package app.model;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private final String username;
    // password hash not stored for security and avoiding leaking to frontend
    private LocalDateTime dateJoined;
    private String accountStatus;

    // for existing users
    public User(int userId, String username, LocalDateTime dateJoined, String accountStatus) {
        this.userId = userId;
        this.username = username;
        this.dateJoined = dateJoined;
        this.accountStatus = accountStatus;
    }

    // for creating new users
    public User(String username) {
        this(0, username, null, "Active");
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDateTime dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}