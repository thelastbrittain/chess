package client;

import org.junit.jupiter.api.*;
import request.RegisterRequest;
import response.RegisterResponse;
import server.Server;

import serverhandling.ServerFacade;


public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void register() throws Exception {
        RegisterResponse regResponse= facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(regResponse.authToken().length() > 10);
    }

}
