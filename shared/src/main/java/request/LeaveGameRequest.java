package request;

public record LeaveGameRequest(String authToken, int gameID) {
}
