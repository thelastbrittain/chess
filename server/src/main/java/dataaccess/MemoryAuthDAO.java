package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private Collection<AuthData> authDataList = new ArrayList<>();

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
    public boolean isVerifiedAuth(String authToken) {
        for (AuthData auth: authDataList){
            System.out.println("Entered Auth Token: " + authToken + "\n Current Auth Token: " + auth.authToken());
            if (auth.authToken().equals(authToken)){
                return true;
            }
        }
        return false;
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

    public void clearAuths(){
            authDataList.clear();
    }
}
