package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import org.eclipse.jetty.security.LoginService;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;
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
        LoginResponse result = new LoginResponse(loginRequest.username(),loginService.login(loginRequest));
        response.status(200);
        return gson.toJson(result);
    }
}
