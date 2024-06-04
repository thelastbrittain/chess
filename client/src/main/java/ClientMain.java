import chess.*;
import ui.BoardCreator;

public class ClientMain {
    public static void main(String[] args) {
        BoardCreator boardCreator = new BoardCreator();
        ChessBoard testBoard = new ChessBoard();
        testBoard.resetBoard();
        testBoard.addPiece(new ChessPosition(4,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        boardCreator.createBoard(ChessGame.TeamColor.BLACK, testBoard);
        System.out.println();
        boardCreator.createBoard(ChessGame.TeamColor.WHITE, testBoard);
    }
}