package request;

import chess.ChessGame;
import chess.ChessMove;

public record MakeMoveRequest(String authToken, int gameID, ChessMove move, ChessGame.TeamColor teamColor) {
}
