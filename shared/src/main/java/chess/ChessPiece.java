package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * attributes: teamColor, pieceType, ennum of PieceTypes
 * Methods: getColor, getTeam
 * PieceMoves (returns collection of possible moves given a board and a position), has one logic line for pawns,
 * and one for every other piece type. Basic overrides
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
        Collection<ChessMove> moves = new ArrayList<>();
        if (type != PieceType.PAWN) {
            RegMoveCalculator moveCalculator = new RegMoveCalculator(board, myPosition, teamColor, moves, type);
            moveCalculator.regPieceMoveLogic();}
        else {
            PawnMoveCalculator pawnMoveCalculator = new PawnMoveCalculator(board, myPosition, teamColor, moves);
            pawnMoveCalculator.calculateMoves();
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