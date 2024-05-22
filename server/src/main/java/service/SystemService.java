package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class SystemService {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public SystemService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public int clearApplication(){
        gameDAO.clearApplication(authDAO, userDAO);
        return 200;
    }
}
