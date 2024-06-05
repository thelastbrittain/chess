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
        System.out.println("Inserted Password: " + newUser.password() + " g");
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
            e.printStackTrace();
            return false;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isVerifiedUser(String username, String password) {
        System.out.println("Inside isVerifiedUser");
        String query = "SELECT password FROM user WHERE username = ?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {

            ps.setString(1, username);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Checking Passwords to see if they match");
                    String hashedPassword =  rs.getString("password");
                    System.out.println("Regular password: " + password + " g");
//                    System.out.println("Input Password: " + BCrypt.hashpw(password, BCrypt.gensalt()));
//                    System.out.println("Hashed password from database: " + hashedPassword);
                    boolean veracity = BCrypt.checkpw(password, hashedPassword);
                    System.out.println("True or not: " + veracity);
                    return BCrypt.checkpw(password, hashedPassword);
                } else {
                    // No password found for the given username
                    return false;
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("SQL Error retreving password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
}

