package io.oigres.ecomm.service.users.usecases.region.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.domain.State;
import io.oigres.ecomm.service.users.exception.NotFoundException;
import io.oigres.ecomm.service.users.repository.StateRepository;

@Component
public class GetStateByIdUseCaseImpl implements GetStateByIdUseCase {

    private final StateRepository stateRepository;

    public GetStateByIdUseCaseImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public State handle(long stateId) throws NotFoundException {
        return stateRepository.findById(stateId).orElseThrow(() -> new NotFoundException("State not found"));
    }
}
