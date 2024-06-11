package websocket.commands;

public class LeaveCommand extends UserGameCommand{
    int gameID;

    public LeaveCommand(String authToken, int gameID) {
        super(authToken, gameID);
        this.commandType = CommandType.LEAVE;
    }
}
