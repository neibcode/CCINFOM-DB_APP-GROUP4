package app.controller;

import app.database.UserDB;
import app.model.User;

import java.util.ArrayList;

public class UserController {
    private final UserDB userDB = new UserDB();

    // so in the GUI this is how itll be used:
        // - Button/Form -> Event Handler -> Calls controller function then backend

    // call this function for registration after user inputs details in form panel
    public boolean registerUser(String username, String password) {
        validateUser(username, password);

        username = username.trim(); // remove whitespace

        if (userDB.getUserByUsername(username) != null) {
            System.out.println("Username is already taken");
        }

        return userDB.addUser(username, password);
    }

    // call this function for login after user inputs details in form panel
    public User login(String username, String password) {
        validateUser(username, password);

        return userDB.login(username, password);
    }

    // call this function for updating password after user inputs details in form panel
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

    // deleting account
    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            System.out.println("Invalid user ID");
        }

        return userDB.deleteUser(userId);
    }

    // searching by userID
    public User getUserById(int userId) {
        if (userId <= 0) {
            System.out.println("Invalid user ID");
        }

        return userDB.getUserById(userId);
    }

    // search by username
    public User getUserByUsername(String username) {
        if (username == null || username.isBlank()) {
            System.out.println("Username is required.");
        }

        return userDB.getUserByUsername(username.trim());
    }

    // list of all users
    public ArrayList<User> getAllUsers() {
        return userDB.getAllUsers();
    }

    // view filtered list of users
    public ArrayList<User> filterUsers(String username) {
        return userDB.filterUsers(username);
    }

    // validate user details (username, password)
    private void validateUser(String username, String password) {
        if (username == null || username.isBlank()) {
            System.out.println("Username is required.");
        }

        if (password == null || password.isBlank()) {
            System.out.println("Password is required.");
        }
    }
}
