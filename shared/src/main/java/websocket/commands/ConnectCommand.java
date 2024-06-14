package websocket.commands;

import chess.ChessGame;

public class ConnectCommand extends UserGameCommand{

    public ConnectCommand(String authToken, int gameID) {
        super(authToken, gameID);
        this.commandType = CommandType.CONNECT;
    }
}
