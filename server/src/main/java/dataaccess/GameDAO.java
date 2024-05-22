package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
     void clearApplication(AuthDAO authData, UserDAO userData);
     int createGame(String gameName);
     boolean isVerifiedGame(int gameID);
     Collection<GameData> listGames();
     void updateUserInGame(int gameID, String username, TeamColor teamColor);
     void updateGame(int gameID, ChessGame game);
     void clearGames();
}
