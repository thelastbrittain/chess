package response;

import chess.ChessGame;

public record GetGameResponse(ChessGame game, String message) {
}
