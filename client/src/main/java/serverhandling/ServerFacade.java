package serverhandling;

import request.RegisterRequest;
import response.RegisterResponse;
import translation.Translator;

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

        //Return result
        return null;
    }

    public RegisterResponse register(String player1, String password, String mail) {
        return null;
    }
}
