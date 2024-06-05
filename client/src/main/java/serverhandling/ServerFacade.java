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
            String stringResponse = clientCommunicator.doPost(url + "/user", jsonRequest, null);
            return Translator.fromJsontoObjectNotRequest(stringResponse, RegisterResponse.class);
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
        }
        //Return result
        return null;
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) {
        String jsonRequest = (String) Translator.fromObjectToJson(request);
        try {
            String response = clientCommunicator.doPost(url + "/game", jsonRequest, authToken);
            return Translator.fromJsontoObjectNotRequest(response, CreateGameResponse.class);
        } catch (IOException e) {
            System.out.println("Creating Game failed: " + e.getMessage());
        }
        //Return result
        return null;
    }

    public void logout(String authToken) {
        try {
            clientCommunicator.doDelete(url, authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
