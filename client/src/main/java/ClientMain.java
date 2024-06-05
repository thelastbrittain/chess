import chess.*;
import ui.BoardCreator;
import ui.ClientMenu;

public class ClientMain {
    public static void main(String[] args) {
        int port = 8080;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        new ClientMenu(8080).run();
    }
}

//"http://localhost:8080";


//BoardCreator boardCreator = new BoardCreator();
//ChessBoard testBoard = new ChessBoard();
//testBoard.resetBoard();
//testBoard.addPiece(new ChessPosition(4,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
//boardCreator.createBoard(ChessGame.TeamColor.BLACK, testBoard);
//System.out.println();
//boardCreator.createBoard(ChessGame.TeamColor.WHITE, testBoard);