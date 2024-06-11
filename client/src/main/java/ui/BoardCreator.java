package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class BoardCreator {


    public void createBoard(ChessGame.TeamColor orientation, ChessBoard board, Collection<ChessMove> validMoves){
//        System.out.print(board);
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        createHeaders(out, orientation);
        createRows(out, orientation, board, validMoves);
        createHeaders(out, orientation);
    }



    private void createHeaders(PrintStream out, ChessGame.TeamColor orientation) {
        assert orientation == ChessGame.TeamColor.WHITE || orientation == ChessGame.TeamColor.BLACK;

        setBoardBackground(out);
        drawBlankSquare(out);

        String[] headers = {"A", "B", "C", "D", "E", "F", "G", "H"};
        if (orientation == ChessGame.TeamColor.WHITE) {
            for (int boardCol = 0; boardCol < 8; ++boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        } else {
            for (int boardCol = 7; boardCol >= 0; --boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        }

        drawBlankSquare(out);
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
//        setBackground(out);
        out.println();
    }

    private void drawHeader(PrintStream out, String header) {
        out.print(EMPTY);
        out.print(header);
        out.print(EMPTY);
    }

    private void drawBlankSquare(PrintStream out){
        out.print(EMPTY.repeat(3));
    }



    private void createRows(PrintStream out,  ChessGame.TeamColor orientation, ChessBoard board, Collection<ChessMove> validMoves) {
        if (orientation == ChessGame.TeamColor.WHITE){
            for (int row = 8; row > 0; row--) {
                createRow(out, row, board, orientation, validMoves);
            }
        } else{
            for (int row = 1; row < 9; row++) {
                createRow(out, row, board, orientation, validMoves);
            }
        }

    }

    private void createRow(PrintStream out, int row, ChessBoard board, ChessGame.TeamColor orientation, Collection<ChessMove> validMoves){
        assert row > 0 && row < 9;

        printRowNumber(out, row);
        if (orientation == ChessGame.TeamColor.WHITE){
            for (int col = 1; col < 9; col++){
                printSquare(out, row, col, board, validMoves);
            }
        } else{
            for (int col = 8; col > 0; col--){
                printSquare(out, row, col, board, validMoves);
            }
        }

        printRowNumber(out, row);
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void printSquare(PrintStream out, int row, int col, ChessBoard board, Collection<ChessMove> validMoves) {
        assert row > 0 && row < 9;

        //putting the background color
        if (validMoves != null){
            if (isStartMove(row, col, validMoves)){
                setStartPosition(out);
            }
            else if (isPossibleMove(row, col, validMoves)){
                checkAndSetEndPositionLightOrDark(row, col, out);
            } else {
                checkAndSetLightOrDark(row, col, out);
            }
        } else {
            checkAndSetLightOrDark(row, col, out);
        }

        //putting the text (null or a piece)
        if (isPiece(row, col, board)){
            printPiece(out, row, col, board);
        } else {
            out.print(EMPTY.repeat(3));
        }

    }

    private void printPiece(PrintStream out, int row, int col, ChessBoard board) {
        ChessPosition position = new ChessPosition(row, col);
        String pieceType = pieceTypeIntoLetter(board.getPiece(position).getPieceType());
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        if (color == ChessGame.TeamColor.WHITE){
            setWhiteText(out);
        } else {
            setBlackText(out);
        }
        out.print(EMPTY);
        out.print(pieceType);
        out.print(EMPTY);
    }

    private String pieceTypeIntoLetter(ChessPiece.PieceType pieceType) {
        return switch (pieceType) {
            case PAWN -> "P";
            case ROOK -> "R";
            case KNIGHT -> "N";
            case BISHOP -> "B";
            case QUEEN -> "Q";
            case KING -> "K";
        };
    }

    private boolean isPiece(int row, int col, ChessBoard board){
        if (board.getPiece(new ChessPosition(row, col)) == null){
            return false;
        } else {
            return true;
        }
    }

    private boolean isPossibleMove(int row, int col, Collection<ChessMove> validMoves){
        ChessPosition endPosition = new ChessPosition(row, col);
        for (ChessMove move : validMoves){
            if (move.getEndPosition().equals(endPosition)){  //may run into problems with equals, but probably not
                return true;
            }
        }
        return false;
    }

    private boolean isStartMove(int row, int col, Collection<ChessMove> validMoves){
        ChessPosition startPosition = new ChessPosition(row, col);
        for (ChessMove move : validMoves){
            if (move.getStartPosition().equals(startPosition)){  //may run into problems with equals, but probably not
                return true;
            }
        }
        return false;
    }

    private boolean isLight(int row, int col){
        if (row % 2 == 0 && col % 2 != 0){
            return true;
        } else if (row % 2 != 0 && col % 2 == 0){
            return true;
        } else {return false;}
    }

    private void checkAndSetLightOrDark(int row, int col, PrintStream out){
        if (isLight(row, col)){
            setLight(out);
        } else {
            setDark(out);
        }
    }

    private void checkAndSetEndPositionLightOrDark(int row, int col, PrintStream out){
        if (isLight(row, col)){
            setLightEndPosition(out);
        } else {
            setDarkEndPosition(out);
        }
    }

    private void printRowNumber(PrintStream out, int row){
        setBoardBackground(out);
        out.print(EMPTY);
        out.print(row);
        out.print(EMPTY);
    }

    private static void setLight(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_TAN);
    }

    private static void setDark(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_BROWN);
    }

    private static void setBoardBackground(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setWhiteText(PrintStream out) {
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlackText(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setStartPosition(PrintStream out){
        out.print(SET_BG_COLOR_BLUE);
//        out.print(SET_BG_COLOR_GREEN);
    }

    private static void setDarkEndPosition(PrintStream out){
        out.print(SET_BG_COLOR_DARK_GREY);
//        out.print(SET_BG_COLOR_RED);
    }

    private static void setLightEndPosition(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
//        out.print(SET_BG_COLOR_BLUE);
    }


}
