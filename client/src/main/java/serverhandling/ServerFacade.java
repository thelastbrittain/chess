package serverhandling;

import request.CreateGameRequest;
import request.RegisterRequest;
import response.CreateGameResponse;
import response.RegisterResponse;
import translation.Translator;

import java.io.IOException;

public class ServerFacade {
    private String url;
    private final ClientCommunicator clientCommunicator = new ClientCommunicator();


    public ServerFacade(int port){
        url = "http://localhost:" + port;
    }

    public RegisterResponse register(RegisterRequest request){
        //translate to json
        String jsonRequest = (String) Translator.fromObjectToJson(request);
        //Perform correct HTTP request
        try {
            clientCommunicator.doPost(url + "/user", jsonRequest, null);
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
        }
        //Return result
        return null;
    }

    public RegisterResponse register(String player1, String password, String mail) {
        return null;
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) {
        String jsonRequest = (String) Translator.fromObjectToJson(request);
        try {
            clientCommunicator.doPost(url + "/game", jsonRequest, authToken);
            System.out.println("Game created Successfully. ");
        } catch (IOException e) {
            System.out.println("Creating Game failed: " + e.getMessage());
        }
        //Return result
        return null;
    }
}
