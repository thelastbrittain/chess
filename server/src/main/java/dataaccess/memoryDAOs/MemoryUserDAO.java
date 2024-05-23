package dataaccess.memoryDAOs;

import dataaccess.interfaces.UserDAO;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO  implements UserDAO {
    private Collection<UserData> userDataList = new ArrayList<>();

    public MemoryUserDAO() {

    }

    public UserData createUser(UserData newUser) {
        userDataList.add(newUser);
        return newUser;
    }

    public UserData getUser(String username) {
        for (UserData currentUser : userDataList) {
            if (currentUser.username().equals(username)) {
                return currentUser;
            }
        };
        return null;
    }

    @Override
    public boolean isVerifiedUser(String username, String password) {
        for (UserData currentUser : userDataList) {
            if (currentUser.username().equals(username) && currentUser.password().equals(password)) {
                return true;
            }
        };
        return false;
    }

    public void clearUsers(){
        userDataList.clear();
    }
}
