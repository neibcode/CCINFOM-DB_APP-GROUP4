package app.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReportDB {
    // separated so that itd have its own db calls/connection
    
    private static final String URL = "jdbc:sqlite:gamereview.db";

    // data class storage
    public static class TopGameDetails {
        private int gameId;
        private String gameTitle;
        private String genre;
        private String publisher;
        private double averageRating;
        private int reviewCount;

        public TopGameDetails(int gameId, String gameTitle, String genre, String publisher, double averageRating, int reviewCount) {
            this.gameId = gameId;
            this.gameTitle = gameTitle;
            this.genre = genre;
            this.publisher = publisher;
            this.averageRating = averageRating;
            this.reviewCount = reviewCount;
        }

        public int getGameId() {
            return gameId;
        }

        public String getGameTitle() {
            return gameTitle;
        }

        public String getGenre() {
            return genre;
        }

        public String getPublisher() {
            return publisher;
        }

        public double getAverageRating() {
            return averageRating;
        }

        public int getReviewCount() {
            return reviewCount;
        }
    }

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

    public ArrayList<TopGameDetails> getTopGamesReport(LocalDate startDate, LocalDate endDate, int topN, String genre, String publisher) throws SQLException {
        if (startDate == null || endDate == null) {
            System.out.println("Start and end dates cannot be empty");
            // throw excpt here?
            return new ArrayList<>();
        }

        if (endDate.isBefore(startDate)) {
            System.out.println("End date cannot be before start date");
            // excpt here?
            return new ArrayList<>();
        }

        if (topN <= 0) {
            System.out.println("Number of games must be greater than zero");
            // excpt here?
            return new ArrayList<>();
        }

        StringBuilder format = new StringBuilder("""
            SELECT g.game_id, g.game_title, g.genre, g.publisher, AVG(r.rating) AS average_rating, COUNT(r.review_id) AS review_count
            FROM games g
            JOIN reviews r ON g.game_id = r.game_id
            WHERE DATE(r.date_posted) BETWEEN ? AND ?
            AND g.archived = 0
            """);

        ArrayList<Object> parameters = new ArrayList<>();

        if (genre != null && !genre.isBlank()) {
            format.append(" AND g.genre LIKE ?");
            parameters.add("%" + genre.trim() + "%");
        }

        if (publisher != null && !publisher.isBlank()) {
            format.append(" AND g.publisher LIKE ?");
            parameters.add("%" + publisher.trim() + "%");
        }

        // group/sortby
        format.append("""
            GROUP BY g.game_id, g.game_title, g.genre, g.publisher
            ORDER BY average_rating DESC, review_count DESC, g.game_id ASC
            LIMIT ?
            """);
        // limit = how many will be shown

        parameters.add(topN);

        ArrayList<TopGameDetails> report = new ArrayList<>();

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format.toString());

            for(int i = 0; i < parameters.size(); i++) {
                query.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet res = query.executeQuery()) {
                while(res.next()) {
                    report.add(new TopGameDetails(
                            res.getInt("game_id"), 
                            res.getString("game_title"), 
                            res.getString("genre"), 
                            res.getString("publisher"), 
                            res.getDouble("average_rating"), 
                            res.getInt("review_count")
                        ));
                }
            }
        }

        return report;
    }
}
