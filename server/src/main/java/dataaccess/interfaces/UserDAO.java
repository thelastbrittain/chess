package dataaccess.interfaces;

import model.UserData;

public interface UserDAO {
        UserData createUser(UserData newUser);
        boolean userExists(String username);
        void clearUsers();
        boolean isVerifiedUser(String username, String password);
}
