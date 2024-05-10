package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 *Don't need to worry about at all for phase 0
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        //Two ways: call pieceMoves, then filter through the list and see if they are okay
            //pros: wouldn't need to change my pieceMoves code,
        // before returning list of moves, first check if they are okay

        //FirstWay:
            //call piece moves:

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new InvalidMoveException();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++){  // look at each position on the board
                ChessPosition enemyPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(enemyPosition);  //get's the piece at that position
                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) { //if there is an enemy piece
                    Collection<ChessMove> enemyMoves = currentPiece.pieceMoves(board, enemyPosition); //look at all its moves
                    for (ChessMove enemyMove : enemyMoves){ //if end position of enemyMove is kingPosition, king is in danger.
                        if(enemyMove.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    private Collection<ChessMove> findFriendlyMoves(TeamColor teamColor) {
        Collection<ChessMove> allFriendlyMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++){
                ChessPosition friendlyPosition = new ChessPosition(i, j);
                ChessPiece friendlyPiece = board.getPiece(friendlyPosition);
                if (friendlyPiece != null && friendlyPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> tempMoves = friendlyPiece.pieceMoves(this.board, friendlyPosition);
                    for (ChessMove move : tempMoves) {
                        allFriendlyMoves.add(move);
                    }
                }
            }
        }
        return allFriendlyMoves;
    }


    private ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPiece mockKing = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++){
                ChessPosition kingPosition = new ChessPosition(i, j);
                if (board.getPiece(kingPosition) != null && board.getPiece(kingPosition).equals(mockKing)) {
                    return kingPosition;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }



    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
