package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import handler.ClearHandler;
import handler.LoginHandler;
import handler.LogoutHandler;
import handler.RegisterHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
//        Spark.init();

        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearHandler(userDAO, authDAO, gameDAO));
        Spark.post("/user", new RegisterHandler(userDAO, authDAO));
        Spark.delete("/session", new LogoutHandler(userDAO, authDAO));
        Spark.post("/session", new LoginHandler(userDAO, authDAO));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
