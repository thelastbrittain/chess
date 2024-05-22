package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface GameDAO {
     void clearApplication(AuthData authData, UserData userData);
     int createGame(String gameName);
     boolean isVerifiedGame(int gameID);
     Collection<GameData> listGames();
     void updateGame(int gameID, String username);
}
