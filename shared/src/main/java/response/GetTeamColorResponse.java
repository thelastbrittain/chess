package response;

import chess.ChessGame;

public record GetTeamColorResponse(String message, ChessGame.TeamColor teamColor) {
}
