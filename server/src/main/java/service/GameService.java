package service;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.*;
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

    public LeaveGameResponse resignGame(ResignGameRequest request) {
        if (!authDAO.isVerifiedAuth(request.authToken())) {
            return new LeaveGameResponse(ErrorMessages.UNAUTHORIZED);
        }
        if (!gameDAO.isVerifiedGame(request.gameID())) {
            return new LeaveGameResponse(ErrorMessages.BADREQUEST);
        }
        ChessGame tempGame = gameDAO.getGame(request.gameID());
        tempGame.setGameOver(true);

        gameDAO.updateGame(request.gameID(), tempGame);

        return new LeaveGameResponse(null);
    }

    public MakeMoveResponse makeMove(MakeMoveRequest request){
        if (!authDAO.isVerifiedAuth(request.authToken())) {
            return new MakeMoveResponse(ErrorMessages.UNAUTHORIZED, false, false, false, null);
        }
        if (!gameDAO.isVerifiedGame(request.gameID())) {
            return new MakeMoveResponse(ErrorMessages.BADREQUEST,false, false,false, null);
        }

        ChessGame game = returnGame(new GetGameRequest(request.authToken(), request.gameID())).game();
        try {
            game.makeMove(request.move());
        } catch (InvalidMoveException e) {
            System.out.println(e.toString() + e.getMessage());
            return new MakeMoveResponse(e.getMessage(), false, false, false, null);
        }
        if (isInCheckmate(request.teamColor(), game)){
            return new MakeMoveResponse(null, false, true, false, game);
        } else if (isInCheck(request.teamColor(), game)){
            return new MakeMoveResponse(null, true, false, false, game);
        } else if (isInStalemate(request.teamColor(), game)){
            return new MakeMoveResponse(null, false, false, true, game);
        } else {
            return new MakeMoveResponse(null,false, false, false, game);
        }


    }

    private boolean isInCheck(ChessGame.TeamColor teamColor, ChessGame game){
        ChessGame.TeamColor enemyColor = getEnemyColor(teamColor);
        if (game.isInCheck(enemyColor)){
            return true;
        } else {
            return false;
        }
    }

    private boolean isInCheckmate(ChessGame.TeamColor teamColor, ChessGame game){
        ChessGame.TeamColor enemyColor = getEnemyColor(teamColor);
        if (game.isInCheckmate(enemyColor)){
            return true;
        } else {
            return false;
        }
    }

    private boolean isInStalemate(ChessGame.TeamColor teamColor, ChessGame game){
        ChessGame.TeamColor enemyColor = getEnemyColor(teamColor);
        if (game.isInStalemate(enemyColor)){
            return true;
        } else {
            return false;
        }
    }

    private ChessGame.TeamColor getEnemyColor(ChessGame.TeamColor teamColor){
        ChessGame.TeamColor enemyTeamColor;
        if (teamColor.equals(ChessGame.TeamColor.WHITE)){
            return  ChessGame.TeamColor.BLACK;
        } else {
            return ChessGame.TeamColor.WHITE;
        }
    }

}
