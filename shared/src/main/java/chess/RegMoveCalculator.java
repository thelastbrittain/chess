package chess;

// a class to makogList

import java.util.ArrayList;
import java.util.Collection;

/**
 * Purpose: A function that takes in a direction and then makes a while loop to keep going that direction. RetuogList.
 * Parameters: board, position, teamColor, listToAddTo (ogList)
 * Methods: Switch function (sets the move positions based on move direction
 */
public class RegMoveCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private ChessGame.TeamColor teamColor;
    private Collection<ChessMove> ogList;

    private int up = 1;
    private int down = -1;
    private int left = -1;
    private int right = 1;

    private int rowDirection;
    private int colDirection;

    public enum KeepMoveDirection {
        UP, DOWN, LEFT, RIGHT, UPRIGHT, DOWNRIGHT, UPLEFT, DOWNLEFT,
        KNIGHTUPRIGHT, KNIGHTMIDDLERIGHTUP, KNIGHTMIDDLERIGHTDOWN, KNIGHTDOWNRIGHT,
        KNIGHTUPLEFT, KNIGHTMIDDLELEFTUP, KNIGHTMIDDLELEFTDOWN, KNIGHTDOWNLEFT;
    }

    public RegMoveCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor, Collection<ChessMove> ogList) {
        this.board = board;
        this.position = position;
        this.teamColor = teamColor;
        this.ogList = ogList;
    }

    public void regPieceMoveLogic (ChessPiece.PieceType pieceType) {
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

