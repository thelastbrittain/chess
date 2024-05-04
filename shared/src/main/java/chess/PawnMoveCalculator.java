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

    public void calculateMoves(){
        if (teamColor == ChessGame.TeamColor.WHITE){
            goUp();
            goUpLeft();
            goUpRight();
        }
        else {
            goDown();
            goDownLeft();
            goDownRight();
        }
    }

    //this function takes in a location. If an enemy or nothing is there, it adds 4 moves to the list, Knight, Rook, Bishop, Queen
    public void promotePiece(ChessPosition newPosition){
        for (ChessPiece.PieceType piece : promotionPieces){
            ChessMove move = new ChessMove(position, newPosition, piece);  //if this break bc making new everytime, move this two lines up
            ogList.add(move);
        }

    }

    public void goUp() {
        if (this.row == 8){return;}
        ChessPosition upPosition = new ChessPosition(row + 1, col);  //creating object to view up position

        //double up case
        if (this.row == 2) {
            if (board.getPiece(upPosition) == null) {          //if nothing there, create new move and add to list
                ChessMove upPiece = new ChessMove(position, upPosition, null);
                ogList.add(upPiece);

                ChessPosition doubleUpPosition = new ChessPosition(row + 2, col);  //now looking at space two up
                if (board.getPiece(doubleUpPosition) == null) {//if nothing there add move
                    ChessMove doubleUpPiece = new ChessMove(position, doubleUpPosition, null);
                    ogList.add(doubleUpPiece);
                }
            }
        }
        //promotion case
        else if (this.row == 7) {
            if (board.getPiece(upPosition) == null) {
                promotePiece(upPosition);
            }
        }

        else {
            if (this.row != 8){
                if (board.getPiece(upPosition) == null) {                        //of nothing there, create new move and add to list
                    ChessMove upPiece = new ChessMove(position, upPosition, null);
                    ogList.add(upPiece);
                }
            }
        }
    }


    public void goUpRight() {
        if (this.col == 8) {
            return;}
        ChessPosition upRightPosition = new ChessPosition(row + 1, col + 1);  //creating object to view up position

        if (board.getPiece(upRightPosition) != null && board.getPiece(upRightPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (this.row == 7) {
                promotePiece(upRightPosition);
            } else {
                ChessMove upRightPiece = new ChessMove(position, upRightPosition, null);
                ogList.add(upRightPiece);
            }

        }
    }

    public void goUpLeft() {
        if (this.col == 1) {
            return;
        }
        ChessPosition upLeftPosition = new ChessPosition(row + 1, col - 1);  //creating object to view up position

        if (board.getPiece(upLeftPosition) != null && board.getPiece(upLeftPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {                        //of nothing there, create new move and add to list
            if (this.row == 7) {
                promotePiece(upLeftPosition);
            }else {
                ChessMove upLeftPiece = new ChessMove(position, upLeftPosition, null);
                ogList.add(upLeftPiece);
            }

        }
    }

    public void goDown() {
        //break if on the edge of the board
        if (this.row == 1){return;}
        ChessPosition downPosition = new ChessPosition(row - 1, col);  //creating object to view up position

        //double up case
        if (this.row == 7) {
            if (board.getPiece(downPosition) == null) {          //if nothing there, create new move and add to list
                ChessMove upPiece = new ChessMove(position, downPosition, null);
                ogList.add(upPiece);

                ChessPosition doubleDownPosition = new ChessPosition(row - 2, col);  //now looking at space two up
                if (board.getPiece(doubleDownPosition) == null) {//if nothing there add move
                    ChessMove doubleDownPiece = new ChessMove(position, doubleDownPosition, null);
                    ogList.add(doubleDownPiece);
                }
            }
        }
        //promotion case
        else if (this.row == 2) {
            if (board.getPiece(downPosition) == null) {
                promotePiece(downPosition);
            }
        }
        //every other case
        else {
            if (board.getPiece(downPosition) == null) {                        //of nothing there, create new move and add to list
                ChessMove downPiece = new ChessMove(position, downPosition, null);
                ogList.add(downPiece);
            }
        }
    }

    public void goDownRight() {
        //break if on the edge
        if (this.col == 1) {
            return;}
        ChessPosition downRightPosition = new ChessPosition(row - 1, col + 1);  //creating object to view up position

        if (board.getPiece(downRightPosition) != null && board.getPiece(downRightPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (this.row == 2) {
                promotePiece(downRightPosition);
            } else {
                ChessMove downRightPiece = new ChessMove(position, downRightPosition, null);
                ogList.add(downRightPiece);
            }

        }
    }

    public void goDownLeft() {
        if (this.col == 1) {
            return;
        }
        ChessPosition downLeftPosition = new ChessPosition(row - 1, col - 1);  //creating object to view up position
        if (board.getPiece(downLeftPosition) != null && board.getPiece(downLeftPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {                        //of nothing there, create new move and add to list
            if (this.row == 2) {
                promotePiece(downLeftPosition);
            }else {
                ChessMove downLeftPiece = new ChessMove(position, downLeftPosition, null);
                ogList.add(downLeftPiece);
            }

        }
    }





}
