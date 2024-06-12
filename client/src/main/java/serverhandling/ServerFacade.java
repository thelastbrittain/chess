package serverhandling;

import request.*;
import response.*;
import translationForClient.TranslatorForClient;
import ui.ClientMenu;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveGameCommand;
import websocket.commands.ResignCommand;

import java.io.IOException;

public class ServerFacade {
    private final String url;
    private final HTTPCommunicator httpCommunicator = new HTTPCommunicator();
    private ClientMenu client;
    private final WSCommunicator wsCommunicator;


    public ServerFacade(int port, ClientMenu client){
        url = "http://localhost:" + port;
        this.client = client;
        this.wsCommunicator = new WSCommunicator(url, this.client);
    }

    public RegisterResponse register(RegisterRequest request){
        //translate to json
        String jsonRequest = (String) TranslatorForClient.fromObjectToJson(request);
        //Perform correct HTTP request
        try {
            String stringResponse = httpCommunicator.doPost(url + "/user", jsonRequest, null);
            return TranslatorForClient.fromJsontoObjectNotRequest(stringResponse, RegisterResponse.class);
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new RegisterResponse(null, null, e.getMessage());
        }
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) {
        String jsonRequest = (String) TranslatorForClient.fromObjectToJson(request);
        try {
            String response = httpCommunicator.doPost(url + "/game", jsonRequest, authToken);
            return TranslatorForClient.fromJsontoObjectNotRequest(response, CreateGameResponse.class);
        } catch (IOException e) {
            System.out.println("Creating Game failed: " + e.getMessage());
        }
        //Return result
        return null;
    }

    public LogoutResponse logout(String authToken) {
        try {
            return new LogoutResponse(httpCommunicator.doDelete(url + "/session", authToken)) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LoginResponse login(LoginRequest request) {
        //translate to json
        String jsonRequest = (String) TranslatorForClient.fromObjectToJson(request);
        //Perform correct HTTP request
        try {
            String stringResponse = httpCommunicator.doPost(url + "/session", jsonRequest, null);
            LoginResponse testResponse = TranslatorForClient.fromJsontoObjectNotRequest(stringResponse, LoginResponse.class);
            return testResponse;
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new LoginResponse(null, null, "Error logging in");
        }
    }

    public ListGamesResponse listGames(String authToken) {
        try {
            String stringResponse = httpCommunicator.doGet(url + "/game", authToken);
            return TranslatorForClient.fromJsontoObjectNotRequest(stringResponse, ListGamesResponse.class);

        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new ListGamesResponse(null, null);
        }
    }

    public JoinGameResponse joinGame(ConnectCommand command) {
        //translate to json
        String jsonRequest = (String) TranslatorForClient.fromObjectToJson(command);
        //Perform correct HTTP request
        try {
            String stringResponse = httpCommunicator.doPut(url + "/game", jsonRequest, command.getAuthString());
            wsCommunicator.connect(command);
            return TranslatorForClient.fromJsontoObjectNotRequest(stringResponse, JoinGameResponse.class);
        } catch (IOException e) {
            System.out.println("Registering user failed: " + e.getMessage());
            return new JoinGameResponse("Join Game Failure");
        }
    }

    public void observeGame(String authToken, int gameID){
        wsCommunicator.connect(new ConnectCommand(authToken, gameID, null));
    }

    public void leaveGame(LeaveGameCommand command){
        wsCommunicator.leave(command);
    }

    public void clearGame(){
        try {
            httpCommunicator.doDelete(url + "/db", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resignGame(ResignCommand command){
        wsCommunicator.resign(command);
    }


}
