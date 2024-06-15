package io.oigres.ecomm.service.users.usecases.region.search;

import io.oigres.ecomm.service.users.domain.State;
import io.oigres.ecomm.service.users.exception.NotFoundException;

public interface GetStateByIdUseCase {
    State handle(long stateId) throws NotFoundException;
}
