package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
        Collection<ChessMove> validMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        ChessGame.TeamColor teamColor = board.getPiece(startPosition).getTeamColor();

        Iterator<ChessMove> iterator = validMoves.iterator();
        for (; iterator.hasNext(); ) {
            ChessMove move = iterator.next();
            try {
                ChessBoard cloneBoard = (ChessBoard) this.board.clone();
                doMove(move, cloneBoard);
                if (isInCheckHelper(teamColor, cloneBoard)) {
                    iterator.remove();
                }
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!moveChecks(move)){return;}
        doMove(move, board);
        changeTurn();
    }

    private boolean moveChecks(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null){throw new InvalidMoveException("No piece there. ");}

        ChessGame.TeamColor teamColor= board.getPiece(move.getStartPosition()).getTeamColor();
        if (getTeamTurn() !=  teamColor){throw new InvalidMoveException("It is not your turn. ");}

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.contains(move)) {throw new InvalidMoveException("Move not valid. ");}

        return true;
    }

    //this is a method that does that actual moving of the pieces on the board. It assumes the move is valid.
    //This function is used for makeMove as well as validMoves.
    private void doMove(ChessMove move, ChessBoard board) throws InvalidMoveException {
        ChessPiece.PieceType pieceType;
        ChessGame.TeamColor teamColor = board.getPiece(move.getStartPosition()).getTeamColor();

        if (move.getPromotionPiece() == null){
            pieceType = board.getPiece(move.getStartPosition()).getPieceType();
        } else {pieceType = move.getPromotionPiece();}

        board.removePiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), new ChessPiece(teamColor, pieceType));
    }

    private void changeTurn(){
        if (getTeamTurn() == TeamColor.WHITE)
        {setTeamTurn(TeamColor.BLACK);} else {setTeamTurn(TeamColor.WHITE);}
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     * if already in check, created a new cloned board. Tries every possible move. If none of the moves take
     * the team out of check, then returns true that it is in checkMate.
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            Collection<ChessMove> allFriendlyMoves = findValidFriendlyMoves(teamColor);
            if (allFriendlyMoves.size() > 0) {
                return false;
            } else {return true;}
        } else {return false;}
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(ChessGame.TeamColor teamColor) {
        return isInCheckHelper(teamColor, this.board);}

    private boolean isInCheckHelper(ChessGame.TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = getKingPosition(teamColor, board);
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
        return false;}

    private ChessPosition getKingPosition(TeamColor teamColor,ChessBoard board) {
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

    private Collection<ChessMove> findValidFriendlyMoves(TeamColor teamColor) {
        Collection<ChessMove> allFriendlyMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++){
                ChessPosition friendlyPosition = new ChessPosition(i, j);
                ChessPiece friendlyPiece = board.getPiece(friendlyPosition);
                if (friendlyPiece != null && friendlyPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> tempMoves = validMoves(friendlyPosition);
                    for (ChessMove move : tempMoves) {
                        allFriendlyMoves.add(move);
                    }
                }
            }
        }
        return allFriendlyMoves;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (getTeamTurn() != teamColor) {return false;}
        if (isInCheck(teamColor)) {return false;}

        Collection<ChessMove> allMoves = findValidFriendlyMoves(teamColor);
        if (allMoves.size() > 0) {return false;}
        else {return true;}
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
