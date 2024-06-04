package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class BoardCreator {

    public void createBoard(ChessGame.TeamColor orientation){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        createHeaders(out, orientation);
        createRows(orientation);
        createHeaders(out, reverseOrientationColor(orientation));
    }

    private ChessGame.TeamColor reverseOrientationColor(ChessGame.TeamColor color){
        assert color == ChessGame.TeamColor.WHITE || color == ChessGame.TeamColor.BLACK;
        if (color == ChessGame.TeamColor.WHITE){
            return  ChessGame.TeamColor.BLACK;
        } else {
            return ChessGame.TeamColor.WHITE;
        }
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

    private void createRows(ChessGame.TeamColor orientation){

    }



    //what colors do I want?
    //background color - Light/Dark Grey
    //light color  -- Dark Tan (could change to light tan
    //dark color -- Dark Brown

    private static void setLight(PrintStream out) {
        out.print(SET_TEXT_COLOR_DARK_TAN);
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
