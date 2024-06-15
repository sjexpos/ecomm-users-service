package io.oigres.ecomm.service.users.usecases.users.validate;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.exception.PasswordInvalidException;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;

public interface ValidateUserUseCase {
    User handle(String email, String password) throws UserNotFoundException, PasswordInvalidException;
}
