package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResponse;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResponse register(RegisterRequest registerRequest){
        if (userDAO.getUser(registerRequest.username()) != null){return null;} //return some sort of message
        userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        String authToken = authDAO.createAuth(registerRequest.username());

        return new RegisterResponse(registerRequest.username(), authToken);
    }
}
