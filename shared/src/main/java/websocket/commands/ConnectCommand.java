package websocket.commands;

import chess.ChessGame;

public class ConnectCommand extends UserGameCommand{

    public ConnectCommand(String authToken, int gameID, ChessGame.TeamColor teamColor) {
        super(authToken, gameID, teamColor);
        this.commandType = CommandType.CONNECT;
    }
}
