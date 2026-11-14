package app.src.main.java.app.model;

import java.time.LocalDateTime;

public class Review {
    private int reviewId;
    private final int userId;
    private final int gameId;
    private final double rating;
    private final String reviewText;
    private LocalDateTime datePosted;

    // Constructor for existing reviews
    public Review(int reviewId, int userId, int gameId, double rating, String reviewText, LocalDateTime datePosted) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.gameId = gameId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.datePosted = datePosted;
    }

    // Constructor for new reviews
    public Review(int userId, int gameId, double rating, String reviewText) {
        this(0, userId, gameId, rating, reviewText, null);
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public int getGameId() {
        return gameId;
    }

    public double getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }
}