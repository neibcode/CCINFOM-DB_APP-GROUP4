package app.database;

import app.model.Review;

import java.sql.*; //Filter down needed modules later
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReviewDB {
    private static final String URL = "jdbc:sqlite:gamereview.db";

    // nested class to store additional details (like a data class for kotlin) 
    // mainly used for displaying reviewer username and game title in the review since not in Review model
    public static class ReviewDetails {
        private Review review;
        private String username;
        private String gameTitle;

        public ReviewDetails(Review review, String username, String gameTitle) {
            this.review = review;
            this.username = username;
            this.gameTitle = gameTitle;
        }

        public Review getReview() {
            return review;
        }

        public String getUsername() {
            return username;
        }

        public String getGameTitle() {
            return gameTitle;
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

    // helper to check if review exists
    // reviewExists return boolean/true if it does
    public boolean reviewExists(int userId, int gameId) throws SQLException {
        // select 1 = if there is a 1 for every row that would exist
        String format = """
            SELECT 1 FROM reviews
            WHERE user_id = ? AND game_id = ?
            """;

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setInt(1, userId);
            query.setInt(2, gameId);

            try (ResultSet res = query.executeQuery()) {
                return res.next(); // returns true of successfully lands in a row
            }
        }
    }

    // addReview
    public void addReview(Review review) throws SQLException {
        if (review == null) {
            System.out.println("Review cannot be empty");
            // throw excpt?
            return;
        }

        if (review.getReviewText() == null || review.getReviewText().isBlank()) {
            System.out.println("Review text cannot be empty");
            // throw excpt?
            return;
        }

        if (reviewExists(review.getUserId(), review.getGameId())) {
            System.out.println("Review already exists for this user on this game");
            // throw excpt?
            return;
        }

        String format = """
            INSERT INTO reviews
            (user_id, game_id, rating, review_text)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setInt(1, review.getUserId());
            query.setInt(2, review.getGameId());
            query.setDouble(3, review.getRating());
            query.setString(4, review.getReviewText());

            query.executeUpdate();

            try {
                Statement idStmt = connection.createStatement();
                ResultSet res = idStmt.executeQuery("SELECT last_insert_rowid()");

                if (res.next()) {
                    review.setReviewId(res.getInt(1));
                }
            }
        }
    }

    // getReviewById
    public Review getReviewById(int reviewId) throws SQLException {
        String format = """
            SELECT *
            FROM reviews
            WHERE review_id = ?
            """;
        
        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setInt(1, reviewId);

            try (ResultSet res = query.executeQuery()) {
                if (res.next()) {
                    return mapReview(res);
                }
            }
        }

        return null; // not found
    }

    // getReviewsByGameId
    public ArrayList<Review> getReviewsByGameId(int gameId) throws SQLException {
        String format = """
            SELECT * FROM reviews
            WHERE game_id = ?
            ORDER BY date_posted DESC, review_id DESC
            """;

        ArrayList<Review> reviews = new ArrayList<>();

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setInt(1, gameId);

            try (ResultSet res = query.executeQuery()) {
                while (res.next()) {
                    reviews.add(mapReview(res));
                }
            }
        }

        return reviews;
    }

    // getReviewsByUserId
    public ArrayList<Review> getReviewsByUserId(int userId) throws SQLException {
        String format = """
            SELECT * FROM reviews
            WHERE user_id = ?
            ORDER BY date_posted DESC, review_id DESC
            """;

        ArrayList<Review> reviews = new ArrayList<>();

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setInt(1, userId);

            try (ResultSet res = query.executeQuery()) {
                while (res.next()) {
                    reviews.add(mapReview(res));
                }
            }
        }

        return reviews;
    }

    // getAllReviews
    public ArrayList<Review> getAllReviews() throws SQLException {
        return filterReviews(null, null, null, null, "date_posted", false);
    }

    // filterReviews(title, reviewer(username), rating, dateposted, sortby, ascending)
    public ArrayList<Review> filterReviews(String gameTitle, String reviewer, Double rating, LocalDate datePosted, String sortBy, boolean ascending) throws SQLException {
        ArrayList<Review> reviews = new ArrayList<>();

        ArrayList<ReviewDetails> details = filterReviewDetails(gameTitle, reviewer, rating, datePosted, sortBy, ascending);

        // for each detail in details
        for (ReviewDetails detail : details) {
            reviews.add(detail.getReview());
        }

        return reviews;
    }

    // get specific review details with username/title
    public ReviewDetails getReviewDetailsById(int reviewId) throws SQLException {
        String format = """
            SELECT r.*, u.username, g.game_title
            FROM reviews r
            JOIN users u ON r.user_id = u.user_id
            JOIN games g ON r.game_id = g.game_id
            WHERE r.review_id = ?
            """;

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setInt(1, reviewId);

            try (ResultSet res = query.executeQuery()) {
                if (res.next()) {
                    return mapReviewDetails(res);
                }
            }
        }

        return null; // not found
    }

    public ArrayList<ReviewDetails> getAllReviewDetails() throws SQLException {
        return filterReviewDetails(null, null, null, null, "date_posted", false);
    }

    public ArrayList<ReviewDetails> filterReviewDetails(String gameTitle, String reviewer, Double rating, LocalDate datePosted, String sortBy, boolean ascending) throws SQLException {
        StringBuilder format = new StringBuilder("""
            SELECT r.*, u.username, g.game_title
            FROM reviews r
            JOIN games g ON r.game_id = g.game_id
            JOIN users u ON r.user_id = u.user_id
            WHERE 1 = 1
            """);
        
        ArrayList<Object> parameters = new ArrayList<>();

        if (gameTitle != null && !gameTitle.isBlank()) {
            format.append(" AND g.game_title LIKE ?");
            parameters.add("%" + gameTitle.trim() + "%");
        }

        if (reviewer != null && !reviewer.isBlank()) {
            format.append(" AND u.username LIKE ?");
            parameters.add("%" + reviewer.trim() + "%");
        }

        if (rating != null) {
            format.append(" AND r.rating = ?");
            parameters.add(rating);
        }

        if (datePosted != null) {
            format.append(" AND DATE(r.date_posted) = ?");
            parameters.add(datePosted.toString());
        }

        // check for order/sortby
        String sortColumn;
        if (sortBy == null || sortBy.equals("date_posted")) {
            sortColumn = "r.date_posted";
        } else if (sortBy.equals("rating")) {
            sortColumn = "r.rating";
        } else {
            System.out.println("Sort by rating or date posted only");
            return new ArrayList<>();
        }

        format.append(" ORDER BY ");
        format.append(sortColumn);
        format.append(ascending ? " ASC" : " DESC"); // if true asc not desc
        format.append(", r.review_id DESC");

        ArrayList<ReviewDetails> reviews = new ArrayList<>();

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format.toString());

            for (int i = 0; i < parameters.size(); i++) {
                query.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet res = query.executeQuery()) {
                while (res.next()) {
                    reviews.add(mapReviewDetails(res));
                }
            }
        }

        return reviews;
    }

    // mapReview
    private Review mapReview(ResultSet res) throws SQLException {
        LocalDateTime datePosted = LocalDateTime.parse(
                res.getString("date_posted").replace(" ", "T")
        );

        return new Review(
                res.getInt("review_id"),
                res.getInt("user_id"),
                res.getInt("game_id"),
                res.getDouble("rating"),
                res.getString("review_text"),
                datePosted
        );
    }

    // map review details
    private ReviewDetails mapReviewDetails(ResultSet res)
            throws SQLException {

        Review review = mapReview(res);

        return new ReviewDetails(
                review,
                res.getString("username"),
                res.getString("game_title")
        );
    }
}
