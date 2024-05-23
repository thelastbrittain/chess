package handler;

import com.google.gson.Gson;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import response.LogoutResponse;
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

        String authToken = request.headers("Authorization");
        UserService logoutService = new UserService(userDAO, authDAO);

        LogoutResponse logoutResponse = logoutService.logout(authToken);
        if (logoutResponse.message() == null){
            response.status(200);
        } else {
            response.status(401);
        }
        return logoutResponse;
    }
}
