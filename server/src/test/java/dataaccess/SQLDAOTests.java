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

    //Is verified Auth

    //Delete auth

    //Clear auths

    //Get Username from Auth



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
