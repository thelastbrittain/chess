package handler;

import com.google.gson.Gson;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import request.LoginRequest;
import response.LoginResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    UserDAO userDAO;
    AuthDAO authDAO;

    public LoginHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson gson = new Gson();
        LoginRequest loginRequest = (LoginRequest)gson.fromJson(request.body(), LoginRequest.class);

        UserService loginService = new UserService(userDAO, authDAO);

        LoginResponse result = loginService.login(loginRequest);
        //fail case is that username/password is wrong
        if (loginRequest.username() == null){
            response.status(401);
            return gson.toJson(result);
        } else {
            response.status(200);
            return gson.toJson(result);
        }

    }
}
