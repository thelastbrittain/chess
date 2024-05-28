package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.JoinGameResponse;
import response.ListGamesResponse;

public class GameService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        System.out.println("The auth Token in the service is " + createGameRequest.authToken());
        if (!authDAO.isVerifiedAuth(createGameRequest.authToken())){return new CreateGameResponse(null, ErrorMessages.UNAUTHORIZED);} //return some error code
        int gameID = gameDAO.createGame(createGameRequest.gameName());
        return new CreateGameResponse(gameID, null);
    }

    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) throws DataAccessException{
        if (!authDAO.isVerifiedAuth(joinGameRequest.authToken())) {
            return new JoinGameResponse(ErrorMessages.UNAUTHORIZED);
        } //add error message
        if (!gameDAO.isVerifiedGame(joinGameRequest.gameID())) {
            return new JoinGameResponse(ErrorMessages.BADREQUEST);
        }   //add error message
        return gameDAO.updateUserInGame(joinGameRequest.gameID(), authDAO.getUsernameFromAuth(joinGameRequest.authToken()), joinGameRequest.playerColor());  //needs username t dangit.
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException{
        if (!authDAO.isVerifiedAuth(authToken)) {return new ListGamesResponse(null, ErrorMessages.UNAUTHORIZED);}
        return new ListGamesResponse(gameDAO.listGames(), null);
    }

}
