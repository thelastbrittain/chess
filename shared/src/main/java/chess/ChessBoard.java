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
//        ChessPiece testPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
//        ChessPosition testPosition = new ChessPosition(5,5);
//        this.addPiece(testPosition, testPiece); Tests to make sure it clears the board
        clearBoard();
        addPawns();
        addRooks();
        addKnights();
        addBishops();
        addKings();
        addQueens();
//
    }

    private void clearBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = null;
            }
        }
    }
    private void addPawns(){
        for (int col = 0; col < 8; col++){
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][col] = pawn;
        }
        for (int col = 0; col < 8; col++){
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            squares[1][col] = pawn;
        }
    }
    private void addRooks(){
        //Black rooks
        ChessPiece rook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[0][0] = rook;
        ChessPiece rook2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[0][7] = rook2;

        //White rooks
        ChessPiece rook3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[7][0] = rook3;
        ChessPiece rook4 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[7][7] = rook4;

    }
    private void addKnights(){
        //Black Knight
        ChessPiece Knight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[0][1] = Knight;
        ChessPiece Knight2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[0][6] = Knight2;

        //White Knight
        ChessPiece Knight3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[7][1] = Knight3;
        ChessPiece Knight4 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[7][6] = Knight4;

    }

    private void addBishops(){
        //Black bishops
        ChessPiece bishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[0][2] = bishop;
        ChessPiece bishop2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[0][5] = bishop2;

        //White bishops
        ChessPiece bishop3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[7][2] = bishop3;
        ChessPiece bishop4 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[7][5] = bishop4;

    }

    private void addQueens(){
        //Black Queen
        ChessPiece Queen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[0][3] = Queen;

        //White  Queen
        ChessPiece Queen2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[7][3] = Queen2;
    }

    private void addKings(){
        //Black King
        ChessPiece King = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        squares[0][4] = King;

        //White  King
        ChessPiece King2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        squares[7][4] = King2;
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

