package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
//    See point code for examples
    private int row;
    private int column;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.column = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return column;
    }

    @Override
    public String toString(){
        return "Row,Column (" + this.getRow() + "," + this.getColumn() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //Check if they are the same instance of the object
        if (o == null || getClass() != o.getClass()) return false; //if the object is null or not the same, return false
        ChessPosition that = (ChessPosition) o; //cast o as a ChessPosition type
        return row == that.row && column == that.column; //Check if they have the same values
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column); //hashes the row and column so that they can be compared
    }
}
