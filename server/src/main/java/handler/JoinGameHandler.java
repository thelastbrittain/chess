package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.JoinGameResponse;
import service.ErrorMessages;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson gson = new Gson();

        String authToken = request.headers("Authorization");
        JoinGameRequest colorAndID = gson.fromJson(request.body(), JoinGameRequest.class);

        if (colorAndID.playerColor() == null){response.status(400);
            return gson.toJson(new JoinGameResponse(ErrorMessages.BADREQUEST));}

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

        return gson.toJson(joinGameResponse);
    }
}
