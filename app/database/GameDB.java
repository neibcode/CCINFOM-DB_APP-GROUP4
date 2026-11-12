package app.database;

import app.model.Game;

public class GameDB {
    private static final String URL = "jdbc:sqlite:gamereview.db";

    // SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE
    public void addGame(Game game) {
        String title = game.getGameTitle();
        String genre = game.getGenre();

        // executes sql queries using jdbc
    }
}