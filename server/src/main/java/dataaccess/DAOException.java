package dataaccess;

public class DAOException extends Exception {
    final private int statusCode;

    public DAOException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int StatusCode() {
        return statusCode;
    }
}
