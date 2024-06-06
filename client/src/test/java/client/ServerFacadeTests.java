package client;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.sqldaos.SQLAuthDAO;
import dataaccess.sqldaos.SQLGameDAO;
import dataaccess.sqldaos.SQLUserDAO;
import org.junit.jupiter.api.*;
import request.RegisterRequest;
import response.RegisterResponse;
import server.Server;

import serverhandling.ServerFacade;


public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade facade;
    private static GameDAO gameDAO = new SQLGameDAO();
    private static UserDAO userDAO = new SQLUserDAO();
    private static AuthDAO authDAO = new SQLAuthDAO();


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

    //login

    //create game

    //list games

    //join game

}
