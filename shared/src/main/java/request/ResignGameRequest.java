package request;

import chess.ChessGame;

public record ResignGameRequest(String authToken, int gameID) {
}
