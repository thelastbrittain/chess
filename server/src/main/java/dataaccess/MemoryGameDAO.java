package dataaccess;


import chess.ChessGame;
import model.GameData;


import java.util.ArrayList;
import java.util.Collection;


public class MemoryGameDAO implements GameDAO{
    private Collection<GameData> gameDataList = new ArrayList<>();
    private int InitialGameID;


    public MemoryGameDAO(){
        InitialGameID = 1;
    }

    @Override
    public void clearApplication(AuthDAO authData, UserDAO userData) {
        authData.clearAuths();
        userData.clearUsers();
        this.clearGames();
    }

    private int createGameID(){
        return InitialGameID ++;
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
    public void updateUserInGame(int gameID, String username, TeamColor teamColor) {
        if (teamColor == TeamColor.BLACK){
            for (GameData gameData: gameDataList){
                if (gameData.getGameID() == gameID){
                    gameData.setBlackUsername(username);
                }
            }
        } else {
            for (GameData gameData: gameDataList){
                if (gameData.getGameID() == gameID){
                    gameData.setWhiteUsername(username);
                }
            }
        }
    }

    public void updateGame(int gameID, ChessGame newGame){
        for (GameData gameData: gameDataList){
            if (gameData.getGameID() == gameID){
                gameData.setGame(newGame);
            }
        }
    }

    public void clearGames(){
        gameDataList.clear();
    }
}