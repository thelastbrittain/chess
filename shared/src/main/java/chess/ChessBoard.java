package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece [][] squares = new  ChessPiece[8][8];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[8 - position.getRow()][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[8 - position.getRow()][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        clearBoard();
        addPawns();
        addPiece(ChessPiece.PieceType.ROOK, 0,7);
        addPiece(ChessPiece.PieceType.KNIGHT, 1,6);
        addPiece(ChessPiece.PieceType.BISHOP, 2,5);
        addRoyal(ChessPiece.PieceType.QUEEN, 3);
        addRoyal(ChessPiece.PieceType.KING, 4);
    }

    private void clearBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = null;
            }
        }
    }

    private void addPiece(ChessPiece.PieceType type, int col, int col2){
        squares[7][col] = new ChessPiece(ChessGame.TeamColor.WHITE, type);
        squares[7][col2] = new ChessPiece(ChessGame.TeamColor.WHITE, type);
        squares[0][col] = new ChessPiece(ChessGame.TeamColor.BLACK, type);
        squares[0][col2] = new ChessPiece(ChessGame.TeamColor.BLACK, type);
    }
    private void addRoyal(ChessPiece.PieceType type, int col){
        squares[7][col] = new ChessPiece(ChessGame.TeamColor.WHITE, type);
        squares[0][col] = new ChessPiece(ChessGame.TeamColor.BLACK, type);
    }

    private void addPawns(){
        for (int col = 0; col < 8; col++) {
            squares[6][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[1][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    @Override
    public String toString() {
        return "ChessBoard{"
                + "\n" + Arrays.toString(squares[0])
                + "\n" + Arrays.toString(squares[1])
                + "\n" + Arrays.toString(squares[2])
                + "\n" + Arrays.toString(squares[3])
                + "\n" + Arrays.toString(squares[4])
                + "\n" + Arrays.toString(squares[5])
                + "\n" + Arrays.toString(squares[6])
                + "\n" + Arrays.toString(squares[7]) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}

