package serverhandling;

import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import translationForClient.TranslatorForClient;
import ui.ClientMenu;

import java.io.IOException;

public class ServerFacade {
    private final String url;
    private final HTTPCommunicator clientCommunicator = new HTTPCommunicator();
    private ClientMenu client;


    public ServerFacade(int port, ClientMenu client){
        url = "http://localhost:" + port;
        this.client = client;
    }

    public RegisterResponse register(RegisterRequest request){
        //translate to json
        String jsonRequest = (String) TranslatorForClient.fromObjectToJson(request);
        //Perform correct HTTP request
        try {
            String stringResponse = clientCommunicator.doPost(url + "/user", jsonRequest, null);
            return TranslatorForClient.fromJsontoObjectNotRequest(stringResponse, RegisterResponse.class);
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new RegisterResponse(null, null, e.getMessage());
        }
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) {
        String jsonRequest = (String) TranslatorForClient.fromObjectToJson(request);
        try {
            String response = clientCommunicator.doPost(url + "/game", jsonRequest, authToken);
            return TranslatorForClient.fromJsontoObjectNotRequest(response, CreateGameResponse.class);
        } catch (IOException e) {
            System.out.println("Creating Game failed: " + e.getMessage());
        }
        //Return result
        return null;
    }

    public LogoutResponse logout(String authToken) {
        try {
            return new LogoutResponse(clientCommunicator.doDelete(url + "/session", authToken)) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LoginResponse login(LoginRequest request) {
        //translate to json
        String jsonRequest = (String) TranslatorForClient.fromObjectToJson(request);
        //Perform correct HTTP request
        try {
            String stringResponse = clientCommunicator.doPost(url + "/session", jsonRequest, null);
            LoginResponse testResponse = TranslatorForClient.fromJsontoObjectNotRequest(stringResponse, LoginResponse.class);
            return testResponse;
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new LoginResponse(null, null, "Error logging in");
        }
    }

    public ListGamesResponse listGames(String authToken) {
        try {
            String stringResponse = clientCommunicator.doGet(url + "/game", authToken);
            return TranslatorForClient.fromJsontoObjectNotRequest(stringResponse, ListGamesResponse.class);

        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new ListGamesResponse(null, null);
        }
    }

    public JoinGameResponse joinGame(JoinGameRequest request) {
        //translate to json
        String jsonRequest = (String) TranslatorForClient.fromObjectToJson(request);
        //Perform correct HTTP request
        try {
            String stringResponse = clientCommunicator.doPut(url + "/game", jsonRequest, request.authToken());
            return TranslatorForClient.fromJsontoObjectNotRequest(stringResponse, JoinGameResponse.class);
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new JoinGameResponse("Join Game Failure");
        }
    }

    public void clearGame(){
        try {
            clientCommunicator.doDelete(url + "/db", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
