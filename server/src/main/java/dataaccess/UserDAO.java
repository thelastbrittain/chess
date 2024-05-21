package dataaccess;

import model.UserData;

public interface UserDAO {
        UserData createUser(UserData newUser);
        UserData getUser(String username);
}
