package dataaccess.memorydaos;


import chess.ChessGame;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import model.GameData;
import response.JoinGameResponse;
import service.ErrorMessages;


import java.util.ArrayList;
import java.util.Collection;


public class MemoryGameDAO implements GameDAO {
    private Collection<GameData> gameDataList = new ArrayList<>();
    private int initialGameID;


    public MemoryGameDAO(){
        initialGameID = 1;
    }

    @Override
    public void clearApplication(AuthDAO authData, UserDAO userData) {
        authData.clearAuths();
        userData.clearUsers();
        this.clearGames();
    }

    private int createGameID(){
        return initialGameID++;
    }
    @Override
    public int createGame(String gameName) {
        int newGameID = createGameID();
        GameData newGame = new GameData(newGameID, null, null, gameName, new ChessGame());
        gameDataList.add(newGame);
        return newGameID;
    }

    @Override
    public boolean isVerifiedGame(int gameID) {
        for (GameData gameData: gameDataList){
            if (gameData.getGameID() == gameID){
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<GameData> listGames() {
        return gameDataList;
    }

    @Override
    public JoinGameResponse updateUserInGame(int gameID, String username, ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.BLACK) {
            for (GameData gameData : gameDataList) {
                if (gameData.getGameID() == gameID) {
                    if (gameData.getBlackUsername() == null) {
                        gameData.setBlackUsername(username);
                        return new JoinGameResponse(null);
                    } else {
                        return new JoinGameResponse(ErrorMessages.ALREADYTAKEN);
                    } //add something better
                }
            }
        } else {
            for (GameData gameData : gameDataList) {
                if (gameData.getGameID() == gameID) {
                    if (gameData.getWhiteUsername() == null) {
                        gameData.setWhiteUsername(username);
                        return new JoinGameResponse(null);
                    } else {
                        return new JoinGameResponse(ErrorMessages.ALREADYTAKEN);
                    } //add something better
                }
            }
        }
        return new JoinGameResponse(ErrorMessages.UNAUTHORIZED);
    }

    public void clearGames(){
        gameDataList.clear();
    }
}
