package handler;

import com.google.gson.Gson;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.CreateGameRequest;
import response.CreateGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public CreateGameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson gson = new Gson();
        String authToken = request.headers("Authorization");
        CreateGameRequest gameName = gson.fromJson(request.body(), CreateGameRequest.class);
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName.gameName(), authToken);


        GameService gameService = new GameService(authDAO, gameDAO);
        CreateGameResponse createGameResponse = gameService.createGame(createGameRequest);
        if (createGameResponse.message() == null){
            response.status(200);
        } else{
            response.status(401);
        }
        return createGameResponse;
    }
}
