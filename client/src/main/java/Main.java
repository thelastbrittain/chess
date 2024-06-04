import chess.*;
import ui.BoardCreator;

public class Main {
    public static void main(String[] args) {
        BoardCreator boardCreator = new BoardCreator();
        boardCreator.createBoard(ChessGame.TeamColor.WHITE);
    }
}