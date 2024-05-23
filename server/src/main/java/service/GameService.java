package service;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
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

//    public void joinGame(JoinGameRequest joinGameRequest){
//        if (!authDAO.isVerifiedAuth(joinGameRequest.authToken())){return;} //add error message
//        if (!gameDAO.isVerifiedGame(joinGameRequest.gameID())){return;}   //add error message
//        gameDAO.updateUserInGame(joinGameRequest.gameID(), joinGameRequest.authToken(), );  //needs username t dangit.


}
