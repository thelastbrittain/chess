package server;

import dataaccess.DAOException;
import dataaccess.DataAccessException;
import dataaccess.memorydaos.MemoryGameDAO;
import dataaccess.sqldaos.SQLGameDAO;
import dataaccess.sqldaos.SQLUserDAO;
import dataaccess.sqldaos.SQLAuthDAO;
import handler.*;
import spark.*;

import static dataaccess.DatabaseManager.configureDatabase;

public class Server {

    public int run(int desiredPort){
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        try {
            configureDatabase();
        } catch (DataAccessException e) {
            System.out.println("Problem in Server: " + e.getMessage() );
        }

        SQLUserDAO userDAO = new SQLUserDAO();
        SQLAuthDAO authDAO = new SQLAuthDAO();
        SQLGameDAO gameDAO = new SQLGameDAO();

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearHandler(userDAO, authDAO, gameDAO));
        Spark.post("/user", new RegisterHandler(userDAO, authDAO));
        Spark.delete("/session", new LogoutHandler(userDAO, authDAO));
        Spark.post("/session", new LoginHandler(userDAO, authDAO));
        Spark.post("/game", new CreateGameHandler(authDAO, gameDAO));
        Spark.put("/game", new JoinGameHandler(authDAO, gameDAO));
        Spark.get("game", new ListGamesHandler(authDAO, gameDAO));
        Spark.exception(DAOException.class, this::exceptionHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(DAOException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
