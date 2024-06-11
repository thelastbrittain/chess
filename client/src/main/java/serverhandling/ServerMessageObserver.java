package serverhandling;

import websocket.messages.ServerMessage;

public interface ServerMessageObserver {
    public void notify(ServerMessage message);
}
