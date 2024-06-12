package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    ChessMove move;

    public MakeMoveCommand(String authToken, int gameID, ChessMove move, ChessGame.TeamColor teamColor) {
        super(authToken, gameID, teamColor);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
    }

}
