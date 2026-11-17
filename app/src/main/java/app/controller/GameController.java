package app.controller;
// since this is a maven project the source root is normally src/main/java, so app.controller lngs
import app.database.GameDB;
import app.model.Game;

import java.time.LocalDate;
import java.util.ArrayList;

public class GameController {
    private final GameDB gameDB = new GameDB();

    // so in the GUI this is how itll be used:
        // - Button/Form -> Event Handler -> Calls controller function then backend

    // call this function for the add game event
    public boolean addGame(Game game) {
        validateGame(game);
        game.setArchived(false);
        return gameDB.addGame(game);
    }

    // call this function for the edit/update game event
    public boolean updateGame(Game game) {
        validateGame(game);

        if (game.getGameId() <= 0) {
            System.out.println("Invalid game ID");
        }

        return gameDB.updateGame(game);
    }

    // call this function for archiving game
    public boolean archiveGame(int gameId) {
        if (gameId <= 0) {
            System.out.println("Invalid game ID");
        }

        return gameDB.archiveGame(gameId);
    }

    // call this when showing a game selected with gameID
    public Game getGameById(int gameId) {
        if (gameId <= 0) {
            System.out.println("Invalid game ID");
        }

        return gameDB.getGameById(gameId);
    }

    // event to use to show all games
    public ArrayList<Game> getAllGames() {
        return gameDB.getAllGames();
    }

    // use in the event for filters
    public ArrayList<Game> filterGames(String title, String genre, String publisher, LocalDate releaseDate, Boolean archived) {
        return gameDB.filterGames(title, genre, publisher, releaseDate, archived);
    }

    private void validateGame(Game game) {
        if (game == null) {
            System.out.println("Game cannot be empty");
        }

        if (game.getGameTitle() == null || game.getGameTitle().isBlank()) {
            System.out.println("Game title cannot be empty");
        }

        if (game.getGenre() == null || game.getGenre().isBlank()) {
            System.out.println("Game genre cannot be empty");
        }

        if (game.getPublisher() == null || game.getPublisher().isBlank()) {
            System.out.println("Publisher cannot be empty.");
        }

        if (game.getReleaseDate() == null) {
            System.out.println("Release date cannot be empty.");
        }
    }
}