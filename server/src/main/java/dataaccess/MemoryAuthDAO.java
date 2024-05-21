package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private Collection<AuthData> authDataList;

    public MemoryAuthDAO(){

    }

    @Override
    public String createAuth(String username) {
        String newAuthToken = UUID.randomUUID().toString();
        AuthData newAuthorization = new AuthData(username, newAuthToken);
        authDataList.add(newAuthorization);
        return newAuthToken;

    }

    @Override
    public AuthData verifyAuth(String authToken) {
        for (AuthData auth: authDataList){
            if (auth.authToken().equals(authToken)){
                return auth;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        AuthData authDataToRemove = null;
        for (AuthData authData: authDataList){
            if (authData.authToken().equals(authToken)){
                authDataToRemove = authData;
            }
        }
        authDataList.remove(authDataToRemove);
    }
}
