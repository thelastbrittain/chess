package websocket.commands;

public class LeaveCommand extends UserGameCommand{

    public LeaveCommand(String authToken, int gameID) {
        super(authToken, gameID);
        this.commandType = CommandType.LEAVE;
    }
}
