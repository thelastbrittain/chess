package dataaccess.interfaces;

import dataaccess.DataAccessException;

public interface AuthDAO {
    String createAuth(String username);
    boolean isVerifiedAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken);
    void clearAuths();
    String getUsernameFromAuth(String authToken);
}
