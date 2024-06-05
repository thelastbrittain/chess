import chess.*;
import ui.BoardCreator;
import ui.ClientMenu;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

//        new ClientMenu(serverUrl).run();
    }
}



//BoardCreator boardCreator = new BoardCreator();
//ChessBoard testBoard = new ChessBoard();
//testBoard.resetBoard();
//testBoard.addPiece(new ChessPosition(4,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
//boardCreator.createBoard(ChessGame.TeamColor.BLACK, testBoard);
//System.out.println();
//boardCreator.createBoard(ChessGame.TeamColor.WHITE, testBoard);