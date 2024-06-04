package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class BoardCreator {

    public void createBoard(ChessGame.TeamColor orientation){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        createHeaders(out, orientation);
        createRows(out, orientation);
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
        setBackground(out);
        out.println();
    }

    private void drawHeader(PrintStream out, String header) {
        out.print(EMPTY);
        out.print(header);
        out.print(EMPTY);
    }

    private void drawBlankSquare(PrintStream out){
        setBoardBackground(out);
        out.print(EMPTY.repeat(3));
    }

    private void createRows(PrintStream out,  ChessGame.TeamColor orientation) {
        for (int i = 1; i < 9; i++) {
            createRow(out, i);
        }
    }

    private void createRow(PrintStream out, int row){
        assert row > 0 && row < 9;

        printRowNumber(out, row);
        for (int col = 1; col < 9; col++){
            printSquare(out, row, col);
        }

        printRowNumber(out, row);
        setBackground(out);
        out.println();
    }

    private void printSquare(PrintStream out, int row, int col) {
        assert row > 0 && row < 9;
        if (isLight(row, col)){
            setLight(out);
            out.print(EMPTY.repeat(3));
        } else {
            setDark(out);
            out.print(EMPTY.repeat(3));
        }
    }

    private boolean isLight(int row, int col){
        if (row % 2 != 0 && col % 2 != 0){
            return true;
        } else if (row % 2 == 0 && col % 2 == 0){
            return true;
        } else {return false;}
    }

    private void printRowNumber(PrintStream out, int row){
        setBoardBackground(out);
        out.print(EMPTY);
        out.print(row);
        out.print(EMPTY);
    }

    private ChessGame.TeamColor reverseOrientationColor(ChessGame.TeamColor color){
        assert color == ChessGame.TeamColor.WHITE || color == ChessGame.TeamColor.BLACK;
        if (color == ChessGame.TeamColor.WHITE){
            return  ChessGame.TeamColor.BLACK;
        } else {
            return ChessGame.TeamColor.WHITE;
        }
    }



    //what colors do I want?
    //background color - Light/Dark Grey
    //light color  -- Dark Tan (could change to light tan
    //dark color -- Dark Brown

    private static void setLight(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_TAN);
        out.print(SET_TEXT_COLOR_DARK_TAN);
    }

    private static void setDark(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_BROWN);
        out.print(SET_TEXT_COLOR_DARK_BROWN);
    }

    private static void setBoardBackground(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setBackground(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }



}
