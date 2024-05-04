package chess;

import java.util.Collection;

public class PawnMoveCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private ChessGame.TeamColor teamColor;
    private Collection<ChessMove> ogList;
    private int row;
    private int col;

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
//            goUp;
//            goUpLeft;
//            goUpRight;
        }
        else {
//            goDown;
//            goDownLeft;
//            goDownRight;
        }
    }

    public void goUP() {
        //double up case
        if (this.row == 2) {
            ChessPosition upPosition = new ChessPosition(row + 1, col);  //creating object to view up position
            if (board.getPiece(upPosition) == null) {                        //of nothing there, create new move and add to list
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
            ChessPosition upPosition = new ChessPosition(row + 1, col);  //creating object to view up position
            if (board.getPiece(upPosition) == null) {
//                promotePiece();

            }
        }
        else {
            if (this.row != 8){
                ChessPosition upPosition = new ChessPosition(row + 1, col);  //creating object to view up position
                if (board.getPiece(upPosition) == null) {                        //of nothing there, create new move and add to list
                    ChessMove upPiece = new ChessMove(position, upPosition, null);
                    ogList.add(upPiece);
                }
            }
        }
    }

}
