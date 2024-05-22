package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import request.CreateGameRequest;

public class GameService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public int createGame(CreateGameRequest createGameRequest){
        if (!authDAO.isVerifiedAuth(createGameRequest.authToken())){return 0;} //return some error code
        return gameDAO.createGame(createGameRequest.gameName());  //returns gameID
    }
}
