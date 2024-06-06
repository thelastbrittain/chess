package serverhandling;

import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import translation.Translator;

import java.io.IOException;

public class ServerFacade {
    private final String url;
    private final ClientCommunicator clientCommunicator = new ClientCommunicator();


    public ServerFacade(int port){
        url = "http://localhost:" + port;
    }

    public RegisterResponse register(RegisterRequest request){
//        if (request.username() == null || request.password() == null || request.email() == null ){
//            return new RegisterResponse(null,null, "Bad Request");
//        }

        //translate to json
        String jsonRequest = (String) Translator.fromObjectToJson(request);
        //Perform correct HTTP request
        try {
            String stringResponse = clientCommunicator.doPost(url + "/user", jsonRequest, null);
            return Translator.fromJsontoObjectNotRequest(stringResponse, RegisterResponse.class);
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new RegisterResponse(null, null, e.getMessage());
        }
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
            clientCommunicator.doDelete(url + "/session", authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LoginResponse login(LoginRequest request) {
        //translate to json
        String jsonRequest = (String) Translator.fromObjectToJson(request);
        //Perform correct HTTP request
        try {
            String stringResponse = clientCommunicator.doPost(url + "/session", jsonRequest, null);
            LoginResponse testResponse = Translator.fromJsontoObjectNotRequest(stringResponse, LoginResponse.class);
            return testResponse;
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new LoginResponse(null, null, "Error logging in");
        }
    }

    public ListGamesResponse listGames(String authToken) {
        try {
            String stringResponse = clientCommunicator.doGet(url + "/game", authToken);
            return Translator.fromJsontoObjectNotRequest(stringResponse, ListGamesResponse.class);

        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new ListGamesResponse(null, null);
        }
    }

    public JoinGameResponse joinGame(JoinGameRequest request) {
        //translate to json
        String jsonRequest = (String) Translator.fromObjectToJson(request);
        //Perform correct HTTP request
        try {
            String stringResponse = clientCommunicator.doPut(url + "/game", jsonRequest, request.authToken());
            return Translator.fromJsontoObjectNotRequest(stringResponse, JoinGameResponse.class);
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new JoinGameResponse("Join Game Failure");
        }
        //Return result

    }
}
