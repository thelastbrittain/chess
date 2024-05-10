package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * attributes: the actualy chessboard (8*8 array of arrays of type ChessPiece called squares), a default constructor
 * Methods: addPiece, getPiece, resetBoard, clearBoard, addPieces, overrides. (Arrays.toString)
 */
public class ChessBoard implements Cloneable {
    private ChessPiece [][] squares = new ChessPiece[8][8];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     * Takes 8-row and col-1 because the dimensions of the playing board and the array board are different
     * Puts a piece at that location in the array of arrays
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[8 - position.getRow()][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * returns the piece at the position. Translates the input position to the array format
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[8 - position.getRow()][position.getColumn() - 1];
    }

    /**
     *
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChessBoard clone = (ChessBoard) super.clone();
        ChessPiece[][] clonedSquares = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                clonedSquares[i][j] = (ChessPiece) squares[i][j].clone();
            }
        }
        clone.squares = clonedSquares;
        return clone;
    }
}


