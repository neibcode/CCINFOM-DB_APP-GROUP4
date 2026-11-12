package app.database;

import app.model.Game;
import java.sql.*; //Filter down needed modules later
import java.time.format.DateTimeFormatter;

public class GameDB {
    private static final String URL = "jdbc:sqlite:gamereview.db";
    private Connection connection;

    public GameDB() {
        try
        {
            connection = DriverManager.getConnection(URL);
        }
        catch (SQLException e)
        {
            System.out.println("Something has gone terribly wrong");
        }
    }

    // SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE
    public void addGame(Game game) {
        String title = game.getGameTitle();
        String description = game.getDescription();
        String genre = game.getGenre();
        String publisher = game.getPublisher();
        String date = game.getReleaseDate().format(DateTimeFormatter.RFC_1123_DATE_TIME);

        // executes sql queries using jdbc
        //PreparedStatement used to avoid SQL injection attacks

        try
        {
            String format = "INSERT INTO games (game_title, description, genre, publisher, release_date)" +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement query = connection.prepareStatement(format);
        }
    }
}