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

    /**
     *
     * @param direction takes in which direction the person wants to go
     * @return returns a list of the row and col direction.
     * This is to be used for the moveDirection method so that row and col direction can be iterators.
     */
    private int[] switchFunction (KeepMoveDirection direction) {
        switch (direction){
            case UP:
                rowDirection = up;
                colDirection = 0;
                break;
            case DOWN:
                rowDirection = down;
                colDirection = 0;
                break;
            case LEFT:
                rowDirection = 0;
                colDirection = left;
                break;
            case RIGHT:
                rowDirection = 0;
                colDirection = right;
                break;

            case UPRIGHT:
                rowDirection = up;
                colDirection = right;
                break;
            case UPLEFT:
                rowDirection = up;
                colDirection = left;
                break;
            case DOWNRIGHT:
                rowDirection = down;
                colDirection = right;
                break;
            case DOWNLEFT:
                rowDirection = down;
                colDirection = left;
                break;

            case KNIGHTUPRIGHT:
                rowDirection = 2;
                colDirection = right;
                break;
            case KNIGHTMIDDLERIGHTUP:
                rowDirection = up;
                colDirection = 2;
                break;
            case KNIGHTMIDDLERIGHTDOWN:
                rowDirection = down;
                colDirection = 2;
                break;
            case KNIGHTDOWNRIGHT:
                rowDirection = -2;
                colDirection = right;
                break;
            case KNIGHTUPLEFT:
                rowDirection = 2;
                colDirection = left;
                break;
            case KNIGHTMIDDLELEFTUP:
                rowDirection = up;
                colDirection = -2;
                break;
            case KNIGHTMIDDLELEFTDOWN:
                rowDirection = down;
                colDirection = -2;
                break;
            case KNIGHTDOWNLEFT:
                rowDirection = -2;
                colDirection = left;
                break;

        }
        return new int[]{rowDirection, colDirection};
    }

    /**
     *
     * @param direction a variable to check which direction to move in.
     * @param oneMove a parameter to see if the loop should only activate once (for king/knight), or if it should go until it hits a wall/other piece.
     */
    public void moveDirection(KeepMoveDirection direction, boolean oneMove) {

        int [] rowCol = switchFunction(direction);
        int rowDirection = rowCol[0];
        int colDirection = rowCol[1];

        //running the script thatogList in a direction
        int tempRow = this.position.getRow();
        int tempColumn = this.position.getColumn();

        int iterator = 1;
        int number;
        if (oneMove == true){number = 1;}
        else {number = 0;}

        while (inbounds(tempRow, tempColumn) && iterator == 1) {
            tempRow += (rowDirection);
            tempColumn += (colDirection);
            iterator += number;

            if (! inbounds(tempRow, tempColumn)) { //just to double check
                break;
            }

            ChessPosition tempPosition = new ChessPosition(tempRow, tempColumn);
            if (board.getPiece(tempPosition) == null) {   //if no piece is there, add that spot to the list
                ChessMove newPiece = new ChessMove(position, tempPosition, null);
                ogList.add(newPiece);
//                ChessPiece tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN); //code used to put a pawn in the available spot for visual aid
//                board.addPiece(tempPosition, tempPiece);  remember to comment these two lines out before the real deal.

            } else if (board.getPiece(tempPosition) != null) { //if there is a piece there, check if it's friend or foe
                ChessGame.TeamColor enemyColor = board.getPiece(tempPosition).getTeamColor();
                if (teamColor != enemyColor) {  //if foe, addogList, then break
                    ogList.add(new ChessMove(position, tempPosition, null));
                    break;
                } else {   //if friend, break
                    break;
                }
            }
        }
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

