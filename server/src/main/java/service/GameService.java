package service;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.CreateGameRequest;
import response.CreateGameResponse;

public class GameService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest){
        System.out.println("The auth Token in the service is " + createGameRequest.authToken());
        if (!authDAO.isVerifiedAuth(createGameRequest.authToken())){return new CreateGameResponse(null, ErrorMessages.UNAUTHORIZED);} //return some error code
        int gameID = gameDAO.createGame(createGameRequest.gameName());
        return new CreateGameResponse(gameID, null);
    }

//    public void joinGame(JoinGameRequest joinGameRequest){
//        if (!authDAO.isVerifiedAuth(joinGameRequest.authToken())){return;} //add error message
//        if (!gameDAO.isVerifiedGame(joinGameRequest.gameID())){return;}   //add error message
//        gameDAO.updateUserInGame(joinGameRequest.gameID(), joinGameRequest.authToken(), );  //needs username t dangit.


}
