package handler;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import response.ClearResponse;
import service.SystemService;
import spark.Request;
import spark.Response;
import spark.Route;
import translation.Translator;

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
        ClearResponse myResponse = clearService.clearApplication();

        response.status(200);
        return Translator.fromObjectToJson(myResponse);
    }
}
