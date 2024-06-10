import chess.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Server;


public class ServerMain {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ServerMain.class);
        Server myServer = new Server();
        myServer.run(8080);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}