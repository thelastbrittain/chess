package chess;

import java.util.Arrays;

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
//        ChessPiece testPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
//        ChessPosition testPosition = new ChessPosition(5,5);
//        this.addPiece(testPosition, testPiece); Tests to make sure it clears the board
        clearBoard();
////        addPawns();
////        addRooks();
////        addKnights();
////        addBishops();
////        addKings();
////        addQueens();
//
    }

    public void clearBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = null;
            }
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
}

