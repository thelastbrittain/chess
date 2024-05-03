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
                bishopKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.UPRIGHT);
                bishopKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.UPLEFT);
                bishopKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.DOWNRIGHT);
                bishopKeepMoving.moveDirection(KeepMoving.KeepMoveDirection.DOWNLEFT);
                break;
            case KING:
                KeepMoving kingKeepMoving = new KeepMoving(board, position, teamColor, ogList);
                kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.UPRIGHT);
                kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.UPLEFT);
                kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.DOWNRIGHT);
                kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.DOWNLEFT);
                kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.RIGHT);
                kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.LEFT);
                kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.UP);
                kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.DOWN);
                break;
        }


    }
}

