package dataaccess.interfaces;

import chess.ChessGame;
import model.GameData;
import response.JoinGameResponse;

import java.util.Collection;

public interface GameDAO {
     void clearApplication(AuthDAO authData, UserDAO userData);
     int createGame(String gameName);
     boolean isVerifiedGame(int gameID);
     Collection<GameData> listGames();
     JoinGameResponse updateUserInGame(int gameID, String username, ChessGame.TeamColor teamColor);
     void clearGames();
     int createGameID();

    ChessGame getGame(int i);

     boolean updateGame(int gameID, ChessGame game);
}
