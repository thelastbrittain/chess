package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class that figures out all of the potential moves for a chess piece on the board
 * Parameters: ChessBoard, ChessPiece
 * Methods: MoveCalculator - takes the board and the piece, returns a list of all possible moves
 * should I feed the parameters in when i instantiate the object or do the method call?
 * If I do it at time of instantiation, can i just use the attributes? Will test
 */
public class BishopMoves {
    private ChessBoard board;
    private ChessPosition position;

    public BishopMoves(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> movesAvailable(){
        Collection<ChessMove> moves = new ArrayList<>();
        //four while loops, to make sure it's in bounds
        int tempRow = position.getRow();
        int tempColumn = position.getColumn();

        while ((tempRow - 1) >0 && (tempColumn + 1) < 7){  //Going up and left
            tempRow --;
            tempColumn ++;
            ChessPosition tempPosition = new ChessPosition(tempRow, tempColumn);
            if (board.getPiece(tempPosition) == null) {
                ChessMove newPiece = new ChessMove(position, tempPosition, null);
                moves.add(newPiece);

            }
        }
        //if in bounds, check if a different piece is there
        //if piece is there, check team
        //if enemy team, good



        return moves;
    }



}
