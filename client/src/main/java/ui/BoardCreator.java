package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class BoardCreator {


    public void createBoard(ChessGame.TeamColor orientation, ChessBoard board){
//        System.out.print(board);
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        createHeaders(out, orientation);
        createRows(out, orientation, board);
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



    private void createRows(PrintStream out,  ChessGame.TeamColor orientation, ChessBoard board) {
        if (orientation == ChessGame.TeamColor.WHITE){
            for (int row = 8; row > 0; row--) {
                createRow(out, row, board, orientation);
            }
        } else{
            for (int row = 1; row < 9; row++) {
                createRow(out, row, board, orientation);
            }
        }

    }

    private void createRow(PrintStream out, int row, ChessBoard board, ChessGame.TeamColor orientation){
        assert row > 0 && row < 9;

        printRowNumber(out, row);
        for (int col = 1; col < 9; col++){
            printSquare(out, row, col, board);
        }

        printRowNumber(out, row);
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void printSquare(PrintStream out, int row, int col, ChessBoard board) {
        assert row > 0 && row < 9;

        //putting the background color
        if (isLight(row, col)){
            setLight(out);
        } else {
            setDark(out);
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

    private boolean isLight(int row, int col){
        if (row % 2 == 0 && col % 2 != 0){
            return true;
        } else if (row % 2 != 0 && col % 2 == 0){
            return true;
        } else {return false;}
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




}
