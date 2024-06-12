package service;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.CreateGameRequest;
import request.GetGameRequest;
import request.JoinGameRequest;
import request.LeaveGameRequest;
import response.*;

public class GameService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest){
        if (!authDAO.isVerifiedAuth(createGameRequest.authToken())){return new CreateGameResponse(null, ErrorMessages.UNAUTHORIZED);}
        int gameID = gameDAO.createGame(createGameRequest.gameName());
        if (gameID == 0){return new CreateGameResponse(null, ErrorMessages.SQLERROR);}
        return new CreateGameResponse(gameID, null);
    }

    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) {
        if (!authDAO.isVerifiedAuth(joinGameRequest.authToken())) {
            return new JoinGameResponse(ErrorMessages.UNAUTHORIZED);
        }
        if (!gameDAO.isVerifiedGame(joinGameRequest.gameID())) {
            return new JoinGameResponse(ErrorMessages.BADREQUEST);
        }
        return gameDAO.updateUserInGame(joinGameRequest.gameID(), authDAO.getUsernameFromAuth(joinGameRequest.authToken()), joinGameRequest.playerColor());
    }

    public LeaveGameResponse leaveGame(LeaveGameRequest request) {
        if (!authDAO.isVerifiedAuth(request.authToken())) {
            return new LeaveGameResponse(ErrorMessages.UNAUTHORIZED);
        }
        if (!gameDAO.isVerifiedGame(request.gameID())) {
            return new LeaveGameResponse(ErrorMessages.BADREQUEST);
        }
        return new LeaveGameResponse(gameDAO.updateUserInGame(request.gameID(), null, request.teamColor()).message());
    }

    public ListGamesResponse listGames(String authToken){
        if (!authDAO.isVerifiedAuth(authToken)) {return new ListGamesResponse(null, ErrorMessages.UNAUTHORIZED);}

        return new ListGamesResponse(gameDAO.listGames(), null);
    }

    public GetGameResponse returnGame(GetGameRequest request){
        if (!authDAO.isVerifiedAuth(request.authToken())) {return new GetGameResponse(null, ErrorMessages.UNAUTHORIZED);}

        return new GetGameResponse(gameDAO.getGame(request.gameID()), null);
    }

}
