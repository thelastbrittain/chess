package handler;

import com.google.gson.Gson;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import request.RegisterRequest;
import response.RegisterResponse;
import service.ErrorMessages;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
    UserDAO userDAO;
    AuthDAO authDAO;

    public RegisterHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson gson = new Gson();
        RegisterRequest registerRequest = (RegisterRequest)gson.fromJson(request.body(), RegisterRequest.class);

        UserService loginService = new UserService(userDAO, authDAO);
        RegisterResponse result = loginService.register(registerRequest);
        if (result.message() == null){
            response.status(200);
        } else {
            if (result.message().equals(ErrorMessages.ALREADYTAKEN)){
                response.status(403);
            }
        }
        return gson.toJson(result);
    }
}
