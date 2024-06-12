package request;

import chess.ChessGame;

public record LeaveGameRequest(String authToken, int gameID, ChessGame.TeamColor teamColor) {
}
