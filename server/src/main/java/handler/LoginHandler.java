package handler;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import request.LoginRequest;
import response.LoginResponse;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import translation.Translator;

public class LoginHandler implements Route {
    UserDAO userDAO;
    AuthDAO authDAO;

    public LoginHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LoginRequest loginRequest = (LoginRequest) Translator.fromJsonToObject(request, LoginRequest.class);;

        UserService loginService = new UserService(userDAO, authDAO);

        LoginResponse result = loginService.login(loginRequest);
        //fail case is that username/password is wrong
        if (result.username() == null){
            response.status(401);
        } else {
            response.status(200);
        }

        return Translator.fromObjectToJson(result);

    }
}
