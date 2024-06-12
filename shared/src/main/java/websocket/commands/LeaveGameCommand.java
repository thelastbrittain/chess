package websocket.commands;

import chess.ChessGame;

public class LeaveGameCommand extends UserGameCommand{

    public LeaveGameCommand(String authToken, int gameID, ChessGame.TeamColor teamColor) {
        super(authToken, gameID, teamColor);
        this.commandType = CommandType.LEAVE;
    }
}
