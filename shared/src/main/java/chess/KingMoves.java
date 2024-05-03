package chess;

import java.util.Collection;

/**
 * Same class as the Bishop, but just modified to fit the King
 */

public class KingMoves {
    private ChessBoard board;
    private ChessPosition position;
    private ChessGame.TeamColor teamColor;
    private Collection<ChessMove> ogList;

    public KingMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor, Collection<ChessMove> ogList){
        this.board = board;
        this.position = position;
        this.teamColor = teamColor;
        this.ogList = ogList;
    }

    public void movesAvailable(){
        KeepMoving kingKeepMoving = new KeepMoving(board, position, teamColor, ogList);
        kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.UPRIGHT);
        kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.UPLEFT);
        kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.DOWNRIGHT);
        kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.DOWNLEFT);
        kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.RIGHT);
        kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.LEFT);
        kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.UP);
        kingKeepMoving.moveDirectionSingle(KeepMoving.KeepMoveDirection.DOWN);
    }
}
