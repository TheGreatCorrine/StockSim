package use_case.execute_sell;

import entity.User;
import utility.exceptions.ValidationException;

public interface ExecuteSellDataAccessInterface {
    User getUserWithCredential(String credential) throws ValidationException;

}
