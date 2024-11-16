package data_access;

import entity.User;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.execute_sell.ExecuteSellDataAccessInterface;
import utility.SessionManager;

import java.util.HashMap;
import java.util.Map;
import utility.exceptions.ValidationException;

/**
 * A class that implements the ExecuteBuyDataAccessInterface and the ExecuteSellDataAccessInterface
 * This class is used to get the user with the given credential
 */
public class InMemoryUserDataAccessObject implements ExecuteBuyDataAccessInterface, ExecuteSellDataAccessInterface {
    private Map<String, User> users;

    @Override
    public User getUserWithCredential(String credential) throws ValidationException {
        SessionManager sessionManager = SessionManager.Instance();
        String username = sessionManager.getUsername(credential).orElseThrow(ValidationException::new);
        return getUserWithUsername(username);
    }

    private User getUserWithUsername(String username) {
        Map<String, User> users = new HashMap<>();

        User user1 = new User("user1", "password");
        user1.addBalance(10000.00);
        users.put(username, user1);

        User user2 = new User("user2", "password");
        user2.addBalance(20000.00);
        users.put(username, user2);

        User user3 = new User("user3", "password");
        user3.addBalance(30000.00);
        users.put(username, user3);

        return users.get(username);
    }
}
