package serverhandling;

import translationForClient.TranslatorForClient;
import websocket.commands.ConnectCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSCommunicator extends Endpoint {
    String url;
    Session session;
    ServerMessageObserver messager;


    public WSCommunicator(String url, ServerMessageObserver messager) {
        try {
            this.url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messager = messager;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = TranslatorForClient.fromJsontoObjectNotRequest(message, ServerMessage.class);
                    messager.notify(notification);
                }
            });

        } catch (URISyntaxException | DeploymentException | IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) {
        try {
            ConnectCommand command = new ConnectCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(TranslatorForClient.fromObjectToJson(command).toString());
        } catch (IOException ex) {
            System.out.println("Unable to send connection: " + ex.getMessage());
        }
    }
}
