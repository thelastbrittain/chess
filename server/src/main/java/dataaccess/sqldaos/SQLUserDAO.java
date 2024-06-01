package dataaccess.sqldaos;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.interfaces.UserDAO;
import model.UserData;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.executeUpdate;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {

    }

    @Override
    public UserData createUser(UserData newUser) {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try {
            var id = executeUpdate(statement, newUser.username(), newUser.password(), newUser.email());
        } catch (DataAccessException e) {
            System.out.println("Problem in inserting a user: "  + e.getMessage());
            return new UserData(null,null,null);
        }
        return newUser;
    }

    @Override
    public boolean getUser(String username) {
        String query = "SELECT COUNT(*) FROM user WHERE username = ?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            ps.setString(1, username);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking user credentials: " + e.getMessage());
            return false;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    @Override
    public void clearUsers() {

    }

    @Override
    public boolean isVerifiedUser(String username, String password) {
        String query = "SELECT COUNT(*) FROM user WHERE username = ? AND password = ?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking user credentials: " + e.getMessage());
            return false;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}

