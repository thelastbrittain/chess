package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class BoardCreator {
    private static final List<String> HEADERLETTERS = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");

    public void createBoard(String orientation){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        createHeaders(out, orientation);
        createRows(orientation);
        createHeaders(orientation);
    }

    private void createHeaders(PrintStream out, String orientation) {

        setBlack(out);

        String[] headers = { "TIC", "TAC", "TOE" };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
        }

        out.println();
    }

    private void createRows(String orientation){

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
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBackground(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }



}
