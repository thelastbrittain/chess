package dataaccess.sqldaos;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import model.GameData;
import response.JoinGameResponse;
import translation.Translator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static dataaccess.DatabaseManager.executeUpdate;
import static dataaccess.DatabaseManager.getConnection;

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
        var statement = "INSERT INTO game (game_id, game_name, game_info) VALUES (?, ?, ?)";
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
        String statement = "SELECT COUNT(*) FROM game WHERE game_id = ?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, String.valueOf(gameID));

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Access Denied: " + e.getMessage());
            return false;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Collection<GameData> listGames() {
        Collection<GameData> gameDataList = new ArrayList<>();
        String statement = "SELECT game_id, white_username, black_username, game_name, game_info FROM game";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                int gameID = rs.getInt("game_id");
                String whiteUsername = rs.getString("white_username");
                String blackUsername = rs.getString("black_username");
                String gameName = rs.getString("game_name");
                String gameInfo = rs.getString("game_info");


                ChessGame game = Translator.fromJsontoObjectNotRequest(gameInfo, ChessGame.class);

                GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                gameDataList.add(gameData);
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Error Listing the games: " + e.getMessage());
            return null;
        }
        return gameDataList;
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
