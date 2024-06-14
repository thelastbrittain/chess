package websocket.commands;

import chess.ChessGame;

public class ResignCommand extends UserGameCommand{

    public ResignCommand(String authToken, int gameID) {
        super(authToken, gameID);
        this.commandType = CommandType.RESIGN;
    }
}
