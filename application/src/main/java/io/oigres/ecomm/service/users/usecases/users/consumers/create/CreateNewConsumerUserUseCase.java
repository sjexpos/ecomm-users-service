package io.oigres.ecomm.service.users.usecases.users.consumers.create;

import io.oigres.ecomm.service.users.api.model.consumer.CreateConsumerUserRequest;
import io.oigres.ecomm.service.users.domain.profile.ConsumerProfile;
import io.oigres.ecomm.service.users.exception.GenderNotFoundException;
import io.oigres.ecomm.service.users.exception.StateNotFoundException;
import io.oigres.ecomm.service.users.exception.ZipcodeNotFoundDomainException;
import io.oigres.ecomm.service.users.exception.profile.*;

public interface CreateNewConsumerUserUseCase {
    ConsumerProfile handle(CreateConsumerUserRequest consumerProfile) throws DeletedProfileException, ExistingProfileException, TypeNotFoundProfileException, GenderNotFoundException, ZipcodeNotFoundDomainException, StateNotFoundException, UserTypeNotFoundException;
}
