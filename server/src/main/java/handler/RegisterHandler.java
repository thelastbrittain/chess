package handler;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import request.RegisterRequest;
import response.RegisterResponse;
import service.ErrorMessages;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import translation.Translator;

public class RegisterHandler implements Route {
    UserDAO userDAO;
    AuthDAO authDAO;

    public RegisterHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        RegisterRequest registerRequest = (RegisterRequest) Translator.fromJsonToObject(request, RegisterRequest.class);
        UserService registerService = new UserService(userDAO, authDAO);
        RegisterResponse result = registerService.register(registerRequest);

        if (result.message() == null){
            response.status(200);
        } else {
            if (result.message().equals(ErrorMessages.ALREADYTAKEN)){
                response.status(403);
            } else if (result.message().equals(ErrorMessages.BADREQUEST)){
                response.status(400);
            } else if (result.message().equals(ErrorMessages.SQLERROR)){
                response.status(500);
            }
        }

        return Translator.fromObjectToJson(result);
    }
}
