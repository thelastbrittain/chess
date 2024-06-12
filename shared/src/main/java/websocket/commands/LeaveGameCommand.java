package websocket.commands;

public class LeaveGameCommand extends UserGameCommand{

    public LeaveGameCommand(String authToken, int gameID) {
        super(authToken, gameID);
        this.commandType = CommandType.LEAVE;
    }
}
