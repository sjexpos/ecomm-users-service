package io.oigres.ecomm.service.users.usecases.users.search;

import io.oigres.ecomm.service.users.domain.User;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;

public interface GetUserByIdUseCase {
    
    User handle(Long id) throws UserNotFoundException;

}
