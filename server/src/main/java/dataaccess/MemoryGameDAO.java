package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        return newGameID;
    }

    @Override
    public boolean isVerifiedGame(int gameID) {
        for (GameData gameData: gameDataList){
            if (gameData.gameID() == gameID){
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
    public void updateGame(int gameID, String username) {
    }

    public void clearGames(){
        gameDataList.clear();
    }
}
