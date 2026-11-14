package app.database;

import java.sql.*;

public class ReportDB {
    // separated so that itd have its own db calls/connection
    
    private static final String URL = "jdbc:sqlite:gamereview.db";

    // turned to a function so it can be called and not a saved connection bc we only need the connection for operations
    private Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);

        try {
            Statement stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON"); // this lets sqlite reject the use of nonexistent foreign keys
        } catch (SQLException e) {
            connection.close();
            System.out.println("Something has gone terribly wrong");
        }

        return connection;
    }
}
