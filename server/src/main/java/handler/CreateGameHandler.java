package handler;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.CreateGameRequest;
import response.CreateGameResponse;
import service.ErrorMessages;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;
import translation.Translator;

public class CreateGameHandler implements Route {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public CreateGameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        System.out.println("Made it to the handler");
        String authToken = request.headers("Authorization");
        System.out.println("Auth token entered into createGame success: " + authToken);
        CreateGameRequest gameName = (CreateGameRequest) Translator.fromJsonToObject(request, CreateGameRequest.class);
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName.gameName(), authToken);
        GameService gameService = new GameService(authDAO, gameDAO);
        CreateGameResponse createGameResponse = gameService.createGame(createGameRequest);

        if (createGameResponse.message() == null){
            response.status(200);
        } else if (createGameResponse.message().equals(ErrorMessages.UNAUTHORIZED)){
            response.status(401);
        } else if (createGameResponse.message().equals(ErrorMessages.SQLERROR)) {
            response.status(500);
        }

        return Translator.fromObjectToJson(createGameResponse);
    }
}
