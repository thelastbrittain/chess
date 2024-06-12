package server.websocket;

import chess.ChessGame;
import dataaccess.sqldaos.SQLAuthDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import translation.Translator;
import websocket.commands.*;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

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
        String messageToOthers = String.format("%s has joined the game", username);
        String messageToUser = "Loading game...";
        NotificationMessage notificationToOthers = new NotificationMessage(messageToOthers);
        LoadGameMessage loadGameMessage = new LoadGameMessage(new ChessGame()); // need to pull the chess game from the database // for now will just be new game
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

    }

    private void leaveGame(Session session, String username, LeaveGameCommand command){

    }

    private void resign(Session session, String username, ResignCommand command){

    }


}
