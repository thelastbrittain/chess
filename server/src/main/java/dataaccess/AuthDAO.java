package dataaccess;

import model.AuthData;

public interface AuthDAO {
    String createAuth(String username);
    AuthData verifyAuth(String authToken);
    void deleteAuth(String authToken);
}
