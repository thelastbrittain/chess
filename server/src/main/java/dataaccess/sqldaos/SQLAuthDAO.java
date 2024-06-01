package dataaccess.sqldaos;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.interfaces.AuthDAO;
import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

import static dataaccess.DatabaseManager.executeUpdate;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {

    }

    @Override
    public String createAuth(String username) {
        String newAuthToken = UUID.randomUUID().toString();
        String statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        try {
            executeUpdate(statement, username, newAuthToken);
        } catch (DataAccessException e) {
            System.out.println("Error in createAuth: " + e.getMessage());
            return null;
        }
        return newAuthToken;
    }

    @Override
    public boolean isVerifiedAuth(String authToken) {
        String statement = "SELECT COUNT(*) FROM auth WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, authToken);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Access Denied: " + e.getMessage());
            return false;
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public String getUsernameFromAuth(String authToken) {
        String statement = "SELECT username FROM auth WHERE authToken = ?";
        try (var ps = DatabaseManager.getConnection().prepareStatement(statement))
        {
            ps.setString(1, authToken);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                } else {
                    return null;
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Unable to retrieve the username given the auth: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        var statement = "DELETE FROM auth WHERE authToken=?";
        try {
            executeUpdate(statement, authToken);
        } catch (DataAccessException e) {
            System.out.println("Failed to Delete AuthToken: " + e.getMessage());
        }

    }

    @Override
    public void clearAuths() {
        var statement = "TRUNCATE auth";
        try {
            executeUpdate(statement);
        } catch (DataAccessException e) {
            System.out.println("Error clearing user table: " + e.getMessage());
        }
    }



}
