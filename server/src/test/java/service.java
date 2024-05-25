import dataaccess.memoryDAOs.MemoryAuthDAO;
import dataaccess.memoryDAOs.MemoryGameDAO;
import dataaccess.memoryDAOs.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.RegisterResponse;
import service.UserService;
import spark.utils.Assert;
import service.ErrorMessages;

public class service {
    String testUsername = "testUsername";
    String testPassword = "testPassword";
    String testEmail = "testEmail";
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();

    @Test
    @DisplayName("Register User Invalid AuthToken")
    void invalidAuthToken(){}


    /**
     * User Service Tests
     */

    //Register Tests

    ////giving proper user/pass/email sets response message to 200
    @Test
    @DisplayName("Register Success")
    void registerSuccess(){
        RegisterRequest regRequest = new RegisterRequest(testUsername, testPassword, testEmail);
        UserService registerService =  new UserService(userDAO, authDAO);
        RegisterResponse actualResponse = registerService.register(regRequest);

        Assertions.assertNotNull(actualResponse.authToken(), "No auth token given");
    }

    ////Sending withouth email returns bad request
    @Test
    @DisplayName("Register No Email Failure")
    void registerEmailFailure(){
        RegisterRequest regRequest = new RegisterRequest(testUsername, testPassword, null);
        UserService registerService =  new UserService(userDAO, authDAO);
        RegisterResponse actualResponse = registerService.register(regRequest);

        Assertions.assertSame(actualResponse.message(), ErrorMessages.BADREQUEST);
    }

    //Login

    ////Success

    ////Failure


    //Logout

    ////Success

    ////Failure

}
