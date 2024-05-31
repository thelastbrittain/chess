package dataaccess.sqldaos;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import dataaccess.DatabaseManager;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.executeUpdate;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            System.out.println("Problem caught in SQLUserDAO");
        }
    }

    @Override
    public UserData createUser(UserData newUser) {
        var statement = "INSERT INTO  (user, password, email) VALUES (?, ?, ?)";
        try {
            var id = executeUpdate(statement, newUser.username(), newUser.password(), newUser.email());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
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

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createUserTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Not working... ");
        }
    }

    private final String[] createUserTable = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL AUTO_INCREMENT,
              `password` varchar(256) NOT NULL,
              `email` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              PRIMARY KEY (`username`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}

