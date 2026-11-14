package app.database;

import app.model.Review;

import java.sql.*; //Filter down needed modules later
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReviewDB {
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

    // helper to check if review exists
    // reviewExists return boolean/true if it does

    // addReview

    // getReviewById

    // getReviewByGameId

    // getReviewsByUserId

    // getAllReviews

    // filterReviews(title, reviewer(username), rating, dateposted, sortby, ascending)

    // mapReview
}
