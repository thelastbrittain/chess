import chess.*;
import org.junit.platform.commons.logging.LoggerFactory;
import ui.BoardCreator;
import ui.ClientMenu;

import java.util.logging.Logger;

public class ClientMain {
    public static void main(String[] args) {
        int port = 8080;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        new ClientMenu(port).run();
    }
}