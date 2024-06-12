package websocket.commands;

import chess.ChessGame;

public class ResignCommand extends UserGameCommand{

    public ResignCommand(String authToken, int gameID, ChessGame.TeamColor teamColor) {
        super(authToken, gameID, teamColor);
        this.commandType = CommandType.RESIGN;
    }
}
