package response;

public record MakeMoveResponse(String message, boolean isInCheck, boolean isInCheckmate, boolean isInStalemate) {
}
