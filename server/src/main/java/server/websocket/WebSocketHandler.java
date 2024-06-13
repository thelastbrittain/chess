package server.websocket;

import chess.ChessGame;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.sqldaos.SQLAuthDAO;
import dataaccess.sqldaos.SQLGameDAO;
import dataaccess.sqldaos.SQLUserDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import request.GetGameRequest;
import request.LeaveGameRequest;
import request.MakeMoveRequest;
import request.ResignGameRequest;
import response.MakeMoveResponse;
import service.GameService;
import translation.Translator;
import websocket.commands.*;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

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

    private void connect(Session session, String username, ConnectCommand command){
        String messageToOthers = String.format("%s has joined the game as %s", username, typeOfPlayer(command));
        String messageToUser = "Loading game...";
        NotificationMessage notificationToOthers = new NotificationMessage(messageToOthers);
        ChessGame gameToReturn = gameService.returnGame(new GetGameRequest(command.getAuthString(), command.getGameID())).game();

        LoadGameMessage loadGameMessage = new LoadGameMessage(gameToReturn);
        try {
            connections.sendMessageToUser(command.getGameID(), username, loadGameMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            connections.sendMessageToAllButUser(command.getGameID(), username, notificationToOthers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand command){
        MakeMoveResponse response = gameService.makeMove(new MakeMoveRequest(command.getAuthString(), command.getGameID(), command.getMove(), command.getTeamColor()));
        String messageToUser = response.message();
        String gameStatusMessage = null;
        String moveMessage= null;
        String enemyColor = typeOfPlayer(command);
        //get the messages
        if (response.isInCheckmate() == true){
            gameStatusMessage = String.format("%s %s", enemyColor, CHECKMATEMESSAGE);
        } else if (response.isInCheck() == true){
            gameStatusMessage = String.format("%s %s", enemyColor, CHECKMESSAGE);
        } else if (response.isInStalemate() == true){
            gameStatusMessage = STALEMATEMESSAGE;
        }
        //remember to return before sending any more messages if the move if a failure
        ChessGame gameToReturn = gameService.returnGame(new GetGameRequest(command.getAuthString(), command.getGameID())).game();
        LoadGameMessage loadGameMessage = new LoadGameMessage(gameToReturn);
        try {
            //if messageToUser is not null, just send error to him, then return
            //else load the game for everyone
            //then send them the move message
            //if gameStatus message is not empty, send that message
            connections.sendMessageToUser(command.getGameID(), username, loadGameMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void leaveGame(Session session, String username, LeaveGameCommand command){
        String messageToOthers = String.format("%s has left the game", username);
        NotificationMessage notificationToOthers = new NotificationMessage(messageToOthers);
        gameService.leaveGame(new LeaveGameRequest(command.getAuthString(), command.getGameID(), command.getTeamColor()));
        try {
            connections.sendMessageToAllButUser(command.getGameID(), username, notificationToOthers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resign(Session session, String username, ResignCommand command){
        String messageToOthers = String.format("%s has resigned the game", username);
        NotificationMessage notificationToOthers = new NotificationMessage(messageToOthers);
        gameService.resignGame(new ResignGameRequest(command.getAuthString(), command.getGameID()));
        try {
            connections.sendMessageToAllButUser(command.getGameID(), username, notificationToOthers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String typeOfPlayer(UserGameCommand command){
        if (command.getTeamColor() == null){
            return "Observer";
        } else if (command.getTeamColor().equals(ChessGame.TeamColor.WHITE)){
            return "White Player";
        } else {
            return "Black Player";
        }
    }
}
