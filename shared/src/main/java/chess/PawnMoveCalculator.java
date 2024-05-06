package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PawnMoveCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private ChessGame.TeamColor teamColor;
    private Collection<ChessMove> ogList;
    private int row;
    private int col;
    private List<ChessPiece.PieceType> promotionPieces = Arrays.asList(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP);

    public PawnMoveCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor, Collection<ChessMove> ogList) {
        this.board = board;
        this.position = position;
        this.teamColor = teamColor;
        this.ogList = ogList;
        this.row = position.getRow();
        this.col = position.getColumn();
    }

    public void calculateMoves() {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            goDirection(1,0);
            goDirection(1,1);
            goDirection(1,-1);
        } else {
            goDirection(-1, 0);
            goDirection(-1, 1);
            goDirection(-1,-1);
        }
    }

    //this function takes in a location. If an enemy or nothing is there, it adds 4 moves to the list, Knight, Rook, Bishop, Queen
    private void promotePiece(ChessPosition newPosition) {
        for (ChessPiece.PieceType piece : promotionPieces) {
            ChessMove move = new ChessMove(position, newPosition, piece);  //if this break bc making new everytime, move this two lines up
            ogList.add(move);
        }
    }

    private void goDirection(int rowChange, int colChange) {
        //create new row and col
        int newRow = row + rowChange;
        int newCol = col + colChange;
        //make sure new rol/col are in bounds
        if (newRow > 8 || newCol > 8 || newRow < 0 || newCol < 0) {
            return;
        }

        //creating variables for promotion/doubleMove
        boolean doubleMove = false;
        boolean promotion = false;
        //setting the promotion and double move bools
        if (teamColor == ChessGame.TeamColor.WHITE) {
            if (row == 2 && colChange == 0) {
                doubleMove = true;
            }
            if (row == 7) {
                promotion = true;
            }
        }
        if (teamColor == ChessGame.TeamColor.BLACK) {
            if (row == 7 && colChange == 0) {
                doubleMove = true;
            }
            if (row == 2) {
                promotion = true;
            }
        }

        //make new position
        //get the piece at the new position
        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        ChessPiece newPiece = board.getPiece(newPosition);

        if (colChange == 0) {
            if (newPiece == null) {
                if (promotion) {
                    promotePiece(newPosition);
                } else {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    ogList.add(newMove);
                    if (doubleMove) {
                        ChessPosition doublePosition = new ChessPosition(newRow + rowChange, newCol);
                        ChessPiece testPiece = board.getPiece(doublePosition);
                        if (testPiece == null) {
                            ChessMove newDoubleMove = new ChessMove(position, doublePosition, null);
                            ogList.add(newDoubleMove);
                        }
                    }
                }
            }
        } else {
            if (newPiece != null && newPiece.getTeamColor() != teamColor) {
                if (promotion) {
                    promotePiece(newPosition);}
                else {
                    ChessMove newMove = new ChessMove(position, newPosition, null);
                    ogList.add(newMove);
                }
            }
        }
    }
}