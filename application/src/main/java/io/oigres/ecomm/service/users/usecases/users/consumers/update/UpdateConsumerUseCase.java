package io.oigres.ecomm.service.users.usecases.users.consumers.update;

import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.exception.GenderNotFoundException;
import io.oigres.ecomm.service.users.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.exception.ZipcodeNotFoundDomainException;
import io.oigres.ecomm.service.users.exception.profile.ProfileUserException;

public interface UpdateConsumerUseCase {
    public ConsumerProfile handle(Long userId, ConsumerProfile request) throws ProfileUserException, GenderNotFoundException, StateNotFoundException, ZipcodeNotFoundDomainException;
}
