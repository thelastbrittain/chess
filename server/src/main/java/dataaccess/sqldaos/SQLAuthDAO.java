package dataaccess.sqldaos;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import model.AuthData;

import java.util.UUID;

import static dataaccess.DatabaseManager.executeUpdate;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {

    }

    @Override
    public String createAuth(String username) {
        String newAuthToken = UUID.randomUUID().toString();
        AuthData newAuthorization = new AuthData(username, newAuthToken);
        String statement = "UPDATE auth SET authToken = ? WHERE username = ?";
        try {
            executeUpdate(statement, newAuthorization, username);
        } catch (DataAccessException e) {
            System.out.println("Error in createAuth: " + e.getMessage());
            return null;
        }
        return newAuthToken;
    }

    @Override
    public boolean isVerifiedAuth(String authToken) {
        return false;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clearAuths() {

    }

    @Override
    public String getUsernameFromAuth(String authToken) {
        return "";
    }



}
