package handler;

import com.google.gson.Gson;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.CreateGameRequest;
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
        System.out.println(createGameRequest.authToken() + " " + createGameRequest.gameName());
        GameService gameService = new GameService(authDAO, gameDAO);

        return gameService.createGame(createGameRequest);
    }


}
