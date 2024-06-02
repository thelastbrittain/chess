package dataaccess;

import chess.ChessGame;
import dataaccess.sqldaos.SQLAuthDAO;
import dataaccess.sqldaos.SQLGameDAO;
import dataaccess.sqldaos.SQLUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import response.JoinGameResponse;

import java.util.ArrayList;
import java.util.Collection;

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
    @Test
    @DisplayName("Create Game Success")
    public void createGameSuccess(){
        int gameID = gameDAO.createGame("Game 1");
        int gameID2 = gameDAO.createGame("Game 2");
        Assertions.assertEquals(1, gameID);
        Assertions.assertEquals(2, gameID2);

    }

    @Test
    @DisplayName("Create Game Failure")
    public void createGameFailure() {
        int gameID = gameDAO.createGame(null);
        Assertions.assertEquals(0, gameID);
    }

    //Is verified Game
    @Test
    @DisplayName("Is Verified Game Successs")
    public void isVerifiedGameSuccess(){
        int gameID = gameDAO.createGame(testGame);
        Assertions.assertTrue(gameDAO.isVerifiedGame(gameID));
    }

    @Test
    @DisplayName("Is Verified Game Failure")
    public void isVerifiedGameFailure(){
        int gameID = gameDAO.createGame(testGame);
        Assertions.assertFalse(gameDAO.isVerifiedGame(5));
    }

    //List games
    @Test
    @DisplayName("List Games Success")
    public void listGamesSuccess(){
        gameDAO.createGame(testGame);
        Collection<GameData> gameData = gameDAO.listGames();
        Assertions.assertFalse(gameData.isEmpty());
    }

    @Test
    @DisplayName("List Games Failure")
    public void listGamesFailure(){
        Collection<GameData> gameData = gameDAO.listGames();
        Assertions.assertTrue(gameData.isEmpty());
    }

    //Update user in game
    @Test
    @DisplayName("Update User in Game Success")
    public void updateUserInGameSuccess(){
        int gameID = gameDAO.createGame(testGame);
        createTestUser();
        JoinGameResponse response = gameDAO.updateUserInGame(gameID, testUsername, ChessGame.TeamColor.WHITE);
        Assertions.assertNull(response.message());
    }

    @Test
    @DisplayName("Update User in Game Failure")
    public void updateUserInGameFailure(){
        int gameID = gameDAO.createGame(testGame);
        createTestUser();
        gameDAO.updateUserInGame(gameID, testUsername, ChessGame.TeamColor.WHITE);
        JoinGameResponse response = gameDAO.updateUserInGame(gameID, testUsername, ChessGame.TeamColor.WHITE);
        Assertions.assertNotNull(response.message());
    }

    //Clear games
    @Test
    @DisplayName("Clear Games Successs")
    public void clearGamesSuccess(){
        gameDAO.createGame(testGame);
        gameDAO.clearGames();
        Collection<GameData> gameData = gameDAO.listGames();
        Assertions.assertTrue(gameData.isEmpty());
    }

    //Clear all data
    @Test
    @DisplayName("Clear All Data Success")
    public void clearAllDataSuccess(){
        createTestUser();
        String authToken = authDAO.createAuth(testUsername);
        gameDAO.createGame(testGame);

        gameDAO.clearApplication(authDAO, userDAO);

        Assertions.assertFalse(userDAO.userExists(testUsername));
        Assertions.assertFalse(authDAO.isVerifiedAuth(authToken));

        Collection<GameData> gameData = gameDAO.listGames();
        Assertions.assertTrue(gameData.isEmpty());

    }

    /**
     * Helper functions
     */

    private void createTestUser(){
        userDAO.createUser(testUser);
    }

}
