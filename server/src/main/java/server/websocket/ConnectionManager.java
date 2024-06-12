package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Collection<Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String username, Session session) {
        var connection = new Connection(username, session);
        connections.compute(gameID, (key, existingCollection) -> {
            if (existingCollection == null) {
                existingCollection = new CopyOnWriteArrayList<>();
            }
            existingCollection.add(connection);
            return existingCollection;
        });
    }

//    public void remove(String visitorName) {
//        connections.remove(visitorName);
//    }

    public void sendMessageToAllButUser(int gameID, String excludeUsername, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        Collection<Connection> gameConnections = connections.get(gameID);
        for (var c : gameConnections) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(message.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            gameConnections.remove(c);
        }
    }

    public void sendMessageToAll(int gameID, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        Collection<Connection> gameConnections = connections.get(gameID);
        for (var c : gameConnections) {
            if (c.session.isOpen()) {
                c.send(message.toString());
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            gameConnections.remove(c);
        }
    }

    public void sendMessageToUser(int gameID, String username, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        Collection<Connection> gameConnections = connections.get(gameID);
        for (var c : gameConnections) {
            if (c.session.isOpen()) {
                if (c.username.equals(username)) {
                    c.send(message.toString());
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            gameConnections.remove(c);
        }
    }

}
