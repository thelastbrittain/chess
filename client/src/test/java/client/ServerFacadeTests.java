package client;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.sqldaos.SQLAuthDAO;
import dataaccess.sqldaos.SQLGameDAO;
import dataaccess.sqldaos.SQLUserDAO;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;
import server.Server;

import serverhandling.ServerFacade;


public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade facade;
    private static GameDAO gameDAO = new SQLGameDAO();
    private static UserDAO userDAO = new SQLUserDAO();
    private static AuthDAO authDAO = new SQLAuthDAO();
    private static final String testUsername = "testUsername";
    private static final String testPassword = "testPassword";
    private static final String testEmail = "testEmail";


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clearServer(){
        gameDAO.clearApplication(authDAO, userDAO);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Register Success")
    void registerSuccess() throws Exception {
        RegisterResponse regResponse= facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        Assertions.assertTrue(regResponse.authToken().length() > 10);
    }

    @Test
    @DisplayName("Register Failure")
    void registerFailure() throws Exception {
        RegisterResponse regResponse= facade.register(new RegisterRequest(null, "password", "p1@email.com"));
        Assertions.assertTrue(regResponse.message()!= null);
    }

    //logout
    void logoutSuccess(){

    }

    //login
    @Test
    @DisplayName("Login Success")
    public void loginSuccess(){
        registerUser();
        LoginResponse loginResponse = facade.login(new LoginRequest(testUsername, testPassword));
        Assertions.assertNull(loginResponse.message());
    }

    //login
    @Test
    @DisplayName("Login Failure")
    public void loginFailure(){
//        registerUser();
        LoginResponse loginResponse = facade.login(new LoginRequest(testUsername, testPassword));
        Assertions.assertNotNull(loginResponse.message());
    }

    //create game


    //list games

    //join game

    private RegisterResponse registerUser(){
        return facade.register(new RegisterRequest("testUsername", "testPassword", "testEmail"));
    }

}
