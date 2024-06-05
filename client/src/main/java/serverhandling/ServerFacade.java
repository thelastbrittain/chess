package serverhandling;

import request.RegisterRequest;
import response.RegisterResponse;

public class ServerFacade {
    int port;

    public ServerFacade(int port){
        this.port = port;
    }

    private RegisterResponse register(RegisterRequest request){
        //translate to json
        //Perform correct HTTP request
        //Return result
        return null;
    }

    public RegisterResponse register(String player1, String password, String mail) {
        return null;
    }
}
