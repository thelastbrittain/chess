package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.logging.LoggerFactory;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import server.Server;

import serverhandling.ServerFacade;

import java.util.logging.Logger;


public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade facade;
    private static final String TESTUSERNAME = "testUsername";
    private static final String TESTPASSWORD = "testPassword";
    private static final String TESTEMAIL = "testEmail";


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clearServer(){
        facade.clearGame();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void clearGameSuccess(){
        String authToken = registerUser().authToken();
        CreateGameResponse createGameResponse = facade.createGame(new CreateGameRequest("TestGame", authToken), authToken);
        facade.clearGame();
        ListGamesResponse response = facade.listGames(authToken);
        Assertions.assertNotNull(response.message());


    }

    @Test
    @DisplayName("Register Success")
    public void registerSuccess() throws Exception {
        RegisterResponse regResponse= facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        Assertions.assertTrue(regResponse.authToken().length() > 10);
    }

    @Test
    @DisplayName("Register Failure")
    public void registerFailure() throws Exception {
        RegisterResponse regResponse= facade.register(new RegisterRequest(null, "password", "p1@email.com"));
        Assertions.assertTrue(regResponse.message()!= null);
    }

    //logout
    @Test
    public void logoutSuccess(){
        String authToken = registerUser().authToken();
        LogoutResponse response = facade.logout(authToken);
        Assertions.assertTrue(response.message().length() < 10);
    }

    @Test
    public void logoutFailure(){
        String authToken = "Blob";
        LogoutResponse response = facade.logout(authToken);
        Assertions.assertNotNull(response.message());
    }

    //login
    @Test
    @DisplayName("Login Success")
    public void loginSuccess(){
        registerUser();
        LoginResponse loginResponse = facade.login(new LoginRequest(TESTUSERNAME, TESTPASSWORD));
        Assertions.assertNull(loginResponse.message());
    }

    //login
    @Test
    @DisplayName("Login Failure")
    public void loginFailure(){
//        registerUser();
        LoginResponse loginResponse = facade.login(new LoginRequest(TESTUSERNAME, TESTPASSWORD));
        Assertions.assertNotNull(loginResponse.message());
    }

    //create game
    @Test
    public void createGameSuccess(){
        String authToken = registerUser().authToken();
        CreateGameResponse response = facade.createGame(new CreateGameRequest("TestGame", authToken), authToken);
        Assertions.assertNull(response.message());
    }

    @Test
    public void createGameFailure(){
        String authToken = "mumbo jumbo";
        CreateGameResponse response = facade.createGame(new CreateGameRequest("TestGame", authToken), authToken);
        Assertions.assertNotNull(response.message());
    }

    //list games
    @Test
    public void listGamesSuccess(){
        String authToken = registerUser().authToken();
        facade.createGame(new CreateGameRequest("TestGame", authToken), authToken);
        ListGamesResponse response = facade.listGames(authToken);
        Assertions.assertNull(response.message());
    }

    @Test
    public void listGamesFailure(){
        String authToken = "mumbo jumbo";
        facade.createGame(new CreateGameRequest("TestGame", authToken), authToken);
        ListGamesResponse response = facade.listGames(authToken);
        Assertions.assertNotNull(response.message());
    }

    //join game
    @Test
    public void joinGameSuccess(){
        RegisterResponse registerResponse = registerUser();
        String authToken = registerResponse.authToken();
        facade.createGame(new CreateGameRequest("TestGame", authToken), authToken);
        JoinGameResponse response = facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE,1,authToken));
        Assertions.assertNull(response.message());
    }

    @Test
    public void joinGameFailure(){
        RegisterResponse registerResponse = registerUser();
        String authToken = registerResponse.authToken();
        facade.createGame(new CreateGameRequest("TestGame", authToken), authToken);
        JoinGameResponse response = facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE,2,authToken));
        Assertions.assertNotNull(response.message());
    }

    private RegisterResponse registerUser(){
        return facade.register(new RegisterRequest(TESTUSERNAME, TESTPASSWORD, TESTEMAIL));
    }

}
