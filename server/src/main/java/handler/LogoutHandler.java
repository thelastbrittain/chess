package handler;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import response.LogoutResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import translation.Translator;

public class LogoutHandler implements Route {
    UserDAO userDAO;
    AuthDAO authDAO;


    public LogoutHandler(UserDAO userDao,AuthDAO authDAO) {
        this.userDAO = userDao;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        System.out.println("In logout handler");
        String authToken = request.headers("Authorization");
        System.out.println("received auth: " + authToken);
        UserService logoutService = new UserService(userDAO, authDAO);
        LogoutResponse logoutResponse = logoutService.logout(authToken);

        if (logoutResponse.message() == null){
            response.status(200);
        } else {
            response.status(401);
        }

        return Translator.fromObjectToJson(logoutResponse);
    }
}
