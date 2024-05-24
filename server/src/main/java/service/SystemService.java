package service;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import response.ClearResponse;

public class SystemService {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public SystemService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ClearResponse clearApplication(){
        gameDAO.clearApplication(authDAO, userDAO);
        return new ClearResponse(null);
    }
}
