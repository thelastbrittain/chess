package dataaccess.sqldaos;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import model.GameData;
import response.JoinGameResponse;
import translation.Translator;

import java.util.Collection;
import java.util.List;

import static dataaccess.DatabaseManager.executeUpdate;

public class SQLGameDAO implements GameDAO {
    private int initialGameID;

    public SQLGameDAO(){
        initialGameID = 1;
    }

    @Override
    public void clearApplication(AuthDAO authData, UserDAO userData) {
        clearGames();
        authData.clearAuths();
        userData.clearUsers();
    }

    @Override
    public int createGameID(){
        return initialGameID++;
    }

    @Override
    public int createGame(String gameName) {
        var statement = "INSERT INTO game (gameID,gameName, game) VALUES (?, ?)";
        int gameID = createGameID();
        String gameJson = (String) Translator.fromObjectToJson(new GameData(gameID, null, null, gameName, new ChessGame()));
        try {
            executeUpdate(statement, gameID, gameName, gameJson);
            return gameID;
        } catch (DataAccessException e) {
            System.out.println("Error creating a game: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean isVerifiedGame(int gameID) {
        return false;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public JoinGameResponse updateUserInGame(int gameID, String username, ChessGame.TeamColor teamColor) {
        return null;
    }

    @Override
    public void clearGames() {
        var statement = "TRUNCATE game";
        try {
            executeUpdate(statement);
        } catch (DataAccessException e) {
            System.out.println("Error clearing user table: " + e.getMessage());
        }
    }
}
