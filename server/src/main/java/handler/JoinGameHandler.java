package handler;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.JoinGameRequest;
import response.JoinGameResponse;
import service.ErrorMessages;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;
import translation.Translator;

public class JoinGameHandler implements Route {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String authToken = request.headers("Authorization");
        JoinGameRequest colorAndID = (JoinGameRequest) Translator.fromJsonToObject(request, JoinGameRequest.class);
        JoinGameRequest joinGameRequest = new JoinGameRequest(colorAndID.playerColor(), colorAndID.gameID(), authToken);
        GameService gameService = new GameService(authDAO, gameDAO);
        JoinGameResponse joinGameResponse = gameService.joinGame(joinGameRequest);

        if (joinGameResponse.message() == null){
            response.status(200);
        }
        else if (joinGameResponse.message().equals(ErrorMessages.UNAUTHORIZED)){
            response.status(401);
        } else if (joinGameResponse.message().equals(ErrorMessages.BADREQUEST)) {
            response.status(400);
        } else if (joinGameResponse.message().equals(ErrorMessages.ALREADYTAKEN)){
            response.status(403);
        } else {
            response.status(500);
        }

        return Translator.fromObjectToJson(joinGameResponse);
    }
}
