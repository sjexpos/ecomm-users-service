package io.oigres.ecomm.service.users.usecases.users.consumers.delete;

import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.exception.UserNotFoundException;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;

public interface DeleteConsumerUserUseCase {
    ConsumerProfile handle(Long userId) throws NotFoundProfileException, UserNotFoundException;
}
