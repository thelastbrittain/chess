package dataaccess.interfaces;

import model.UserData;

public interface UserDAO {
        UserData createUser(UserData newUser);
        UserData getUser(String username);
        void clearUsers();
        boolean isVerifiedUser(String username, String password);
}
