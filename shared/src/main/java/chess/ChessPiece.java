package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor teamColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN;


    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
         return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //Check what type of piece it is
        //Switch statement calling various functions calling other functions based on what the piece is.
        ChessPiece.PieceType pieceType = this.getPieceType(); //edited this and added this. see if it works.
        ChessGame.TeamColor teamColor = this.getTeamColor();  //added this as well.
        Collection<ChessMove> moves = new ArrayList<>();
        switch (pieceType) {
            case BISHOP:
                RegMoveCalculator bishopKeepMoving = new RegMoveCalculator(board, myPosition, teamColor, moves);
                bishopKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.UPRIGHT, false);
                bishopKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.UPLEFT, false);
                bishopKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.DOWNRIGHT, false);
                bishopKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.DOWNLEFT, false);
                break;
            case KING:
                RegMoveCalculator kingKeepMoving = new RegMoveCalculator(board, myPosition, teamColor, moves);
                kingKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.UPRIGHT, true);
                kingKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.UPLEFT, true);
                kingKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.DOWNRIGHT, true);
                kingKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.DOWNLEFT, true);
                kingKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.LEFT, true);
                kingKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.UP, true);
                kingKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.RIGHT, true);
                kingKeepMoving.moveDirection(RegMoveCalculator.KeepMoveDirection.DOWN, true);
                break;
            case KNIGHT:
                RegMoveCalculator knightMove = new RegMoveCalculator(board, myPosition, teamColor, moves);
                knightMove.moveDirection(RegMoveCalculator.KeepMoveDirection.KNIGHTDOWNLEFT, true);
                knightMove.moveDirection(RegMoveCalculator.KeepMoveDirection.KNIGHTMIDDLELEFTDOWN, true);
                knightMove.moveDirection(RegMoveCalculator.KeepMoveDirection.KNIGHTMIDDLELEFTUP, true);
                knightMove.moveDirection(RegMoveCalculator.KeepMoveDirection.KNIGHTUPLEFT, true);
                knightMove.moveDirection(RegMoveCalculator.KeepMoveDirection.KNIGHTUPRIGHT, true);
                knightMove.moveDirection(RegMoveCalculator.KeepMoveDirection.KNIGHTMIDDLERIGHTUP, true);
                knightMove.moveDirection(RegMoveCalculator.KeepMoveDirection.KNIGHTMIDDLERIGHTDOWN, true);
                knightMove.moveDirection(RegMoveCalculator.KeepMoveDirection.KNIGHTDOWNRIGHT, true);
                break;
            case PAWN:
                PawnMoveCalculator pawnMove = new PawnMoveCalculator(board, myPosition, teamColor, moves);
                pawnMove.calculateMoves();
                break;
            case QUEEN:
                RegMoveCalculator queenMove = new RegMoveCalculator(board, myPosition, teamColor, moves);
                queenMove.moveDirection(RegMoveCalculator.KeepMoveDirection.UPRIGHT, false);
                queenMove.moveDirection(RegMoveCalculator.KeepMoveDirection.UPLEFT, false);
                queenMove.moveDirection(RegMoveCalculator.KeepMoveDirection.DOWNRIGHT, false);
                queenMove.moveDirection(RegMoveCalculator.KeepMoveDirection.DOWNLEFT, false);
                queenMove.moveDirection(RegMoveCalculator.KeepMoveDirection.RIGHT, false);
                queenMove.moveDirection(RegMoveCalculator.KeepMoveDirection.LEFT, false);
                queenMove.moveDirection(RegMoveCalculator.KeepMoveDirection.UP, false);
                queenMove.moveDirection(RegMoveCalculator.KeepMoveDirection.DOWN, false);
                break;
            case ROOK:
                RegMoveCalculator rookMove = new RegMoveCalculator(board, myPosition, teamColor, moves);
                rookMove.moveDirection(RegMoveCalculator.KeepMoveDirection.RIGHT, false);
                rookMove.moveDirection(RegMoveCalculator.KeepMoveDirection.LEFT, false);
                rookMove.moveDirection(RegMoveCalculator.KeepMoveDirection.UP, false);
                rookMove.moveDirection(RegMoveCalculator.KeepMoveDirection.DOWN, false);
                break;

        }
        return moves;
    }

    @Override
    public String toString() {
        return type + " " + teamColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, type);
    }
}

//Don't create a class for each type of piece
//Don't write pieceMOves as a bunch of each moves
//Create a class that is a move calculator for each type of piece -- in piecemoves just figure out what kind of piece
//It is and call the wright class method depending on that. Figure out what a switch statement is.