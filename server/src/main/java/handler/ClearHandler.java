package handler;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.SystemService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public ClearHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        SystemService clearService = new SystemService(userDAO, authDAO, gameDAO);
        int myResponse = clearService.clearApplication();

        return String.valueOf(myResponse);
    }
}
