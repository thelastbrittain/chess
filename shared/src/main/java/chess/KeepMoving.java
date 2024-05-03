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
        UP, DOWN, LEFT, RIGHT, UPRIGHT, DOWNRIGHT, UPLEFT, DOWNLEFT;
    }

    public KeepMoving(ChessBoard board, ChessPosition position, ChessGame.TeamColor teamColor, Collection<ChessMove> ogList) {
        this.board = board;
        this.position = position;
        this.teamColor = teamColor;
        this.ogList = ogList;



    }

    public void moveDirection(KeepMoveDirection direction) {
        Collection<ChessMove> moves = new ArrayList<>();  //Creating a new array to hold the moves

        //Changing the way the piece will move depending on what was passed into the function
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

        }


        //running the script that will check all of the moves in a direction
        int tempRow = this.position.getRow();
        int tempColumn = this.position.getColumn();
        while (tempRow > 1 && tempRow < 8 && tempColumn > 1 && tempColumn < 8) {
            tempRow += (rowDirection);
            tempColumn += (colDirection);

            ChessPosition tempPosition = new ChessPosition(tempRow, tempColumn);
            if (board.getPiece(tempPosition) == null) {   //if no piece is there, add that spot to the list
                ChessMove newPiece = new ChessMove(position, tempPosition, null);
                moves.add(newPiece);
                ChessPiece tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN); //code used to put a pawn in the available spot for visual aid
                board.addPiece(tempPosition, tempPiece);

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

    public void moveDirectionSingle(KeepMoveDirection direction) {
        Collection<ChessMove> moves = new ArrayList<>();  //Creating a new array to hold the moves

        //Changing the way the piece will move depending on what was passed into the function
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

        }


        //running the script that will check all of the moves in a direction
        int tempRow = this.position.getRow();
        int tempColumn = this.position.getColumn();
        int iterator = 1;

        while (tempRow > 1 && tempRow < 8 && tempColumn > 1 && tempColumn < 8 && iterator == 1) { //making sure the program only runs once.
            iterator ++;
            tempRow += (rowDirection);
            tempColumn += (colDirection);

            ChessPosition tempPosition = new ChessPosition(tempRow, tempColumn);
            if (board.getPiece(tempPosition) == null) {   //if no piece is there, add that spot to the list
                ChessMove newPiece = new ChessMove(position, tempPosition, null);
                moves.add(newPiece);
                ChessPiece tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN); //code used to put a pawn in the available spot for visual aid
                board.addPiece(tempPosition, tempPiece);

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

}

