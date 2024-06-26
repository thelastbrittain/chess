package handler;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import response.ListGamesResponse;
import service.ErrorMessages;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;
import translation.Translator;

public class ListGamesHandler implements Route {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public ListGamesHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String authToken = request.headers("Authorization");
        GameService listGames = new GameService(authDAO, gameDAO);
        ListGamesResponse listGamesResponse = listGames.listGames(authToken);

        if (listGamesResponse.message() == null){
            response.status(200);
        } else if (listGamesResponse.message().equals(ErrorMessages.UNAUTHORIZED)){
            response.status(401);
        }

        return Translator.fromObjectToJson(listGamesResponse);
    }
}
