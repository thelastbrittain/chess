package chess;

// a class to make it easier to program moves

import java.util.ArrayList;
import java.util.Collection;

/**
 * Purpose: A function that takes in a direction and then makes a while loop to keep going that direction. Returns a list of possible moves.
 * Parameters: direction to move, the chess board
 * Methods:
 */
public class KeepMoving {
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

    public KeepMoving(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor, Collection<ChessMove> ogList) {
        this.board = board;
        this.position = position;
        this.teamColor = teamColor;
        this.ogList = ogList;



    }
    //A function that takes in the move direction and returns iterators for the row and column in a list of integers
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

    //Function that takes in a move direction and adds to a predetermined list the possible moves in that direction
    public void moveDirection(KeepMoveDirection direction, boolean oneMove) {

        Collection<ChessMove> moves = new ArrayList<>();  //Creating a new array to hold the moves

        int [] rowCol = switchFunction(direction);
        int rowDirection = rowCol[0];
        int colDirection = rowCol[1];

        //running the script that will check all of the moves in a direction
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
                moves.add(newPiece);
//                ChessPiece tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN); //code used to put a pawn in the available spot for visual aid
//                board.addPiece(tempPosition, tempPiece);  remember to comment these two lines out before the real deal.

            } else if (board.getPiece(tempPosition) != null) { //if there is a piece there, check if it's friend or foe
                ChessGame.TeamColor enemyColor = board.getPiece(tempPosition).getTeamColor();
                if (teamColor != enemyColor) {  //if foe, add that spot to possible moves, then break
                    moves.add(new ChessMove(position, tempPosition, null));
                    break;
                } else {   //if friend, break
                    break;
                }
            }
        }
        //so that there are not lists inside of list, this adds each move into the original list
        for (ChessMove move : moves) {
            ogList.add(move);
        }
    }

    public void pawnDirection(){
        int row = position.getRow();
        int col = position.getColumn();

        if (this.teamColor == ChessGame.TeamColor.WHITE){
            if (row == 2){                                                      //this is the case if it's the pawn's first move.
                ChessPosition upPosition = new ChessPosition(row+1, col);  //creating object to view up position
                if (board.getPiece(upPosition) == null){                        //of nothing there, create new move and add to list
                    ChessMove upPiece = new ChessMove(position, upPosition, null);
                    ogList.add(upPiece);

                    ChessPosition doubleUpPosition = new ChessPosition(row+2, col);  //now looking at space two up
                    if (board.getPiece(doubleUpPosition) == null){//if nothing there add move
                        ChessMove doubleUpPiece = new ChessMove(position, doubleUpPosition, null);
                        ogList.add(doubleUpPiece);
                    }

                }
            }
            else {
                ChessPosition upPosition = new ChessPosition(row+1, col);  //creating object to view up position
                if (board.getPiece(upPosition) == null){                        //of nothing there, create new move and add to list
                    ChessMove upPiece = new ChessMove(position, upPosition, null);  //look into logic of promotion later
                    ogList.add(upPiece);
                }
                if (inbounds(row+1, col-1)){
                    ChessPosition upLeftPosition = new ChessPosition(row+1, col-1);
                    if (board.getPiece(upLeftPosition) != null && board.getPiece(upLeftPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        ChessMove upLeftPiece = new ChessMove(position, upLeftPosition, null);
                        ogList.add(upLeftPiece);
                    }
                }
                if (inbounds(row+1, col+1)) {
                    ChessPosition upRightPosition = new ChessPosition(row + 1, col + 1);
                    if (board.getPiece(upRightPosition) != null && board.getPiece(upRightPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        ChessMove upRightPiece = new ChessMove(position, upRightPosition, null);
                        ogList.add(upRightPiece);
                    }
                }
            }
        }}

    public boolean inbounds(int row, int col){
        if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
            return true;
        }
        else {
            return false;
        }
    }

}

