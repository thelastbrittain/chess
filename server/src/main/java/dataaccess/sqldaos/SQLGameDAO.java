package dataaccess.sqldaos;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import model.GameData;
import response.JoinGameResponse;
import service.ErrorMessages;
import translation.Translator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
        var statement = "INSERT INTO game (game_name, game_info) VALUES (?, ?)";
//        int gameID = createGameID();
        String gameJson = (String) Translator.fromObjectToJson(new ChessGame());
        try {
            return executeUpdate(statement, gameName, gameJson);
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
        } catch (SQLException | DataAccessException e) {
            System.out.println("Access Denied: " + e.getMessage());
            e.printStackTrace();
            return false;
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
            e.printStackTrace();
            return gameDataList;
        }
        return gameDataList;
    }

    @Override
    public ChessGame getGame(int gameID){
        String statement = "SELECT game_info FROM game WHERE game_id = ?";
        ChessGame game = null;

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String gameInfo = rs.getString("game_info");
                    game = Translator.fromJsontoObjectNotRequest(gameInfo, ChessGame.class);
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Error getting the game with ID " + gameID + ": " + e.getMessage());
            e.printStackTrace();
        }

        return game;
    }

    @Override
    public boolean updateGame(int gameID, ChessGame game){
        String statement= "UPDATE game SET game_info = ? WHERE game_id = ?";
        String gameJson = (String) Translator.fromObjectToJson(game);
        try{
            executeUpdate(statement, gameJson, gameID);
            return true;
        }catch(DataAccessException e){
            System.out.println("Error updating a game:" + e.getMessage());
            return false;
        }
    }

    @Override
    public JoinGameResponse updateUserInGame(int gameID, String username, ChessGame.TeamColor teamColor) {
        if (!(teamColor == ChessGame.TeamColor.WHITE || teamColor == ChessGame.TeamColor.BLACK)){
            return new JoinGameResponse(ErrorMessages.BADREQUEST);
        }
        if (!isEmpty(gameID, teamColor) && username != null){return new JoinGameResponse(ErrorMessages.ALREADYTAKEN);}
        String statement;
        if (teamColor.equals(ChessGame.TeamColor.WHITE)){
            statement = "UPDATE game SET white_username = ? WHERE game_id = ?";
        } else {
            statement = "UPDATE game SET black_username = ? WHERE game_id = ?";
        }

        try {
            executeUpdate(statement, username, gameID);
        } catch (DataAccessException e) {
            System.out.println("Error inserting username: " + e.getMessage());
            return new JoinGameResponse(ErrorMessages.SQLERROR);
        }
        return new JoinGameResponse(null);
    }


    private boolean isEmpty(int gameID, ChessGame.TeamColor teamColor){
        String usernameType;
        String statement;
        if (teamColor == ChessGame.TeamColor.WHITE){
            usernameType = "white_username";
            statement = "SELECT white_username FROM game WHERE game_id = ?";
        } else {
            usernameType = "black_username";
            statement = "SELECT black_username FROM game WHERE game_id = ?";
        }

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setInt(1, gameID);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString(usernameType);
                    return (username == null || username.isEmpty());
                } else {
                    System.out.println("Game does not exist.");
                    return false;
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("SQL Error in checking if game user is empty: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ChessGame.TeamColor getTeamColor(int gameID, String username) {
        String statement = "SELECT white_username, black_username FROM game WHERE game_id = ?";
        ChessGame.TeamColor teamColor = null;

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {

            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String whiteUsername = rs.getString("white_username");
                    String blackUsername = rs.getString("black_username");

                    if (username.equals(whiteUsername)) {
                        teamColor = ChessGame.TeamColor.WHITE;
                    } else if (username.equals(blackUsername)) {
                        teamColor = ChessGame.TeamColor.BLACK;
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Error getting team color for username " + username + " and gameID " + gameID + ": " + e.getMessage());
            e.printStackTrace();
        }

        return teamColor;
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
