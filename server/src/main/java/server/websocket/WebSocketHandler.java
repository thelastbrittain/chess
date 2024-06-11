package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import translation.Translator;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            UserGameCommand command = Translator.fromJsontoObjectNotRequest(message, UserGameCommand.class);

//            String username = getUsername(command.getAuthString()); //use handler/service/dao to get username
//
//            saveSession(command.getGameID(), session);              //make class that holds all required info
//
//            switch (command.getCommandType()) {
//                case CONNECT -> connect(session, username, (ConnectCommand) command);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);
//            }
//        } catch (UnauthorizedException ex) {
//            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
//        }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
