package serverhandling;

import translationforclient.TranslatorForClient;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSCommunicator extends Endpoint {
    String url;
    Session session;
    ServerMessageObserver messageObserver;


    public WSCommunicator(String url, ServerMessageObserver messager) {
        try {
            this.url = url.replace("http", "ws");
            URI socketURI = new URI(this.url + "/ws");
            this.messageObserver = messager;
//            System.out.println("Here is the URL: " + socketURI);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
//            System.out.println("Made a connection: " + session);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
//                    System.out.println("Made it into onMessage: " + message);
                    ServerMessage notification = TranslatorForClient.fromJsontoObjectNotRequest(message, ServerMessage.class);
//                    System.out.println("THis is the message: " + notification);
                    messageObserver.notify(notification);
//                    System.out.println("Message Observer notification sent.");
                }
            });

        } catch (URISyntaxException | DeploymentException | IOException ex){
            System.out.println("Error connecting to websocket. In WS Communicator " + ex.getMessage());
        }
    }
    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(ConnectCommand command) {
        try {
            assert this.session != null;
            this.session.getBasicRemote().sendText(TranslatorForClient.fromObjectToJson(command).toString());
        } catch (IOException ex) {
            System.out.println("Unable to send connection: " + ex.getMessage());
        }
    }

    public void makeMove(MakeMoveCommand command){
        System.out.println("Made it into makeMove in WSCommunicator");
        try{
            this.session.getBasicRemote().sendText(TranslatorForClient.fromObjectToJson(command).toString());
        } catch (IOException e) {
            System.out.println("Unable to make move: " + e.getMessage());
        }
    }

    public void leave(LeaveGameCommand command){
        try{
            this.session.getBasicRemote().sendText(TranslatorForClient.fromObjectToJson(command).toString());
        } catch (IOException e) {
            System.out.println("Unable to leave game: " + e.getMessage());
        }
    }

    public void resign(ResignCommand command){
        try{
            this.session.getBasicRemote().sendText(TranslatorForClient.fromObjectToJson(command).toString());
        } catch (IOException e) {
            System.out.println("Unable to resign game: " + e.getMessage());
        }
    }
}
