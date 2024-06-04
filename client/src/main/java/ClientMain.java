import chess.*;
import ui.BoardCreator;

public class ClientMain {
    public static void main(String[] args) {
        BoardCreator boardCreator = new BoardCreator();
        ChessBoard testBoard = new ChessBoard();
        testBoard.resetBoard();
        boardCreator.createBoard(ChessGame.TeamColor.WHITE, testBoard);
    }
}