package app.controller;

import app.database.UserDB;
import app.model.User;

import java.util.ArrayList;

public class UserController {
    private final UserDB userDB = new UserDB();

    public boolean registerUser(String username, String password) {
        validateUser(username, password);

        username = username.trim(); // remove whitespace

        if (userDB.getUserByUsername(username) != null) {
            System.out.println("Username is already taken");
        }

        return userDB.addUser(username, password);
    }

    public User login(String username, String password) {
        validateUser(username, password);

        return userDB.login(username, password);
    }

    public boolean updatePassword(int userId, String currentPassword, String newPassword) {
        if (userId <= 0) {
            System.out.println("Invalid user ID");
        }

        if (currentPassword == null || currentPassword.isBlank()) {
            System.out.println("Current password is required.");
        }

        if (newPassword == null || newPassword.isBlank()) {
            System.out.println("New password is required.");
        }

        return userDB.updatePassword(userId, currentPassword, newPassword);
    }

    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            System.out.println("Invalid user ID");
        }

        return userDB.deleteUser(userId);
    }

    public User getUserById(int userId) {
        if (userId <= 0) {
            System.out.println("Invalid user ID");
        }

        return userDB.getUserById(userId);
    }

    public User getUserByUsername(String username) {
        if (username == null || username.isBlank()) {
            System.out.println("Username is required.");
        }

        return userDB.getUserByUsername(username.trim());
    }

    public ArrayList<User> getAllUsers() {
        return userDB.getAllUsers();
    }

    public ArrayList<User> filterUsers(String username) {
        return userDB.filterUsers(username);
    }

    private void validateUser(String username, String password) {
        if (username == null || username.isBlank()) {
            System.out.println("Username is required.");
        }

        if (password == null || password.isBlank()) {
            System.out.println("Password is required.");
        }
    }
}
