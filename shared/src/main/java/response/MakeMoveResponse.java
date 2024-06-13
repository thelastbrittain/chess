package response;

import chess.ChessGame;

public record MakeMoveResponse(String message, boolean isInCheck, boolean isInCheckmate, boolean isInStalemate, ChessGame game) {
}
