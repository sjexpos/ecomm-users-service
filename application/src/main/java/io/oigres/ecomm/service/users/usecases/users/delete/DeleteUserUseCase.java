package io.oigres.ecomm.service.users.usecases.users.delete;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;

public interface DeleteUserUseCase {
    User handle(Long id) throws UserNotFoundException;
}
