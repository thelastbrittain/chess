package typeadapter;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import websocket.commands.*;

import java.io.IOException;

public class CommandTypeAdapter extends TypeAdapter<UserGameCommand> {
    @Override
    public void write(JsonWriter jsonWriter, UserGameCommand command) throws IOException {
        Gson gson = new Gson();

        switch(command.getCommandType()) {
            case LEAVE -> gson.getAdapter(LeaveCommand.class).write(jsonWriter, (LeaveCommand) command);
            case CONNECT -> gson.getAdapter(ConnectCommand.class).write(jsonWriter, (ConnectCommand) command);
            case RESIGN -> gson.getAdapter(ResignCommand.class).write(jsonWriter, (ResignCommand) command);
        }
    }

    @Override
    public UserGameCommand read(JsonReader jsonReader) throws IOException {
        String authToken = null;
        int gameID = 0;
        UserGameCommand.CommandType commandType = null;
        ChessMove move = null;

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "authToken" -> authToken = jsonReader.nextString();
                case "gameID" -> gameID = jsonReader.nextInt();
                case "commandType" -> commandType = UserGameCommand.CommandType.valueOf(jsonReader.nextString());
                case "move" -> move = readChessMove(jsonReader);
            }
        }

        jsonReader.endObject();

        if(commandType == null) {
            return null;
        } else {
            return switch (commandType) {
                case CONNECT -> new ConnectCommand(authToken, gameID);
                case MAKE_MOVE -> new MakeMoveCommand(authToken, gameID, move);
                case LEAVE -> new LeaveCommand(authToken, gameID);
                case RESIGN -> new ResignCommand(authToken, gameID);
            };
        }
    }

    private ChessMove readChessMove(JsonReader jsonReader) throws IOException {
        ChessPosition startPosition = null;
        ChessPosition endPosition = null;
        ChessPiece.PieceType promotionPiece = null;

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "startPosition" -> startPosition = readChessPosition(jsonReader);
                case "endPosition" -> endPosition = readChessPosition(jsonReader);
                case "promotionPiece" -> promotionPiece = ChessPiece.PieceType.valueOf(jsonReader.nextString());
            }
        }

        jsonReader.endObject();

        return new ChessMove(startPosition, endPosition, promotionPiece);
    }

    private ChessPosition readChessPosition(JsonReader jsonReader) throws IOException {
        int x = 0;
        int y = 0;

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "x" -> x = jsonReader.nextInt();
                case "y" -> y = jsonReader.nextInt();
            }
        }

        jsonReader.endObject();

        return new ChessPosition(x, y);
    }

}
