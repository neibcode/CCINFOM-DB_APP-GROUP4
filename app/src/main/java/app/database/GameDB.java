package app.database;

import app.model.Game;

import java.sql.*; //Filter down needed modules later
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GameDB {
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

    // adds a game, called in controller
    public void addGame(Game game) {
        if (game == null) {
            System.out.println("Game cannot be empty");
            // throw excpt?
            return;
        }

        // triple quotes = text blocks no need for using the + to concat
        String format = """
                    INSERT INTO games
                    (game_title, description, genre, publisher,
                    release_date, archived)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;
        // connection is only local
        try (Connection connection = connect();) {
            //PreparedStatement used to avoid SQL injection attacks
            PreparedStatement query = connection.prepareStatement(format);

            query.setString(1, game.getGameTitle());
            query.setString(2, game.getDescription());
            query.setString(3, game.getGenre());
            query.setString(4, game.getPublisher());
            query.setString(5, game.getReleaseDate().toString()); // time isnt needed for release so just date
            query.setInt(6, game.isArchived() ? 1 : 0);

            query.executeUpdate();

            try {
                // for getting the id of recently made game
                Statement idStmt = connection.createStatement();
                ResultSet res = idStmt.executeQuery("SELECT last_insert_rowid()");

                // if there is new game made
                if (res.next()) {
                    // sets the retrieved id to the stored game's id
                    game.setGameId(res.getInt(1));
                }
            }
        }
    }

    public Game getGameById(int gameId) throws SQLException {
        String format = "SELECT * FROM games WHERE game_id = ?";

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setInt(1, gameId); // set the ? to gameId param

            try (ResultSet res = query.executeQuery()) { // gets resulting row
                if (res.next()) { // if successfully retreived
                    return mapGame(res); // return game from the db row info that was gathered
                }
            }
        }
        return null;
    }

    public List<Game> getAllGames() throws SQLException {
        // filter games by nothin
        return filterGames(null, null, null, null, false);
    }

    // update game returns if updated or not
    public boolean updateGame(Game game) throws SQLException {
        String format = """
            UPDATE games
            SET game_title = ?,
                description = ?,
                genre = ?,
                publisher = ?,
                release_date = ?,
                archived = ?
            WHERE game_id = ?
            """;

        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);

            query.setString(1, game.getGameTitle());
            query.setString(2, game.getDescription());
            query.setString(3, game.getGenre());
            query.setString(4, game.getPublisher());
            query.setString(5, game.getReleaseDate().toString());
            query.setInt(6, game.isArchived() ? 1 : 0);
            query.setInt(7, game.getGameId());

            return query.executeUpdate() > 0; // if updated more than 0 values
        }
    }

    // archive game
    public boolean archiveGame(int gameId) throws SQLException {
        String format = """
            UPDATE games
            SET archived = 1
            WHERE game_id = ?
            """;
        
        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format);
            query.setInt(1, gameId);

            return query.executeUpdate() > 0; // if updated return true
        }
    }

    // filter games
    public ArrayList<Game> filterGames(String title, String genre, String publisher, LocalDate releaseDate, Boolean archived) throws SQLException {
        StringBuilder format = new StringBuilder("SELECT * FROM games WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>(); // stores the parameters that are used to filter search
        // this is done so that setting prepared statement is parametarized

        if (title != null && !title.isBlank()) {
            format.append(" AND game_title LIKE ?");
            parameters.add("%" + title.trim() + "%");
        }

        if (genre != null && !genre.isBlank()) {
            format.append(" AND genre LIKE ?");
            parameters.add("%" + genre.trim() + "%");
        }

        if (publisher != null && !publisher.isBlank()) {
            format.append(" AND publisher LIKE ?");
            parameters.add("%" + publisher.trim() + "%");
        }

        if (releaseDate != null) {
            format.append(" AND release_date = ?");
            parameters.add(releaseDate.toString());
        }

        if (archived != null) {
            format.append(" AND archived = ?");
            parameters.add(archived ? 1 : 0); // to int instead of boolean
        }

        format.append (" ORDER BY game_title COLLATE NOCASE, game_id");

        ArrayList<Game> games = new ArrayList();
        // add the games based on parameters gathered
        try (Connection connection = connect()) {
            PreparedStatement query = connection.prepareStatement(format.toString());

            // for each parameter set value for query
            for (int i = 0; i < parameters.size(); i++) {
                query.setObject(i + 1, parameters.get(i)); // since 1 indexed preparestatement, and zero index arraylist
            }

            try (ResultSet res = query.executeQuery()) {
                // while there are games
                while (res.next()) {
                    // add mapped game to game object to arraylist
                    games.add(mapGame(res));
                }
            }
        }

        return games;
    }

    // turns a db row into a game object
    private Game mapGame(ResultSet res) throws SQLException {
        return new Game(
            res.getInt("game_id"),
            res.getString("game_title"),
            res.getString("description"),
            res.getString("genre"),
            res.getString("publisher"),
            LocalDate.parse(res.getString("release_date")),
            res.getInt("archived") == 1 // if 1 true else false boolean
        );
    }
}