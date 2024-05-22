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
        System.out.println("The auth Token in the service is " + createGameRequest.authToken());
        if (!authDAO.isVerifiedAuth(createGameRequest.authToken())){
            System.out.println("Auth Token not authorized through the service. ");
            return 0;} //return some error code
        return gameDAO.createGame(createGameRequest.gameName());  //returns gameID
    }
}
