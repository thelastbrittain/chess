package chess;

import java.util.Collection;

/**
 * Class that figures out all of the potential moves for a chess piece on the board
 * Parameters: ChessBoard, ChessPiece
 * Methods: MoveCalculator - takes the board and the piece, returns a list of all possible moves
 * should I feed the parameters in when i instantiate the object or do the method call?
 * If I do it at time of instantiation, can i just use the attributes? Will test
 */
public class PieceMoves {
    private ChessBoard board;
    private ChessPosition position;
    private ChessGame.TeamColor teamColor;
    private Collection<ChessMove> ogList;
    private ChessPiece.PieceType pieceType;

    public PieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor, Collection<ChessMove> ogList, ChessPiece.PieceType pieceType){
        this.board = board;
        this.position = position;
        this.teamColor = teamColor;
        this.ogList = ogList;
        this.pieceType = pieceType;
    }

    public void movesAvailable() {
        switch (pieceType) {
            case BISHOP:
                KeepMoving bishopKeepMoving = new KeepMoving(board, position, teamColor, ogList);
                bishopKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.UPRIGHT, false);
                bishopKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.UPLEFT, false);
                bishopKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.DOWNRIGHT, false);
                bishopKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.DOWNLEFT, false);
                break;
            case KING:
                KeepMoving kingKeepMoving = new KeepMoving(board, position, teamColor, ogList);
                kingKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.UPRIGHT, true);
                kingKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.UPLEFT, true);
                kingKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.DOWNRIGHT, true);
                kingKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.DOWNLEFT, true);
                kingKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.RIGHT, true);
                kingKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.LEFT, true);
                kingKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.UP, true);
                kingKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.DOWN, true);
                break;
            case KNIGHT:
                KeepMoving knightMove = new KeepMoving(board, position, teamColor, ogList);
                knightMove.moveDirection(KeepMoving.KeepMoveDirection.KNIGHTDOWNLEFT, true);
                knightMove.moveDirection(KeepMoving.KeepMoveDirection.KNIGHTMIDDLELEFTDOWN, true);
                knightMove.moveDirection(KeepMoving.KeepMoveDirection.KNIGHTMIDDLELEFTUP, true);
                knightMove.moveDirection(KeepMoving.KeepMoveDirection.KNIGHTUPLEFT, true);
                knightMove.moveDirection(KeepMoving.KeepMoveDirection.KNIGHTUPRIGHT, true);
                knightMove.moveDirection(KeepMoving.KeepMoveDirection.KNIGHTMIDDLERIGHTUP, true);
                knightMove.moveDirection(KeepMoving.KeepMoveDirection.KNIGHTMIDDLERIGHTDOWN, true);
                knightMove.moveDirection(KeepMoving.KeepMoveDirection.KNIGHTDOWNRIGHT,true);
                break;
        }


    }
}

