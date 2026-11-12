package app.controller;

import app.database.GameDB;
import app.model.Game;

public class GameController {

    private GameDB gameDB = new GameDB();

    // SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE SAMPLE
    public void handleAddGame() {
        // normally got from text fields in UI
        // Game game = new Game("Minecraft", "Sandbox"); // create new game
        Game game = null;
        
        // verifier stuff here
        gameDB.addGame(game);
    }
}