package dataaccess;

import model.UserData;

import java.util.Collection;

public class MemoryUserDAO  implements UserDAO {
    private Collection<UserData> userDataList;

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

    public void clearUsers(){
        userDataList.clear();
    }
}
