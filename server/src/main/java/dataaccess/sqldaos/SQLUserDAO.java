package dataaccess.sqldaos;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.executeUpdate;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {

    }

    @Override
    public UserData createUser(UserData newUser) {
        if (newUser.password() == null){return new UserData(null, null, null);}
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
        try {
            var id = executeUpdate(statement, newUser.username(), hashedPassword, newUser.email());
        } catch (DataAccessException e) {
            System.out.println("Problem in inserting a user: "  + e.getMessage());
            return new UserData(null,null,null);
        }
        return newUser;
    }

    @Override
    public boolean userExists(String username) {
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
        var statement = "DELETE FROM user";
        try {
            executeUpdate(statement);
        } catch (DataAccessException e) {
            System.out.println("Error clearing user table: " + e.getMessage());
        }
    }

    @Override
    public boolean isVerifiedUser(String username, String password) {
        String query = "SELECT password FROM user WHERE username = ?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {

            ps.setString(1, username);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword =  rs.getString("password");
                    return BCrypt.checkpw(password, hashedPassword);
                } else {
                    // No password found for the given username
                    return false;
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("SQL Error retreving password: " + e.getMessage());
            return false;
        }
    }
}

