package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        switch (pieceType){
            case BISHOP:
                PieceMoves bMoves = new PieceMoves(board, myPosition, teamColor, moves, pieceType);
                bMoves.movesAvailable();
                break;
            case KING:
                PieceMoves kMoves = new PieceMoves(board, myPosition, teamColor, moves, pieceType);
                kMoves.movesAvailable();
                break;
            case KNIGHT:
                break;
        }

        return moves;


    }

    @Override
    public String toString() {
        return type + " " + teamColor;
    }
}

//Don't create a class for each type of piece
//Don't write pieceMOves as a bunch of each moves
//Create a class that is a move calculator for each type of piece -- in piecemoves just figure out what kind of piece
//It is and call the wright class method depending on that. Figure out what a switch statement is.