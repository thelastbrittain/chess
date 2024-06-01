package dataaccess;

import dataaccess.sqldaos.SQLAuthDAO;
import dataaccess.sqldaos.SQLGameDAO;
import dataaccess.sqldaos.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SQLDAOTests {
    SQLGameDAO gameDAO = new SQLGameDAO();
    SQLAuthDAO authDAO = new SQLAuthDAO();
    SQLUserDAO userDAO = new SQLUserDAO();
    String testUsername = "testUsername";
    String testPassword = "testPassword";
    String testEmail = "testEmail";
    UserData testUser = new UserData(testUsername,testPassword,testEmail);
    String testGame = "testGame";

    //Before Each: Clear the database
    @Test
    @BeforeEach
    public void resetData(){
        gameDAO.clearApplication(authDAO, userDAO);
    }

    /**
     * User DAO Tests
     */

    //Create User

    @Test
    @DisplayName("Create User Success")
    public void createUserSuccess(){
        UserData newUser = testUser;
        UserData goodUser = userDAO.createUser(newUser);

        Assertions.assertEquals(newUser, goodUser);
    }

    @Test
    @DisplayName("Create User Failure no password")
    public void createUserFailure(){
        UserData badUser = new UserData(testUsername, null, testEmail);
        UserData testUser = userDAO.createUser(badUser);
        Assertions.assertNull(testUser.username());
    }

    //userExists

    @Test
    @DisplayName("User Exists Test Success")
    public void userExistsSuccess(){
        createTestUser();
        Assertions.assertTrue(userDAO.userExists(testUser.username()));
    }

    @Test
    @DisplayName("User Exists Failure No User Created")
    public void userExistsFailure(){
        Assertions.assertFalse(userDAO.userExists(testUsername));
    }

    //Is verified user
    @Test
    @DisplayName("Is Verified User Success")
    public void isVerifiedUserSuccess(){
        createTestUser();
        Assertions.assertTrue(userDAO.isVerifiedUser(testUsername,testPassword));
    }

    @Test
    @DisplayName("Is Verified User Failure")
    public void isVerifiedUserFailure(){
        Assertions.assertFalse(userDAO.isVerifiedUser(testUsername,testPassword));
    }

    //Clear users
    @Test
    @DisplayName("Clear Users Success")
    public void clearUsersSuccess(){
        createTestUser();
        userDAO.clearUsers();
        Assertions.assertFalse(userDAO.userExists(testUsername));
    }


    /**
     * Auth DAO Tests
     */

    //Create Auth
    @Test
    @DisplayName("Create Auth Success")
    public void createAuthSuccess(){
        String authToken = authDAO.createAuth(testUsername);
        Assertions.assertNotNull(authToken);
    }

    @Test
    @DisplayName("Create Auth Failure")
    public void createAuthFailure(){
        String authToken = authDAO.createAuth(null);
        Assertions.assertNull(authToken);
    }

    //Is verified Auth
    @Test
    @DisplayName("Is Verified Auth Success")
    public void isVerifiedAuthSuccess(){
        String authToken = authDAO.createAuth(testUsername);
        Assertions.assertTrue(authDAO.isVerifiedAuth(authToken));
    }

    @Test
    @DisplayName("Is Verified Auth Failure")
    public void isVerifiedAuthFailure(){
        Assertions.assertFalse(authDAO.isVerifiedAuth("Not a valid auth"));
    }

    //Get Username from Auth
    @Test
    @DisplayName("Get Username From Auth Success")
    public void getUsernameFromAuthSuccess(){
        String authToken = authDAO.createAuth(testUsername);
        String username = authDAO.getUsernameFromAuth(authToken);
        Assertions.assertEquals(testUsername, username);
    }

    @Test
    @DisplayName("Get Username From Auth Failure")
    public void getUsernameFromAuthFailure(){
        String authToken = authDAO.createAuth(testUsername);
        String username = authDAO.getUsernameFromAuth("Faulty Token");
        Assertions.assertNotEquals(testUsername, username);
    }

    //Delete auth
    @Test
    @DisplayName("Delete Auth Success")
    public void deleteAuthSuccess(){
        String authToken = authDAO.createAuth(testUsername);
        authDAO.deleteAuth(authToken);
        Assertions.assertFalse(authDAO.isVerifiedAuth(authToken));
    }

    @Test
    @DisplayName("Delete Auth Failure")
    public void deleteAuthFailure(){
        String authToken = authDAO.createAuth(testUsername);
        authDAO.deleteAuth("Faulty Auth");
        Assertions.assertTrue(authDAO.isVerifiedAuth(authToken));
    }

    //Clear auths
    @Test
    @DisplayName("Clear Auths Success")
    public void clearAuthSuccess(){
        String authToken = authDAO.createAuth(testUsername);
        authDAO.clearAuths();
        Assertions.assertFalse(authDAO.isVerifiedAuth(authToken));
    }





    /**
     * Game DAO Tests
     */

    //Create Game

    //Is verified Game

    //List games

    //Update user in game

    //Clear games

    //Clear all data

    /**
     * Helper functions
     */

    private void createTestUser(){
        userDAO.createUser(testUser);
    }

}
