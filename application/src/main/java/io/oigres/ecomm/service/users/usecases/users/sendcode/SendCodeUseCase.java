package io.oigres.ecomm.service.users.usecases.users.sendcode;

import io.oigres.ecomm.service.users.exception.InvalidException;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;

public interface SendCodeUseCase {
    void handle(String email) throws UserNotFoundException, InvalidException;
}
