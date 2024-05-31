package dataaccess.sqldaos;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import dataaccess.DatabaseManager;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.executeUpdate;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {

    }

    @Override
    public UserData createUser(UserData newUser) {
        var statement = "INSERT INTO user (user, password, email) VALUES (?, ?, ?)";
        try {
            var id = executeUpdate(statement, newUser.username(), newUser.password(), newUser.email());
        } catch (DataAccessException e) {
            System.out.println("Problem in inserting a user: "  + e.getMessage());
            return new UserData(null,null,null);
        }
        return newUser;
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void clearUsers() {

    }

    @Override
    public boolean isVerifiedUser(String username, String password) {
        return false;
    }




}

