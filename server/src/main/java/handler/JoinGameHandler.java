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
        ChessGame.TeamColor tColor = ChessGame.TeamColor.BLACK;

        System.out.println(colorAndID.playerColor());
        if (colorAndID.playerColor() == null){response.status(400);
            return new JoinGameResponse(ErrorMessages.BADREQUEST);}
        else if (colorAndID.playerColor().equals(ChessGame.TeamColor.WHITE)){
            tColor = ChessGame.TeamColor.WHITE;
        } else if (colorAndID.playerColor().equals(ChessGame.TeamColor.BLACK)){
            tColor = ChessGame.TeamColor.BLACK;
        }



        JoinGameRequest joinGameRequest = new JoinGameRequest(tColor, colorAndID.gameID(), authToken);


        GameService gameService = new GameService(authDAO, gameDAO);
        JoinGameResponse joinGameResponse = gameService.joinGame(joinGameRequest);

        return gson.toJson(joinGameResponse);
    }
}
