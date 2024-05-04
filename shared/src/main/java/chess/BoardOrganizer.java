package chess;

//This class is to help with the ChessBoard resetBoard function. It cleans off the board and then puts all new pieces on.
public class BoardOrganizer {
    private ChessBoard board;

    public BoardOrganizer(ChessBoard board){
        this.board = board;
    }

    void clearBoard(){
        for (int i = 1; i<9; i++){
            for (int j = 1; j < 9; j++){
//                this.board[i][j] = null;
            }
        }
    }
}
