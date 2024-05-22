package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import request.AuthToken;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    UserDAO userDAO;
    AuthDAO authDAO;


    public LogoutHandler(UserDAO userDao,AuthDAO authDAO) {
        this.userDAO = userDao;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson gson = new Gson();

        AuthToken authToken = new AuthToken(request.headers("Authorization"));
        UserService logoutService = new UserService(userDAO, authDAO);

        logoutService.logout(authToken.authToken());
        response.status(200);
        return String.valueOf(200);
    }
}
