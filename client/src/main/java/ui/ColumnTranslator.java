package ui;

public class ColumnTranslator {

    public static int translateCol(String location){
        if (location.length() == 2 && Character.isLetter(location.charAt(0)) && Character.isDigit(location.charAt(1))) {
            // Get the second character and convert it to an integer
            char columnLetter = location.charAt(0);
            int col;
            switch (columnLetter){
                case 'a':
                    col = 1;
                    break;
                case 'b':
                    col = 2;
                    break;
                case 'c':
                    col = 3;
                    break;
                case 'd':
                    col = 4;
                    break;
                case 'e':
                    col = 5;
                    break;
                case 'f':
                    col = 6;
                    break;
                case 'g':
                    col = 7;
                    break;
                case 'h':
                    col = 8;
                    break;
                default:
                    col = 10;
                    break;
            }
            return col;
        } else {
            return 10; // out of range
        }
    }
}
