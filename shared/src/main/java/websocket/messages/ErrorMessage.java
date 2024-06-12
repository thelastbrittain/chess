package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private String message;

    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }

    public String getErrorMessage() {
        return message;
    }
}
