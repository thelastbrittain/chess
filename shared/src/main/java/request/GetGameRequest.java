package request;

public record GetGameRequest(String authToken, int gameID) {
}
