package io.oigres.ecomm.service.users.usecases.users.consumers.search;

import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.exception.profile.NotFoundProfileException;

public interface GetConsumerByIdUseCase {
    ConsumerProfile handle(Long userId) throws NotFoundProfileException;
}
