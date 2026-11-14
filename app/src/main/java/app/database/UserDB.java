package app.src.main.java.app.database;

import app.src.main.java.app.model.User;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*; //Filter down needed modules later
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserDB {
    private static final String URL = "jdbc:sqlite:gamereview.db";

    // turned to a function so it can be called and not a saved connection bc we only need the connection for operations
    private Connection connect() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL);
            try
            {
                Statement stmt = connection.createStatement();
                stmt.execute("PRAGMA foreign_keys = ON"); // this lets sqlite reject the use of nonexistent foreign keys

            } catch (SQLException e)
            {
                connection.close();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("Something has gone terribly wrong");
        }

        return connection;
    }

    // create user to db
    public void addUser(String username, String password) {
        if (username == null || password == null || username.isBlank() || password.isEmpty()) {
            System.out.println("Username and password are required");
            // throw exception?
            return;
        }

        String format = """
            INSERT INTO users (username, password_hash)
            VALUES (?, ?)
            """;

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setString(1, username.trim());
            query.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));

            query.executeQuery();
        }
        catch (SQLException e)
        {
            System.out.println("Something has gone terribly wrong.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public User getUserById(int userId) {
        // DONT GET HASH we dont store it
        String format = """
            SELECT user_id, username, date_joined, account_status
            FROM users
            WHERE user_id = ?
            """;

            try (Connection connection = connect()) {
                PreparedStatement query = connection.prepareStatement(format);

                query.setInt(1, userId);

                try (ResultSet res = query.executeQuery()) {
                    if (res.next()) {
                        return mapUser(res);
                    }
                }
            }
            catch (SQLException e)
            {
                System.out.println("Something has gone terribly wrong.");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        return null; // not found
    }

    public User getUserByUsername(String username) {
        String format = """
            SELECT user_id, username, date_joined, account_status
            FROM users
            WHERE username = ?
            """;

        try (Connection connection = connect()) {
                PreparedStatement query = connection.prepareStatement(format);

                query.setString(1, username);

                try (ResultSet res = query.executeQuery()) {
                    if (res.next()) {
                        return mapUser(res);
                    }
                }
        }
        catch (SQLException e)
        {
            System.out.println("Something has gone terribly wrong.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null; // not found
    }

    public ArrayList<User> getAllUsers() {
        return filterUsers("");
    }

    public ArrayList<User> filterUsers(String username) {
        String format = """
            SELECT user_id, username, date_joined, account_status
            FROM users
            WHERE username LIKE ?
            ORDER BY username COLLATE NOCASE
            """; // collate nocase = ignores case when comparing strings

        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            // get the search value after trimming and checking if null
            String search = username == null ? "" : username.trim();
            query.setString(1, "%" + search + "%"); // %search% = check if search is anywhere in the username

            try (ResultSet res = query.executeQuery()) {
                while (res.next()) {
                    users.add(mapUser(res));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Something has gone terribly wrong.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    /*
    checks if user exist by selecting including the hash BUT the hash grabbed is not stored anywhere
    - it is only used to check if 
    */
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String format = """
            SELECT user_id, username, password_hash, date_joined, account_status
            FROM users
            WHERE username = ?
            AND account_status = 'Active'
            """;

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setString(1, username.trim());

            try (ResultSet res = query.executeQuery()) {
                if (res.next()) {
                    // verify password once user is found
                    if (BCrypt.checkpw(password, res.getString("password_hash"))) {
                        return mapUser(res);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Something has gone terribly wrong.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null; // not found/invalid
    }

    // update query used to change the value of stored hash
    /*
    - null/empty checks for input fields
    - checks first if user exists by calling select
    - then compares if the currentpassword inputted for changing it is the same as the hash stored
    - new password is hashed and stored if valid by update query
    */
    public boolean updatePassword(int userId, String currentPassword, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            System.out.println("New password cannot be empty");
            // throw excpt?
            return false;
        }

        if (currentPassword == null) {
            return false;
        }

        ResultSet res;
        try (Connection connection = connect()) {
            String format = """
                SELECT password_hash
                FROM users
                WHERE user_id = ?
                AND account_status = 'Active'
                """;

            String hash;

            try (PreparedStatement query = connection.prepareStatement(format)) {
                query.setInt(1, userId);

                res = query.executeQuery();
                if (!res.next()) {
                    return false; // no acct found
                }

            }

            // if invalid check, current pass entered is wrong
            if (!BCrypt.checkpw(currentPassword, res.getString("password_hash"))) {
                return false;
            }

            String update = """
                UPDATE users
                SET password_hash = ?
                WHERE user_id = ?
                AND account_status = 'Active'
                """;

            try (PreparedStatement query = connection.prepareStatement(update)) {
                query.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                query.setInt(2, userId);

                // check if properly updated the values, return true if updated
                return query.executeUpdate() > 0;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Something has gone terribly wrong.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // update query used to change the value of acc status
    public boolean deleteUser(int userId) {
        String format = """
            UPDATE users
            SET account_status = 'Deleted'
            WHERE user_id = ?
            AND account_status = 'Active'
            """;

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setInt(1, userId);

            return query.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            System.out.println("Something has gone terribly wrong.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // similar to how it worked in game
    private User mapUser(ResultSet res) {
        // to format timestamp into a localdatetime, get timestamp -> replace " " to "T" to follow LocalDateTime parsing format, YYYY-MM-DDTHH:MM:SS
        try
        {
            LocalDateTime dateJoined = LocalDateTime.parse(res.getString("date_joined").replace(" ", "T"));
            return new User(
                    res.getInt("user_id"),
                    res.getString("username"),
                    dateJoined,
                    res.getString("account_status")
            );
        }
        catch (SQLException e)
        {
            System.out.println("Something has gone terribly wrong.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
