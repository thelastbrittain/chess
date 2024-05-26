import chess.ChessGame;
import dataaccess.memoryDAOs.MemoryAuthDAO;
import dataaccess.memoryDAOs.MemoryGameDAO;
import dataaccess.memoryDAOs.MemoryUserDAO;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import service.UserService;
import service.GameService;
import service.ErrorMessages;
import service.SystemService;

import java.awt.*;

public class service {
    String testUsername = "testUsername";
    String testPassword = "testPassword";
    String testEmail = "testEmail";
    String testGame = "testGame";

    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    UserService userService = new UserService(userDAO, authDAO);
    GameService gameService = new GameService(authDAO, gameDAO);
    SystemService systemService = new SystemService(userDAO, authDAO, gameDAO);


    @Test
    @BeforeEach
    void resetServer(){
        systemService.clearApplication();
    }

    /**
     * User Service Tests
     */

    @Test
    @DisplayName("Register Success")
    void registerSuccess(){
        RegisterRequest regRequest = new RegisterRequest(testUsername, testPassword, testEmail);
        UserService registerService =  new UserService(userDAO, authDAO);
        RegisterResponse actualResponse = registerService.register(regRequest);

        Assertions.assertNotNull(actualResponse.authToken(), "No auth token given");
    }

    @Test
    @DisplayName("Register No Email Failure")
    void registerEmailFailure(){
        RegisterRequest regRequest = new RegisterRequest(testUsername, testPassword, null);
        UserService registerService =  new UserService(userDAO, authDAO);
        RegisterResponse actualResponse = registerService.register(regRequest);

        Assertions.assertSame(actualResponse.message(), ErrorMessages.BADREQUEST);
    }

    @Test
    @DisplayName("Login Success")
    void loginSuccess(){
        registerAndLogoutUser();
        LoginResponse loginResponse = userService.login(new LoginRequest(testUsername, testPassword));

        Assertions.assertNotNull(loginResponse.authToken());
    }

    @Test
    @DisplayName("Login Password Failure")
    void loginFailure() {
        registerAndLogoutUser();
        LoginResponse loginResponse = userService.login(new LoginRequest(testUsername, "Incorrect Password"));
        Assertions.assertSame(ErrorMessages.UNAUTHORIZED, loginResponse.message());
    }

    @Test
    @DisplayName("logout Success")
    void logoutSuccess(){
        LogoutResponse logoutResponse = registerAndLogoutUser();
        Assertions.assertNull(logoutResponse.message());
    }

    @Test
    @DisplayName("Logout Bad Auth Token Failure")
    void logoutFailure(){
        RegisterResponse registerRequest = registerTestUser();
        LogoutResponse logoutResponse = userService.logout("Incorrect Auth");

        Assertions.assertSame(ErrorMessages.UNAUTHORIZED, logoutResponse.message());
    }

    /**
     * GameService Tests
     */

    @Test
    @DisplayName("Create Game Success")
    void createGameSuccess(){
        CreateGameResponse createGameResponse = createUserAndGameReturnGame();
        Assertions.assertNull(createGameResponse.message());
    }

    @Test
    @DisplayName("Create Game Bad Auth Failure")
    void createGameFailure(){
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest(testGame, "False Auth"));
        Assertions.assertSame(ErrorMessages.UNAUTHORIZED, createGameResponse.message());
    }

    @Test
    @DisplayName("Join Game Success")
    void joinGameSuccess(){
        RegisterResponse registerResponse = registerTestUser();
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest(testGame, registerResponse.authToken()));
        JoinGameResponse joinGameResponse = gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE,
                createGameResponse.gameID(), registerResponse.authToken()));

        Assertions.assertNull(joinGameResponse.message());
    }

    @Test
    @DisplayName("Join User Position in Game Failure")
    void joinGameFailure(){
        RegisterResponse registerResponse = registerTestUser();
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest(testGame, registerResponse.authToken()));

        JoinGameRequest testJoinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE,
                createGameResponse.gameID(), registerResponse.authToken());

        JoinGameResponse joinGameResponse = gameService.joinGame(testJoinGameRequest);
        JoinGameResponse secondJoinGameResponse = gameService.joinGame(testJoinGameRequest);

        Assertions.assertSame(ErrorMessages.ALREADYTAKEN, secondJoinGameResponse.message());
    }

    @Test
    @DisplayName("ListGamesSuccess")
    void listGamesSuccess(){
        RegisterResponse registerResponse = createUserAndGameReturnUser();
        ListGamesResponse listGamesResponse = gameService.listGames(registerResponse.authToken());

        Assertions.assertNotNull(listGamesResponse.games());

    }

    @Test
    @DisplayName("List Games Authentication Failure")
    void listGamesFailure(){
        RegisterResponse registerResponse = createUserAndGameReturnUser();
        ListGamesResponse listGamesResponse = gameService.listGames("False Auth");

        Assertions.assertEquals(ErrorMessages.UNAUTHORIZED, listGamesResponse.message());

    }

    /**
     * System Service Tests
     */
    @Test
    @DisplayName("Clear Database Success")
    void clearDatabaseSuccess(){
        RegisterResponse registerResponse = createUserAndGameReturnUser();
        systemService.clearApplication();

        ListGamesResponse listGamesResponse = gameService.listGames(registerResponse.authToken());

        Assertions.assertEquals(ErrorMessages.UNAUTHORIZED, listGamesResponse.message());
    }


    /**
     *Helper Functions
     */
    RegisterResponse registerTestUser(){
        return userService.register(new RegisterRequest(testUsername, testPassword, testEmail));
    }

    LogoutResponse registerAndLogoutUser(){
        return userService.logout(userService.register(new RegisterRequest(testUsername, testPassword, testEmail)).authToken());
    }

    CreateGameResponse createUserAndGameReturnGame() {
        RegisterResponse registerResponse = registerTestUser();
        return gameService.createGame(new CreateGameRequest(testGame, registerResponse.authToken()));
    }

    RegisterResponse createUserAndGameReturnUser(){
        RegisterResponse registerResponse = registerTestUser();
        gameService.createGame(new CreateGameRequest(testGame, registerResponse.authToken()));
        return registerResponse;
    }
}
