package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.sqldaos.SQLAuthDAO;
import dataaccess.sqldaos.SQLGameDAO;
import dataaccess.sqldaos.SQLUserDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import request.GetGameRequest;
import request.LeaveGameRequest;
import request.MakeMoveRequest;
import request.ResignGameRequest;
import response.GetGameResponse;
import response.LeaveGameResponse;
import response.MakeMoveResponse;
import service.GameService;
import translation.Translator;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    SQLUserDAO userDAO;
    SQLAuthDAO authDAO;
    SQLGameDAO gameDAO;
    GameService gameService;
    private static final String CHECKMESSAGE = "is in Check";
    private static final String CHECKMATEMESSAGE = "is in Checkmate";
    private static final String STALEMATEMESSAGE = "Game is in Stalemate";

    public WebSocketHandler(SQLUserDAO userDAO, SQLAuthDAO authDAO, SQLGameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.gameService = new GameService(authDAO,gameDAO);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            UserGameCommand command = Translator.fromJsontoObjectNotRequest(message, UserGameCommand.class);
            String username = new SQLAuthDAO().getUsernameFromAuth(command.getAuthString()); //use handler/service/dao to get username
            if (username == null){
                session.getRemote().sendString(Translator.fromObjectToJson(new ErrorMessage("Invalid authToken.")).toString());
                System.out.println("Bad auth token in onMesage WSHandler " + Translator.fromObjectToJson(new ErrorMessage("Invalid authToken.")).toString());
                return;
            }
            connections.add(command.getGameID(), username, session);


            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(Session session, String username, ConnectCommand command) {
        String messageToOthers = String.format("%s has joined the game as %s", username, typeOfPlayer(command));
        NotificationMessage notificationToOthers = new NotificationMessage(messageToOthers);
        GetGameResponse response = gameService.returnGame(new GetGameRequest(command.getAuthString(), command.getGameID()));


        LoadGameMessage loadGameMessage = new LoadGameMessage(response.game());
        try {
            if (response.message() != null) {
                connections.sendMessageToUser(command.getGameID(), username, new ErrorMessage(response.message()));
            } else {
                connections.sendMessageToUser(command.getGameID(), username, loadGameMessage);
                connections.sendMessageToAllButUser(command.getGameID(), username, notificationToOthers);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand command){
        MakeMoveResponse response = gameService.makeMove(new MakeMoveRequest(command.getAuthString(), command.getGameID(), command.getMove(), command.getTeamColor()));

        String messageToUser = response.message();
        String gameStatusMessage = null;
        String enemyColor = typeOfPlayer(command);
        if (response.isInCheckmate()){
            gameStatusMessage = String.format("%s %s", enemyColor, CHECKMATEMESSAGE);
        } else if (response.isInCheck()){
            gameStatusMessage = String.format("%s %s", enemyColor, CHECKMESSAGE);
        } else if (response.isInStalemate()){
            gameStatusMessage = STALEMATEMESSAGE;
        }

        try {
            //if messageToUser is not null, just send error to him, then return
            if (messageToUser != null){
                connections.sendMessageToUser(command.getGameID(), username, new ErrorMessage(messageToUser));
            } else {
                //load the new game for everyone
                ChessGame gameToReturn = response.game();
                LoadGameMessage loadGameMessage = new LoadGameMessage(gameToReturn);
                connections.sendMessageToAll(command.getGameID(), loadGameMessage);
                //send what move he made to everyone
                String moveMessage = moveTranslator(command.getMove(), command.getTeamColor());
                connections.sendMessageToAllButUser(command.getGameID(), username, new NotificationMessage(moveMessage));
                //send game status message if there is one.
                if (gameStatusMessage != null){
                    connections.sendMessageToAll(command.getGameID(), new NotificationMessage(gameStatusMessage));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void leaveGame(Session session, String username, LeaveGameCommand command){
        String messageToOthers = String.format("%s has left the game", username);
        NotificationMessage notificationToOthers = new NotificationMessage(messageToOthers);
        LeaveGameResponse response =  gameService.leaveGame(new LeaveGameRequest(command.getAuthString(), command.getGameID(), command.getTeamColor()));
        try {
            if (response.message() != null){
                connections.sendMessageToUser(command.getGameID(),username ,new ErrorMessage(response.message()));
            } else {
                connections.sendMessageToAllButUser(command.getGameID(), username, notificationToOthers);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resign(Session session, String username, ResignCommand command){
        String messageToOthers = String.format("%s has resigned the game", username);
        NotificationMessage notificationToOthers = new NotificationMessage(messageToOthers);
        LeaveGameResponse response = gameService.resignGame(new ResignGameRequest(command.getAuthString(), command.getGameID()));
        try {
            if (response.message() != null){
                connections.sendMessageToUser(command.getGameID(),username ,new ErrorMessage(response.message()));
            } else {
                connections.sendMessageToAllButUser(command.getGameID(), username, notificationToOthers);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String typeOfPlayer(ChessGame.TeamColor teamColor) {
        if (teamColor == null){
            return "Observer";
        } else if (teamColor.equals(ChessGame.TeamColor.WHITE)){
            return "White Player";
        } else {
            return "Black Player";
        }
    }

    private String moveTranslator(ChessMove move, ChessGame.TeamColor teamColor) {
        int fromRow = move.getStartPosition().getRow();
        String fromCol =  translateCol(move.getStartPosition().getColumn());
        int toRow = move.getEndPosition().getRow();
        String toCol = translateCol(move.getEndPosition().getColumn());
        String from = fromCol + fromRow;
        String to = toCol + toRow;

        return String.format("%s player moved from %s to %s",teamColor, from, to );
    }

    private String translateCol(int col){
        String columnLetter = " ";
        if (col > 0 && col < 9) {
            // Get the second character and convert it to an integer

            switch (col) {
                case 1:
                    columnLetter = "a";
                    break;
                case 2:
                    columnLetter = "b";
                    break;
                case 3:
                    columnLetter = "c";
                    break;
                case 4:
                    columnLetter = "d";
                    break;
                case 5:
                    columnLetter = "e";
                    break;
                case 6:
                    columnLetter = "f";
                    break;
                case 7:
                    columnLetter = "g";
                    break;
                case 8:
                    columnLetter = "h";
                    break;
            }
            return columnLetter;
        }
         else {
            return "Not valid"; // out of range
        }
    }

    private ChessGame.TeamColor getTeamColor(String username, int gameID){
        return gameDAO.getTeamColor(gameID, username);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage() + throwable.getStackTrace());
        // Add additional error handling logic here
    }
}
