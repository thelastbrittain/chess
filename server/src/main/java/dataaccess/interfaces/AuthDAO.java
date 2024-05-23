package dataaccess.interfaces;

public interface AuthDAO {
    String createAuth(String username);
    boolean isVerifiedAuth(String authToken);
    void deleteAuth(String authToken);
    void clearAuths();
}
