package chess;

// a class to makogList

import java.util.ArrayList;
import java.util.Collection;

/**
 * Purpose: A class that is meant to add possible moves to a list of possible moves
 * Attributes: board, position, teamColor, ogList, pieceType
 * Methods: regPieceMoveLogic - handles all of the logic for what happens with the pieces using a switch statement
 * based on pieceType
 * moveDirection Multiple - takes a row and col iterator. Tries every possible move going in that direction. Adds to the
 * list potential moves going that direction.
 * moveDirectionSingle - same things as above but only tries to move once instead of moving in a loop.
 * inBounds - takes a row and a col and returns a bool based on if they are in bounds or not
 */
public class RegMoveCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private ChessGame.TeamColor teamColor;
    private Collection<ChessMove> ogList;
    private ChessPiece.PieceType pieceType;

    public RegMoveCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor, Collection<ChessMove> ogList,ChessPiece.PieceType pieceType) {
        this.board = board;
        this.position = position;
        this.teamColor = teamColor;
        this.ogList = ogList;
        this.pieceType = pieceType;
    }

    public void regPieceMoveLogic () {
        switch (pieceType) {
            case QUEEN:
                moveDirectionMultiple(1,0);
                moveDirectionMultiple(1,-1);
                moveDirectionMultiple(1,1);
                moveDirectionMultiple(0,1);
                moveDirectionMultiple(0,-1);
                moveDirectionMultiple(-1,0);
                moveDirectionMultiple(-1,-1);
                moveDirectionMultiple(-1,1);
                break;
            case KING:
                moveDirectionSingle(1,0);
                moveDirectionSingle(1,-1);
                moveDirectionSingle(1,1);
                moveDirectionSingle(0,1);
                moveDirectionSingle(0,-1);
                moveDirectionSingle(-1,0);
                moveDirectionSingle(-1,-1);
                moveDirectionSingle(-1,1);
                break;
            case KNIGHT:
                moveDirectionSingle(2,1);
                moveDirectionSingle(2,-1);
                moveDirectionSingle(1,2);
                moveDirectionSingle(1,-2);
                moveDirectionSingle(-1,2);
                moveDirectionSingle(-1,-2);
                moveDirectionSingle(-2,1);
                moveDirectionSingle(-2,-1);
                break;
            case BISHOP:
                moveDirectionMultiple(1,1);
                moveDirectionMultiple(1,-1);
                moveDirectionMultiple(-1,-1);
                moveDirectionMultiple(-1,1);
                break;
            case ROOK:
                moveDirectionMultiple(1,0);
                moveDirectionMultiple(-1,0);
                moveDirectionMultiple(0,1);
                moveDirectionMultiple(0,-1);
                break;
        }
    }

    private void moveDirectionMultiple(int rowDirection, int colDirection) {
        int tempRow = this.position.getRow();
        int tempColumn = this.position.getColumn();

        while (inbounds(tempRow, tempColumn)) {
            tempRow += (rowDirection);
            tempColumn += (colDirection);

            if (! inbounds(tempRow, tempColumn)) { //just to double check
                break;
            }

            ChessPosition tempPosition = new ChessPosition(tempRow, tempColumn);
            if (board.getPiece(tempPosition) == null) {   //if no piece is there, add that spot to the list
                ogList.add(new ChessMove(position, tempPosition, null));
            }
            else if (board.getPiece(tempPosition).getTeamColor() != teamColor)  //if there is a piece there, check if it's friend or foe
                {
                    ogList.add(new ChessMove(position, tempPosition, null));
                    break;
                }
            else { break;}
            }
        }

    private void moveDirectionSingle(int rowDirection, int colDirection) {
        int tempRow = this.position.getRow();
        int tempColumn = this.position.getColumn();

        tempRow += (rowDirection);
        tempColumn += (colDirection);

        if (! inbounds(tempRow, tempColumn)) { //just to double check
            return;
        }

        ChessPosition tempPosition = new ChessPosition(tempRow, tempColumn);
        if (board.getPiece(tempPosition) == null) {   //if no piece is there, add that spot to the list
            ogList.add(new ChessMove(position, tempPosition, null));
        }
        else if (board.getPiece(tempPosition).getTeamColor() != teamColor)  //if there is a piece there, check if it's friend or foe
        {
            ogList.add(new ChessMove(position, tempPosition, null));
        }
        else {return;}
        }

    public boolean inbounds(int row, int col){
        if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
            return true;
        }
        else {
            return false;
        }
    }

}

