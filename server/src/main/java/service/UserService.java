package service;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResponse register(RegisterRequest registerRequest){
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null){
            return new RegisterResponse(null, null, ErrorMessages.BADREQUEST); }
        if (userDAO.getUser(registerRequest.username()) != null){return new RegisterResponse(null, null, ErrorMessages.ALREADYTAKEN);} //return some sort of message
        userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        String authToken = authDAO.createAuth(registerRequest.username());

        return new RegisterResponse(registerRequest.username(), authToken, null);
    }
    public LoginResponse login(LoginRequest loginRequest){
        if (!userDAO.isVerifiedUser(loginRequest.username(), loginRequest.password())){return new LoginResponse(null, null, ErrorMessages.UNAUTHORIZED);} //change to error message
        return new LoginResponse(loginRequest.username(), authDAO.createAuth(loginRequest.username()), null);
    }

    public LogoutResponse logout(String authToken){
        if (!authDAO.isVerifiedAuth(authToken)){return new LogoutResponse(ErrorMessages.UNAUTHORIZED);} // maybe change to return error message
        authDAO.deleteAuth(authToken);
        return new LogoutResponse(null);
    }

}
