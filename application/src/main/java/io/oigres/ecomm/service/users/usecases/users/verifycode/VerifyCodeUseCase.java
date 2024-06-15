package io.oigres.ecomm.service.users.usecases.users.verifycode;

import io.oigres.ecomm.service.users.exception.InvalidException;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;

public interface VerifyCodeUseCase {
    void handle(String email, String code) throws UserNotFoundException, InvalidException;
}
