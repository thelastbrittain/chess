package websocket.commands;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken, int gameID) {
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private final String authToken;

    public String getAuthString() {
        return authToken;
    }

    private final ChessGame.TeamColor teamColor;

    public ChessGame.TeamColor getTeamColor(){return teamColor;}

    private final int gameID;

    public CommandType getCommandType() {
        return this.commandType;
    }

    public int getGameID() {return gameID;}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
