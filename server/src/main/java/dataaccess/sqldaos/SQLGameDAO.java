package dataaccess.sqldaos;

import chess.ChessGame;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import model.GameData;
import response.JoinGameResponse;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clearApplication(AuthDAO authData, UserDAO userData) {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
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

    }
}
