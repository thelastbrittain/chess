package typeadapter;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class ServerMessageTypeAdapter extends TypeAdapter<ServerMessage> {

    @Override
    public void write(JsonWriter jsonWriter, ServerMessage message) throws IOException {
        Gson gson = new Gson();

        switch(message.getServerMessageType()) {
            case ERROR -> gson.getAdapter(ErrorMessage.class).write(jsonWriter, (ErrorMessage) message);
            case NOTIFICATION -> gson.getAdapter(NotificationMessage.class).write(jsonWriter, (NotificationMessage) message);
            case LOAD_GAME -> gson.getAdapter(LoadGameMessage.class).write(jsonWriter, (LoadGameMessage) message);
        }
    }

    @Override
    public ServerMessage read(JsonReader jsonReader) throws IOException {
        String message = null;
        ServerMessage.ServerMessageType serverMessageType = null;
        ChessGame game = null;

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "message" -> message = jsonReader.nextString();
                case "serverMessageType" -> serverMessageType = ServerMessage.ServerMessageType.valueOf(jsonReader.nextString());
                case "game" -> game = readChessGame(jsonReader);
            }
        }

        jsonReader.endObject();

        if (serverMessageType == null) {
            return null;
        }

        return switch (serverMessageType) {
            case ERROR -> new ErrorMessage(message);
            case NOTIFICATION -> new NotificationMessage(message);
            case LOAD_GAME -> new LoadGameMessage(game);
        };
    }

    private ChessGame readChessGame(JsonReader jsonReader) throws IOException {
        ChessGame game = new ChessGame();
        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "teamTurn" -> game.setTeamTurn(ChessGame.TeamColor.valueOf(jsonReader.nextString()));
                case "board" -> game.setBoard(readChessBoard(jsonReader));
                case "gameOver" -> game.setGameOver(jsonReader.nextBoolean());
            }
        }

        jsonReader.endObject();
        return game;
    }

    private ChessBoard readChessBoard(JsonReader jsonReader) throws IOException {
        ChessBoard board = new ChessBoard();
        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("squares")) {
                board = readSquares(jsonReader);
            }
        }

        jsonReader.endObject();
        return board;
    }

    private ChessBoard readSquares(JsonReader jsonReader) throws IOException {
        ChessBoard board = new ChessBoard();
        jsonReader.beginArray();

        for (int i = 0; i < 8; i++) {
            jsonReader.beginArray();
            for (int j = 0; j < 8; j++) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    board.addPiece(new ChessPosition(8 - i, j + 1), readChessPiece(jsonReader));
                } else {
                    jsonReader.nextNull();
                }
            }
            jsonReader.endArray();
        }

        jsonReader.endArray();
        return board;
    }

    private ChessPiece readChessPiece(JsonReader jsonReader) throws IOException {
        ChessPiece.PieceType pieceType = null;
        ChessGame.TeamColor teamColor = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "type" -> pieceType = ChessPiece.PieceType.valueOf(jsonReader.nextString());
                case "teamColor" -> teamColor = ChessGame.TeamColor.valueOf(jsonReader.nextString());
            }
        }
        jsonReader.endObject();

        return new ChessPiece(teamColor, pieceType);
    }

//    private ChessMove readChessMove(JsonReader jsonReader) throws IOException {
//        ChessPosition startPosition = null;
//        ChessPosition endPosition = null;
//        ChessPiece.PieceType promotionPiece = null;
//
//        jsonReader.beginObject();
//
//        while (jsonReader.hasNext()) {
//            String name = jsonReader.nextName();
//            switch (name) {
//                case "startPosition" -> startPosition = readChessPosition(jsonReader);
//                case "endPosition" -> endPosition = readChessPosition(jsonReader);
//                case "promotionPiece" -> promotionPiece = ChessPiece.PieceType.valueOf(jsonReader.nextString());
//            }
//        }
//
//        jsonReader.endObject();
//
//        return new ChessMove(startPosition, endPosition, promotionPiece);
//    }

//    private ChessPosition readChessPosition(JsonReader jsonReader) throws IOException {
//        int x = 0;
//        int y = 0;
//
//        jsonReader.beginObject();
//
//        while (jsonReader.hasNext()) {
//            String name = jsonReader.nextName();
//            switch (name) {
//                case "x" -> x = jsonReader.nextInt();
//                case "y" -> y = jsonReader.nextInt();
//            }
//        }
//
//        jsonReader.endObject();
//
//        return new ChessPosition(x, y);
//    }

}


